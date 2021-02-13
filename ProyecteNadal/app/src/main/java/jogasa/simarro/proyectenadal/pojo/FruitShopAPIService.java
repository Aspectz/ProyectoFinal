package jogasa.simarro.proyectenadal.pojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FruitShopAPIService {
     @GET("/products/{id}")
     Call<Producto> getComick(@Path("id")int id);
}
