package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String nombre,metodoFacturacion,direccionEnvio;
    private String fechacreacionPedido;
   // private float precio;
   // private ArrayList<Producto> productos=new ArrayList<Producto>();
   // private int cantidadPedido;
    private String idUser;
    private boolean isFinished;
    private Estados estado;


    public Pedido(){
        this.setId(count.incrementAndGet());
    }
    public Pedido(String nombre, String metodoFacturacion, String direccionEnvio, String fechacreacionPedido, float precio, ArrayList<Producto> productos) {
        this.nombre = nombre;
        this.metodoFacturacion = metodoFacturacion;
        this.direccionEnvio = direccionEnvio;
        this.fechacreacionPedido = fechacreacionPedido;
        //this.precio = precio;
        this.setId(count.incrementAndGet());
        //this.productos = productos;
    }
    //PEDIDO SIN ACABAR
    public Pedido(String nombre){
        this.nombre=nombre;
        this.setId(count.incrementAndGet());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFechacreacionPedido() {
        return fechacreacionPedido;
    }

    public void setFechacreacionPedido(String fechacreacionPedido) {
        this.fechacreacionPedido = fechacreacionPedido;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }



    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }



    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }
}
