package com.ducnhan.model;

import java.sql.Timestamp;

public class UsersQldtSchedule {

	private String usernameQldt;
	private String scheduleId;
	private Timestamp created;
	
	public UsersQldtSchedule() {
	}

	public UsersQldtSchedule(String usernameQldt, String scheduleId, Timestamp created) {
		super();
		this.usernameQldt = usernameQldt;
		this.scheduleId = scheduleId;
		this.created = created;
	}

	public String getUsernameQldt() {
		return usernameQldt;
	}

	public void setUsernameQldt(String usernameQldt) {
		this.usernameQldt = usernameQldt;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "UsersQldtSchedule [usernameQldt=" + usernameQldt + ", scheduleId=" + scheduleId + ", created=" + created
				+ "]";
	}
}
