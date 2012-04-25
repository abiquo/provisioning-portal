package portal.util;

import org.jclouds.abiquo.AbiquoContext;

public class CurrentUserContext {
	public static ThreadLocal<AbiquoContext> userThreadLocal = new ThreadLocal<AbiquoContext>();
	public static final ThreadLocal<String> usernameThread = new ThreadLocal<String>();
	//public static final ThreadLocal<String> username = new ThreadLocal<String>();
	
	
	public static void setContext(AbiquoContext context)
	{
		userThreadLocal.set(context);
	}
	public static void unsetContext()
	{
		userThreadLocal.remove();
	}
	public static AbiquoContext getContext()
	{
		return userThreadLocal.get();
	}
	
	public static void setUser(String username)
	{
		usernameThread.set(username);
	}
	public static void unsetUSer()
	{
		usernameThread.remove();
	}
	public static String getUser()
	{
		return usernameThread.get();
	}
	
	
	
	
}
