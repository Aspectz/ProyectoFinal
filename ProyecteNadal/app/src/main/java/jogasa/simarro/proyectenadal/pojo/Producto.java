package jogasa.simarro.proyectenadal.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/*public class Producto {


    @SerializedName("name")
    @Expose
    private String name;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}*/
/*
public class Producto {


    @SerializedName("name")
    @Expose
    private String name;

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

}*/

/*public class Producto{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("photo_url")
    @Expose
    private String photoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}*/

public class Producto implements Serializable {
    private String nombre,descripcion;
    private int foto;
    private int id;
    private float precio;
    private int limiteProducto;
    private int cantidad;
    private boolean isFav;


    public Producto(){}
    public Producto(String nombre, String descripcion, float precio,int foto,int limiteProducto,boolean isFav) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.foto=foto;
        this.limiteProducto=limiteProducto;
        this.isFav=isFav;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getLimiteProducto() {
        return limiteProducto;
    }

    public void setLimiteProducto(int limiteProducto) {
        this.limiteProducto = limiteProducto;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
