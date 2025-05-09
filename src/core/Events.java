package core;

import java.sql.Timestamp;

public class Events {
	private int author_id;
	private String name;
	private Timestamp date;
	private int capacity;
	private String address;
	
	public Events(int author_id, String name, Timestamp date, int capacity, String address) {
		this.author_id = author_id;
		this.name = name;
		this.date = date;
		this.capacity = capacity;
		this.address = address;
	}
	public Events(String name, Timestamp date, int capacity, String address) {
		this.name = name;
		this.date = date;
		this.capacity = capacity;
		this.address = address;
	}
	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getDatetime() {
		return date;
	}
	public void setDatetime(Timestamp date) {
		this.date = date;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}