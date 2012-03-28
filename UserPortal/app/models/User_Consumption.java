package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.GenericModel;
@Entity
public class User_Consumption extends GenericModel {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer iduser_consumption;
	private String userid;
	
	/*@OneToOne( cascade = CascadeType.ALL)
	private sc_offer offer;*/
	private Integer sc_offer_id_ref;
	@OneToMany( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Deploy_Bundle.class)
	@JoinTable(name = "UserConsumption_DeployNode", joinColumns = { @JoinColumn(name = "iduser_consumption") }, inverseJoinColumns = { @JoinColumn(name = "bundle_id") })
	private Set<Deploy_Bundle> nodes = new HashSet<Deploy_Bundle>();
	
	private Date purchase_date;
	private Date expiration_date;
	private Date destroy_date;
	
	@Column(length = 45)
	private String vdc_name;
	private Integer vdc_id;
	
	public User_Consumption(){
		
	}
	
	
	public Integer getIduser_consumption() {
		return iduser_consumption;
	}

	public void setIduser_consumption(Integer iduser_consumption) {
		this.iduser_consumption = iduser_consumption;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
/*
	public sc_offer getOffer() {
		return offer;
	}

	public void setOffer(sc_offer offer) {
		this.offer = offer;
	}*/

	public Set<Deploy_Bundle> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Deploy_Bundle> nodes) {
		this.nodes = nodes;
	}

	public Date getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}

	public Date getDestroy_date() {
		return destroy_date;
	}

	public void setDestroy_date(Date destroy_date) {
		this.destroy_date = destroy_date;
	}

	public String getVdc_name() {
		return vdc_name;
	}

	public void setVdc_name(String vdc_name) {
		this.vdc_name = vdc_name;
	}

	public Integer getSc_offer_id_ref() {
		return sc_offer_id_ref;
	}

	public void setSc_offer_id_ref(Integer sc_offer_id_ref) {
		this.sc_offer_id_ref = sc_offer_id_ref;
	}


	public Integer getVdc_id() {
		return vdc_id;
	}


	public void setVdc_id(Integer vdc_id) {
		this.vdc_id = vdc_id;
	}
	
	
}
