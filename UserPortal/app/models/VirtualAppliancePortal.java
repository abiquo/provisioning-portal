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
