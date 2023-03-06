/*
 * ContactListFragment
 * Samba Diagne
 */
package edu.uncc.weather;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import edu.uncc.weather.databinding.FragmentCurrentWeatherBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    OkHttpClient client = new OkHttpClient();
    FragmentCurrentWeatherBinding binding;
    private double longitude;
    private double latitude;
    private final String appId = "dd093f84cb15092259b3d56ba5bd515f";
    private double windDegree;
    private String iconID,link;
    private ImageView icon;

    private ImageView weatherIcon;
    private ArrayList<WeatherForecast> weatherForecastArrayList;
    WeatherAdapter.ViewHolder holder;



    public CurrentWeatherFragment() {
    }

    public static CurrentWeatherFragment newInstance(DataService.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
            weatherForecastArrayList = new ArrayList<>();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false);
//        weatherIcon = binding.imageViewWeatherIcon;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");
        longitude = mCity.getLon();
        latitude = mCity.getLon();

        weatherIcon = binding.imageViewWeatherIcon;

        Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q="+mCity.getCity()+"&units=imperial&appid="+appId).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        JSONObject weatherJsonObject = new JSONObject(response.body().string());
                        JSONObject temperatureObj = weatherJsonObject.getJSONObject("main");
                        JSONArray weatherDescArray = weatherJsonObject.getJSONArray("weather");
                        JSONObject weatherDescObj = weatherDescArray.getJSONObject(0);
                        JSONObject windObj = weatherJsonObject.getJSONObject("wind");
                        JSONObject cloudObj = weatherJsonObject.getJSONObject("clouds");
                        windDegree = windObj.getDouble("deg");

                        binding.textViewCityName.setText(mCity.getCity()+","+mCity.getCountry());
                        binding.textViewTemp.setText(temperatureObj.getDouble("temp") + " F");
                        binding.textViewTempMax.setText(temperatureObj.getDouble("temp_max") + " F");
                        binding.textViewTempMin.setText(temperatureObj.getDouble("temp_min") + " F");
                        binding.textViewDesc.setText(weatherDescObj.getString("description"));
                        binding.textViewHumidity.setText(temperatureObj.getDouble("humidity") + "%");
                        binding.textViewWindSpeed.setText(windObj.getDouble("speed") + " miles/hr");
                        binding.textViewWindDegree.setText(windDegree + " degrees");
                        binding.textViewCloudiness.setText(cloudObj.getDouble("all") + " %");
                        iconID = weatherDescObj.getString("icon");
                        Log.d("demo", "onResponse: "+icon);
                        link = "//openweathermap.org/img/wn/"+iconID+".png";
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get().load("https:"+link).into(weatherIcon);
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
        binding.buttonCheckForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ilistener.goToWeatherForecast();
            }
        });
    }

    CurrentWeatherListener ilistener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ilistener = (CurrentWeatherListener) context;
    }



    public interface CurrentWeatherListener{
        void goToWeatherForecast();
    }
}