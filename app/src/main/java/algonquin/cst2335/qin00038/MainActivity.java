package algonquin.cst2335.qin00038;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w( TAG, "Message" );
        Log.w( TAG, "the first function that gets created when an application is launched" );

        Button btn = findViewById(R.id.button);
        EditText email = findViewById(R.id.myedittext);
        btn.setOnClickListener( click-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress", email.getText().toString() );
            startActivity(nextPage);
        } );

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String previous = prefs.getString("emailAddress","");
        email.setText(previous);
        SharedPreferences.Editor writer = prefs.edit();
        writer.putString("emailAddress",email.getText().toString());
        writer.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( TAG, "The application no longer responds to user input" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( TAG, "The application is now responding to user input" );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( TAG, "The application is now visible on screen." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( TAG, "Any memory used by the application is freed." );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( TAG, "The application is no longer visible." );
    }
}