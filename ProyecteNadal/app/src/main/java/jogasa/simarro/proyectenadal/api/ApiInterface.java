package jogasa.simarro.proyectenadal.api;

import jogasa.simarro.proyectenadal.api_models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("everything")
    Call<News> getNews(

            @Query("language") String language,
            @Query("q") String q,
            @Query("sortBy")String sortBy,
            @Query("pageSize")String pageSize,
            @Query("apiKey") String apiKey
    );
}
