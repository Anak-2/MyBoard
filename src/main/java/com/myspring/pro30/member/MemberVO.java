package com.myspring.pro30.member;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component("memberVO")
public class MemberVO {
	private String id;
	private String pwd;
	private String name;
	private String email;
	private Date joinDate;

	public MemberVO() {
	
	}

	public MemberVO(String id, String pwd, String name, String email) {
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
	}

	public MemberVO(MemberVO memberVO) {
		this.id = memberVO.getId();
		this.pwd = memberVO.getPwd();
		this.name = memberVO.getName();
		this.email = memberVO.getEmail();
		this.joinDate = new Date(memberVO.getJoinDate().getTime());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public String toString() {
		return "[Id]: "+getId()+
				" [Pwd]: "+getPwd()+
				" [Name]: "+getName()+
				" [Email]: "+getEmail(); 
	}
}
