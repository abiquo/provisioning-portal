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
package controllers;

import java.util.List;

import javax.persistence.Query;

import models.MKT_Configuration;
import play.db.jpa.JPA;

public class MarketDAO {

	public static List<MKT_Configuration> getMKTConfiguration(
			Integer enterprise_id) {
		Query query1 = JPA.em().createNamedQuery("getMktConfiguration");
		query1.setParameter(1, enterprise_id);
		List<MKT_Configuration> mkt_conf = query1.getResultList();
		return mkt_conf;
	}

	public static List<MKT_Configuration> getDeployEnterprise(
			Integer enterprise_id) {
		Query query1 = JPA.em().createNamedQuery("getDeployEnterprise");
		query1.setParameter(1, enterprise_id);
		List<MKT_Configuration> mkt_conf = query1.getResultList();
		return mkt_conf;
	}
	/*
	 * public static List<mkt_enterprise_view> getOffersForEnterprise ( Integer
	 * enterprise_id , String service_level) { Query query =
	 * JPA.em().createNamedQuery("getOffersForEnterprise");
	 * query.setParameter(1, enterprise_id); query.setParameter(2,
	 * service_level); List<mkt_enterprise_view> resultSet3 =
	 * query.getResultList(); return resultSet3; }
	 */
}
