package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.db.jpa.GenericModel;
import play.mvc.Controller;
@Entity
@NamedQueries({
@NamedQuery(name="getUser",query=" select p from UserDetails as p where p.id = ?1")
})
public class UserDetails extends GenericModel{
	@Id
	private String id;
	private  String emailID;
	
	
	public UserDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDetails(String id, String emailID) {
		super();
		this.id = id;
		this.emailID = emailID;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
 
 
}
