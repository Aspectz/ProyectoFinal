package jogasa.simarro.proyectenadal.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.adapters.ApiAdapter;
import jogasa.simarro.proyectenadal.api.ApiClient;
import jogasa.simarro.proyectenadal.api.ApiInterface;
import jogasa.simarro.proyectenadal.api_models.Article;
import jogasa.simarro.proyectenadal.api_models.News;
import jogasa.simarro.proyectenadal.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNoticias extends Fragment {

    public static final String API_KEY = "40e50f9ed7ae4cfbbc129344448e86b6";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private ApiAdapter apiAdapter;
    private String TAG = getTag();

    public FragmentNoticias() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_noticias, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LoadJson();

    }

    public void LoadJson() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call;

        SharedPreferences pref =getActivity().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String languagePref=pref.getString("idioma","esp");




        String country = Utils.getCountry();
        String q;
        String sortBy = "publishedAt";
        String pageSize = "50";
        String language="es";
        if(languagePref.compareTo("esp")==0)  language="es";q="fruta";
        if(languagePref.compareTo("eng")==0)  language="en";q="fruit";


        call = apiInterface.getNews(language ,q, sortBy, pageSize, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getArticle() != null){

                    articles = response.body().getArticle();
                    apiAdapter = new ApiAdapter(articles, getContext());
                    recyclerView.setAdapter(apiAdapter);
                    apiAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), "NO NEWS FOUNDED", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(), "LOAD FAILURE", Toast.LENGTH_SHORT).show();
            }
        });

    }

}