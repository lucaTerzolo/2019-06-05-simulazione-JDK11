package it.polito.tdp.crimes.db;

import it.polito.tdp.crimes.model.Distretto;
import it.polito.tdp.crimes.model.Event;

public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		Distretto d=dao.getDistrettoMenoCriminoso(2015, dao.getDistretti(2015));
		System.out.println(d);
	}

}
