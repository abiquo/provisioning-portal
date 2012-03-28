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

@Entity
public class Nodes_Resources extends GenericModel{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id_nodes_resources;
	
	//@Column(columnDefinition = "default '1'")
	private Integer sequence;
	private Integer resourceType;
	private Long value;
	
	@ManyToOne( cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
	@JoinTable(name = "Node_Resource", joinColumns = { @JoinColumn(name = "id_nodes_resources") }, inverseJoinColumns = { @JoinColumn(name = "id_node") })
	private Nodes node;
	
	public Nodes_Resources(){
		
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
