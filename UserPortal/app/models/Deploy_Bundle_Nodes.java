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

import play.db.jpa.GenericModel;

/**
 * 
 * @author David López Virtual machine details that gets deployed when user buy
 *         an offer. Refer also OfferPurchased, Deploy_Bundle
 */

@Entity
public class Deploy_Bundle_Nodes extends GenericModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idbundle_nodes;
	private String node_template_name;
	private String node_name;
	private Integer node_id;
	private Integer cpu;
	private Integer ram;
	private String vdrpIP;
	private Integer vdrpPort;
	private String vdrp_password;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Deploy_Bundle.class)
	@JoinTable(name = "Bundle_Node", joinColumns = { @JoinColumn(name = "idbundle_nodes") }, inverseJoinColumns = { @JoinColumn(name = "bundle_id") })
	private Deploy_Bundle deploy_bundle;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Deploy_Nodes_Resources.class)
	@JoinTable(name = "Deploy_Node_Resource", joinColumns = { @JoinColumn(name = "idbundle_nodes") }, inverseJoinColumns = { @JoinColumn(name = "iddeploy_nodes_resources") })
	private Set<Deploy_Nodes_Resources> resources = new HashSet<Deploy_Nodes_Resources>();

	public Deploy_Bundle_Nodes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdbundle_nodes() {
		return idbundle_nodes;
	}

	public void setIdbundle_nodes(Integer idbundle_nodes) {
		this.idbundle_nodes = idbundle_nodes;
	}

	public String getNode_template_name() {
		return node_template_name;
	}

	public void setNode_template_name(String node_template_name) {
		this.node_template_name = node_template_name;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public Integer getNode_id() {
		return node_id;
	}

	public void setNode_id(Integer node_id) {
		this.node_id = node_id;
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

	public String getVdrpIP() {
		return vdrpIP;
	}

	public void setVdrpIP(String vdrpIP) {
		this.vdrpIP = vdrpIP;
	}

	public Integer getVdrpPort() {
		return vdrpPort;
	}

	public void setVdrpPort(Integer vdrpPort) {
		this.vdrpPort = vdrpPort;
	}

	public String getVdrp_password() {
		return vdrp_password;
	}

	public void setVdrp_password(String vdrp_password) {
		this.vdrp_password = vdrp_password;
	}

	public Deploy_Bundle getDeploy_bundle() {
		return deploy_bundle;
	}

	public void setDeploy_bundle(Deploy_Bundle deploy_bundle) {
		this.deploy_bundle = deploy_bundle;
	}

	public Set<Deploy_Nodes_Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Deploy_Nodes_Resources> resources) {
		this.resources = resources;
	}

}
