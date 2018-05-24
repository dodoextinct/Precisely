package com.pankaj.maukascholars.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pankaj.maukascholars.R;
import com.pankaj.maukascholars.application.PreciselyApplication;
import com.pankaj.maukascholars.dialogs.ChangeEmailDialog;
import com.pankaj.maukascholars.dialogs.ConnectWithAlexaDialog;
import com.pankaj.maukascholars.dialogs.CreditsDialog;
import com.pankaj.maukascholars.dialogs.LanguageDialog;
import com.pankaj.maukascholars.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends BaseNavigationActivity {

    private CircleImageView circleImageView;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private LinearLayout credits_layout, alexa_layout;
    private LinearLayout language_layout , tutorials_goto;
    private Switch notification_toggle;
    private Switch email_toggle;
    final Context context = this;
    private TextView language_status, alexa_status;
    public String credits="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.toolbar_title = "User Profile";
        setContentView(R.layout.activity_user_profile);
        getCredits();
        TextView username = findViewById(R.id.user_name);
        if (Constants.user_name!=null && Constants.user_name.length()>0) {
            String[] strArray = Constants.user_name.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap).append(" ");
            }
            username.setText(builder.toString());
        }
        else
            username.setText("User Name");
        language_status= findViewById(R.id.language_status);
        alexa_status = findViewById(R.id.alexa_status);

        final SharedPreferences sp = PreciselyApplication.getSharedPreferences();
        if (sp.contains(Constants.sp_alexacode))
            alexa_status.setText(sp.getString(Constants.sp_alexacode, ""));

        circleImageView = findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        credits_layout = findViewById(R.id.credits_layout);
        credits_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreditsDialog dialog = new CreditsDialog(UserProfileActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        TextView credit_status = findViewById(R.id.credits_status);
                        if (credits.length()<5)
                            credit_status.setText("$ " + (credits.contentEquals("0")?0:credits));
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        language_layout = findViewById(R.id.language_layout);
        language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageDialog cdd = new LanguageDialog(UserProfileActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                cdd.show();
            }
        });

        notification_toggle = findViewById(R.id.notification_toggle);
        notification_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(UserProfileActivity.this, "You will receive notifications for latest opportunities!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        email_toggle = findViewById(R.id.email_toggle);
        email_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ChangeEmailDialog dialog = new ChangeEmailDialog(UserProfileActivity.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                }

            }
        });

        tutorials_goto = findViewById(R.id.tutorials_goto);
        tutorials_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tutorials = new Intent(UserProfileActivity.this, TutorialsActivity.class);
                startActivity(tutorials);
            }
        });

        alexa_layout = findViewById(R.id.alexa_layout);
        alexa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectWithAlexaDialog dialog = new ConnectWithAlexaDialog(UserProfileActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (sp.contains(Constants.sp_alexacode))
                            alexa_status.setText(sp.getString(Constants.sp_alexacode, ""));
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

    }

    private void getCredits(){
        final int[] status_code = new int[1];
        StringRequest request = new StringRequest(Request.Method.GET, Constants.url_get_credits, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status_code[0] == 200) {
                    TextView credit_status = findViewById(R.id.credits_status);
                    if (response.length()<5)
                        credit_status.setText("$ " + (response.contentEquals("0")?0:response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Constants.user_id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code[0] = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        PreciselyApplication.getInstance().addToRequestQueue(request, "credits");
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(UserProfileActivity.this)

                        .setIcon(R.drawable.ic_account_circle_black_24dp)
                        .setTitle("Upload Photo")
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            }
                        })

                        .setNegativeButton("Choose from Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            }
                        })
                        .show();
            } else {
                new AlertDialog.Builder(UserProfileActivity.this)

                        .setIcon(R.drawable.ic_account_circle_black_24dp)
                        .setTitle("Upload Photo")
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        .setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                                String message = "Press OK to give permissions";
                                message = message + " ";
                                new AlertDialog.Builder(UserProfileActivity.this)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ActivityCompat.requestPermissions(UserProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                                                        1);
                                            }
                                        })
                                        .create()
                                        .show();


                            }
                        })

                        .setNegativeButton("Choose from Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            }
                        })
                        .show();

                // Need Rationale

            }
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

//                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                circleImageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
//            Log.e("TAG", resultCode + "prep");
            if (resultCode != 0) {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());
                    circleImageView.setImageBitmap(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
//                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 200);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);

                } else {
                    Toast.makeText(UserProfileActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

