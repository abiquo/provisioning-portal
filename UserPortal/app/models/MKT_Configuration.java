package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;
@Entity
@NamedQueries({
@NamedQuery(name="getMktConfi",query=" select p from MKT_Configuration as p where p.id = ?1")
})
public class MKT_Configuration extends GenericModel{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String mkt_deploy_enterprise;
	private String mkt_deploy_user;
	private String mkt_deploy_pw;
	
	
	public MKT_Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MKT_Configuration(Integer id, String mkt_deploy_enterprise,
			String mkt_deploy_user, String mkt_deploy_pw) {
		super();
		this.id = id;
		this.mkt_deploy_enterprise = mkt_deploy_enterprise;
		this.mkt_deploy_user = mkt_deploy_user;
		this.mkt_deploy_pw = mkt_deploy_pw;
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
	public String getMkt_deploy_enterprise() {
		return mkt_deploy_enterprise;
	}
	public void setMkt_deploy_enterprise(String mkt_deploy_enterprise) {
		this.mkt_deploy_enterprise = mkt_deploy_enterprise;
	}
	
	
	
}
