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
package monitor;

import java.io.IOException;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.events.handlers.AbstractEventHandler;
import org.jclouds.abiquo.events.monitor.CompletedEvent;
import org.jclouds.abiquo.events.monitor.FailedEvent;
import org.jclouds.abiquo.events.monitor.MonitorEvent;
import org.jclouds.abiquo.events.monitor.TimeoutEvent;
//import org.jclouds.abiquo.monitor.VirtualMachineMonitor;

import play.Logger;

import com.google.common.eventbus.Subscribe;

import controllers.Consumer;
import controllers.Mails;

/**
 * Handles events related to a concrete virtual machine.
 */
public class VmEventHandler extends AbstractEventHandler<VirtualMachine> {
	/** Used to close the context when the job finishes. */
	private AbiquoContext context;

	/** The monitored virtual machine. */
	private VirtualMachine vm;

	public VmEventHandler(final AbiquoContext context, final VirtualMachine vm) {

		this.context = context;
		this.vm = vm;

	}

	/**
	 * Async monitors will receive all events, so we need to be careful and
	 * handle only the events we are interested in.
	 * 
	 * @param event
	 *            The populated event. It holds the monitored object in the
	 *            event.getTarget() property.
	 * @return A boolean indicating if this handler instance must handle the
	 *         given event.
	 */
	@Override
	protected boolean handles(final MonitorEvent<VirtualMachine> event) {
		System.out.println("---handles-----");
		try {

			System.out.println("Taking care of");
			return event.getTarget().getId().equals(vm.getId());
		} catch (Exception e) {
			Logger.warn(e, "EXCEPTION OCCURED", event.getTarget().getId());
			return false;
		}
	}

	/**
	 * This method will be called when the monitored job completes without
	 * error.
	 */
	@Subscribe
	// The subscribe annotation registers the method as an event handler.
	public void onComplete(final CompletedEvent<VirtualMachine> event) {
		try {
			if (handles(event)) {
				System.out.println("VM " + event.getTarget().getNameLabel()
						+ " deployed");
				unregisterAndClose();
				VirtualMachine vm = event.getTarget();
				System.out.println(" Virtual Machine to monitor  :"
						+ vm.getNameLabel() + " with id " + vm.getId());
				System.out
						.println("VDC : " + vm.getVirtualDatacenter().getId());
				Mails.updateUserConsumption_onSuccess(vm.getVirtualDatacenter()
						.getId(), vm.getVirtualAppliance(), vm.getId(), vm);
				//Mails.sendEmail(vncPort, vncAddress, password, name, offerName, useremail, exp_date)ail(vm.getVirtualDatacenter().getId()
				// ,vm.getVirtualAppliance().getId() , vm.getId());

			}
		} catch (Exception e) {
			Logger.warn(e, "EXCEPTION OCCURED", event.getTarget().getId());
		}
	}

	/**
	 * This method will be called when the monitored job fails.
	 */
	@Subscribe
	// The subscribe annotation registers the method as an event handler.
	public void onFailure(final FailedEvent<VirtualMachine> event) {
		try {

			if (handles(event)) {
				System.out.println("Deployment for"
						+ event.getTarget().getNameLabel() + " failed");
				unregisterAndClose();
				VirtualMachine vm = event.getTarget();
				System.out.println(" Virtual Machine to monitor  :"
						+ vm.getNameLabel() + " with id " + vm.getId());
				Mails.updateUserConsumption_onFailure(vm.getVirtualDatacenter()
						.getId(), vm.getVirtualAppliance(), vm);

			}
		} catch (Exception e) {
			Logger.warn(e, "EXCEPTION OCCURED", event.getTarget().getId());
		}
	}

	/**
	 * This method will be called when the monitored job times out.
	 * <p>
	 * In our example we are not invoking the
	 * {@link VirtualMachineMonitor#monitorDeploy(Long, java.util.concurrent.TimeUnit, VirtualMachine...)}
	 * method to specify a timeout, so this method will never be called.
	 */
	@Subscribe
	// The subscribe annotation registers the method as an event handler.
	public void onTimeout(final TimeoutEvent<VirtualMachine> event) {
		try {

			if (handles(event)) {
				System.out.println("Deployment for vm "
						+ event.getTarget().getNameLabel() + " timed out");
				// Mails.welcome();
				// Stop listening to events and close the context (in this
				// example when the vm is
				// deployed the application should end)
				unregisterAndClose();
			}
		} catch (Exception e) {
			Logger.warn(e, "EXCEPTION OCCURED", event.getTarget().getId());
		}
	}

	/**
	 * Unregisters the handler and closes the context.
	 */
	private void unregisterAndClose() {
		context.getMonitoringService().unregister(this);
		// context.close();
		System.out.println("Terminating monitoring thread");
	}
}
