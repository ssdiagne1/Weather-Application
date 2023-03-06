/*
 * Homework 06
 * ContactListFragment
 *Group12_HW06
 * Samba Diagne
 * Chris Overcash
 */
package edu.uncc.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<WeatherForecast> weatherForecastArrayList;
    public ImageView img;

    public WeatherAdapter(Context context, ArrayList<WeatherForecast> weatherForecastArrayList) {
        this.context = context;
        this.weatherForecastArrayList = weatherForecastArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_row_item,parent, false);


        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherForecast weatherForecast = weatherForecastArrayList.get(position);
        Picasso.get().load("https:".concat(weatherForecast.imageLink)).into(holder.weatherIcon);
        holder.temp.setText(weatherForecast.temp + "F");
        holder.tempMax.setText("Max: "+weatherForecast.tempMax + "F");
        holder.tempMin.setText("Min: "+weatherForecast.tempMin + "F");
        holder.humidity.setText("Humidity: "+weatherForecast.humidity +"%");
        holder.desc.setText(weatherForecast.weatherDesc);
        holder.date.setText(weatherForecast.date);
    }

    @Override
    public int getItemCount() {
        return weatherForecastArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView temp;
        public TextView tempMin;
        public TextView tempMax;
        public TextView humidity;
        public TextView desc;
        public ImageView weatherIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewDateTime);
            temp = itemView.findViewById(R.id.textViewTemp);
            tempMax = itemView.findViewById(R.id.textViewTempMax);
            tempMin = itemView.findViewById(R.id.textViewTempMin);
            humidity = itemView.findViewById(R.id.textViewHumidity);
            desc = itemView.findViewById(R.id.textViewDesc);
            weatherIcon = itemView.findViewById(R.id.imageViewWeatherIcon);
        }
    }
}
