package com.mydrawer.bean;

public class Member 
{
	private String mbrSk = null;
	private String email = null;
	private String name = null;

	private String registrationDt = null;
	private String lastLoginDt = null;
	private String loginCnt = null;

	private String statusInd = null;

	public String getMbrSk() {
		return mbrSk;
	}

	public void setMbrSk(String mbrSk) {
		this.mbrSk = mbrSk;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegistrationDt() {
		return registrationDt;
	}

	public void setRegistrationDt(String registrationDt) {
		this.registrationDt = registrationDt;
	}

	public String getLastLoginDt() {
		return lastLoginDt;
	}

	public void setLastLoginDt(String lastLoginDt) {
		this.lastLoginDt = lastLoginDt;
	}

	public String getLoginCnt() {
		return loginCnt;
	}

	public void setLoginCnt(String loginCnt) {
		this.loginCnt = loginCnt;
	}

	public String getStatusInd() {
		return statusInd;
	}

	public void setStatusInd(String statusInd) {
		this.statusInd = statusInd;
	}

}
