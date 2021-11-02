package algonquin.cst2335.qin00038;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/** This class allow user to input a password in a edittext and check if it's complex enough by loop each character of
 * the password through if-else statement.
 * @author Ning Qin
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen */
    TextView tv = null;

    /** This holds the edittext at the horizontal centre of the screen and 32dp below the textview */
    EditText et = null;

    /** This holds the button at the horizontal centre of the screen and 50dp above the bottom */
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.myEdit);
        btn = findViewById(R.id.myButton);

        btn.setOnClickListener( clk->{
            String password = et.getText().toString();
            Boolean flag = checkPasswordComplexity(password);
            if(flag){
                tv.setText("Your password meets the requirements");
            }else{
                tv.setText("You shall not pass!");
            }
        });
    }

    /**This function can check if a password is too simple or not, if it does not include specified letters and symbols,
     * the function will provide notification accordingly.
     *
     * @param pw The String object that we are checking
     * @return Returns true if the password is complex enough, and returns false if it is not complex enough.
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            if(Character.isDigit(pw.charAt(i)) == true){
                foundNumber = true;
            }else if(Character.isUpperCase(pw.charAt(i)) == true){
                foundUpperCase = true;
            }else if(Character.isLowerCase(pw.charAt(i)) == true){
                foundLowerCase = true;
            }else if(isSpecialCharacter(pw.charAt(i)) == true){
                foundSpecial = true;
            }
        }

        if(!foundNumber)
        {
            Toast.makeText(MainActivity.this,"The password are missing a number",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundLowerCase)
        {
            Toast.makeText(MainActivity.this,"The password are missing a lower case letter",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundUpperCase)
        {
            Toast.makeText(MainActivity.this,"The password are missing a Upper case letter",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!foundSpecial)
        {
            Toast.makeText(MainActivity.this,"The password are missing a special character",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }

    }

    /** This function will check if the Char is one of the special character #$%^&*!@?.
     *
     * @param c The char that we are checking
     * @return Returns true if the char is one of the special character #$%^&*!@?, otherwise returns false.
     */
    boolean isSpecialCharacter(char c){

        switch(c){
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }















    }

}