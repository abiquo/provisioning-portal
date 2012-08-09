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

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.db.jpa.Model;

/**
 * 
 * @author Harpreet Kaur
 * This model saves offers enabled for an enterprise
 */
@Entity
@NamedQueries({
@NamedQuery(name="getOffersForEnterprise",query="select distinct p.sc_offer_id from mkt_enterprise_view as p where p.enterprise_id = ?1 and p.service_level = ?2 "),
})
public class mkt_enterprise_view extends Model{

	private Integer enterprise_id;
	private Integer sc_offer_id ;
	private String service_level;
	
	public mkt_enterprise_view() {
		super();
		
	}
	public Integer getEnterprise_id() {
		return enterprise_id;
	}
	public void setEnterprise_id(Integer enterprise_id) {
		this.enterprise_id = enterprise_id;
	}
	public Integer getSc_offer_id() {
		return sc_offer_id;
	}
	public void setSc_offer_id(Integer sc_offer_id) {
		this.sc_offer_id = sc_offer_id;
	}
	public String getService_level() {
		return service_level;
	}
	public void setService_level(String service_level) {
		this.service_level = service_level;
	}
	
}
