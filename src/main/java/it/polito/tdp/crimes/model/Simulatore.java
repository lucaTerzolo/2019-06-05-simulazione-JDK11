package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Evento.eventType;

public class Simulatore {
	//entrata
	private int n; //numero agenti
	private int giorno;
	private int mese;
	private int anno;
	
	//situazione
	private Graph<Distretto,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private Map<Integer,Integer> agenti; //distretto, nAgenti
	
	//Coda eventi
	private PriorityQueue<Evento> coda;
	
	//uscita
	private int eventiMalGestiti;
	
	
	public Simulatore() {
		dao=new EventsDao();
		this.eventiMalGestiti=0;
		this.coda=new PriorityQueue<>();
	}
	
	public void init(int n,Graph<Distretto,DefaultWeightedEdge> grafo,int giorno, int mese, int anno) {
		this.n=n;
		this.grafo=grafo;
		this.giorno=giorno;
		this.mese=mese;
		this.anno=anno;
		agenti=new HashMap<>();
		for(Distretto d:this.grafo.vertexSet()) {
			agenti.put(d.getDistrictId()-1, 0);
		}
		//Stanzio gli agenti nel quartiere meno criminoso
		//Distretto iniziale=dao.getDistrettoMenoCriminoso(anno,dao.getDistretti(anno));
		//System.out.print(iniziale.getDistrictId());
		Distretto iniziale=dao.getDistretti(2015).get(6);
		this.agenti.put(iniziale.getDistrictId()-1, n);
		
		//Carico tutti gli eventi criminosi accaduti in citta nel dato giorno
		for(Event e: this.dao.getEventiByGiorno(giorno, mese, anno)) {
			this.coda.add(new Evento(eventType.CRIME,e.getReported_date(),e));
		}
		//Poi passo all'esecuzione della simulazione
	}
	
	public void run(int anno) {
		Evento e=coda.poll();
		while(e!=null) {
			switch(e.getTipo()) {
			case CRIME:
				//Devo mandare un agente sul posto
				Integer partenza=null; //quartiere di partenza
				partenza=cercaAgente(e.getEvento().getDistrict_id());
				if(partenza!=null) {
					this.agenti.put(partenza-1, this.agenti.get(partenza-1)-1);
					Double distance;
					if(partenza.equals(e.getEvento().getDistrict_id())) {
						distance=0.0;
					}else {
						List<Distretto> distretti=new ArrayList<>(this.grafo.vertexSet());
						distance=this.grafo.getEdgeWeight(this.grafo.getEdge(distretti.get(partenza-1), 
								distretti.get(e.getEvento().getDistrict_id()-1)));
					}
					long secondi=(long) (distance*1000/(60/3.6));
					this.coda.add(new Evento(eventType.ARRIVA_AGENTE,e.getData().plusSeconds(secondi),e.getEvento()));
				}else {
					//non ci sono abbastanza agenti quindi creo un evento mal gestito
					this.eventiMalGestiti++;
				}
				break;
			case ARRIVA_AGENTE:
				//L'agente deve gestire il problema
				long durata=getDurata(e.getEvento().getOffense_category_id());
				this.coda.add(new Evento(eventType.GESTITO,e.getData().plusSeconds(durata),e.getEvento()));
				if(e.getData().isAfter(e.getEvento().getReported_date().plusMinutes(15))) {
					this.eventiMalGestiti++;
				}
				break;
			case GESTITO:
				//L'agente è pronto per andare al prossimo evento criminoso
				this.agenti.put(e.getEvento().getDistrict_id(), this.agenti.get(e.getEvento().getDistrict_id()-1)+1);
				break;
			}
		}
	}


	private long getDurata(String offense_category_id) {
		if(offense_category_id.equals("all_other_crimes")) {
			double caso=Math.random();
			if(caso<0.5) {
				return 1*3600;
			}else {
				return 2*3600;
			}
		}else {
			return 2*3600;
		}
	}

	private Integer cercaAgente(Integer district_id) {
		double distance=100000;
		List<Distretto> distretti=new ArrayList<>(this.grafo.vertexSet());
		int res=-2;
		for(Integer d:this.agenti.keySet()) {
			if(this.agenti.get(d)>0) {
				if(d.equals(district_id)) {
					distance=0;
					res=d;
				}else if(this.grafo.getEdgeWeight(this.grafo.getEdge(distretti.get(d), distretti.get(district_id-1)))<distance){
					distance=this.grafo.getEdgeWeight(this.grafo.getEdge(distretti.get(d), distretti.get(district_id-1)));
					res=d;
				}
			}
		}
		return res;
	}

	public int getEventiMalGestiti() {
		return eventiMalGestiti;
	}
	
}
