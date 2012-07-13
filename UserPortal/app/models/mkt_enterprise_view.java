package models;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.db.jpa.Model;

/**
 * 
 * @author Harpreet Kaur
 * This model saves offers enabled for an enterprise
 */
@Entity
@NamedQueries({
@NamedQuery(name="getOffersForEnterprise",query="select distinct p.sc_offer_id from mkt_enterprise_view as p where p.enterprise_id = ?1 and p.service_level = ?2 "),
})
public class mkt_enterprise_view extends Model{

	private Integer enterprise_id;
	private Integer sc_offer_id ;
	private String service_level;
	
	public mkt_enterprise_view() {
		super();
		
	}
	public Integer getEnterprise_id() {
		return enterprise_id;
	}
	public void setEnterprise_id(Integer enterprise_id) {
		this.enterprise_id = enterprise_id;
	}
	public Integer getSc_offer_id() {
		return sc_offer_id;
	}
	public void setSc_offer_id(Integer sc_offer_id) {
		this.sc_offer_id = sc_offer_id;
	}
	public String getService_level() {
		return service_level;
	}
	public void setService_level(String service_level) {
		this.service_level = service_level;
	}
	
}
