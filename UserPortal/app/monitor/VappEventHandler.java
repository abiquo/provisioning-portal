package monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;

import models.OfferPurchased;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.events.handlers.AbstractEventHandler;
import org.jclouds.abiquo.events.monitor.CompletedEvent;
import org.jclouds.abiquo.events.monitor.FailedEvent;
import org.jclouds.abiquo.events.monitor.MonitorEvent;
import org.jclouds.abiquo.events.monitor.TimeoutEvent;
import org.jclouds.abiquo.monitor.VirtualApplianceMonitor;

import play.Play;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import portal.util.Context;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.Monitor;

import controllers.Mails;
import controllers.ProducerDAO;

public class VappEventHandler extends AbstractEventHandler<VirtualAppliance> {

	private final VirtualApplianceMonitor monitor;

	public VappEventHandler(VirtualApplianceMonitor monitor) {
		this.monitor = monitor;
	}

	@Subscribe
	public void onComplete(final CompletedEvent<VirtualAppliance> event) {
		// Handle completion here
		VirtualAppliance vapp = event.getTarget();
		try {
			JPAPlugin.startTx(Boolean.TRUE);
			List<OfferPurchased> resultSet1 = ProducerDAO
					.getOffersPurchasedFromVappId(vapp.getId());
			OfferPurchased op = resultSet1.get(0);
			Mails.sendEmail(op, vapp);

			Properties props = new Properties();
			// load a properties file
			props.load(new FileInputStream(Play
					.getFile("conf/config.properties")));

			monitor.unregister(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// read only
			JPAPlugin.closeTx(Boolean.FALSE);
		}
	}

	@Subscribe
	public void onFailure(final FailedEvent<VirtualAppliance> event) {
		// Handle failures here
		VirtualAppliance vapp = event.getTarget();
		try {
			JPAPlugin.startTx(Boolean.TRUE);
			List<OfferPurchased> resultSet1 = ProducerDAO
					.getOffersPurchasedFromVappId(vapp.getId());
			OfferPurchased op = resultSet1.get(0);
			//Mails.sendEmail(op, vapp);
			Mails.sendFailureEmail(op.getOffer().getName(), op.getUser().getNick(), op.getUser().getEmail());

			Properties props = new Properties();
			// load a properties file
			props.load(new FileInputStream(Play
					.getFile("conf/config.properties")));

			monitor.unregister(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// read only
			JPAPlugin.closeTx(Boolean.FALSE);
		}
	}

	@Subscribe
	public void onTimeout(final TimeoutEvent<VirtualAppliance> event) {
		// Handle timeout here
		VirtualAppliance vapp = event.getTarget();
		try {
			JPAPlugin.startTx(Boolean.TRUE);
			List<OfferPurchased> resultSet1 = ProducerDAO
					.getOffersPurchasedFromVappId(vapp.getId());
			OfferPurchased op = resultSet1.get(0);
			//Mails.sendEmail(op, vapp);
			Mails.sendFailureEmail(op.getOffer().getName(), op.getUser().getNick(), op.getUser().getEmail());

			Properties props = new Properties();
			// load a properties file
			props.load(new FileInputStream(Play
					.getFile("conf/config.properties")));

			monitor.unregister(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// read only
			JPAPlugin.closeTx(Boolean.FALSE);
		}
	}

	@Override
	protected boolean handles(MonitorEvent<VirtualAppliance> arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
