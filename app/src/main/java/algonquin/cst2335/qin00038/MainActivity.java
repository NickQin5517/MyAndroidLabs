package algonquin.cst2335.qin00038;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mytext = findViewById(R.id.textview);
        Button mybutton = findViewById(R.id.mybutton);
        EditText myedit = findViewById(R.id.myedittext);
        mybutton.setOnClickListener( (vw) -> {
            String editString = myedit.getText().toString();
             mytext.setText("Your edit text has: " + editString); });
        CheckBox cb = findViewById(R.id.mycheckbox);
        Switch sw = findViewById(R.id.myswitch);
        RadioButton rb = findViewById(R.id.myradiobutton);
        cb.setOnCheckedChangeListener((b,isChecked)->
                Toast.makeText(MainActivity.this,"You clicked on the Checkbox and it is now: "+ isChecked,Toast.LENGTH_LONG).show()
        );
        sw.setOnCheckedChangeListener((b,isChecked)->
                Toast.makeText(MainActivity.this,"You clicked on the Switch and it is now: "+ isChecked,Toast.LENGTH_SHORT).show()
        );
        rb.setOnCheckedChangeListener((b,isChecked)->
                Toast.makeText(MainActivity.this,"You clicked on the RadioButton and it is now: "+ isChecked,Toast.LENGTH_SHORT).show()
        );
        ImageView myimage = findViewById(R.id.logo_algonquin);
        ImageButton imgbtn = findViewById( R.id.myimagebutton );
        imgbtn.setOnClickListener(vw ->
           Toast.makeText(MainActivity.this,"The width = " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight(),Toast.LENGTH_LONG).show()
                );
        CheckBox cb = findViewById(R.id.mycheckbox);
        Switch sw = findViewById(R.id.myswitch);
        RadioButton rb = findViewById(R.id.myradiobutton);
        cb.setOnCheckedChangeListener((b,c)->
                Toast.makeText(MainActivity.this,"You clicked on the Checkbox and it is now: "+cb.isChecked(),Toast.LENGTH_LONG).show()
        );
        sw.setOnCheckedChangeListener((b,c)->
                Toast.makeText(MainActivity.this,"You clicked on the Switch and it is now: "+sw.isChecked(),Toast.LENGTH_SHORT).show()
        );
        rb.setOnCheckedChangeListener((b,c)->
                Toast.makeText(MainActivity.this,"You clicked on the RadioButton and it is now: "+rb.isChecked(),Toast.LENGTH_SHORT).show()
        );
        ImageView myimage = findViewById(R.id.logo_algonquin);
        ImageButton imgbtn = findViewById( R.id.myimagebutton );
        imgbtn.setOnClickListener(vw ->
           Toast.makeText(MainActivity.this,"The width = " + imgbtn.getWidth() + " and height = " + imgbtn.getHeight(),Toast.LENGTH_LONG).show()
                );
    }
}