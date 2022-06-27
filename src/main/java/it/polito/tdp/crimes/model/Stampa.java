package it.polito.tdp.crimes.model;

public class Stampa implements Comparable<Stampa>{
	private int districtId;
	private double distance;
	
	public Stampa(int districtId, double distance) {
		super();
		this.districtId = districtId;
		this.distance = distance;
	}

	public int getDistrictId() {
		return districtId;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public int compareTo(Stampa o) {
		return (int) (this.distance-o.distance);
	}
	
	
}
