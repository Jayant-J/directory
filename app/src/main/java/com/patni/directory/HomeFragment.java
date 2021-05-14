package com.patni.directory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.patni.directory.model.Adds;
import com.patni.directory.utils.RetrofitConfig;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView addImageView;
    TextView welcomeMessageView;
    String fileName;
    private String mParam1;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeMessageView = rootView.findViewById(R.id.welcomeMessage);
        addImageView = rootView.findViewById(R.id.addImageView1);

        Call<Adds> addsCall = RetrofitConfig.apiCalls.getAdds(1L);
        addsCall.enqueue(new Callback<Adds>() {
            @Override
            public void onResponse(Call<Adds> call, Response<Adds> response) {
                if (!response.isSuccessful()) {
                    Log.i("response adds", String.valueOf(response.code()));
                } else {
                    fileName = response.body().getAddImageUrl();
                    ImageDownloader imageDownloader = new ImageDownloader();
                    Bitmap myImage;
                    try {
                        myImage = imageDownloader.execute(fileName).get();
                        addImageView.setImageBitmap(myImage);

                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onFailure(Call<Adds> call, Throwable t) {
                Log.i("ImageDownloaadError",  t.getMessage());
            }
        });
        return rootView;
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (Exception e) {
                Log.i("ImageDownloadError", e.getMessage());
            }
            return null;
        }
    }
}