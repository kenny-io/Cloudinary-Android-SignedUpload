package com.example.ekene.cloudinarsample;


import android.util.Log;

import com.cloudinary.android.signed.Signature;
import com.cloudinary.android.signed.SignatureProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackendServerSignatureProvider implements SignatureProvider {
    private  OkHttpClient client;

    public BackendServerSignatureProvider() {
          client = new OkHttpClient();
    }

    @Override
    public Signature provideSignature(Map options) {

        // Initialize Builder (not RequestBody)
        FormBody.Builder builder = new FormBody.Builder();

        // Add Params to Builder
        for ( Object key : options.keySet() ) {
            Log.d("fff",key+"   "+options.get(key));
            builder.add( key.toString(), options.get(key).toString());
        }

        // Create RequestBody
        RequestBody formBody = builder.build();

        // Create Request (same)
        Request request = new Request.Builder()
                .url( "https://wt-97b8897f620c6dc18f4ea6a53e0e0796-0.run.webtask.io/express-cloudinary-android-signed-upload" )
                .post( formBody )
                .build();


         try {
            //Invoke the server call
            Response response = client.newCall(request).execute();

            //Convert to JSON
            JSONObject obj = new JSONObject(response.body().string());

            //Return new Signature
            return new Signature(obj.getString("signature"), obj.getString("api_key"), obj.getLong("timestamp"));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public String getName() {
        return "SampleSignatureProvider";
    }
}

