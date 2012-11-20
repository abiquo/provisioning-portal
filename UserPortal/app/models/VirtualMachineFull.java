package models;

import java.util.List;

import org.jclouds.abiquo.domain.cloud.HardDisk;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;

public class VirtualMachineFull {
	private VirtualMachine virtualMachine;

	private Integer cpu;
	private Integer ram;

	// hd in MBytes
	private Integer hd;

	private String template_name;
	private String template_path;
	// VNC details
	private String vncAddresss;
	private Integer vncPort;

	public VirtualMachineFull(final VirtualMachine virtualMachine) {
		setVirtualMachine(virtualMachine);
	}

	public VirtualMachine getVirtualMachine() {
		return virtualMachine;
	}

	public void setVirtualMachine(VirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
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

	public Integer getHd() {
		return hd;
	}

	public void setHd(Integer hd) {
		this.hd = hd;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public String getTemplate_path() {
		return template_path;
	}

	public void setTemplate_path(String template_path) {
		this.template_path = template_path;
	}

	public String getVncAddresss() {
		return vncAddresss;
	}

	public void setVncAddresss(String vncAddresss) {
		this.vncAddresss = vncAddresss;
	}

	public Integer getVncPort() {
		return vncPort;
	}

	public void setVncPort(Integer vncPort) {
		this.vncPort = vncPort;
	}

}
