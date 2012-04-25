package models;

import play.*;
import play.data.validation.Unique;
import play.db.jpa.*;

import javax.persistence.*;



import java.util.*;


/**
 * 
 * @author Harpreet Kaur
 * Offer( i.e virtual appliance)  added to service catalog by the producer.
 * See also Nodes 
 */
@Entity

/*@Table( uniqueConstraints = {
		@UniqueConstraint(columnNames = "sc_offer_name")})
*/
@NamedQueries({
@NamedQuery(name="getAllOffers",query="select p.sc_offer_id from sc_offer as p "),
@NamedQuery(name="groupByVDC",query="select p from sc_offer as p GROUP BY p.virtualDataCenter_name"),
@NamedQuery(name="groupByVDC_EnterpriseView",query="select p from sc_offer as p where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) GROUP BY p.virtualDataCenter_name"),
@NamedQuery(name="getOfferDetails",query="select p from sc_offer as p where sc_offer_id = ?1 "),
@NamedQuery(name="getVappListForVDC",query="select p from sc_offer as p where p.virtualDataCenter_name = ?1"),
@NamedQuery(name="getVappListForVDC_EnterpriseView",query="select p from sc_offer as p where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) and  p.virtualDataCenter_name = ?2")
})

public class sc_offer extends GenericModel {
    
	@Id
	private Integer sc_offer_id;
	
	private String sc_offer_name;
	public String icon_name;
	public Blob icon;
	private Blob image;
	@Column(length = 30)
	private String short_description;
	private String description;
	private Integer datacenter;
	private String hypervisorType;
	@Column(length = 45)
	private String default_network_type;
	private Integer idVirtualDataCenter_ref;
	@Column(length = 40)
	private String service_type;
	private String virtualDataCenter_name;
	
	@OneToMany( cascade = CascadeType.ALL,  fetch = FetchType.LAZY, targetEntity = Nodes.class)
	@JoinTable(name = "Offer_Node", joinColumns = { @JoinColumn(name = "sc_offer_id") }, inverseJoinColumns = { @JoinColumn(name = "id_node") })
	private Set<Nodes> nodes = new HashSet<Nodes>();

	
	
	public sc_offer() {
		
	}
	
	
	
	
	public sc_offer(Integer sc_offer_id, String sc_offer_name,
			String icon_name, Blob icon, Blob image, String short_description,
			String description, Integer datacenter, String hypervisorType,
			String default_network_type, Integer idVirtualDataCenter_ref,
			String service_type, String virtualDataCenter_name, Set<Nodes> nodes) {
		super();
		this.sc_offer_id = sc_offer_id;
		this.sc_offer_name = sc_offer_name;
		this.icon_name = icon_name;
		this.icon = icon;
		this.image = image;
		this.short_description = short_description;
		this.description = description;
		this.datacenter = datacenter;
		this.hypervisorType = hypervisorType;
		this.default_network_type = default_network_type;
		this.idVirtualDataCenter_ref = idVirtualDataCenter_ref;
		this.service_type = service_type;
		this.virtualDataCenter_name = virtualDataCenter_name;
		this.nodes = nodes;
	}




	public Integer getIdVirtualDataCenter_ref() {
		return idVirtualDataCenter_ref;
	}

	public String getVirtualDatacenter_name() {
		return virtualDataCenter_name;
	}

	public void setVirtualDatacenter_name(String virtualDatacenter_name) {
		this.virtualDataCenter_name = virtualDatacenter_name;
	}

	public void setIdVirtualDataCenter_ref(Integer idVirtualDataCenter_ref) {
		this.idVirtualDataCenter_ref = idVirtualDataCenter_ref;
	}

	public Integer getSc_offer_id() {
		return sc_offer_id;
	}
	
	
	public void setSc_offer_id(Integer sc_offer_id) {
		this.sc_offer_id = sc_offer_id;
	}
	
	public String getSc_offer_name() {
		return sc_offer_name;
	}
	public void setSc_offer_name(String sc_offer_name) {
		this.sc_offer_name = sc_offer_name;
	}
	
	public Blob getIcon() {
		return icon;
	}
	public void setIcon(Blob icon) {
		this.icon = icon;
	}
	
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getDatacenter() {
		return datacenter;
	}
	public void setDatacenter(Integer datacenter) {
		this.datacenter = datacenter;
	}
	
	public String getHypervisorType() {
		return hypervisorType;
	}
	public void setHypervisorType(String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}
	
	public String getDefault_network_type() {
		return default_network_type;
	}
	public void setDefault_network_type(String default_network_type) {
		this.default_network_type = default_network_type;
	}
	
	public Integer getId_VirtualDatacenter_ref() {
		return idVirtualDataCenter_ref;
	}
	public void setId_VirtualDatacenter_ref(Integer id_VirtualDatacenter_ref) {
		this.idVirtualDataCenter_ref = id_VirtualDatacenter_ref;
	}
	
	public String getService_type() {
		return service_type;
	}
	public void setService_type(String service_type) {
		this.service_type = service_type;
	}
	
	public Set<Nodes> getNodes() {
		return this.nodes;
	}

	public void setNodes(Set<Nodes> nodes) {
		this.nodes = nodes;
	}


	public String getVirtualDataCenter_name() {
		return virtualDataCenter_name;
	}

	public void setVirtualDataCenter_name(String virtualDataCenter_name) {
		this.virtualDataCenter_name = virtualDataCenter_name;
	}

	

	@Override
	public String toString(){
	return "sc_offer [ID :"+ sc_offer_id + ", Name : " + sc_offer_name +", Icon: "+ icon +" , Datacenter:"+ datacenter + ", Hypervisor Type: "+ hypervisorType + ", Service Type:" + service_type + "]";
	}


	public Blob getImage() {
		return image;
	}


	public void setImage(Blob image) {
		this.image = image;
	}


	public String getIcon_name() {
		return icon_name;
	}


	public void setIcon_name(String icon_name) {
		this.icon_name = icon_name;
	}
	
}
