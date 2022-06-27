package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class Distretto {
	private Integer districtId;
	private LatLng posizione;
	
	public Distretto(Integer districtId, LatLng posizione) {
		super();
		this.districtId = districtId;
		this.posizione = posizione;
	}

	public Integer getDistrictId() {
		return districtId;
	}
	
	public LatLng getPosizione() {
		return posizione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((districtId == null) ? 0 : districtId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distretto other = (Distretto) obj;
		if (districtId == null) {
			if (other.districtId != null)
				return false;
		} else if (!districtId.equals(other.districtId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Distretto "+ districtId + " : " + posizione;
	}
	
	
}
