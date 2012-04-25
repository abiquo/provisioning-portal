package models;





import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import play.mvc.Controller;

public class DateParts extends Model{ 
	
 private String year;
 private String month;
 private String day;
 
 public DateParts(){
	 
 }
public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
public String getMonth() {
	return month;
}
public void setMonth(String month) {
	this.month = month;
}
public String getDay() {
	return day;
}
public void setDay(String day) {
	this.day = day;
}
}
