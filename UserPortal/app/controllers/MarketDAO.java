package controllers;

import java.util.List;

import javax.persistence.Query;

import models.MKT_Configuration;
import models.mkt_enterprise_view;
import play.db.jpa.JPA;

public class MarketDAO {

	public static List<MKT_Configuration> getMKTConfiguration ( Integer enterprise_id )
	{
			Query query1 = JPA.em().createNamedQuery("getMktConfiguration");
			query1.setParameter(1, enterprise_id);
			List<MKT_Configuration> mkt_conf = query1.getResultList();
			return mkt_conf;
	}
	public static List<MKT_Configuration> getDeployEnterprise ( Integer enterprise_id )
	{
			Query query1 = JPA.em().createNamedQuery("getDeployEnterprise");
			query1.setParameter(1, enterprise_id);
			List<MKT_Configuration> mkt_conf = query1.getResultList();
			return mkt_conf;
	}
	public static List<mkt_enterprise_view> getOffersForEnterprise ( Integer enterprise_id , String service_level)
	{
			Query query = JPA.em().createNamedQuery("getOffersForEnterprise");
			query.setParameter(1, enterprise_id);
			query.setParameter(2, service_level);
			List<mkt_enterprise_view> resultSet3 = query.getResultList();
			return resultSet3;
	}
}
