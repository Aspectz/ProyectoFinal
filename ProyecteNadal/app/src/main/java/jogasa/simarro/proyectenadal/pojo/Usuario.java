package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String email;
    private String contraseña;

    private ArrayList<Pedido> pedidos=new ArrayList<Pedido>();

    public Usuario(String nombre, String email, String contraseña) {
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
    }
    public Usuario(){}

    public void anadirPedido(Pedido p){
        pedidos.add(p);
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }


    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
