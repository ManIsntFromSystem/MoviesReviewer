package com.example.moviesapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.R;
import com.example.moviesapp.data.MovieAdapter;
import com.example.moviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private RequestQueue requestQueue;
    private ImageView searchImgView;
    private EditText searchNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchImgView = findViewById(R.id.imageViewSearch);
        searchNameField = findViewById(R.id.editNameMovie);

        recyclerView = findViewById(R.id.recyclerViewMovies);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getMovies("John");

    }

    private void getMovies(String nameMovie) {
        String url = "http://www.omdbapi.com/?apikey=93de5337&s=" + nameMovie;
        Log.d("UrlMovie", "URL: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("Search");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String title = jsonObject.getString("Title");
                            String year = jsonObject.getString("Year");
                            String poster = jsonObject.getString("Poster");

                            Movie movie = new Movie();
                            movie.setTitle(title);
                            movie.setYear(year);
                            movie.setPosterUrl(poster);

                            movies.add(movie);
                        }

                        movieAdapter = new MovieAdapter(MainActivity.this, movies);
                        recyclerView.setAdapter(movieAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
    }

    public void searchImageView(View view) {
        //String nameMovie = searchNameField.getText().toString();
        movies.clear();
        getMovies(searchNameField.getText().toString());
    }
}
