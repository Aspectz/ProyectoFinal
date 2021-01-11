package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;
import java.util.Calendar;

public class Pedido implements Serializable {
    private String nombre,metodoFacturacion,direccionEnvio;
    private Calendar fechacreacionPedido;
    private float precio;
    private Producto producto;

    public Pedido(String nombre, String metodoFacturacion, String direccionEnvio, Calendar fechacreacionPedido, float precio, Producto producto) {
        this.nombre = nombre;
        this.metodoFacturacion = metodoFacturacion;
        this.direccionEnvio = direccionEnvio;
        this.fechacreacionPedido = fechacreacionPedido;
        this.precio = precio;
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMetodoFacturacion() {
        return metodoFacturacion;
    }

    public void setMetodoFacturacion(String metodoFacturacion) {
        this.metodoFacturacion = metodoFacturacion;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public Calendar getFechacreacionPedido() {
        return fechacreacionPedido;
    }

    public void setFechacreacionPedido(Calendar fechacreacionPedido) {
        this.fechacreacionPedido = fechacreacionPedido;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
