/*******************************************************************************
 * Abiquo community edition
 * cloud management application for hybrid clouds
 *  Copyright (C) 2008-2010 - Abiquo Holdings S.L.
 * 
 *  This application is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU LESSER GENERAL PUBLIC
 *  LICENSE as published by the Free Software Foundation under
 *  version 3 of the License
 * 
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  LESSER GENERAL PUBLIC LICENSE v.3 for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA 02111-1307, USA.
 ******************************************************************************/
package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

/**
 * 
 * @author David López
 * This model saves all offer entries in a service catalog and their details
 * It might need design reconsideration in future. 
 * start_date and expiration date - not clear 
 */
@Entity
@NamedQueries({
@NamedQuery(name="listSubscribedOffers",query=" select p from OfferPurchased as p "),
@NamedQuery(name="getSubscribedOffers",query=" select p from OfferPurchased as p where p.serviceLevel = ?1"),
@NamedQuery(name="getSubscribedOffers1",query=" select p.offer from OfferPurchased as p where p.serviceLevel = ?1"),
@NamedQuery(name="getSubscribedOffersGroupByServiceLevels",query="select p from OfferPurchased as p order BY p.serviceLevel ASC"),
@NamedQuery(name="deleteOffer",query="delete from OfferPurchased as p where p.id = ?1"),
@NamedQuery(name="getSubscribedOfferDetails ",query="select p from OfferPurchased as p where p.id = ?1")
})
public class OfferPurchased extends Model{
	
	private Date start;
	private Date expiration;
	
	
	private Integer idVirtualApplianceUser;
	
	// Relations
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Offer.class)
	@JoinTable(name = "Offer", joinColumns = { @JoinColumn(name = "id") })
	private Offer offer; 
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = User.class)
	@JoinTable(name = "User", joinColumns = { @JoinColumn(name = "id") })
	private User user; 
		
	@OneToMany( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Deploy_Bundle.class)
	@JoinTable(name = "OfferPurchased_DeployNode", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = { @JoinColumn(name = "bundle_id") })
	private Set<Deploy_Bundle> nodes = new HashSet<Deploy_Bundle>();

	private String serviceLevel;
	private String leasePeriod;
	// private Integer allowed_enterprise_id;
	
	public OfferPurchased() {
		super();
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Integer getIdVirtualApplianceUser() {
		return idVirtualApplianceUser;
	}

	public void setIdVirtualApplianceUser(Integer idVirtualApplianceUser) {
		this.idVirtualApplianceUser = idVirtualApplianceUser;
	}
	
	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getLeasePeriod() {
		return leasePeriod;
	}

	public void setLeasePeriod(String leasePeriod) {
		this.leasePeriod = leasePeriod;
	}
	
	public Set<Deploy_Bundle> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Deploy_Bundle> nodes) {
		this.nodes = nodes;
	}
}
