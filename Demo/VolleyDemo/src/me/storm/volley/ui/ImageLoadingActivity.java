/*
 * Created by Storm Zhang, Feb 13, 2014.
 */

package me.storm.volley.ui;

import me.storm.volley.R;
import me.storm.volley.data.RequestManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ImageLoadingActivity extends BaseActivity {
	private NetworkImageView mImageView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_loading);

		mImageView = (NetworkImageView) findViewById(R.id.iv_image);

		Button btnImageLoadingRequest = (Button) findViewById(R.id.btn_image_loading);
		btnImageLoadingRequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageLoader imageLoader = RequestManager.getImageLoader();
				mImageView.setImageUrl("https://www.baidu.com/img/bd_logo1.png", imageLoader);
			}
		});
	}
}
