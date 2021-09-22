package algonquin.cst2335.qin00038;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
        String editString = myedit.getText().toString();
        mytext.setText( "Your edit text has: " + editString);
        mybutton.setOnClickListener( vw -> mytext.setText("Your edit text has: " + editString) );
    }
}