package models;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.GenericModel;

/**
 * 
 * @author Harpreet Kaur
 * The offer i.e virtual appliance details that gets deployed 
 * Refer also User_Consumption
 */
@Entity

@NamedQueries({
	@NamedQuery(name = "getVdcIdByVappId", query = "select d.vdc_name from Deploy_Bundle as d where d.vapp_id = ?1")
})
public class Deploy_Bundle extends GenericModel{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer bundle_id;
	private Integer deploy_datacenter;
	private String deploy_hypervisorType;
	//@Column(length = 45)
	private String deploy_network;
	
	private Integer vdc_name;
	@Column(length = 30)
	private String vapp_name;
	private Integer vapp_id;
	/*
	@ManyToOne( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = User_Consumption.class)
	@JoinTable(name = "UserConsumption_DeployNode", joinColumns = { @JoinColumn(name = "bundle_id") }, inverseJoinColumns = { @JoinColumn(name = "iduser_consumption") })
*/
	@OneToOne( cascade = CascadeType.ALL)
	private User_Consumption userConsumption;
	
	@OneToMany( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Deploy_Bundle_Nodes.class)
	@JoinTable(name = "Bundle_Node", joinColumns = { @JoinColumn(name = "bundle_id") }, inverseJoinColumns = { @JoinColumn(name = "idbundle_nodes") })
	private Set<Deploy_Bundle_Nodes> nodes = new HashSet<Deploy_Bundle_Nodes>();

	
	public Deploy_Bundle() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(Integer bundle_id) {
		this.bundle_id = bundle_id;
	}

	public Integer getDeploy_datacenter() {
		return deploy_datacenter;
	}

	public void setDeploy_datacenter(Integer deploy_datacenter) {
		this.deploy_datacenter = deploy_datacenter;
	}

	public String getDeploy_hypervisorType() {
		return deploy_hypervisorType;
	}

	public void setDeploy_hypervisorType(String deploy_hypervisorType) {
		this.deploy_hypervisorType = deploy_hypervisorType;
	}

	public String getDeploy_network() {
		return deploy_network;
	}

	public void setDeploy_network(String deploy_network) {
		this.deploy_network = deploy_network;
	}

	public Integer getVdc_name() {
		return vdc_name;
	}

	public void setVdc_name(Integer vdc_name) {
		this.vdc_name = vdc_name;
	}

	public String getVapp_name() {
		return vapp_name;
	}

	public void setVapp_name(String vapp_name) {
		this.vapp_name = vapp_name;
	}

	
	public Integer getVapp_id() {
		return vapp_id;
	}


	public void setVapp_id(Integer vapp_id) {
		this.vapp_id = vapp_id;
	}


	public User_Consumption getUserConsumption() {
		return userConsumption;
	}

	public void setUserConsumption(User_Consumption userConsumption) {
		this.userConsumption = userConsumption;
	}

	public Set<Deploy_Bundle_Nodes> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Deploy_Bundle_Nodes> nodes) {
		this.nodes = nodes;
	}
	

}
