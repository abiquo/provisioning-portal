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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import play.db.jpa.GenericModel;

/**
 * 
 * @author David López Deployed virtual machine resources such as harddisk and
 *         networks. Currently only harddisk is taken into consideration. Refer
 *         User_Consumption, Deploy_Bundle, Deploy_Bundle_Nodes
 */
@Entity
public class Deploy_Nodes_Resources extends GenericModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer iddeploy_nodes_resources;

	private Integer sequence;
	private Integer resourceType;
	private Long value;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "Deploy_Node_Resource", joinColumns = { @JoinColumn(name = "iddeploy_nodes_resources") }, inverseJoinColumns = { @JoinColumn(name = "idbundle_nodes") })
	private Deploy_Bundle_Nodes deploy_Bundle_Nodes;

	public Deploy_Nodes_Resources() {
		super();

	}

	public Deploy_Nodes_Resources(Integer iddeploy_nodes_resources,
			Integer sequence, Integer resourceType, Long value,
			Deploy_Bundle_Nodes deploy_Bundle_Nodes) {
		super();
		this.iddeploy_nodes_resources = iddeploy_nodes_resources;
		this.sequence = sequence;
		this.resourceType = resourceType;
		this.value = value;
		this.deploy_Bundle_Nodes = deploy_Bundle_Nodes;
	}

	public Integer getIddeploy_nodes_resources() {
		return iddeploy_nodes_resources;
	}

	public void setIddeploy_nodes_resources(Integer iddeploy_nodes_resources) {
		this.iddeploy_nodes_resources = iddeploy_nodes_resources;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getResourceType() {
		return resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Deploy_Bundle_Nodes getDeploy_Bundle_Nodes() {
		return deploy_Bundle_Nodes;
	}

	public void setDeploy_Bundle_Nodes(Deploy_Bundle_Nodes deploy_Bundle_Nodes) {
		this.deploy_Bundle_Nodes = deploy_Bundle_Nodes;
	}

}
