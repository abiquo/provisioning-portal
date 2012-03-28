package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
@Entity
@NamedQueries({
@NamedQuery(name="getSubscribedOffers",query=" from sc_offers_subscriptions as p where p.service_level = ?1"),
@NamedQuery(name="getSubscribedOffersGroupByServiceLevels",query="select p from sc_offers_subscriptions as p GROUP BY p.service_level"),
@NamedQuery(name="deleteOffer",query="delete from sc_offers_subscriptions as p where p.id = ?1"),
@NamedQuery(name="getSubscribedOfferDetails ",query="select p from sc_offers_subscriptions as p where p.id = ?1")
})
public class sc_offers_subscriptions extends Model{

	private Date start_date;
	private Date expiration_date;
	private boolean mkt_customization_allowed; 
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn
	 private sc_offer sc_offer;
	 private String service_level;
	 
	public sc_offers_subscriptions() {
		super();
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getExpiration_date() {
		return expiration_date;
	}
	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}
	public boolean isMkt_customization_allowed() {
		return mkt_customization_allowed;
	}
	public void setMkt_customization_allowed(boolean mkt_customization_allowed) {
		this.mkt_customization_allowed = mkt_customization_allowed;
	}
	public sc_offer getSc_offer() {
		return sc_offer;
	}
	public void setSc_offer(sc_offer sc_offer) {
		this.sc_offer = sc_offer;
	}
	public String getService_level() {
		return service_level;
	}
	public void setService_level(String service_level) {
		this.service_level = service_level;
	}

}
