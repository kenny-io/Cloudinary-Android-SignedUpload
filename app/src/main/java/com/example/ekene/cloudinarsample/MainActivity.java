package com.example.ekene.cloudinarsample;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private List<ListClass> listClassItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView imageView;
    private static final int INTENT_REQUEST_CODE = 222;
    private Button postBtn;
    EditText edt_price;
    private EditText edt_title;
    private LinearLayout postLayout, displayLayout;
    private FloatingActionButton fab;
    private String image_title;
    private String image_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_price = findViewById(R.id.edt_price);
        edt_title = findViewById(R.id.edt_title);
        image_price = edt_price.getText().toString();
        image_title = edt_title.getText().toString();
        recyclerView = findViewById(R.id.recyclerviewpost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        postLayout = findViewById(R.id.postLayout);
        displayLayout = findViewById(R.id.displayLayout);
        displayLayout.setVisibility(View.INVISIBLE);
        fab = findViewById(R.id.fab);
        MediaManager.init(this, new BackendServerSignatureProvider());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_price.setText("");
                edt_title.setText("");
                imageView.setColorFilter(4);
                postLayout.setVisibility(View.VISIBLE);
            }
        });
        listClassItems = new ArrayList<>();
        imageView = findViewById(R.id.imageBtn);
        postBtn = findViewById(R.id.postBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromDevice();
            }
        });
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postLayout.setVisibility(View.INVISIBLE);
                displayLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    public void chooseImageFromDevice() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, INTENT_REQUEST_CODE);
    }
    private void showSnackBar(String message) {
        Snackbar.make(fab, message, Snackbar.LENGTH_LONG).show();
    }
    public String getUrlForMaxWidth(String imageId) {
        int width = Utils.getScreenWidth(this);
        return MediaManager.get().getCloudinary().url().transformation(new Transformation().width(width)).generate(imageId);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == INTENT_REQUEST_CODE && data != null &&
                data.getData() != null) {
            if (resultCode == RESULT_OK && requestCode == INTENT_REQUEST_CODE && data != null &&
                    data.getData() != null) {

                image_price = edt_price.getText().toString();
                image_title = edt_title.getText().toString();

                final Map<String,String> map = new HashMap<>();
                map.put("title", image_title);
                map.put("price", image_price);

                String contextReq = "title ="+image_title+"|price="+image_price;

                if (!TextUtils.isEmpty(image_price) && !TextUtils.isEmpty(image_title)){
                    MediaManager.get().upload(data.getData())
                            .option("folder", "images/")
                            .option("tags", "food")
                            .option("context", contextReq)
                            .callback(new UploadCallback() {
                                @Override
                                public void onStart(String requestId) {
                                    showSnackBar("Upload started...");
                                }
                                @Override
                                public void onProgress(String requestId, long bytes,
                                                       long totalBytes) {
                                }
                                @Override
                                public void onSuccess(String requestId, Map resultData) {
                                    showSnackBar("Upload complete!");
                                    Picasso.with(MainActivity.this).load(getUrlForMaxWidth(resultData
                                            .get("public_id").toString())).into(imageView);

                                    //Reading the context response

                                    Map<String, HashMap<String,String>> contextResponseData  = (HashMap)resultData.get("context");
                                    String titleValue = String.valueOf(contextResponseData.get("custom").get("title"));
                                    String priceValue = String.valueOf(contextResponseData.get("custom").get("price"));

                                    Log.d("VALUE", titleValue + priceValue);

                                    ListClass Lclass = new ListClass(getUrlForMaxWidth(resultData
                                            .get("public_id").toString()),
                                            titleValue, priceValue);
                                    listClassItems.add(Lclass);
                                    adapter = new MyAdapter(listClassItems, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    Log.d("RESPONSE", resultData.toString());

                                }
                                @Override
                                public void onError(String requestId, ErrorInfo error) {
                                    showSnackBar("Upload error: " + error.getDescription());
                                }
                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {
                                    showSnackBar("Upload rescheduled.");
                                }
                            }).dispatch();
                }else {
                    showSnackBar("Empty fields...");
                }

            }
        }

    }
}
