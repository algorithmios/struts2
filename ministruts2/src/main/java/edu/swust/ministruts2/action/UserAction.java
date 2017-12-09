package edu.swust.ministruts2.action;

import edu.swust.ministruts2.context.ActionContext;

public class UserAction {
    public String execute() {
    	System.out.println(ActionContext.getContext().getRequest().getParameter("name"));
		System.out.println("UserAction execute ........");
		return "success";
	}
}
