package edu.swust.ministruts2.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionContext {

	private HttpServletRequest request;
	
	private HttpServletResponse response;

	/**
	 * 线程安全问题的解决
	 * 当前线程的id为key 不需要传key
	 */
	private static ThreadLocal<ActionContext> local = new ThreadLocal<ActionContext>();
//	private static ActionContext context;

	public static ActionContext getContext() {
		return local.get();
	}

	public static void setContext(ActionContext context) {
		local.set(context);
	}

	public ActionContext(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public static void name() {
		
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	
	
}
