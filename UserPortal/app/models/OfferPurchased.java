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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.jclouds.abiquo.domain.cloud.VirtualMachine;

import play.db.jpa.GenericModel;

import com.abiquo.server.core.cloud.VirtualApplianceState;

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
@NamedQuery(name="getSubscribedOffers",query=" select p from Offer as p where p.defaultServiceLevel = ?1"),
@NamedQuery(name="getSubscribedOffers1",query=" select p.offer from OfferPurchased as p where p.serviceLevel = ?1"),
@NamedQuery(name="getSubscribedOffersGroupByServiceLevels",query="select p from OfferPurchased as p order by p.serviceLevel ASC"),
@NamedQuery(name="deleteOffer",query="delete from OfferPurchased as p where p.id = ?1"),
@NamedQuery(name="getOffersPurchasedFromUserId",query="select p from OfferPurchased as p where p.user.idAbiquo = ?1"),
@NamedQuery(name="getOffersPurchasedFromEnterpriseId",query="select p from OfferPurchased as p where p.user.idEnterprise = ?1"),
@NamedQuery(name="getSubscribedOfferDetails ",query="select p from OfferPurchased as p where p.id = ?1")
})

@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "id"))})
public class OfferPurchased extends GenericModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Date start;
	private Date expiration;
	
	private Integer idVirtualApplianceUser;
	private Integer idVirtualDatacenterUser;	
	private VirtualApplianceState virtualApplianceState;
	
	//vm updated from jClouds
	private LinkedList<VirtualMachine> virtualMachines = new LinkedList<VirtualMachine>();
	
	// Relations
	@ManyToOne(fetch = FetchType.LAZY)
	private Offer offer; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	private UserPortal user; 
		
	//@OneToOne( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Deploy_Bundle.class)
	//@JoinTable(name = "OfferPurchased_DeployNode", joinColumns = { @JoinColumn(name = "bundle_id") }, inverseJoinColumns = { @JoinColumn(name = "id") })
	@Transient
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
	
	public UserPortal getUser() {
		return user;
	}

	public void setUser(UserPortal user) {
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

	public Integer getIdVirtualDatacenterUser() {
		return idVirtualDatacenterUser;
	}

	public void setIdVirtualDatacenterUser(Integer idVirtualDatacenterUser) {
		this.idVirtualDatacenterUser = idVirtualDatacenterUser;
	}

	public VirtualApplianceState getVirtualApplianceState() {
		return virtualApplianceState;
	}

	public void setVirtualApplianceState(VirtualApplianceState virtualApplianceState) {
		this.virtualApplianceState = virtualApplianceState;
	}

	public LinkedList<VirtualMachine> getVirtualMachines() {
		return virtualMachines;
	}

	public void setVirtualMachines(LinkedList<VirtualMachine> list) {
		this.virtualMachines = list;
	}
}
