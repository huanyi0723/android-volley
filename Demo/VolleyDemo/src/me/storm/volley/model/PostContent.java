package me.storm.volley.model;


public class PostContent {
	
	public long id;
	public User post_author; //用户信息
	public String post_date; //更新时间
	public String post_title; //标题
	public Post_Content_Filtered post_content_filtered; //图片加简介 自己解析 后面改成类
	public int comment_count; //评论数
	public int view_count; //点击数
	public int rec_count; // 推荐数
	
}
