package com.bvaleo.taskpostapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bvaleo.taskpostapp.activities.UserActivity;
import com.bvaleo.taskpostapp.adapters.PostsAdapter;
import com.bvaleo.taskpostapp.adapters.clicklisteners.PostsAdapterClickListener;
import com.bvaleo.taskpostapp.model.Post;
import com.bvaleo.taskpostapp.net.IPostService;
import com.bvaleo.taskpostapp.util.Constants;
import com.bvaleo.taskpostapp.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_main)
    public RecyclerView recyclerView;
    @BindView(R.id.iv_main)
    public ImageView imageView;
    @BindView(R.id.btn_save_logcat)
    public Button mBtnSave;
    @BindView(R.id.rg_indicators)
    public RadioGroup rgIndicators;

    private GridLayoutManager mLayoutManager;
    private PostsAdapter mAdapter;
    private IPostService mService;
    private int childOfRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Picasso.with(this)
                .load(Constants.IMAGE_URL)
                .error(R.drawable.error_case)
                .into(imageView);

        imageView.setAnimation(getImageAnimation());
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLogCatToFile();
            }
        });

        childOfRadioGroup = rgIndicators.getChildCount();
        mService = NetworkUtil.getPostService();
        mAdapter = new PostsAdapter(new ArrayList<Post>(0));
        mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(getClickListener());
        recyclerView.addOnScrollListener(getScrollListener());

        loadPosts();

    }

    private void loadPosts() {
        mService.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()){
                    mAdapter.updateData(response.body());
                } else {
                    Log.e(Constants.MAIN_ACTIVITY, Constants.RESPONSE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e(Constants.MAIN_ACTIVITY, Constants.RETROFIT_ERROR, t);
            }
        });
    }

    private void saveLogCatToFile() {
        if (isStoragePermissionGranted()) {

            File appDirectory = new File(Environment.getExternalStorageDirectory() + Constants.APP_DIRECTORY);
            File logDirectory = new File(appDirectory + Constants.APP_LOG);
            File logFile = new File(logDirectory, "logcat" + System.currentTimeMillis() + ".log");

            if (!appDirectory.exists()) {
                appDirectory.mkdir();
            }

            if (!logDirectory.exists()) {
                logDirectory.mkdir();
            }
            try {
                Runtime.getRuntime().exec("logcat -f " + logFile);
                Log.d(Constants.MAIN_ACTIVITY, Constants.SAVE_LOGCAT);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, Constants.WRITE_EXTERNAL_STORAGE, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    private Animation getImageAnimation() {
        Animation popin = AnimationUtils.loadAnimation(this, R.anim.popin);
        popin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnSave.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return popin;
    }

    @NonNull
    private PostsAdapterClickListener getClickListener() {
        return new PostsAdapterClickListener(this, recyclerView, new PostsAdapterClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra(Constants.PARCELABLE_DATA, mAdapter.getPost(position));
                Log.d(Constants.MAIN_ACTIVITY, "Start UserActivity for Post #" + Integer.toString(position + 1));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @NonNull
    private RecyclerView.OnScrollListener getScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int itemCount = mAdapter.getItemCount();
                int elementPerDot = itemCount / childOfRadioGroup + 1;
                int lastVisibleElement = mLayoutManager.findLastVisibleItemPosition();
                int currentActiveDot = lastVisibleElement / elementPerDot;
                ((RadioButton) rgIndicators.getChildAt(currentActiveDot)).setChecked(true);
            }
        };
    }
}
