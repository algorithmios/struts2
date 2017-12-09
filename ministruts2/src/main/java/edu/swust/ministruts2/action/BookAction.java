package edu.swust.ministruts2.action;

public class BookAction {
	private Long id;
	private String name;
    public String execute() {
		System.out.println("BookAction execute ........");
		System.out.println("id = " + id + ", name = " + name);
		return "error";
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
}
