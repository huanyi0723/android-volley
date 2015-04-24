使用方法
http://blog.csdn.net/guolin_blog/article/details/17482095
1. Volley简介
Android系统中主要提供了两种方式来进行HTTP通信，HttpURLConnection和HttpClient
2. 下载Volley
3. StringRequest的用法
前面已经说过，Volley的用法非常简单，那么我们就从最基本的HTTP通信开始学习吧，即发起一条HTTP请求，然后接收HTTP响应。
首先需要获取到一个RequestQueue对象，可以调用如下方法获取到：
	
	RequestQueue mQueue = Volley.newRequestQueue(context);

注意这里拿到的RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，然后按照一定的算法并发地发出这些请求。
RequestQueue内部的设计就是非常合适高并发的，因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，
这是非常浪费资源的，基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。
接下来为了要发出一条HTTP请求，我们还需要创建一个StringRequest对象，如下所示：

	StringRequest stringRequest = new StringRequest("http://www.baidu.com",
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Log.d("TAG", response);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("TAG", error.getMessage(), error);
							}
						});

可以看到，这里new出了一个StringRequest对象，StringRequest的构造函数需要传入三个参数，第一个参数就是目标服务器的URL地址，
第二个参数是服务器响应成功的回调，第三个参数是服务器响应失败的回调。其中，目标服务器地址我们填写的是百度的首页，
然后在响应成功的回调里打印出服务器返回的内容，在响应失败的回调里打印出失败的详细信息。
最后，将这个StringRequest对象添加到RequestQueue里面就可以了，如下所示：

	mQueue.add(stringRequest); 

没错，百度返回给我们的就是这样一长串的HTML代码，虽然我们看起来会有些吃力，但是浏览器却可以轻松地对这段HTML代码进行解析，
然后将百度的首页展现出来。
这样的话，一个最基本的HTTP发送与响应的功能就完成了。你会发现根本还没写几行代码就轻易实现了这个功能，
主要就是进行了以下三步操作：
1. 创建一个RequestQueue对象。
2. 创建一个StringRequest对象。
3. 将StringRequest对象添加到RequestQueue里面。
不过大家都知道，HTTP的请求类型通常有两种，GET和POST，刚才我们使用的明显是一个GET请求，
那么如果想要发出一条POST请求应该怎么做呢？StringRequest中还提供了另外一种四个参数的构造函数，
其中第一个参数就是指定请求类型的，我们可以使用如下方式进行指定：

	StringRequest stringRequest = new StringRequest(Method.POST, url,  listener, errorListener);

可是这只是指定了HTTP请求方式是POST，那么我们要提交给服务器的参数又该怎么设置呢？很遗憾，
StringRequest中并没有提供设置POST参数的方法，但是当发出POST请求的时候，
Volley会尝试调用StringRequest的父类——Request中的getParams()方法来获取POST参数，那么解决方法自然也就有了，
我们只需要在StringRequest的匿名类中重写getParams()方法，在这里设置POST参数就可以了，代码如下所示：

	StringRequest stringRequest = new StringRequest(Method.POST, url,  listener, errorListener) {
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		Map<String, String> map = new HashMap<String, String>();
		map.put("params1", "value1");
		map.put("params2", "value2");
		return map;
	}
	};

4. JsonRequest的用法
学完了最基本的StringRequest的用法，我们再来进阶学习一下JsonRequest的用法。类似于StringRequest，
JsonRequest也是继承自Request类的，不过由于JsonRequest是一个抽象类，因此我们无法直接创建它的实例，
那么只能从它的子类入手了。JsonRequest有两个直接的子类，JsonObjectRequest和JsonArrayRequest，
从名字上你应该能就看出它们的区别了吧？一个是用于请求一段JSON数据的，一个是用于请求一段JSON数组的。
至于它们的用法也基本上没有什么特殊之处，先new出一个JsonObjectRequest对象，如下所示：

	JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null,
		new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d("TAG", response.toString());
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});

可以看到，这里我们填写的URL地址是 http://m.weather.com.cn/data/101010100.html
这是中国天气网提供的一个查询天气信息的接口，响应的数据就是以JSON格式返回的，然后我们在onResponse()方法中将返回的数据打印出来。
最后再将这个JsonObjectRequest对象添加到RequestQueue里就可以了，如下所示：

	mQueue.add(jsonObjectRequest);  

由此可以看出，服务器返回给我们的数据确实是JSON格式的，并且onResponse()方法中携带的参数也正是一个JSONObject对象，
之后只需要从JSONObject对象取出我们想要得到的那部分数据就可以了。
你应该发现了吧，JsonObjectRequest的用法和StringRequest的用法基本上是完全一样的，Volley的易用之处也在这里体现出来了，
会了一种就可以让你举一反三，因此关于JsonArrayRequest的用法相信已经不需要我再去讲解了吧。

1. ImageRequest的用法
前面我们已经学习过了StringRequest和JsonRequest的用法，并且总结出了它们的用法都是非常类似的，基本就是进行以下三步操作即可：
1. 创建一个RequestQueue对象。
2. 创建一个Request对象。
3. 将Request对象添加到RequestQueue里面。
其中，StringRequest和JsonRequest都是继承自Request的，所以它们的用法才会如此类似。那么不用多说，今天我们要学习的ImageRequest，
相信你从名字上就已经猜出来了，它也是继承自Request的，因此它的用法也是基本相同的，首先需要获取到一个RequestQueue对象，
可以调用如下方法获取到：
	
	RequestQueue mQueue = Volley.newRequestQueue(context);  

接下来自然要去new出一个ImageRequest对象了，代码如下所示：

	ImageRequest imageRequest = new ImageRequest(
		"http://c1.mifile.cn/f/i/2014/cn/goods/mipad/p-114.jpg",
		new Response.Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				imageView.setImageBitmap(response);
			}
		}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				imageView.setImageResource(R.drawable.default_image);
			}
		});

可以看到，ImageRequest的构造函数接收六个参数，第一个参数就是图片的URL地址，这个没什么需要解释的。
第二个参数是图片请求成功的回调，这里我们把返回的Bitmap参数设置到ImageView中。
第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，
则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。
第五个参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，其中ARGB_8888可以展示最好的颜色属性，
每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个字节大小。
第六个参数是图片请求失败的回调，这里我们当请求失败时在ImageView中显示一张默认图片。
最后将这个ImageRequest对象添加到RequestQueue里就可以了，如下所示：

	mQueue.add(imageRequest); 

现在如果运行一下程序，并尝试发出这样一条网络请求，很快就能看到网络上的图片在ImageView中显示出来了

2. ImageLoader的用法
如果你觉得ImageRequest已经非常好用了，那我只能说你太容易满足了 ^_^。实际上，
Volley在请求网络图片方面可以做到的还远远不止这些，而ImageLoader就是一个很好的例子。
ImageLoader也可以用于加载网络上的图片，并且它的内部也是使用ImageRequest来实现的，
不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，还可以过滤掉重复的链接，避免重复发送请求。
由于ImageLoader已经不是继承自Request的了，所以它的用法也和我们之前学到的内容有所不同，总结起来大致可以分为以下四步：
1. 创建一个RequestQueue对象。
2. 创建一个ImageLoader对象。
3. 获取一个ImageListener对象。
4. 调用ImageLoader的get()方法加载网络上的图片。
下面我们就来按照这个步骤，学习一下ImageLoader的用法吧。首先第一步的创建RequestQueue对象我们已经写过很多遍了，
相信已经不用再重复介绍了，那么就从第二步开始学习吧，新建一个ImageLoader对象，代码如下所示：

	ImageLoader imageLoader = new ImageLoader(mQueue, new ImageCache() {
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
	}

	@Override
	public Bitmap getBitmap(String url) {
		return null;
	}
	});

可以看到，ImageLoader的构造函数接收两个参数，第一个参数就是RequestQueue对象，第二个参数是一个ImageCache对象，
这里我们先new出一个空的ImageCache的实现即可。接下来需要获取一个ImageListener对象，代码如下所示：

	ImageListener listener = ImageLoader.getImageListener(imageView,
		R.drawable.default_image, R.drawable.failed_image);

我们通过调用ImageLoader的getImageListener()方法能够获取到一个ImageListener对象，getImageListener()方法接收三个参数，
第一个参数指定用于显示图片的ImageView控件，第二个参数指定加载图片的过程中显示的图片，第三个参数指定加载图片失败的情况下
显示的图片。
最后，调用ImageLoader的get()方法来加载图片，代码如下所示：

	imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg", listener);  

get()方法接收两个参数，第一个参数就是图片的URL地址，第二个参数则是刚刚获取到的ImageListener对象。当然，
如果你想对图片的大小进行限制，也可以使用get()方法的重载，指定图片允许的最大宽度和高度，如下所示：

	imageLoader.get("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",
				listener, 200, 200);

现在运行一下程序并开始加载图片，你将看到ImageView中会先显示一张默认的图片，等到网络上的图片加载完成后，
ImageView则会自动显示该图，效果如下图所示。

虽然现在我们已经掌握了ImageLoader的用法，但是刚才介绍的ImageLoader的优点却还没有使用到。为什么呢？
因为这里创建的ImageCache对象是一个空的实现，完全没能起到图片缓存的作用。其实写一个ImageCache也非常简单，
但是如果想要写一个性能非常好的ImageCache，最好就要借助Android提供的LruCache功能了，如果你对LruCache还不了解，
可以参考我之前的一篇博客Android高效加载大图、多图解决方案，有效避免程序OOM。
这里我们新建一个BitmapCache并实现了ImageCache接口，如下所示：

	public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mCache;

	public BitmapCache() {
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}

	}

可以看到，这里我们将缓存图片的大小设置为10M。接着修改创建ImageLoader实例的代码，第二个参数传入BitmapCache的实例，如下所示：

	ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());  

3. NetworkImageView的用法
除了以上两种方式之外，Volley还提供了第三种方式来加载网络图片，即使用NetworkImageView。不同于以上两种方式，
NetworkImageView是一个自定义控制，它是继承自ImageView的，具备ImageView控件的所有功能，并且在原生的基础之上加入了加载网络图片的功能。
NetworkImageView控件的用法要比前两种方式更加简单，大致可以分为以下五步：
1. 创建一个RequestQueue对象。
2. 创建一个ImageLoader对象。
3. 在布局文件中添加一个NetworkImageView控件。
4. 在代码中获取该控件的实例。
5. 设置要加载的图片地址。

其中，第一第二步和ImageLoader的用法是完全一样的，因此这里我们就从第三步开始学习了。首先修改布局文件中的代码，
在里面加入NetworkImageView控件，如下所示：
	
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Send Request" />
    
    <com.android.volley.toolbox.NetworkImageView 
        android:id="@+id/network_image_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_gravity="center_horizontal"
        />

	</LinearLayout>

接着在Activity获取到这个控件的实例，这就非常简单了，代码如下所示：
	
	networkImageView = (NetworkImageView) findViewById(R.id.network_image_view); 

得到了NetworkImageView控件的实例之后，我们可以调用它的setDefaultImageResId()方法、setErrorImageResId()方法和setImageUrl()方法
来分别设置加载中显示的图片，加载失败时显示的图片，以及目标图片的URL地址，如下所示：

	networkImageView.setDefaultImageResId(R.drawable.default_image);
	networkImageView.setErrorImageResId(R.drawable.failed_image);
	networkImageView.setImageUrl("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg",
				imageLoader);

其中，setImageUrl()方法接收两个参数，第一个参数用于指定图片的URL地址，第二个参数则是前面创建好的ImageLoader对象。
好了，就是这么简单，现在重新运行一下程序，你将看到和使用ImageLoader来加载图片一模一样的效果，这里我就不再截图了。
这时有的朋友可能就会问了，使用ImageRequest和ImageLoader这两种方式来加载网络图片，都可以传入一个最大宽度和高度的参数来对图片进行压缩，
而NetworkImageView中则完全没有提供设置最大宽度和高度的方法，那么是不是使用NetworkImageView来加载的图片都不会进行压缩呢？
其实并不是这样的，NetworkImageView并不需要提供任何设置最大宽高的方法也能够对加载的图片进行压缩。这是由于NetworkImageView是一个控件，
在加载图片的时候它会自动获取自身的宽高，然后对比网络图片的宽度，再决定是否需要对图片进行压缩。也就是说，
压缩过程是在内部完全自动化的，并不需要我们关心，NetworkImageView会始终呈现给我们一张大小刚刚好的网络图片，不会多占用任何一点内存，
这也是NetworkImageView最简单好用的一点吧。
当然了，如果你不想对图片进行压缩的话，其实也很简单，只需要在布局文件中把NetworkImageView的layout_width和layout_height都
设置成wrap_content就可以了，这样NetworkImageView就会将该图片的原始大小展示出来，不会进行任何压缩。
这样我们就把使用Volley来加载网络图片的用法都学习完了，今天的讲解也就到此为止，下一篇文章中我会带大家继续探究Volley的更多功能。
感兴趣的朋友请继续阅读Android Volley完全解析(三)，定制自己的Request。

经过前面两篇文章的学习，我们已经掌握了Volley各种Request的使用方法，包括StringRequest、JsonRequest、ImageRequest等。
其中StringRequest用于请求一条普通的文本数据，JsonRequest(JsonObjectRequest、JsonArrayRequest)用于请求一条JSON格式的数据，
ImageRequest则是用于请求网络上的一张图片。
可是Volley提供给我们的Request类型就只有这么多，而我们都知道，在网络上传输的数据通常有两种格式，JSON和XML，
那么如果想要请求一条XML格式的数据该怎么办呢？其实很简单，Volley提供了非常强的扩展机制，
使得我们可以很轻松地定制出任意类型的Request，这也就是本篇文章的主题了。

1. 自定义XMLRequest

下面我们准备自定义一个XMLRequest，用于请求一条XML格式的数据。那么该从哪里开始入手呢？额，好像是有些无从下手。
遇到这种情况，我们应该去参考一下Volley的源码，看一看StringRequest是怎么实现的，然后就可以模仿着写出XMLRequest了。
首先看下StringRequest的源码，如下所示：

public class StringRequest extends Request<String> {
    private final Listener<String> mListener;

    
    public StringRequest(int method, String url, Listener<String> listener,
            ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    
    public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}

以看到，StringRequest的源码很简练，根本就没几行代码，我们一起来分析下。首先StringRequest是继承自Request类的，
Request可以指定一个泛型类，这里指定的当然就是String了，接下来StringRequest中提供了两个有参的构造函数，参数包括请求类型，
请求地址，以及响应回调等，由于我们已经很熟悉StringRequest的用法了，相信这几个参数的作用都不用再解释了吧。
但需要注意的是，在构造函数中一定要调用super()方法将这几个参数传给父类，因为HTTP的请求和响应都是在父类中自动处理的。

另外，由于Request类中的deliverResponse()和parseNetworkResponse()是两个抽象方法，因此StringRequest中需要对这两个方法进行实现。
deliverResponse()方法中的实现很简单，仅仅是调用了mListener中的onResponse()方法，并将response内容传入即可，
这样就可以将服务器响应的数据进行回调了。parseNetworkResponse()方法中则应该对服务器响应的数据进行解析，
其中数据是以字节的形式存放在NetworkResponse的data变量中的，这里将数据取出然后组装成一个String，
并传入Response的success()方法中即可。

了解了StringRequest的实现原理，下面我们就可以动手来尝试实现一下XMLRequest了，代码如下所示：

public class XMLRequest extends Request<XmlPullParser> {

	private final Listener<XmlPullParser> mListener;

	public XMLRequest(int method, String url, Listener<XmlPullParser> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
	}

	public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
		this(Method.GET, url, listener, errorListener);
	}

	@Override
	protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
		try {
			String xmlString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(xmlString));
			return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (XmlPullParserException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(XmlPullParser response) {
		mListener.onResponse(response);
	}

}

可以看到，其实并没有什么太多的逻辑，基本都是仿照StringRequest写下来的，XMLRequest也是继承自Request类的，
只不过这里指定的泛型类是XmlPullParser，说明我们准备使用Pull解析的方式来解析XML。在parseNetworkResponse()方法中，
先是将服务器响应的数据解析成一个字符串，然后设置到XmlPullParser对象中，在deliverResponse()方法中则是将XmlPullParser对象进行回调。

好了，就是这么简单，下面我们尝试使用这个XMLRequest来请求一段XML格式的数据。
http://flash.weather.com.cn/wmaps/xml/china.xml
这个接口会将中国所有的省份数据以XML格式进行返回，如下所示：

确定了访问接口后，我们只需要在代码中按照以下的方式来使用XMLRequest即可：
	XMLRequest xmlRequest = new XMLRequest(
		"http://flash.weather.com.cn/wmaps/xml/china.xml",
		new Response.Listener<XmlPullParser>() {
			@Override
			public void onResponse(XmlPullParser response) {
				try {
					int eventType = response.getEventType();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_TAG:
							String nodeName = response.getName();
							if ("city".equals(nodeName)) {
								String pName = response.getAttributeValue(0);
								Log.d("TAG", "pName is " + pName);
							}
							break;
						}
						eventType = response.next();
					}
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
	mQueue.add(xmlRequest);

可以看到，这里XMLRequest的用法和StringRequest几乎是一模一样的，我们先创建出一个XMLRequest的实例，并把服务器接口地址传入，
然后在onResponse()方法中解析响应的XML数据，并把每个省的名字打印出来，最后将这个XMLRequest添加到RequestQueue当中。

2. 自定义GsonRequest

JsonRequest的数据解析是利用Android本身自带的JSONObject和JSONArray来实现的，配合使用JSONObject和JSONArray就可以解析出
任意格式的JSON数据。不过也许你会觉得使用JSONObject还是太麻烦了，还有很多方法可以让JSON数据解析变得更加简单，
比如说GSON。遗憾的是，Volley中默认并不支持使用自家的GSON来解析数据，不过没有关系，通过上面的学习，
相信你已经知道了自定义一个Request是多么的简单，那么下面我们就来举一反三一下，自定义一个GsonRequest。

接着定义一个GsonRequest继承自Request，代码如下所示：

public class GsonRequest<T> extends Request<T> {

	private final Listener<T> mListener;

	private Gson mGson;

	private Class<T> mClass;

	public GsonRequest(int method, String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mGson = new Gson();
		mClass = clazz;
		mListener = listener;
	}

	public GsonRequest(String url, Class<T> clazz, Listener<T> listener,
			ErrorListener errorListener) {
		this(Method.GET, url, clazz, listener, errorListener);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mGson.fromJson(jsonString, mClass),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

}

可以看到，GsonRequest是继承自Request类的，并且同样提供了两个构造函数。在parseNetworkResponse()方法中，
先是将服务器响应的数据解析出来，然后通过调用Gson的fromJson方法将数据组装成对象。在deliverResponse方法中仍然是将最终的数据进行回调。

那么下面我们就来测试一下这个GsonRequest能不能够正常工作吧，调用
http://www.weather.com.cn/data/sk/101010100.html
这个接口可以得到一段JSON格式的天气数据，如下所示：

接下来我们使用对象的方式将这段JSON字符串表示出来。新建一个Weather类，代码如下所示：
public class Weather {

	private WeatherInfo weatherinfo;

	public WeatherInfo getWeatherinfo() {
		return weatherinfo;
	}

	public void setWeatherinfo(WeatherInfo weatherinfo) {
		this.weatherinfo = weatherinfo;
	}

}

Weather类中只是引用了WeatherInfo这个类。接着新建WeatherInfo类，代码如下所示：
public class WeatherInfo {

	private String city;

	private String temp;

	private String time;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}

WeatherInfo类中含有city、temp、time这几个字段。下面就是如何调用GsonRequest了，其实也很简单，代码如下所示：
GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(
		"http://www.weather.com.cn/data/sk/101010100.html", Weather.class,
		new Response.Listener<Weather>() {
			@Override
			public void onResponse(Weather weather) {
				WeatherInfo weatherInfo = weather.getWeatherinfo();
				Log.d("TAG", "city is " + weatherInfo.getCity());
				Log.d("TAG", "temp is " + weatherInfo.getTemp());
				Log.d("TAG", "time is " + weatherInfo.getTime());
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
mQueue.add(gsonRequest);

可以看到，这里onResponse()方法的回调中直接返回了一个Weather对象，我们通过它就可以得到WeatherInfo对象，
接着就能从中取出JSON中的相关数据了。现在运行一下代码，观察控制台日志，打印数据如下图所示：