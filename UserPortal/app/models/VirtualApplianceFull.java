package models;

import java.util.ArrayList;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;

public class VirtualApplianceFull {

	private VirtualAppliance virtualAppliance;
	private ArrayList<VirtualMachineFull> listVirtualMachines;

	public VirtualApplianceFull(final VirtualAppliance virtualAppliance,
			final ArrayList<VirtualMachineFull> listVirtualMachines) {
		this.setVirtualAppliance(virtualAppliance);
		this.setListVirtualMachines(listVirtualMachines);
	}

	public VirtualAppliance getVirtualAppliance() {
		return virtualAppliance;
	}

	public void setVirtualAppliance(VirtualAppliance virtualAppliance) {
		this.virtualAppliance = virtualAppliance;
	}

	public Iterable<VirtualMachineFull> getListVirtualMachines() {
		return listVirtualMachines;
	}

	public void setListVirtualMachines(
			ArrayList<VirtualMachineFull> listVirtualMachines) {
		this.listVirtualMachines = listVirtualMachines;
	}

}
