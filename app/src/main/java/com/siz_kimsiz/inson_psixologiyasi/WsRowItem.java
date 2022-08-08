package com.siz_kimsiz.inson_psixologiyasi;

public class WsRowItem {

	private String member_name;
	private int profile_pic_id;
	private String status;
	private String links;
	private String contactType;

	public WsRowItem(String member_name, int profile_pic_id, String status, String links,
                     String contactType) {

		this.member_name = member_name;
		this.profile_pic_id = profile_pic_id;
		this.status = status;
		this.links = links;
		this.contactType = contactType;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}


	public int getProfile_pic_id() {
		return profile_pic_id;
	}

	public void setProfile_pic_id(int profile_pic_id) {
		this.profile_pic_id = profile_pic_id;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}


	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

}