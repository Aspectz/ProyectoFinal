package jogasa.simarro.projectefinal.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String metodoFacturacion;
    private String idUser;
    private Estados estado;
    private Direccion direccion;

    private String fecha;

    public Pedido(){
        this.setId(count.incrementAndGet());
    }
    public Pedido(String metodoFacturacion, String fecha, float precio, ArrayList<Producto> productos) {
        this.metodoFacturacion = metodoFacturacion;
        this.fecha = fecha;
        this.setId(count.incrementAndGet());
    }
    //PEDIDO SIN ACABAR
    public Pedido(String nombre){
        this.setId(count.incrementAndGet());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getMetodoFacturacion() {
        return metodoFacturacion;
    }

    public void setMetodoFacturacion(String metodoFacturacion) {
        this.metodoFacturacion = metodoFacturacion;
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
                ", metodoFacturacion='" + metodoFacturacion + '\'' +
                ", fechacreacionPedido='" + fecha + '\'' +
                ", idUser='" + idUser + '\'' +
                ", estado=" + estado +
                '}';
    }
}
