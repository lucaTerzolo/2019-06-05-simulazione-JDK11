package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento> {

	enum eventType{
		CRIME,
		ARRIVA_AGENTE,
		GESTITO
	}
	
	
	
	public Evento(eventType tipo, LocalDateTime data, Event evento) {
		super();
		this.tipo = tipo;
		this.data = data;
		this.evento = evento;
	}

	private eventType tipo;
	private LocalDateTime data;
	private Event evento;
	
	public eventType getTipo() {
		return tipo;
	}
	
	public LocalDateTime getData() {
		return data;
	}
	
	public Event getEvento() {
		return evento;
	}

	@Override
	public int compareTo(Evento o) {
		return this.data.compareTo(o.getData());
	}
		
}
