package jogasa.simarro.proyectenadal.pojo;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String nombre,metodoFacturacion,direccionEnvio;
    private String idUser;
    private Estados estado;
    private String fecha;

    public Pedido(){
        this.setId(count.incrementAndGet());
    }
    public Pedido(String nombre, String metodoFacturacion, String direccionEnvio, String fecha, float precio, ArrayList<Producto> productos) {
        this.nombre = nombre;
        this.metodoFacturacion = metodoFacturacion;
        this.direccionEnvio = direccionEnvio;
        this.fecha = fecha;
        this.setId(count.incrementAndGet());
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }





    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", metodoFacturacion='" + metodoFacturacion + '\'' +
                ", direccionEnvio='" + direccionEnvio + '\'' +
                ", fechacreacionPedido='" + fecha + '\'' +
                ", idUser='" + idUser + '\'' +
                ", estado=" + estado +
                '}';
    }
}
