/*
 * Homework 06
 * ContactListFragment
 *Group12_HW06
 * Samba Diagne
 * Chris Overcash
 */
package edu.uncc.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.weather.databinding.FragmentWeatherForecastBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecastFragment extends Fragment {


    private static final String ARG_PARAM_WEATHERFORECAST = "ARG_PARAM_WEATHERFORECAST";
    private OkHttpClient client = new OkHttpClient();
    private DataService.City city;
    FragmentWeatherForecastBinding binding;
    private final String appId = "dd093f84cb15092259b3d56ba5bd515f";
    private  ArrayList<WeatherForecast> weatherForecastArrayList;
    private WeatherAdapter weatherAdapter;
    private RecyclerView recyclerView;
    public String date;
    public double temp;
    public double tempMax;
    public double tempMin;
    public double humidity;
    public String weatherDesc;
    public String imageLink;
    RecyclerView.LayoutManager layoutManager;

    public WeatherForecastFragment() {

    }

    public static WeatherForecastFragment newInstance(DataService.City choosenCity) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_WEATHERFORECAST, choosenCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = (DataService.City) getArguments().getSerializable(ARG_PARAM_WEATHERFORECAST);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherForecastBinding.inflate(inflater,container,false);
        recyclerView = binding.recyclerView;
        weatherForecastArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/forecast?q="+city.getCity()+"&lat="+city.getLat()+"&lon="+city.getLon()+"&units=imperial&appid="+appId).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }


            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    binding.textViewCityName.setText(String.valueOf(city.getCity()+","+city.getCountry()));
                    try {
                        JSONObject weatherJsonObject = new JSONObject(response.body().string());
                        JSONArray weatherArray = weatherJsonObject.getJSONArray("list");
                        Log.d("obj", "onSuccessResponse: "+weatherArray);

                        for (int i = 0; i < weatherArray.length(); i++) {
                            WeatherForecast wf  = new WeatherForecast();
                            JSONObject weatherObj = weatherArray.getJSONObject(i);
                            date = weatherObj.getString("dt_txt");
                            JSONObject weatherMainObj = weatherObj.getJSONObject("main");
                            JSONArray weatherDescArray = weatherObj.getJSONArray("weather");
                            JSONObject weatherDescObj = weatherDescArray.getJSONObject(0);
                            temp = weatherMainObj.getDouble("temp");
                            tempMax = weatherMainObj.getDouble("temp_max");
                            tempMin = weatherMainObj.getDouble("temp_min");
                            weatherDesc = weatherDescObj.getString("description");
                            humidity = weatherMainObj.getDouble("humidity");
                            imageLink = "//openweathermap.org/img/wn/"+weatherDescObj.getString("icon")+".png";
                            weatherForecastArrayList.add(new WeatherForecast(date,temp,tempMax,tempMin,humidity,weatherDesc,imageLink));

                            
                        }
                        weatherAdapter = new WeatherAdapter(getContext(),weatherForecastArrayList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(weatherAdapter);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    Log.d("demo", "onResponse Echec: "+body);
                }
            }
        });
    }

}