package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

/**
 * 
 * @author Harpreet Kaur Offer( i.e virtual appliance) added to service catalog
 *         by the producer. See also Nodes
 */
@Entity
/*
 * @Table( uniqueConstraints = {
 * 
 * @UniqueConstraint(columnNames = "sc_offer_name")})
 */
@NamedQueries({
		@NamedQuery(name = "getAllOffersByState", query = "select p from sc_offer as p where p.state = ?1"),
		@NamedQuery(name = "getAvailableOffersByState", query = "select p from sc_offer as p  where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) and  p.state = ?2"),
		@NamedQuery(name = "getAllOffers", query = "select p.sc_offer_id from sc_offer as p "),
		@NamedQuery(name = "groupByVDC", query = "select p from sc_offer as p GROUP BY p.virtualDataCenter_name"),
		@NamedQuery(name = "groupByVDC_EnterpriseView", query = "select p from sc_offer as p where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) GROUP BY p.virtualDataCenter_name"),
		@NamedQuery(name = "getOfferDetails", query = "select p from sc_offer as p where sc_offer_id = ?1 "),
		@NamedQuery(name = "getVappListForVDC", query = "select p from sc_offer as p where p.virtualDataCenter_name = ?1"),
		@NamedQuery(name = "getVappListForVDC_EnterpriseView", query = "select p from sc_offer as p where p.sc_offer_id in ( select s.sc_offer_id from mkt_enterprise_view as s where s.enterprise_id = ?1 ) and  p.virtualDataCenter_name = ?2") })
public class sc_offer extends GenericModel {

	@Id
	private Integer sc_offer_id;

	private String sc_offer_name;
	public String icon_name;
	public Blob icon;
	private Blob image;
	@Column(length = 30)
	@MaxSize(30)
	private String short_description;
	@MaxSize(255)
	private String description;
	private Integer datacenter;
	private String hypervisorType;
	@Column(length = 45)
	private String default_network_type;
	private Integer idVirtualDataCenter_ref;
	private Integer idVirtualAppliance_ref;
	@Column(length = 40)
	private String service_type;
	private String virtualDataCenter_name;
	private String state;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Nodes.class)
	@JoinTable(name = "Offer_Node", joinColumns = { @JoinColumn(name = "sc_offer_id") }, inverseJoinColumns = { @JoinColumn(name = "id_node") })
	private Set<Nodes> nodes = new HashSet<Nodes>();

	public sc_offer() {

	}

	public sc_offer(final Integer sc_offer_id, final String sc_offer_name,
			final String icon_name, final Blob icon, final Blob image,
			final String short_description, final String description,
			final Integer datacenter, final String hypervisorType,
			final String default_network_type,
			final Integer idVirtualDataCenter_ref, final String service_type,
			final String virtualDataCenter_name, final Set<Nodes> nodes) {
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
		setState("NOT_PURCHASED");
	}

	@Override
	public void _delete() {
		super._delete();
		icon.getFile().delete();
		// image.getFile().delete();
	}

	public Integer getIdVirtualDataCenter_ref() {
		return idVirtualDataCenter_ref;
	}

	public String getVirtualDatacenter_name() {
		return virtualDataCenter_name;
	}

	public void setVirtualDatacenter_name(final String virtualDatacenter_name) {
		this.virtualDataCenter_name = virtualDatacenter_name;
	}

	public void setIdVirtualDataCenter_ref(final Integer idVirtualDataCenter_ref) {
		this.idVirtualDataCenter_ref = idVirtualDataCenter_ref;
	}

	public Integer getSc_offer_id() {
		return sc_offer_id;
	}

	public void setSc_offer_id(final Integer sc_offer_id) {
		this.sc_offer_id = sc_offer_id;
	}

	public String getSc_offer_name() {
		return sc_offer_name;
	}

	public void setSc_offer_name(final String sc_offer_name) {
		this.sc_offer_name = sc_offer_name;
	}

	public Blob getIcon() {
		return icon;
	}

	public void setIcon(final Blob icon) {
		this.icon = icon;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(final String short_description) {
		this.short_description = short_description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Integer getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(final Integer datacenter) {
		this.datacenter = datacenter;
	}

	public String getHypervisorType() {
		return hypervisorType;
	}

	public void setHypervisorType(final String hypervisorType) {
		this.hypervisorType = hypervisorType;
	}

	public String getDefault_network_type() {
		return default_network_type;
	}

	public void setDefault_network_type(final String default_network_type) {
		this.default_network_type = default_network_type;
	}

	public Integer getId_VirtualDatacenter_ref() {
		return idVirtualDataCenter_ref;
	}

	public void setId_VirtualDatacenter_ref(
			final Integer id_VirtualDatacenter_ref) {
		this.idVirtualDataCenter_ref = id_VirtualDatacenter_ref;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(final String service_type) {
		this.service_type = service_type;
	}

	public Set<Nodes> getNodes() {
		return this.nodes;
	}

	public void setNodes(final Set<Nodes> nodes) {
		this.nodes = nodes;
	}

	public String getVirtualDataCenter_name() {
		return virtualDataCenter_name;
	}

	public void setVirtualDataCenter_name(final String virtualDataCenter_name) {
		this.virtualDataCenter_name = virtualDataCenter_name;
	}

	@Override
	public String toString() {
		return "sc_offer [ID :" + sc_offer_id + ", Name : " + sc_offer_name
				+ ", Icon: " + icon + " , Datacenter:" + datacenter
				+ ", Hypervisor Type: " + hypervisorType + ", Service Type:"
				+ service_type + "]";
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(final Blob image) {
		this.image = image;
	}

	public String getIcon_name() {
		return icon_name;
	}

	public void setIcon_name(final String icon_name) {
		this.icon_name = icon_name;
	}

	public String getState() {
		return state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public Integer getIdVirtualAppliance_ref() {
		return idVirtualAppliance_ref;
	}

	public void setIdVirtualAppliance_ref(Integer idVirtualAppliance_ref) {
		this.idVirtualAppliance_ref = idVirtualAppliance_ref;
	}

}
