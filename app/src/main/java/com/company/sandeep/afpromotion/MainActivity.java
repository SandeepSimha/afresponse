package com.company.sandeep.afpromotion;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.retrofit.Ok3Client;

import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pojo.AFResponse;
import pojo.Promotions;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    List<Promotions> mList;
    private RecyclerView recyclerView;
    PromotionAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("A&F Promotional Card");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        progressBar.setVisibility(View.VISIBLE);

        //callApi();
        callMyApi();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void callMyApi() {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .cache(new Cache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024)) // 10 MB //LRU
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (isNetworkAvailable(MainActivity.this)) {
                            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                        } else {
                            request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();


        RestAdapter retrofit = new RestAdapter.Builder()
                .setClient(new Ok3Client(client))
                .setEndpoint(RestService.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        RestService restService = retrofit.create(RestService.class);
        restService.getFeed(new Callback<AFResponse>() {
            @Override
            public void success(final AFResponse afResponse, Response response) {
                mList = afResponse.getPromotions();
                adapter = new PromotionAdapter(mList, R.layout.promotion_layout, MainActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

                recyclerView.addOnItemTouchListener(new RecyclerTouchlistener(getApplicationContext(), recyclerView,
                        new ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Intent intent = new Intent(MainActivity.this, FirstActivity.class);

                                Promotions p = afResponse.getPromotions().get(position);
                                intent.putExtra("data", p);

                                startActivity(intent);
                            }

                            @Override
                            public void onLongClick(View view, int position) {
//                                Toast.makeText(MainActivity.this, "onLongClick-- " + position, Toast.LENGTH_SHORT).show();
                            }
                        }));
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "Failure--", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    class RecyclerTouchlistener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTouchlistener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}
