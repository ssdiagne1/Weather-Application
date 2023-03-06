/*
 * Main Activity
 * Samba Diagne
 */
package edu.uncc.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesFragmentListener, CurrentWeatherFragment.CurrentWeatherListener{
    private DataService.City mcity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void gotoCurrentWeather(DataService.City city) {
        mcity = city;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToWeatherForecast() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, WeatherForecastFragment.newInstance(mcity))
                .addToBackStack(null)
                .commit();
    }
}