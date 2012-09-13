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

import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.db.jpa.GenericModel;
import play.mvc.Controller;
/**
 * 
 * @author David López
 * This model saves all offer entries in a service catalog and their details
 * It might need design reconsideration in future. 
 * start_date and expiration date - not clear 
 */
@Entity
@NamedQueries({
@NamedQuery(name="getUser",query=" select p from UserPortal as p where p.id = ?1")
})
public class UserPortal extends GenericModel{
	
	@Id
	private Integer idAbiquo;	
	private String nick;
	private String email;
	
	
	public UserPortal() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserPortal(final Integer idAbiquo, final String nick, final String email) {
		super();
		this.idAbiquo = idAbiquo;
		this.email = email;
		this.nick = nick;
	}
	
	public Integer getIdAbiquo() {
		return idAbiquo;
	}

	public void setIdAbiquo(Integer idAbiquo) {
		this.idAbiquo = idAbiquo;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

 
}
