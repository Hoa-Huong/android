package hoahuong.hust.appweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;//NGười dùng truyền vào
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listview,null);
        Weather weather = arrayList.get(i);
        //Thuộc tính trong listView
        TextView tvday2=(TextView)view.findViewById(R.id.tvday2);
        TextView tvstatus2=(TextView)view.findViewById(R.id.tvstatus2);
        TextView min_temp=(TextView)view.findViewById(R.id.min_temp);
        TextView max_temp=(TextView)view.findViewById(R.id.max_temp);
        ImageView icon2 = (ImageView)view.findViewById(R.id.icon2);

        tvday2.setText(weather.Day);
        tvstatus2.setText(weather.Status);
        min_temp.setText(weather.MinTemp + "ºC");
        max_temp.setText(weather.MaxTemp + "ºC");
        Picasso.with(context).load("http://openweathermap.org/img/w/"+weather.Image+".png").into(icon2);
        return view;
    }
}
