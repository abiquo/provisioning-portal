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
