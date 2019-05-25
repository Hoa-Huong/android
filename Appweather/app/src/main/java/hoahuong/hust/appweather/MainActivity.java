package hoahuong.hust.appweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity {

    EditText search ;
    ImageView imgicon;
    Button ok,btnnextday ;
    TextView tvcity , tvcountry ,tvtemp ,tvstatus ,tvhumidity ,tvcloud , tvwind, tvday ;
    String City = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainweather();
        getCurrentWeatherData("hanoi");
        //Bắt chức năng btnok
       ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = search.getText().toString() ;//Lấy giá trị của editText
                if (city.equals("")){
                    City = "Hanoi";
                    getCurrentWeatherData(City);
                }
                else {
                    City =city;
                    getCurrentWeatherData(City);
                }

            }
        });
       //Bắt sự kiện onClick btnnext day
        btnnextday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu từ màn hình 1 sang màn hình 2
                String city = search.getText().toString() ;
                //Chuyển màn hình
                Intent intent= new Intent(MainActivity.this,Activity2.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }
    public void getCurrentWeatherData(String data){
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);//biến này giúp thực thi các request mà client gửi đi
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=94b18d75b567301b72b7b5aba78ae148";

        //Đọc dữ liệu của đường dẫn trước (kiểu method,url,action listener, errlistener)
        //stringRequest là dữ liệu gửi đi
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                // trả về giá trị đọc đc
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      Log.d("Kết quả", response);
                        try {
                            //city-day
                            JSONObject jsonObject=new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            tvcity.setText(name);
                            //format day
                            long l=Long.valueOf(day);
                            Date date= new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String Day = simpleDateFormat.format(date);
                            tvday.setText(Day);

                            //weatherArray
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            // weather status
                            String status=jsonObjectWeather.getString("description");
                            tvstatus.setText(status);

                            //weather icon
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgicon);

                            //mainObject

                            JSONObject jsonObjectMain=jsonObject.getJSONObject("main");

                            //Temp
                            String temp = jsonObjectMain.getString("temp");
                            Double nhietdo = Double.valueOf(temp);
                            String Temp=String.valueOf(nhietdo.intValue());
                            tvtemp.setText(Temp+"ºC");
                            //humidity
                            String humidity = jsonObjectMain.getString("humidity");
                            tvhumidity.setText(humidity +"%");

                            //windObject
                            JSONObject jsonObjectwind=jsonObject.getJSONObject("wind");
                            String wind = jsonObjectwind.getString("speed");
                            tvwind.setText(wind+"m/s");
                            //cloud
                            JSONObject jsonObjectcloud = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectcloud.getString("all");
                            tvcloud.setText(cloud+"%");
                            //country
                            JSONObject jsonObjectCoutry=jsonObject.getJSONObject("sys");
                            String country = jsonObjectCoutry.getString("country");
                            tvcountry.setText(country+" , " +country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                //báo lỗi
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);


    }
    private void mainweather(){
        search = (EditText)findViewById(R.id.search);
        ok = (Button)findViewById(R.id.btnok);
        btnnextday = (Button) findViewById(R.id.nextday);
        tvcity = (TextView)findViewById(R.id.city);
        tvcountry = (TextView)findViewById(R.id.country);
        tvtemp = (TextView)findViewById(R.id.temp);
        tvstatus = (TextView)findViewById(R.id.status);
        tvhumidity = (TextView)findViewById(R.id.humidity);
        tvcloud = (TextView)findViewById(R.id.cloud);
        tvwind = (TextView)findViewById(R.id.wind);
        tvday = (TextView)findViewById(R.id.day);
        imgicon = (ImageView)findViewById(R.id.icon);
    }
}
