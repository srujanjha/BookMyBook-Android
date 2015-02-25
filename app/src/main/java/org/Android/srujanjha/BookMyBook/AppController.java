package org.android.srujanjha.bookmybook;

/**
 * Created by Srujan Jha on 01-01-2015.
 */

    import android.app.Application;
    import android.text.TextUtils;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.toolbox.ImageLoader;
    import com.android.volley.toolbox.Volley;

    public class AppController extends Application {

        public static final String TAG = AppController.class.getSimpleName();

        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;

        private static AppController mInstance;

        @Override
        public void onCreate() {
            super.onCreate();
            mInstance = this;
        }

        public static synchronized AppController getInstance() {
            return mInstance;
        }

        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }

            return mRequestQueue;
        }

        public ImageLoader getImageLoader() {
            getRequestQueue();
            if (mImageLoader == null) {
                mImageLoader = new ImageLoader(this.mRequestQueue,
                        new LruBitmapCache());
            }
            return this.mImageLoader;
        }
    }