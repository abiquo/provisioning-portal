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

import models.Offer;
import play.db.jpa.JPA;

public class ConsumerDAO {
	public static List<Offer> getPublishedOffers() {
		Query query1 = JPA.em().createNamedQuery("getAllOffersByState");
		query1.setParameter(1, "PUBLISHED");
		List<Offer> listOffers = query1.getResultList();
		return listOffers;
	}

	public static List<Offer> getPublishedOffersByEnt(
			final Integer enterprise_id) {
		Query query1 = JPA.em().createNamedQuery("getAvailableOffersByState");
		query1.setParameter(1, enterprise_id);
		query1.setParameter(2, "PUBLISHED");
		List<Offer> listOffers = query1.getResultList();
		return listOffers;
	}

	public static Integer getVdcId(final Integer vappId) {
		Query query1 = JPA.em().createNamedQuery("getVdcIdByVappId");
		query1.setParameter(1, vappId);
		List<Integer> vdcIdList = query1.getResultList();
		if (!vdcIdList.isEmpty())
			return vdcIdList.get(0);
		else
			return -1;
	}

}
