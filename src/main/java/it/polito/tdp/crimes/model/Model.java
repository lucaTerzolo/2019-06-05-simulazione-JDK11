package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<Distretto, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private List<Distretto> distretti;
	private Simulatore sim;
	
	public Model() {
		dao=new EventsDao();
		sim=new Simulatore();
	}
	
	public String creaGrafo(int anno) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, dao.getDistretti(anno));
		distretti=new ArrayList<>(dao.getDistretti(anno));
	
		for(Distretto d: distretti) {
			for(Distretto di:distretti)
				if(!d.equals(di))
				Graphs.addEdge(this.grafo, di,d, LatLngTool.distance(di.getPosizione(),
						d.getPosizione(),LengthUnit.KILOMETER));
		}
		
		
		return "Grafo creato:\n#vertici: "+this.grafo.vertexSet().size()+"\n#Archi: "
		+this.grafo.edgeSet().size()+"\n";
	}
	
	public List<Integer> getAnni(){
		return dao.getListAnni();
	}
	
	private String getListaVicini(Distretto d) {
		String result="Distretti vicini di "+d.getDistrictId()+":\n";
		List<Stampa> stampa=new ArrayList<>();
		for(Distretto di: Graphs.neighborListOf(this.grafo, d)) {
			stampa.add(new Stampa(di.getDistrictId(),this.grafo.getEdgeWeight(this.grafo.getEdge(di, d))));
		}
		Collections.sort(stampa);
		for(Stampa s:stampa)
			result+=""+s.getDistrictId()+" "+s.getDistance()+"\n";
		return result;
	}
	
	public String stampaVicini() {
		String res=new String();
		for(Distretto d:this.distretti) {
			res+=this.getListaVicini(d);
		}
		return res;
	}
	
	public int simulatore(int n,int giorno, int mese, int anno) {
		sim.init(n, this.grafo, giorno, mese, anno);
		sim.run(anno);
		return sim.getEventiMalGestiti();
	}
}
