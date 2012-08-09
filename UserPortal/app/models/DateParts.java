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

import play.db.jpa.Model;

public class DateParts extends Model {

	private String year;
	private String month;
	private String day;

	public DateParts() {

	}

	public String getYear() {
		return year;
	}

	public void setYear(final String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(final String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(final String day) {
		this.day = day;
	}
}
