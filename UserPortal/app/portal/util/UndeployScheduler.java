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

import java.util.Calendar;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualDatacenter;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.predicates.cloud.VirtualDatacenterPredicates;
import org.jclouds.abiquo.predicates.cloud.VirtualMachinePredicates;

import controllers.Helper;

import models.Deploy_Bundle;
import models.Deploy_Bundle_Nodes;
import models.OfferPurchased;

import play.Logger;
import play.db.jpa.JPA;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * Undeploys the virtual applinace after lease period has expired.
 * Runs as scheduled job.
 * Set @Every ()to required undeploy timer. 
 */

/*@OnApplicationStart
@Every("1min")
public class UndeployScheduler extends Job {

	@Override
	public void doJob()
	{
			Logger.info(" ------------INSIDE  doJob-------------");
			Query query = JPA.em().createQuery("select p from User_Consumption as p");
			List<User_Consumption> result = query.getResultList();
			Logger.info("No of Results retrieved : " + result.size());
			for ( User_Consumption record : result )
			{
					Integer vdc_id = record.getVdc_id();
					Date exp_date = record.getExpiration_date();
					Logger.info("exp date:"+ exp_date);
					Date current_date = new Date();
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(exp_date);
					cal2.setTime(current_date);
					if(cal1.before(cal2))
					{
								Logger.info("Lease has expired.");
								Set<Deploy_Bundle> deployed_vapps = record.getNodes();
    		
								for (Deploy_Bundle deployed_vapp  :deployed_vapps)
								{
										Integer vapp_id = deployed_vapp.getVapp_id();
										Set<Deploy_Bundle_Nodes> deployed_vms = deployed_vapp.getNodes();
  
										for ( Deploy_Bundle_Nodes deployed_vm : deployed_vms)
										{
												Integer vm_id = deployed_vm.getNode_id();
												Logger.info("vdc, vapp, vm " + vdc_id +" "+  vapp_id +"  " +  vm_id );
												undeployOffer(vdc_id, vapp_id, vm_id);
    						
    			    			   				
										}
								}
					}
    	
			}
			Logger.info(" ------------EXITING  doJob-------------");
	}
	
	 static void undeployOffer(Integer vdc_id, Integer vapp_id, Integer vm_id)
	 {
		 AbiquoContext context = null;
	try{
		 	Logger.info("-------------INSIDE UNDEPLOY()---------------------");
			context = Context.getContext("hkaur", "hkaur");
			VirtualDatacenter vdc_undploy = context.getCloudService().getVirtualDatacenter(vdc_id);
			// .findVirtualDatacenter(VirtualDatacenterPredicates.name("Single VM/Private Net ONLY"));
			 if ( vdc_undploy == null)
			    		Logger.info("Specified datacenter doesnot exist");
			    else 
			    {
			    		Logger.info(" VDC to undeploy :" + vdc_undploy.getName());
			    		VirtualAppliance va_undeploy = vdc_undploy.getVirtualAppliance(vapp_id);
			    		//VirtualAppliance va_undeploy = vdc_undploy.findVirtualAppliance(VirtualAppliancePredicates.name("2/9/12-1"));
			    		if ( va_undeploy == null)
					    		Logger.info("Specified virtual appliance doesnot exist");
					    else 
					    {
					    		Logger.info(" VDC to undeploy :" + va_undeploy.getName());
					    		VirtualMachine vm_undeploy = va_undeploy.getVirtualMachine(vm_id);
					    		if ( vm_undeploy == null)
						    	Logger.info("Specified virtual machine doesnot exist");
						    	else 
						    	{
						    			Logger.info("undeploying VM.....");
						    			vm_undeploy.undeploy();
						    			Logger.info("undeploying VA.....");
						    			//AsyncTask task = vm_undeploy.undeploy();
										//Logger.info(task.getState());
						    			Logger.info("found VM");
						    	}
					    		Logger.info("undeploying VA.....");
					    		va_undeploy.undeploy();
					    		Logger.info("undeployed VA.....");
					    }
			    }
			
		} catch(Exception e)
		{
			
		}
		finally{
				context.close();
	    }
			
	}
	
}
*/
