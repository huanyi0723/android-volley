package me.storm.volley.model;

import java.util.List;


public class PostContentListResp extends ResponseBase{
	
	public PostContentList data;
	public class PostContentList{
		public List<PostContent> post_contents;
		public int limit;
		public long start_id;
	}

}
