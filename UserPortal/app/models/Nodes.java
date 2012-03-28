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
@Entity
public class Nodes extends GenericModel{

	@Id
	private Integer id_node;
	private String node_name;
	private Integer idImage;
	private Integer cpu;
	private Integer ram;
	private String icon;
	private String description;
	
	@ManyToOne( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = sc_offer.class)
	@JoinTable(name = "Offer_Node", joinColumns = { @JoinColumn(name = "id_node") }, inverseJoinColumns = { @JoinColumn(name = "sc_offer_id") })
	
	private sc_offer scOffer;
		
	@OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Nodes_Resources.class)
	@JoinTable(name = "Node_Resource", joinColumns = { @JoinColumn(name = "id_node") }, inverseJoinColumns = { @JoinColumn(name = "id_nodes_resources") })
	
	private Set<Nodes_Resources> resources = new HashSet<Nodes_Resources>();

	
	public Nodes() {
		
	}

	public Nodes(Integer id_node, String node_name, Integer idImage,
			Integer cpu, Integer ram, String icon, String description,
			sc_offer scOffer, Set<Nodes_Resources> resources) {
		super();
		this.id_node = id_node;
		this.node_name = node_name;
		this.idImage = idImage;
		this.cpu = cpu;
		this.ram = ram;
		this.icon = icon;
		this.description = description;
		this.scOffer = scOffer;
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

	public sc_offer getScOffer() {
		return scOffer;
	}

	public void setScOffer(sc_offer scOffer) {
		this.scOffer = scOffer;
	}

	public Set<Nodes_Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Nodes_Resources> resources) {
		this.resources = resources;
	}
	
	@Override
	public String toString(){
	return "Node [ID :"+ id_node + ", CPU: " + cpu +", RAM: "+ ram +" , ICON:"+ icon + ", sc_offer: "+ scOffer + ", Nodes_Resources:" + resources + "]";
}
	
}
