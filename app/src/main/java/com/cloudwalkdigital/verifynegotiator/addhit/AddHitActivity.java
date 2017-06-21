package com.cloudwalkdigital.verifynegotiator.addhit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudwalkdigital.verifynegotiator.App;
import com.cloudwalkdigital.verifynegotiator.R;
import com.cloudwalkdigital.verifynegotiator.data.APIService;
import com.cloudwalkdigital.verifynegotiator.data.models.Auth;
import com.cloudwalkdigital.verifynegotiator.data.models.Hit;
import com.cloudwalkdigital.verifynegotiator.data.models.User;
import com.cloudwalkdigital.verifynegotiator.events.EventSelectionActivity;
import com.cloudwalkdigital.verifynegotiator.services.CreateHitService;
import com.cloudwalkdigital.verifynegotiator.services.FileUploadService;
import com.cloudwalkdigital.verifynegotiator.utils.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@RuntimePermissions
public class AddHitActivity extends AppCompatActivity {
    @Inject Retrofit retrofit;
    @Inject SessionManager sessionManager;
    @Inject SharedPreferences sharedPreferences;

    private final String TAG = "ADDHITACTIVITY";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private int projectId;
    private String mCurrentPhotoPath;

    @BindView(R.id.parentView) LinearLayout parentView;

    @BindView(R.id.inputName) EditText mName;
    @BindView(R.id.inputEmail) EditText mEmail;
    @BindView(R.id.inputContactNo) EditText mContactNo;
    @BindView(R.id.inputSchoolName) EditText mSchoolName;
    @BindView(R.id.inputDesignation) EditText mDesignation;
    @BindView(R.id.inputAddress) EditText mAddress;
    @BindView(R.id.inputOtherDetails) EditText mOtherDetails;
    @BindView(R.id.spinnerLocation) Spinner mLocation;
    @BindView(R.id.imageCapture) ImageView mImage;
    @BindView(R.id.btn_submit) Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hit);

        projectId = getIntent().getIntExtra("projectId", 0);

        if (projectId == 0) {
            finish();
            return;
        }

        // Bind
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        setupToolbar();

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.location_arrays, R.layout.spinner_item);
        mLocation.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Creating a new record");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = getBitmap(mCurrentPhotoPath);
            mImage.setImageBitmap(imageBitmap);
            mImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public Bitmap getBitmap(String filepath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        options.inSampleSize= calculateInSampleSize(options, 160, 120);

        return bitmap = BitmapFactory.decodeFile(filepath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @OnClick(R.id.imageCapture)
    public void captureImage() {
        AddHitActivityPermissionsDispatcher.showCameraWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.cloudwalkdigital.verifynegotiator",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.btn_submit)
    public void submit(Button button) {
        if (! checkFields()) {
            return;
        }

        button.setEnabled(false);
        button.setText("Saving Record...");

        Hit hit = new Hit(mName.getText().toString(),
                mSchoolName.getText().toString(),
                mEmail.getText().toString(),
                mContactNo.getText().toString(),
                mDesignation.getText().toString(),
                mAddress.getText().toString(),
                mOtherDetails.getText().toString(),
                mLocation.getSelectedItem().toString());

//        Intent i = new Intent(this, CreateHitService.class);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(hit, Hit.class);
//
//        i.putExtra("projectId", projectId);
//        i.putExtra("hit", json);
//
//        startService(i);

        Auth auth = sessionManager.getAuthInformation();
        APIService service = retrofit.create(APIService.class);
        Call<Hit> call = service.createHit("Bearer " + auth.getAccessToken(), projectId, hit);

        call.enqueue(new Callback<Hit>() {
            @Override
            public void onResponse(Call<Hit> call, Response<Hit> response) {
                Log.i(TAG, "onAPIResponse: " + response.raw().toString());

                if (response.isSuccessful()) {
                    showSuccessSnackbar("Successfully added a hit");

                    Hit responseHit = response.body();
                    Intent i = new Intent(AddHitActivity.this, FileUploadService.class);
                    i.putExtra("hitId", responseHit.getId());
                    i.putExtra("photoPath", mCurrentPhotoPath);

                    startService(i);
                }

                mSubmit.setEnabled(false);
                mSubmit.setText(R.string.btn_submit);
                clearFields();
            }

            @Override
            public void onFailure(Call<Hit> call, Throwable t) {
                Log.i(TAG, "onAPIResponseFailed: " + t.getMessage());
                showFailedSnackbar("Failed to add a hit, check your network connection");

                mSubmit.setEnabled(false);
                mSubmit.setText(R.string.btn_submit);
            }
        });
    }

    private void clearFields() {
        mName.setText("");
        mSchoolName.setText("");
        mEmail.setText("");
        mContactNo.setText("");
        mDesignation.setText("");
        mAddress.setText("");
        mOtherDetails.setText("");
        mLocation.setSelection(0);

        mImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImage.setImageDrawable(getDrawable(R.drawable.camera));
    }

    private Boolean checkFields() {
        if (isEmpty(mSchoolName)) {
            showFailedSnackbar("School name is required");

            return false;
        }

        if (isEmpty(mName)) {
            showFailedSnackbar("Name is required");

            return false;
        }

        if (isEmpty(mEmail)) {
            showFailedSnackbar("Email is required");

            return false;
        }

        if (isEmpty(mContactNo)) {
            showFailedSnackbar("Contact Number is required");

            return false;
        }

        if (isEmpty(mDesignation)) {
            showFailedSnackbar("Designation is required");

            return false;
        }

        if (isEmpty(mAddress)) {
            showFailedSnackbar("Address is required");

            return false;
        }

        return true;
    }

    private void showFailedSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));

        snackbar.show();
    }

    private void showSuccessSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));

        snackbar.show();
    }

    private Boolean isEmpty(EditText editText) {
        String text = editText.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            return true;
        }

        return false;
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        Toast.makeText(this, "You denied the write to storage permission", Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        Toast.makeText(this, "You checked never to ask again", Toast.LENGTH_SHORT).show();
        finish();
    }
}
