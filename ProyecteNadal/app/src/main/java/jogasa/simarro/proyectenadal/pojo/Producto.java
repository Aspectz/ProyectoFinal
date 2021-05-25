package jogasa.simarro.proyectenadal.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Producto implements Serializable {
    private String nombre,descripcion;
    private ArrayList<String> fotos;
    private String id;
    private String idSupplier;
    private float precio;
    private int limiteProducto;
    private boolean isFav;


    public Producto(){
        this.fotos=new ArrayList<>();
    }
    public Producto(String nombre, String descripcion, float precio,ArrayList<String> foto,int limiteProducto,boolean isFav) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fotos=foto;
        this.limiteProducto=limiteProducto;
        this.isFav=isFav;
    }
    public Producto(String nombre, String descripcion, float precio,int limiteProducto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.limiteProducto=limiteProducto;
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

    public ArrayList<String> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<String> fotos) {
        this.fotos = fotos;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(String idSupplier) {
        this.idSupplier = idSupplier;
    }
}
