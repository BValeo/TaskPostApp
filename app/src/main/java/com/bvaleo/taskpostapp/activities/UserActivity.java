package com.bvaleo.taskpostapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bvaleo.taskpostapp.R;
import com.bvaleo.taskpostapp.model.Geo;
import com.bvaleo.taskpostapp.model.Post;
import com.bvaleo.taskpostapp.model.User;
import com.bvaleo.taskpostapp.net.IPostService;
import com.bvaleo.taskpostapp.util.Constants;
import com.bvaleo.taskpostapp.util.NetworkUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.exceptions.RealmError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {


    @BindView(R.id.tw_id_post)
    public TextView twId;
    @BindView(R.id.tw_user)
    public TextView mTwUser;
    @BindView(R.id.tw_username)
    public TextView mTwUsername;
    @BindView(R.id.tw_email)
    public TextView mTwEmail;
    @BindView(R.id.tw_site)
    public TextView mTwSite;
    @BindView(R.id.tw_phone)
    public TextView mTwPhone;
    @BindView(R.id.tw_city)
    public TextView mTwCity;
    @BindView(R.id.btn_save_user)
    public Button btnSaveUser;

    private User mUser;
    private Post mPost;
    private IPostService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        mPost = getIntent().getParcelableExtra(Constants.PARCELABLE_DATA);
        mService = NetworkUtil.getPostService();
        mService.getUserById(mPost.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    mUser = response.body();
                    inflateView();
                } else {
                    Toast.makeText(UserActivity.this, Constants.RESPONSE_ERROR, Toast.LENGTH_SHORT).show();
                    Log.e(Constants.USER_ACTIVITY, Constants.RESPONSE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(Constants.NETWORK_ERROR, Constants.RETROFIT_ERROR, t);
                finish();
            }
        });

        setClickListenersToElements();
    }


    private void setClickListenersToElements(){
        mTwEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEmailIntent();
            }
        });

        mTwSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSiteIntent();
            }
        });

        mTwPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoneIntent();
            }
        });

        mTwCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMapIntent();
            }
        });

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserToDatabase();
            }
        });
    }


    private void inflateView(){
        getSupportActionBar().setTitle("Contact #" + mPost.getUserId());
        twId.setText(Integer.toString(mPost.getId()));
        mTwUser.setText(mUser.getName());
        mTwUsername.setText(mUser.getUsername());
        mTwEmail.setText(mUser.getEmail());
        mTwSite.setText(mUser.getWebsite());
        mTwPhone.setText(mUser.getPhone());
        mTwCity.setText(mUser.getAddress().getCity());
    }



    private void startEmailIntent(){
        Intent intent = new Intent();
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mTwEmail.getText().toString()});
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,"");
        startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
    }
    private void startSiteIntent(){
        String site = mTwSite.getText().toString();
        if(!site.startsWith(Constants.PREFIX_URL))
            site = Constants.PREFIX_URL.concat(site);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
        startActivity(intent);
    }
    private void startMapIntent(){
        Geo geo = mUser.getAddress().getGeo();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + geo.getLat() + ',' + geo.getLng()));
        startActivity(Intent.createChooser(intent, getString(R.string.open_map)));
    }
    private void startPhoneIntent(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTwPhone.getText().toString()));
        startActivity(intent);
    }


    private void saveUserToDatabase(){
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (realm.where(User.class).equalTo("id", mUser.getId()).findFirst() == null) {
                        realm.copyToRealm(mUser);
                        Log.d(Constants.LOG_REALM, "User have added");
                    } else {
                        Log.d(Constants.LOG_REALM, "User already exist");
                    }

                }
            });

        } catch (RealmError error){
            Log.e(Constants.REALM_ERROR, error.getMessage());
        } finally {
            realm.close();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}