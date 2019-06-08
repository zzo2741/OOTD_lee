package com.example.ootd.ootd_1.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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

import com.example.ootd.ootd_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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
import java.util.concurrent.ExecutionException;

public class AddImageActivity extends AppCompatActivity {

    private static final String TAG = "PhotoCaptureActivity";
    private static final int REQUEST_TAKE_PHOTO = 22;
    private static final int REQUEST_IMAGE_CROP = 44;
    //
    Bitmap bit;
    Post post;
    BitmapDrawable d;
    String base;
    String e;
    String type;
    String sync;
    EditText edit1;
    TextView text1;
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

        add_image_btn = findViewById(R.id.add_image_btn);
        to_server_btn = findViewById(R.id.to_server_btn);
        iv_image = findViewById(R.id.iv_image);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        mUser = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        tedPermission();

        //
        edit1 = findViewById(R.id.edit1);
        text1 = findViewById(R.id.text1);


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
                CameraOpen();
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

    private void CameraOpen() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "camera error", Toast.LENGTH_SHORT).show();
        }
    }

    private void goServerFile() {
        final Bitmap[] Result = new Bitmap[1];
        d = (BitmapDrawable) iv_image.getDrawable();
        bit = d.getBitmap();
        base = encodeToBase64(bit);
        e = edit1.getText().toString();
        type = "mantop";
        sync = "false";
        post = new Post();
        try {
            Result[0] = post.execute().get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        Log.d("result", String.valueOf(Result[0]));
        iv_image.setImageBitmap(Result[0]);
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ootd");
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void cropImage() {
        Log.i("cropImage", "Call");
        Log.i("cropImage", "/imageURI" + imageUri);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(photoURI, "image/*");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//        cropIntent.putExtra("outputX", 200); //crop한 이미지의 크기
//        cropIntent.putExtra("outputY", 200);
//        cropIntent.putExtra("aspectX", 1); // crop 박스의 비율, 1&1이면 정사각형
//        cropIntent.putExtra("aspectY", 1);
//
//        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", photoURI);
        // 크랍된 이미지를 해당 경로에 저장

        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    try {
                        File photoFile = null;
                        photoFile = createImageFile();

                        photoURI = data.getData();
                        imageUri = Uri.fromFile(photoFile);

                        cropImage();
                    } catch (Exception e) {
                    }
                }
            case REQUEST_IMAGE_CROP:
                ExifInterface exif = null;

                if (resultCode == Activity.RESULT_OK) {
                    galleryAddPic();
                    int exifOrientation;
                    int exifDegree;
                    if (exif != null) {
                        exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        exifDegree = exifOrientationToDegress(exifOrientation);
                    } else {
                        exifDegree = 0;
                    }
                    try {
                        Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        iv_image.setImageBitmap(rotate(bit, exifDegree));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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

    class Post extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap result = null;
            String baseurl = "http://10.10.105.33/";
            Log.d("URL", base);
            String a = "source=" + base + "&type=" + type + "&count=" + e + "&async=" + sync;
            try {
                HttpURLConnection con = null;
                BufferedReader bufferedReader = null;

                try {
                    String url1 = "http://10.10.105.33/v/search";
                    URL urls = new URL(url1);
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
                        Log.d("URL", String.valueOf(con.getResponseCode()));
                    } else {
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

                    Log.d("debug", response.toString());
                    String ar = response.toString();
                    JSONObject jsonObject = new JSONObject(ar);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    String result1 = jsonObject1.getString("result");
                    Log.d("data", result1);
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
                    for (int j = 0; j < arrayurl.length; j++) {
                        Bitmap imgbitmap = GetImage(baseurl + arrayurl[j]);
                        imgarray[j] = imgbitmap;
                    }
                    result = imgarray[0];
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

            return result;
        }


        protected void onProgressUpdate(Void... Void) {
            text1.setText("진행중");
        }

        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            text1.setText("");
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

}

