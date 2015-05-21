##  需要掌握的第三方库
volley
1 干嘛用的？
用来快速从网络下载数据的
下载的数据有 字符串 Json 图片
2 怎么使用

3 具体例子
1 下载字符串
点击按钮事件
	btnRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				executeRequest(new StringRequest(Method.GET, VolleyApi.BAIDU, responseListener(),
						errorListener()));
			}
		});
父类中方法
	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}
统一封装
public class RequestManager {
	private static RequestQueue mRequestQueue; //普通的
	private static ImageLoader mImageLoader; //图像的

	private RequestManager() {
		// no instances
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);

		int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }
	
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}
}
成功与失败的回调方法
	//成功的
	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				mTvResult.setText(response);
			}
		};
	}
	//失败的
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
			}
		};
	}
	
2 下载Json数据 GET方法
	executeRequest(new JsonObjectRequest(Method.GET, VolleyApi.JSON_TEST, null,
						responseListener(), errorListener()));
	private Response.Listener<JSONObject> responseListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mTvResult.setText(response.toString());
			}
		};
	}

3 下载JSON数据 POST方法 向服务端发送数据再接受数据
				executeRequest(new StringRequest(Method.POST, VolleyApi.POST_TEST, responseListener(),
						errorListener()) {
					protected Map<String, String> getParams() {
						return new ApiParams().with("device_id", "865002026443077"); //向服务端发送的数据
					}
				});

4 利用Gson库 自动将Json数据映射成对象
executeRequest(new GsonRequest<PostContentListResp>(VolleyApi.GSON_TEST, PostContentListResp.class,
						responseListener(), errorListener()));

public class PostContentListResp extends ResponseBase{
	
	public PostContentList data;
	public class PostContentList{
		public List<PostContent> post_contents;
		public int limit;
		public long start_id;
	}

}

5 下载图片
NetworkImageView

				ImageLoader imageLoader = RequestManager.getImageLoader();
				mImageView.setImageUrl("https://www.baidu.com/img/bd_logo1.png", imageLoader);


4 源码解析
https://github.com/android-cn/android-open-project-analysis/tree/master/volley

5 常见问题
返回的字符串是乱码解决办法
修改volley源码
HttpHeaderParser类中
	public static String parseCharset(Map<String, String> headers) {
        //return parseCharset(headers, HTTP.DEFAULT_CONTENT_CHARSET);
    	return parseCharset(headers, HTTP.UTF_8);
    }



