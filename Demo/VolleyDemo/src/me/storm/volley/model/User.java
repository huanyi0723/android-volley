package me.storm.volley.model;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = -6236121247136067778L;
	public Long user_id;
	/** 昵称 */
	public String user_name;
	/** 登录帐号 */
	public String user_login;
	/** 用户密码 */
	public String user_phone_pass;
	/** 用户状态码, 品委会的是2 */
	public Integer user_status;
	public Integer follower_num;
	public Boolean follow_flag;
	/** 头像 */
	public String avatar_path;
	/** 品委会的描述 */
	public String aboutme;
	public String device_id;
	
	
	/**
	 * 是否是品委会
	 * @return
	 */
	public boolean isCommission(){
		// status == 2 的是品委会
		return user_status != null && user_status == 2;
	}


	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_name=" + user_name + ", user_status=" + user_status
				+ ", follower_num=" + follower_num + ", follow_flag=" + follow_flag + ", avatar_path=" + avatar_path
				+ ", aboutme=" + aboutme + ", device_id=" + device_id + "]";
	}
}
