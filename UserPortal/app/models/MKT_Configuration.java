/*******************************************************************************
 * Abiquo community edition
 * cloud management application for hybrid clouds
 *  Copyright (C) 2008-2010 - Abiquo Holdings S.L.
 * 
 *  This application is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU LESSER GENERAL PUBLIC
 *  LICENSE as published by the Free Software Foundation under
 *  version 3 of the License
 * 
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  LESSER GENERAL PUBLIC LICENSE v.3 for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the
 *  Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 *  Boston, MA 02111-1307, USA.
 ******************************************************************************/
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

/**
 * 
 * @author David Lopez This model saves the market configuration details.
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "getMktConfi", query = " select p from MKT_Configuration as p where p.id = ?1"),
		@NamedQuery(name = "getMktConfiguration", query = " select p from MKT_Configuration as p where p.enterprise_id = ?1"),
		@NamedQuery(name = "getDeployEnterprise", query = " select p from MKT_Configuration as p where p.deploy_enterprise_id = ?1") })
public class MKT_Configuration extends GenericModel {

	@Id
	private Integer enterprise_id;
	private String enterprise_name;
	@Required
	private Integer deploy_enterprise_id;
	/* private String mkt_deploy_enterprise; */
	@Required
	private String mkt_deploy_user;
	@Required
	private String mkt_deploy_pw;
	private String mkt_branding_css;
	private String mkt_theme_css;
	private String mkt_branding_icon;
	private String mkt_url;

	public MKT_Configuration() {
		super();

	}

	public String getMkt_deploy_pw() {
		return mkt_deploy_pw;
	}

	public void setMkt_deploy_pw(String mkt_deploy_pw) {
		this.mkt_deploy_pw = mkt_deploy_pw;
	}

	public String getMkt_deploy_user() {
		return mkt_deploy_user;
	}

	public void setMkt_deploy_user(String mkt_deploy_user) {
		this.mkt_deploy_user = mkt_deploy_user;
	}

	/*
	 * public String getMkt_deploy_enterprise() { return mkt_deploy_enterprise;
	 * } public void setMkt_deploy_enterprise(String mkt_deploy_enterprise) {
	 * this.mkt_deploy_enterprise = mkt_deploy_enterprise; }
	 */

	public Integer getEnterprise_id() {
		return enterprise_id;
	}

	public void setEnterprise_id(Integer enterprise_id) {
		this.enterprise_id = enterprise_id;
	}

	public String getMkt_branding_css() {
		return mkt_branding_css;
	}

	public void setMkt_branding_css(String mkt_branding_css) {
		this.mkt_branding_css = mkt_branding_css;
	}

	public String getMkt_theme_css() {
		return mkt_theme_css;
	}

	public void setMkt_theme_css(String mkt_theme_css) {
		this.mkt_theme_css = mkt_theme_css;
	}

	public String getMkt_branding_icon() {
		return mkt_branding_icon;
	}

	public void setMkt_branding_icon(String mkt_branding_icon) {
		this.mkt_branding_icon = mkt_branding_icon;
	}

	public String getMkt_url() {
		return mkt_url;
	}

	public void setMkt_url(String mkt_url) {
		this.mkt_url = mkt_url;
	}

	public String getEnterprise_name() {
		return enterprise_name;
	}

	public void setEnterprise_name(String enterprise_name) {
		this.enterprise_name = enterprise_name;
	}

	public Integer getDeploy_enterprise_id() {
		return deploy_enterprise_id;
	}

	public void setDeploy_enterprise_id(Integer deploy_enterprise_id) {
		this.deploy_enterprise_id = deploy_enterprise_id;
	}

	/*
	 * public sc_offers_subscriptions getScOffer() { return scOffer; }
	 * 
	 * 
	 * 
	 * public void setScOffer(sc_offers_subscriptions scOffer) { this.scOffer =
	 * scOffer; }
	 */

	/*
	 * public Integer getMarket_id() { return market_id; }
	 * 
	 * 
	 * 
	 * public void setMarket_id(Integer market_id) { this.market_id = market_id;
	 * }
	 */

}
