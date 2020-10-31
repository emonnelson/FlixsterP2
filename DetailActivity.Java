package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import model.Movie;
import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String YAPI_KEY = "AIzaSyD0xNtAMGAWFKT8XSWc5Cl8bGDu8EIetTQ";
    // temporary test video id -- TODO replace with movie trailer video id
    final String videoURL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    YouTubePlayerView YouTubePlayerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView txtMovies1 = findViewById(R.id.txtMovies);
        TextView txtOverview1 = findViewById(R.id.txtOverview);
        RatingBar rBar1 = findViewById(R.id.rBar);
        YouTubePlayerView = (YouTubePlayerView) findViewById(R.id.player);

        String title = getIntent().getStringExtra("title");
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        txtMovies1.setText(movie.getTitle());
        txtOverview1.setText(movie.getOverView());
        rBar1.setRating((float) movie.getRating());
        final String videoId = "tKodtNFpzBA";

        AsyncHttpClient c = new AsyncHttpClient();
        c.get(String.format(videoURL, movie.getMovieID()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
                        return;
                    }
                    String yTubeKey = results.getJSONObject(0).getString("key");
                    Log.d("DetailActivity", yTubeKey);
                    initializeYTube(yTubeKey);
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Failed to parse JSON");
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    private void initializeYTube(final String yTubeKey) {
        YouTubePlayerView.initialize(YAPI_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        Log.e("DetailActivity", "onInitializationSuccess");
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(yTubeKey);

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        // log the error
                        Log.d("DetailActivity", "onInitializationFailure");
                    }
                });
            }


}


