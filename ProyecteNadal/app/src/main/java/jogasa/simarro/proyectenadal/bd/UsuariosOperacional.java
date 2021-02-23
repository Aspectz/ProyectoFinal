package jogasa.simarro.proyectenadal.bd;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.pojo.OrderProducto;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class UsuariosOperacional implements Serializable {
    private MiBD miBD;

    protected UsuariosOperacional(Context context) {
        miBD = MiBD.getInstance(context);
    }

    private static UsuariosOperacional instance = null;

    //***************************************
    // Interfaz publica de la API de IndiEvents
    //***************************************

    // Constructor. Obtiene una instancia del mismo para operar
    public static UsuariosOperacional getInstance(Context context) {
        if (instance == null) {
            instance = new UsuariosOperacional(context);
        }
        return instance;
    }

    // Operacion Login: Verifica que el Usuario existe y que su contraseña es correcta. Recibira un Usuario
    // que solo contendrá el Usuarioname y la password.
    public Usuario login(Usuario u) throws ParseException {
        Usuario aux = (Usuario) miBD.getUserDAO().search(u);
        if (aux == null) {
            return null;
        } else if (aux.getContraseña().equals(u.getContraseña())) {
            return aux;
        } else {
            return null;
        }
    }

    public Usuario loginGoogle(Usuario u) throws ParseException {
        Usuario aux = (Usuario) miBD.getUserDAO().search(u);


        if (aux == null) {

            return null;
        } else if (aux!=null) {
            return aux;
        }
        return null;
    }

    public ArrayList getUserOrders(Usuario u , Pedido p){
        ArrayList<Pedido> pedidosUsuario=miBD.getOrderDAO().getPedidos(u);
        ArrayList<OrderProducto> pedidoProductoUsuario=miBD.getOrderProductsDAO().getOrderProductos_O(p);

        return pedidoProductoUsuario;

    }


    public Usuario comprobarRegistro(Usuario u) throws ParseException {
        Usuario aux = (Usuario) miBD.getUserDAO().search(u);
        if (aux == null)
            return null;
        else
            return aux;
    }

    public void registrarUsuario(Usuario u) {
        miBD.getUserDAO().add(u);
    }

    // Operacion changePassword: Cambia la password del cliente. Recibirá el cliente de la aplicación con la password cambiada.
    // Si devuelve un 1 es que ha verificado el cambio de password como correcto y todo ha ido bien, mientras que si devuelve
    // mientras que si devuelve un 0 no ha verificado el cambio de password como correcto y ha habido un error al cambiarlo.
    public int changePassword(Usuario u) {
        int resultado =  miBD.getUserDAO().update(u);
        if (resultado == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    // Operacion getUsuarios: Obtiene un ArrayList de los Usuarios de un studio que recibe como parámetro
    public ArrayList<Pedido> getPedido(Usuario u) {
        return miBD.getOrderDAO().getPedidos(u);
    }

    // Operacion getGames: Obtiene un ArrayList de los games de una studio que recibe como parámetro
    public ArrayList<OrderProducto> getOrderProducto() throws ParseException {
        return miBD.getOrderProductsDAO().getAll();
    }


    // Operacion getUsuarios: Obtiene un ArrayList de los Usuarios de un studio que recibe como parámetro
    public ArrayList<Producto> getEvents() throws ParseException {
        return miBD.getProductDAO().getAll();
    }

    // Operacion getUsuarios: Obtiene un ArrayList de los Usuarios de un studio que recibe como parámetro
    public ArrayList<Usuario> getEventsStudios() throws ParseException {
        return miBD.getUserDAO().getAll();
    }

}