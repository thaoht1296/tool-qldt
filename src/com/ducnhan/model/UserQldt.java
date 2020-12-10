package com.ducnhan.model;

import java.util.Date;

public class UserQldt {

	private String username; // Khoa chinh
	private String password;
	private String name;
	private Date date;

	public UserQldt(String username, String password, String name, Date date) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "UserQldt [username=" + username + ", password=" + password + ", name=" + name + ", date=" + date + "]";
	}

}
