package com.example.ootd.ootd_1.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ootd.ootd_1.Fragment.GridFragment;
import com.example.ootd.ootd_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class AddImageActivity extends AppCompatActivity {
    private static final String TAG = "PhotoCaptureActivity";
    private static final int REQUEST_TAKE_PHOTO = 22;
    private static final int REQUEST_IMAGE_CROP = 44;
    //
    String myIp = "192.168.0.46";
    Bitmap bit;
    Post post;
    BitmapDrawable d;
    String Result[];
    String base;
    String e;
    String type;
    String sync;
    private ImageButton backBtn;
    private int id1, id2;
    private FirebaseAuth mAuth;
    private String uid, mCurrentPhotoPath;
    private Button add_image_btn, to_server_btn;
    private ImageView iv_image;
    private Uri photoURI, imageUri;
    private DatabaseReference mUser;
    private RadioGroup radioGroup1, radioGroup2;
    private RadioButton male_btn, female_btn, top_btn, bottom_btn, rb1, rb2;
    private Spinner sp_num;
    private Activity activity;
    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        add_image_btn = findViewById(R.id.add_image_btn);
        to_server_btn = findViewById(R.id.to_server_btn);
        iv_image = findViewById(R.id.iv_image);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        tedPermission();
        activity = this;
        //

        //male_btn, female_btn, top_btn, bottom_btn,
        male_btn = findViewById(R.id.male_btn);
        female_btn = findViewById(R.id.female_btn);
        top_btn = findViewById(R.id.top_btn);
        bottom_btn = findViewById(R.id.bottom_btn);

        backBtn = findViewById(R.id.backBtn);
        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);
        id1 = radioGroup1.getCheckedRadioButtonId();
        id2 = radioGroup2.getCheckedRadioButtonId();
        rb1 = findViewById(id1);
        rb2 = findViewById(id2);
        sp_num = findViewById(R.id.sp_num);


        //버튼 클릭 이벤트
        add_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddImageActivity.this);
                //CameraOpen();
            }
        });

        to_server_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //업로드
                goServerFile();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AddImageActivity.this, MainActivity.class);
                startActivity(in);
            }
        });
    }

    private void goServerFile() {

        Result = null;

        d = (BitmapDrawable) iv_image.getDrawable();
        bit = d.getBitmap();
        base = encodeToBase64(bit);
        e = sp_num.getSelectedItem().toString();
        type = "mantop";
        sync = "false";
        post = new Post();
        try {
            Result = post.execute().get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        //Log.d("result", String.valueOf(Result));
        //Glide.with(this).load(Result[0]).into(iv_image);
        //iv_image.setImageURI(Uri.parse(Result[0]));
        /*GridFragment gridFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("result", Result);
        gridFragment.setArguments(bundle);
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    iv_image.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
        }
    }

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    class Post extends AsyncTask<String, Void, String[]> {


        HttpURLConnection con = null;
        BufferedReader bufferedReader = null;
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... url) {
            Bitmap[] result = null;
            String[] resultUrlArray = null;
            String baseurl = "http://"+ myIp +"/";

            Log.d("URL", base);
            //male_btn, female_btn, top_btn, bottom_btn,
            if (male_btn.isChecked()){
                if(top_btn.isChecked()){
                    type = "mantop";
                }else{
                    type = "manbottom";
                }
            }else{
                if(top_btn.isChecked()){
                    type = "womantop";
                }else{
                    type = "womanbottom";//동작안함
                }
            }
            System.out.println("type = " + type);
            String a = "source=" + base + "&type=" + type + "&count=" + e + "&async=" + sync;
            try {


                try {
                    String url1 = baseurl + "/v/search";
                    URL urls = new URL("http://" + myIp +"/v/search");
                    con = (HttpURLConnection) urls.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    con.setConnectTimeout(40000);
                    con.setReadTimeout(40000);

                    con.setDoOutput(true);
                    con.setDoInput(true);


                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(a);
                    wr.flush();
                    wr.close();

                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("rescode" + con.getResponseCode());
                        Log.d("URL", String.valueOf(con.getResponseCode()));
                    } else {
                        System.out.println("rescode" + con.getResponseCode());
                        Log.d("URL1", String.valueOf(con.getResponseCode()));
                    }

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println(response.toString());
                    String ar = response.toString();
                    JSONObject jsonObject = new JSONObject(ar);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String result1 = jsonObject1.getString("result");
                    Log.d("dddd", result1);
                    JSONArray fin = new JSONArray(result1);
                    String[] arrayprob = new String[fin.length()];
                    String[] arrayurl = new String[fin.length()];
                    for (int i = 0; i < fin.length(); i++) {
                        JSONObject finobject = fin.getJSONObject(i);
                        String prob = finobject.getString("prob");
                        arrayprob[i] = prob;
                        String url_fin = finobject.getString("url");
                        url_fin = url_fin.replace("\\", "");
                        String urlname = url_fin.split("/")[3];
                        url_fin = url_fin.split("/")[4];
                        url_fin = url_fin.replace(".jpg", "");
                        String filename = URLEncoder.encode(url_fin, "UTF-8");
                        arrayurl[i] = "images/origin/" + urlname + "/" + filename + ".jpg";
                    }
                    Log.d("arrayprob", String.valueOf(arrayprob));
                    Log.d("arrayurl", String.valueOf(arrayurl));
                    Bitmap[] imgarray = new Bitmap[arrayurl.length];
                    resultUrlArray = new String[arrayurl.length];
                    HashSet<String> strings = new HashSet<>();
                    for (int j = 0; j < arrayurl.length; j++) {
                        strings.add(baseurl + arrayurl[j]);
                    }
                    SharedPreferences.Editor edit = getSharedPreferences("info", MODE_PRIVATE).edit();
                    edit.putStringSet("result", strings);
                    edit.commit();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultUrlArray;
        }


        protected void onProgressUpdate(Void... Void) {

        }

        protected void onPostExecute(String[] result) {
            con.disconnect();
            Glide.with(activity).load(result[0]).into(iv_image);
            System.out.println(result.length);
            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(RESULT_OK, intent);
            finish();
            super.onPostExecute(result);
        }

        public Bitmap GetImage(String imageurl) {
            Bitmap imgBitmap = null;
            try {
                URL url = new URL(imageurl);
                URLConnection conn = url.openConnection();
                conn.connect();

                int nSize = conn.getContentLength();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
                imgBitmap = BitmapFactory.decodeStream(bis);

                bis.close();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return imgBitmap;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}

