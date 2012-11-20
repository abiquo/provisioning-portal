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
package portal.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.Query;

import models.OfferPurchased;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.monitor.VirtualApplianceMonitor;
import org.jclouds.rest.AuthorizationException;
import org.joda.time.Days;
import org.joda.time.Months;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import com.abiquo.server.core.cloud.VirtualApplianceState;
import com.google.common.collect.Lists;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import controllers.Mails;

/**
 * Undeploys the virtual applinace after lease period has expired. Runs as
 * scheduled job. Set @Every ()to required undeploy timer.
 */

@OnApplicationStart
public class UndeployScheduler extends Job {

	@Override
	public void doJob() {
		Logger.info(" ------------INSIDE  doJob-------------");
		Query query = JPA.em().createQuery(
				"select op from OfferPurchased as op");
		List<OfferPurchased> result = query.getResultList();
		Logger.info("No of Results retrieved : " + result.size());
		for (OfferPurchased record : result) {
			Date currentDate = new Date();
			Date expDate = record.getExpiration();

			// Get days from difference
			Integer days = Integer.parseInt(DurationFormatUtils
					.formatDurationHMS(
							expDate.getTime() - currentDate.getTime()).split(
							":")[0]) / 24;

			final Collection<Integer> daysToSendMail = Lists.newArrayList(1, 2,
					3, 4, 5, 7, 15, 30);
			if (expDate.before(currentDate)) {
				Logger.info("Lease has expired.");
				deleteOffer(record);
				Mails.sendExpiredEmail(record);
			} else if (daysToSendMail.contains(days)) {
				Logger.info("Lease is almost expired.");
				Mails.sendExtendEmail(record);
			}
		}
		Logger.info(" ------------EXITING  doJob-------------");
	}

	public static void deleteOffer(OfferPurchased offerPurchased) {

		Properties props = new Properties();
		// load a properties file
		try {
			props.load(new FileInputStream(Play
					.getFile("conf/config.properties")));
			final String admin = props.getProperty("admin");
			final String password = props.getProperty("password");
			AbiquoContext context = Context.getApiClient(admin, password);
			if (context != null) {
				AbiquoUtils.setAbiquoUtilsContext(context);
				try {

					VirtualDatacenter vdc = context
							.getCloudService()
							.getVirtualDatacenter(
									offerPurchased.getIdVirtualDatacenterUser());
					VirtualAppliance vapp = vdc
							.getVirtualAppliance(offerPurchased
									.getIdVirtualApplianceUser());

					VirtualApplianceMonitor monitorVapp = context
							.getMonitoringService()
							.getVirtualApplianceMonitor();
					AsyncTask[] undeployTasks = vapp.undeploy();
					monitorVapp.awaitCompletionUndeploy(vapp);

					if (vapp.getState() == VirtualApplianceState.NOT_DEPLOYED) {
						vapp.delete();
						vdc.delete();
						offerPurchased.delete();
					} else {
						AbiquoUtils.checkErrorsInTasks(undeployTasks);
						Logger.info("Tasks Checked");
					}
					Logger.info("OFFER DELETED ......");
					Logger.info("------------EXITING CONSUMER DEPLOY()--------------");

				} catch (AuthorizationException ae) {

					Logger.warn(ae, "EXCEPTION OCCURED IN deploy()");
				} catch (Exception ae) {

					Logger.warn(ae, "EXCEPTION OCCURED  IN deploy()");
					if (context != null) {
						context.close();
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * static void undeployOffer(Integer vdc_id, Integer vapp_id, Integer vm_id)
	 * { AbiquoContext context = null; try{
	 * Logger.info("-------------INSIDE UNDEPLOY()---------------------");
	 * context = Context.getContext("hkaur", "hkaur"); VirtualDatacenter
	 * vdc_undploy = context.getCloudService().getVirtualDatacenter(vdc_id); //
	 * .findVirtualDatacenter(VirtualDatacenterPredicates.name(
	 * "Single VM/Private Net ONLY")); if ( vdc_undploy == null)
	 * Logger.info("Specified datacenter doesnot exist"); else {
	 * Logger.info(" VDC to undeploy :" + vdc_undploy.getName());
	 * VirtualAppliance va_undeploy = vdc_undploy.getVirtualAppliance(vapp_id);
	 * //VirtualAppliance va_undeploy =
	 * vdc_undploy.findVirtualAppliance(VirtualAppliancePredicates
	 * .name("2/9/12-1")); if ( va_undeploy == null)
	 * Logger.info("Specified virtual appliance doesnot exist"); else {
	 * Logger.info(" VDC to undeploy :" + va_undeploy.getName()); VirtualMachine
	 * vm_undeploy = va_undeploy.getVirtualMachine(vm_id); if ( vm_undeploy ==
	 * null) Logger.info("Specified virtual machine doesnot exist"); else {
	 * Logger.info("undeploying VM....."); vm_undeploy.undeploy();
	 * Logger.info("undeploying VA....."); //AsyncTask task =
	 * vm_undeploy.undeploy(); //Logger.info(task.getState());
	 * Logger.info("found VM"); } Logger.info("undeploying VA.....");
	 * va_undeploy.undeploy(); Logger.info("undeployed VA....."); } }
	 * 
	 * } catch(Exception e) {
	 * 
	 * } finally{ context.close(); }
	 * 
	 * }
	 */

}
