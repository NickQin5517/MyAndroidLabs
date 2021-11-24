package algonquin.cst2335.qin00038;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/** This class allow user to input a password in a edittext and check if it's complex enough by loop each character of
 * the password through if-else statement.
 * @author Ning Qin
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {


    private String stringURL;
    TextView currentTemp ;//= findViewById(R.id.temp);
    TextView maxTemp ;//= findViewById(R.id.maxTemp);
    TextView minTemp ;//= findViewById(R.id.minTemp);
    TextView theHumidity ;//= findViewById(R.id.humidity);
    TextView theDescription ;//= findViewById(R.id.description);
    ImageView icon ;//= findViewById(R.id.icon);
    EditText cityText;// = findViewById(R.id.myEdit);
    float oldSize = 14;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        switch (item.getItemId()){

            case 5:
                String cityName = item.getTitle().toString();
                runForecast(cityName);
                break;


            case R.id.hide_views:
                currentTemp.setVisibility(View.INVISIBLE);
                maxTemp.setVisibility(View.INVISIBLE);
                minTemp.setVisibility(View.INVISIBLE);
                theHumidity.setVisibility(View.INVISIBLE);
                theDescription.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);
                cityText.setText("");
                break;

            case R.id.id_increase:
                oldSize++;
                currentTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                theHumidity.setTextSize(oldSize);
                theDescription.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;

            case R.id.id_decrease:
                oldSize = Float.max(oldSize-1,5);
                currentTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                theHumidity.setTextSize(oldSize);
                theDescription.setTextSize(oldSize);
                cityText.setTextSize(oldSize);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastBtn = findViewById(R.id.myButton);
        cityText = findViewById(R.id.myEdit);

        Toolbar  myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener( (item)-> {

            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);

            return false;

        });

        forecastBtn.setOnClickListener((click) -> {

            String cityName = cityText.getText().toString();
            myToolbar.getMenu().add(0, 5, 0, cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runForecast(String cityName){

//

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {

                try {


                    stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + URLEncoder.encode(cityName, "UTF-8")
                            + "&appid=1850a4dd109a61473853638162ccebb1&Units=Metric";

                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    JSONObject theDocument = new JSONObject( text );
                    JSONObject coord = theDocument.getJSONObject( "coord" );

                    int vis = theDocument.getInt("visibility");
                    String name = theDocument.getString( "name" );
                    JSONArray weatherArray = theDocument.getJSONArray("weather");
                    JSONObject position0 = weatherArray.getJSONObject(0);
                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");


                    JSONObject mainObject = theDocument.getJSONObject( "main" );
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");

                    Bitmap image = null;
                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());

                    }


                    FileOutputStream fOut ;
                    try {
                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                    File file = new File(getFilesDir(), iconName + ".png");
                    if(file.exists()){
                        image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                    }
                    else{

                        if (responseCode == 200) {
                            image = BitmapFactory.decodeStream(connection.getInputStream());
                            image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName+".png", Activity.MODE_PRIVATE));

                        }
                    }

                    Bitmap finalImage = image;

                    runOnUiThread( (  )  -> {

                        currentTemp = findViewById(R.id.temp);
                        currentTemp.setText("The current temperature is " + current);
                        currentTemp.setVisibility(View.VISIBLE);

                        maxTemp = findViewById(R.id.maxTemp);
                        maxTemp.setText("The max temperature is " + max);
                        maxTemp.setVisibility(View.VISIBLE);

                        minTemp = findViewById(R.id.minTemp);
                        minTemp.setText("The min temperature is " + min);
                        minTemp.setVisibility(View.VISIBLE);

                        theHumidity = findViewById(R.id.humidity);
                        theHumidity.setText("The humidity is " + humidity);
                        theHumidity.setVisibility(View.VISIBLE);

                        theDescription = findViewById(R.id.description);
                        theDescription.setText("The description is " + description);
                        theDescription.setVisibility(View.VISIBLE);

                        icon = findViewById(R.id.icon);
                        icon.setImageBitmap(finalImage);
                        icon.setVisibility(View.VISIBLE);

                    });




                }
                catch (IOException | JSONException ioe) {
                    Log.e("Connection error:", ioe.getMessage());
                }
            });



    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }


}