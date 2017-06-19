package com.cloudwalkdigital.verifynegotiator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cloudwalkdigital.verifynegotiator.data.models.Auth;
import com.cloudwalkdigital.verifynegotiator.data.models.User;
import com.cloudwalkdigital.verifynegotiator.events.EventSelectionActivity;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity {
    @Inject SharedPreferences sharedPreferences;
    @Inject Retrofit retrofit;

    @BindView(R.id.btn_login) Button mBtnLogin;
    @BindView(R.id.inputEmail) EditText mEmail;
    @BindView(R.id.inputPassword) EditText mPassword;
    @BindView(R.id.loginParentContainer) ConstraintLayout parentView;

    private final String TAG = "LOGINACTIVITY";

    private final String grantType = "password";
    private final Integer clientId = 2;
    private final String clientSecret = "3GsvS8QeY31WT4rovd78LKyXzj6RJ18aM0QJO4a4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind butterknife and dagger
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.btn_login)
    public void doLogin(Button button) {
        button.setText(R.string.btn_logging_in);
        button.setEnabled(false);

        String username = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        // Attempt to login
        AuthTask authTask = new AuthTask();
        authTask.execute(username, password);
    }

    private void resetButton() {
        mBtnLogin.setText(R.string.btn_login);
        mBtnLogin.setEnabled(true);
    }

    private void showFailedSnackbar() {
        Snackbar snackbar = Snackbar.make(parentView, R.string.failed_login, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));

        snackbar.show();
    }

    private void saveUserInformation(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        editor.putString("user", gson.toJson(user));
        editor.apply();
    }

    private void saveAuthInformation(Auth auth) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        editor.putString("auth", gson.toJson(auth));
        editor.apply();
    }

    /**
     * Login
     *
     * @param username
     * @param password
     */
    private Boolean attemptAuth(String username, String password) {
        AuthenticationService service = retrofit.create(AuthenticationService.class);
        Call<Auth> call = service.authenticate(grantType, clientId,
                clientSecret, username, password);

        try {
            Response<Auth> response = call.execute();

            if (! response.isSuccessful()) {
                return false;
            }

            Auth auth = response.body();

            // Get user information
            return getUserInfo(auth);
        } catch (IOException e) {
            e.printStackTrace();
            resetButton();
            showFailedSnackbar();
        }

        return false;
//        call.enqueue(new Callback<Auth>() {
//            @Override
//            public void onResponse(Call<Auth> call, Response<Auth> response) {
//                if (! response.isSuccessful()) {
//                    resetButton();
//                    showFailedSnackbar();
//
//                    return;
//                }
//
//                Auth auth = response.body();
//
//                // Get user information
//                getUserInfo(auth);
//
//                // Save Auth Session into SharedPref
//                saveAuthInformation(auth);
//
//                // Move into the next activity
//                Intent intent = new Intent(LoginActivity.this, EventSelectionActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onFailure(Call<Auth> call, Throwable t) {
//                resetButton();
//                showFailedSnackbar();
//            }
//        });
    }

    /**
     * Get user information
     *
     * @param auth
     * @return
     */
    private Boolean getUserInfo(Auth auth) {
        AuthenticationService service = retrofit.create(AuthenticationService.class);
        Call<User> call = service.getUserInfo("Bearer " + auth.getAccessToken());

        try {
            Response<User> response = call.execute();

            if (! response.isSuccessful()) {
                return false;
            }

            // Save Auth Session into SharedPref
            saveAuthInformation(auth);
            saveUserInformation(response.body());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            resetButton();
            showFailedSnackbar();
        }

        return false;
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (! response.isSuccessful()) {
//                    return;
//                }
//
//                User user = response.body();
//                saveUserInformation(user);
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
    }

    /**
     * Auth service
     */
    public interface AuthenticationService {
        @FormUrlEncoded
        @POST("oauth/token")
        Call<Auth> authenticate(@Field("grant_type") String grantType,
                                @Field("client_id") Integer clientId,
                                @Field("client_secret") String clientSecret,
                                @Field("username") String username,
                                @Field("password") String password);

        @GET("api/v1/me")
        Call<User> getUserInfo(@Header("Authorization") String accessToken);
    }

    public class AuthTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result = attemptAuth(params[0], params[1]);

            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            if (! s) {
                resetButton();
                showFailedSnackbar();

                return;
            }

            // Move into the next activity
            Intent intent = new Intent(LoginActivity.this, EventSelectionActivity.class);
            startActivity(intent);
        }
    }
}
