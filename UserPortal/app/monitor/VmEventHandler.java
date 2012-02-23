package monitor;
import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.events.handlers.AbstractEventHandler;
import org.jclouds.abiquo.events.monitor.CompletedEvent;
import org.jclouds.abiquo.events.monitor.FailedEvent;
import org.jclouds.abiquo.events.monitor.MonitorEvent;
import org.jclouds.abiquo.events.monitor.TimeoutEvent;
import org.jclouds.abiquo.monitor.VirtualMachineMonitor;

import com.google.common.eventbus.Subscribe;

/**
 * Handles events related to a concrete virtual machine.
 */
public class VmEventHandler extends AbstractEventHandler<VirtualMachine>
{
    /** Used to close the context when the job finishes. */
    private AbiquoContext context;

    /** The monitored virtual machine. */
    private VirtualMachine vm;

    public VmEventHandler(final AbiquoContext context, final VirtualMachine vm)
    {
        super();
        this.context = context;
        this.vm = vm;
    }

    /**
     * Async monitors will receive all events, so we need to be careful and handle only the events
     * we are interested in.
     * 
     * @param event The populated event. It holds the monitored object in the event.getTarget()
     *            property.
     * @return A boolean indicating if this handler instance must handle the given event.
     */
    @Override
    protected boolean handles(final MonitorEvent<VirtualMachine> event)
    {
        return event.getTarget().getId().equals(vm.getId());
    }

    /**
     * This method will be called when the monitored job completes without error.
     */
    @Subscribe  //The subscribe annotation registers the method as an event handler.
    public void onComplete(final CompletedEvent<VirtualMachine> event)
    {
        if (handles(event))
        {
            System.out.println("VM " + event.getTarget().getName() + " deployed");

            // Stop listening to events and close the context (in this example when the vm is
            // deployed the application should end)
            unregisterAndClose();
        }
    }

    /**
     * This method will be called when the monitored job fails.
     */
    @Subscribe  //The subscribe annotation registers the method as an event handler.
    public void onFailure(final FailedEvent<VirtualMachine> event)
    {
        if (handles(event))
        {
            System.out.println("Deployment for" + event.getTarget().getName() + " failed");

            // Stop listening to events and close the context (in this example when the vm is
            // deployed the application should end)
            unregisterAndClose();
        }
    }

    /**
     * This method will be called when the monitored job times out.
     * <p>
     * In our example we are not invoking the
     * {@link VirtualMachineMonitor#monitorDeploy(Long, java.util.concurrent.TimeUnit, VirtualMachine...)}
     * method to specify a timeout, so this method will never be called.
     */
    @Subscribe  //The subscribe annotation registers the method as an event handler.
    public void onTimeout(final TimeoutEvent<VirtualMachine> event)
    {
        if (handles(event))
        {
            System.out.println("Deployment for vm " + event.getTarget().getName() + " timed out");

            // Stop listening to events and close the context (in this example when the vm is
            // deployed the application should end)
            unregisterAndClose();
        }
    }

    /**
     * Unregisters the handler and closes the context.
     */
    private void unregisterAndClose()
    {
        context.getMonitoringService().unregister(this);
        context.close();
        System.out.println("Terminating monitoring thread");
    }
}
