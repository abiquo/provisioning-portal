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
@Entity
public class Deploy_Nodes_Resources extends GenericModel {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer iddeploy_nodes_resources;
	
	private Integer sequence;
	private Integer resourceType;
	private Long value;

	@ManyToOne( cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
	@JoinTable(name = "Deploy_Node_Resource", joinColumns = { @JoinColumn(name = "iddeploy_nodes_resources") }, inverseJoinColumns = { @JoinColumn(name = "idbundle_nodes") })
	private Deploy_Bundle_Nodes deploy_Bundle_Nodes;

	
	public Deploy_Nodes_Resources() {
		super();
		// TODO Auto-generated constructor stub
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