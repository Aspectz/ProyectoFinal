package jogasa.simarro.proyectenadal.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Tienda implements Serializable {
    public static ArrayList<Producto> productos=new ArrayList<Producto>();

    public static ArrayList<Producto> getProductos() {
        return productos;
    }
    public static void anadirProducto(Producto p){
        productos.add(p);
    }
    public static void setProductos(ArrayList<Producto> productos) {
        Tienda.productos = productos;
    }
}
