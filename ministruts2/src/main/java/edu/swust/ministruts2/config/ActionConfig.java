package edu.swust.ministruts2.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于封装mini.xml中的参数
 *
 */
public class ActionConfig {
    //默认方法
	private static final String DEFAULT_METHOD = "execute";
    //类的名称
	private String name;
	//类的路径
	private String path;
	//待访问的方法
	private String method = DEFAULT_METHOD;
	//结果视图
	private Map<String, ResultView> views = new HashMap<String, ResultView>();
	
	public void addResult(String key, ResultView value) {
		views.put(key, value);
	}
	public ResultView getResult(String key) {
		return views.get(key);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	@Override
	public String toString() {
		return "ActionConfig [name=" + name + ", path=" + path + ", method=" + method + ", views=" + views + "]";
	}

	
	
    
}
