package models;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;

public class VirtualAppliancePortal {
	private VirtualAppliance virtualAppliance;
	private sc_offer offer;

	public VirtualAppliance getVirtualAppliance() {
		return virtualAppliance;
	}

	public void setVirtualAppliance(final VirtualAppliance virtualAppliance) {
		this.virtualAppliance = virtualAppliance;
	}

	public sc_offer getOffer() {
		return offer;
	}

	public void setOffer(final sc_offer offer) {
		this.offer = offer;
	}

}
