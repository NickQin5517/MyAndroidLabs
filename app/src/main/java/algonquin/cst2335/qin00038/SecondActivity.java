package algonquin.cst2335.qin00038;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        TextView welcome = findViewById(R.id.textView3);
        welcome.setText("Welcome back " + emailAddress);
        Button callNumber = findViewById(R.id.button2);
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        callNumber.setOnClickListener( click-> {

            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
            startActivity(call);

        } );

        Button changePic = findViewById(R.id.button3);
        ImageView profileImage = findViewById(R.id.imageView);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            File WhereAmI = getFilesDir();
                            Bitmap thumbnail = data.getParcelableExtra("data");

                            try {
                                FileOutputStream fOut  = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut );
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e)
                            { e.printStackTrace();
                            }
                            catch(IOException e ){
                                Log.w("IOException","CAN'T OUTPUT PNG!");

                            }

                            profileImage.setImageBitmap(thumbnail);
                            Log.i("got bitmap","image");
                        }
                        else if(result.getResultCode() == Activity.RESULT_CANCELED){
                            Log.i("got bitmap","User refused the image");
                        }
                    }
                });
       File file = new File( getFilesDir(), "Picture.png");
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap( theImage );
        }
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("emailAddress",emailAddress);
        phoneNumber.setText(prefs.getString("phoneNumber", ""));
        writer.putString("phoneNumber",phoneNumber.getText().toString());
        writer.apply();



        changePic.setOnClickListener( click-> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        } );

    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneNumber = findViewById(R.id.editTextPhone);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("phoneNumber",phoneNumber.getText().toString());
        writer.apply();

    }


}