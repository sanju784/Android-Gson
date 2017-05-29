package com.sanju784.android_gson;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    private TextView txtName, txtEmail;
    private LinearLayout uploadForm;
    private RelativeLayout userForm;
    private EditText name, email;
    private ImageView img;
    private Bitmap bitmap;
    private Gson gson;
    private String uploadUrl = "http://10.0.2.2/contactApp/SaveContact.php";
    private Contact contact;
    private static final int Img_Request = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        name = (EditText) findViewById(R.id.editText);
        email = (EditText) findViewById(R.id.editText2);
        uploadForm = (LinearLayout) findViewById(R.id.uploadForm);
        userForm = (RelativeLayout)findViewById(R.id.userForm);
        img = (ImageView)findViewById(R.id.imageView);
        txtName = (TextView)findViewById(R.id.textName);
        txtEmail = (TextView)findViewById(R.id.textEmail);
    }

    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Img_Request);
    }

    public void uploadContact(View v) {
        String image = imageToString();
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        contact = new Contact(Name, Email, image);
        gson = new Gson();
        final String uploadJson = gson.toJson(contact);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        gson = new Gson();
                        contact = gson.fromJson(response, Contact.class);
                        displayAlert(contact);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                    params.put("contact", uploadJson);
                    return params;
            }
        }
                ;
                MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Img_Request && requestCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                img.setImageBitmap(bitmap);
                userForm.setVisibility(View.GONE);
                uploadForm.setVisibility(View.VISIBLE);
                txtName.setText(name.getText().toString());
                txtEmail.setText(email.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageArray, Base64.DEFAULT);
    }

    private void displayAlert(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Server Response");
        builder.setMessage(contact.getResponse());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }
}
