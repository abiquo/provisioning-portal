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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
import play.db.jpa.Model;

/**
 * 
 * @author David Lopez VIRTUAL MACHINES CONTIANED IN AN OFFER REFER ALSO
 *         sc_offer
 */
@Entity
public class Nodes extends GenericModel {

	@Id
	private Integer id_node;
	private String node_name;
	private Integer idImage;
	private Integer cpu;
	private Integer ram;
	private Integer hd;
	private String icon;
	private String description;
	private String vncAddress;
	private Integer vncPort;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Offer.class)
	@JoinTable(name = "Offer_Node", joinColumns = { @JoinColumn(name = "id_node") }, inverseJoinColumns = { @JoinColumn(name = "offer_id") })
	private Offer offer;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Nodes_Resources.class)
	@JoinTable(name = "Node_Resource", joinColumns = { @JoinColumn(name = "id_node") }, inverseJoinColumns = { @JoinColumn(name = "id_nodes_resources") })
	private Set<Nodes_Resources> resources = new HashSet<Nodes_Resources>();

	public Nodes() {

	}

	public Nodes(Integer id_node, String node_name, Integer idImage,
			Integer cpu, Integer ram, String icon, String description,
			Offer offer, Set<Nodes_Resources> resources) {
		super();
		this.id_node = id_node;
		this.node_name = node_name;
		this.idImage = idImage;
		this.cpu = cpu;
		this.ram = ram;
		this.icon = icon;
		this.description = description;
		this.offer = offer;
		this.resources = resources;
	}

	public Integer getId_node() {
		return id_node;
	}

	public void setId_node(Integer id_node) {
		this.id_node = id_node;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public Integer getIdImage() {
		return idImage;
	}

	public void setIdImage(Integer idImage) {
		this.idImage = idImage;
	}

	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getRam() {
		return ram;
	}

	public void setRam(Integer ram) {
		this.ram = ram;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Offer getScOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Set<Nodes_Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Nodes_Resources> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return "Node [ID :" + id_node + ", CPU: " + cpu + ", RAM: " + ram
				+ " , ICON:" + icon + ", Offer: " + offer
				+ ", Nodes_Resources:" + resources + "]";
	}

	public Integer getHd() {
		return hd;
	}

	public void setHd(Integer hd) {
		this.hd = hd;
	}

	public String getVncAddress() {
		return vncAddress;
	}

	public void setVncAddress(String vncAddress) {
		this.vncAddress = vncAddress;
	}

	public Integer getVncPort() {
		return vncPort;
	}

	public void setVncPort(Integer vncPort) {
		this.vncPort = vncPort;
	}

}
