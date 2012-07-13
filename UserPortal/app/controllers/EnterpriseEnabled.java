package controllers;

import org.jclouds.abiquo.domain.enterprise.Enterprise;

public class EnterpriseEnabled {

	public Enterprise enterprise;
	public boolean enabled;

	public EnterpriseEnabled(final Enterprise ent, final boolean enable) {
		setEnterprise(ent);
		enabled = enable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(final Enterprise enterprise) {
		this.enterprise = enterprise;
	}

}
