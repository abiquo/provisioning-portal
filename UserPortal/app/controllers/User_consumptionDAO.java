package controllers;

import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;

public interface  User_consumptionDAO {
	public void save(VirtualDatacenter vdc, Integer sc_offer_id) ;

}
