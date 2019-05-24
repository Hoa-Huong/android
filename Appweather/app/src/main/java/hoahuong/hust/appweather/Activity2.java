package hoahuong.hust.appweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Activity2 extends AppCompatActivity {

    String tenthanhpho="";
    //Khai báo thuộc tính đã tạo trên màn hình 2
    ImageView imgviewback;
    TextView tvcity2, tvcountry2;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Weather> arrayListWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=getIntent();
        mainWeather2();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Thành phố: " + city);
        if (city.equals("")){
            tenthanhpho = "London";
            get7Days(tenthanhpho);
        }
        else {
            tenthanhpho = city;
            get7Days(city);
        }
        imgviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void mainWeather2(){
        imgviewback=(ImageView)findViewById(R.id.imgviewBack);
        tvcity2=(TextView)findViewById(R.id.tvcity2);
        tvcountry2=(TextView)findViewById(R.id.tvcountry2);
        listView=(ListView)findViewById(R.id.listview);
        arrayListWeather = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(Activity2.this,arrayListWeather);
        listView.setAdapter(customAdapter);


    }
    private void get7Days(String data){
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+data +"&units=metric&cnt=7&appid=94b18d75b567301b72b7b5aba78ae148";
        RequestQueue requestQueue= Volley.newRequestQueue(Activity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    Log.d("name", "JSON: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String city = jsonObjectCity.getString("name");
                            tvcity2.setText(city);
                            String country = jsonObjectCity.getString("country");
                            tvcountry2.setText(country);


                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for (int i = 0; i<jsonArrayList.length(); i++){
                                JSONObject jsonObjectList=jsonArrayList.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");
                                long l=Long.valueOf(day);
                                Date date= new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy ");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max_temp= jsonObjectTemp.getString("max");
                                String min_temp= jsonObjectTemp.getString("min");

                                Double a = Double.valueOf(min_temp);
                                String min=String.valueOf(a.intValue());
                                Double b = Double.valueOf(max_temp);
                                String max=String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");
                                arrayListWeather.add(new Weather(Day, status, icon, max,min));
                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }


}

