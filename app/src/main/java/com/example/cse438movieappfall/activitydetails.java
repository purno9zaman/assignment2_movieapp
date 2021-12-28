package com.example.cse438movieappfall;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class activitydetails extends AppCompatActivity {

    Result result;

    OkHttpClient client = new OkHttpClient();

    Castresponse castResponse;

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new StrictMode.ThreadPolicy.Builder().permitAll().build();


        setContentView(R.layout.activity_details);

        result = (Result) getIntent().getSerializableExtra("result");

        TextView movieNames = findViewById(R.id.moviename);
        movieNames.setText(result.getTitle());



        TextView movieDesc = findViewById(R.id.description);
        movieDesc.setText(result.getOverview());

        ImageView sPoster = findViewById(R.id.character_dp);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500"+ result.getPosterPath())
                .centerCrop()
                .into(sPoster);

        ImageView bPoster = findViewById(R.id.movie_banner);

        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500"+ result.getPosterPath())
                .centerCrop()
                .into(bPoster);

        RecyclerView movieCast = findViewById(R.id.castRV);
        movieCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        try {
            String data = run("https://api.themoviedb.org/3/movie/" + result.getId()+ "/credits?api_key=3fa9058382669f72dcb18fb405b7a831");

            castResponse = new Gson().fromJson(data, castResponse.getClass());
            movieCast.setAdapter(new NewAdapter());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class NewAdapter<DetailsActivity> extends RecyclerView.Adapter<CastViewHolder>{

        @NonNull
        @Override
        public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View newView = LayoutInflater.from(activitydetails.this).inflate(R.layout.activity_details, parent, false);
            return new CastViewHolder(newView);
        }

        @Override
        public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

            holder.nameCast.setText(""+castResponse.getCast());

            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+ castResponse.getCast())
                    .centerCrop()
                    .into(holder.imgCast);

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    class CastViewHolder extends RecyclerView.ViewHolder {


        ImageView imgCast;
        TextView nameCast;


        public CastViewHolder(@NonNull View itemView) {

            super(itemView);

        }
    }
}