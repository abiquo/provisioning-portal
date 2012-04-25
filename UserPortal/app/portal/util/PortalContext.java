package portal.util;

import org.jclouds.abiquo.AbiquoContext;

public class PortalContext {

	private  AbiquoContext context ;
	
	public  void setContext(AbiquoContext contextt)
	{
		this.context= contextt;
	}
	
	public  AbiquoContext getContext( )
	{
		return context;
	}
	
}
