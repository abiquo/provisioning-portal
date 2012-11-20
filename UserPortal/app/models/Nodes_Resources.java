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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * 
 * @author David Lopez The virtual machine resources such as harddisks ,
 *         network. Currently only harddisk is considered .
 * 
 */
@Entity
public class Nodes_Resources extends GenericModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id_nodes_resources;

	// @Column(columnDefinition = "default '1'")
	private Integer sequence;
	private Integer resourceType;
	private Long value;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "Node_Resource", joinColumns = { @JoinColumn(name = "id_nodes_resources") }, inverseJoinColumns = { @JoinColumn(name = "id_node") })
	private Nodes node;

	public Nodes_Resources() {

	}

	public Nodes_Resources(Integer id_nodes_resources, Integer sequence,
			Integer resourceType, Long value, Nodes node) {
		super();
		this.id_nodes_resources = id_nodes_resources;
		this.sequence = sequence;
		this.resourceType = resourceType;
		this.value = value;
		this.node = node;
	}

	public Integer getId_nodes_resources() {
		return id_nodes_resources;
	}

	public void setId_nodes_resources(Integer id_nodes_resources) {
		this.id_nodes_resources = id_nodes_resources;
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

	public Nodes getNode() {
		return node;
	}

	public void setNode(Nodes node) {
		this.node = node;
	}

}
