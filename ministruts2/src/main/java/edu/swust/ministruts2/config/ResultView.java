package edu.swust.ministruts2.config;

public class ResultView {
    private String name;
    private String type;
    private String path;
    public ResultView() {
	}
    
	public ResultView(String name, String type, String path) {
		this.name = name;
		this.type = type;
		this.path = path;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ResultView [name=" + name + ", type=" + type + ", path=" + path + "]";
	}
    
    
}
