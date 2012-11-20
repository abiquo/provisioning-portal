package models;

import java.util.ArrayList;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;

public class VirtualDatacenterFull {

	private VirtualDatacenter virtualDatacenter;
	private ArrayList<VirtualAppliance> listVirtualAppliances;

	public VirtualDatacenterFull(final VirtualDatacenter virtualDatacenter,
			final ArrayList<VirtualAppliance> listVirtualAppliances) {
		this.setVirtualDatacenter(virtualDatacenter);
		this.setListVirtualAppliances(listVirtualAppliances);
	}

	public ArrayList<VirtualAppliance> getListVirtualAppliances() {
		return listVirtualAppliances;
	}

	public void setListVirtualAppliances(
			ArrayList<VirtualAppliance> listVirtualAppliances) {
		this.listVirtualAppliances = listVirtualAppliances;
	}

	public VirtualDatacenter getVirtualDatacenter() {
		return virtualDatacenter;
	}

	public void setVirtualDatacenter(VirtualDatacenter virtualDatacenter) {
		this.virtualDatacenter = virtualDatacenter;
	}
}
