package jogasa.simarro.proyectenadal.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.OrderProducto;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class OrderProductsDAO{


    // Event StudioDAO
    public long add(Object obj) {
        ContentValues contentValues = new ContentValues();
        OrderProducto oe = (OrderProducto) obj;
        contentValues.put("idOrder" , oe.getIdOrder());
        contentValues.put("idProduct",oe.getIdProducto());

        return MiBD.getDB().insert("orderProducts", null, contentValues);
    }

    public int update(Object obj) {

        ContentValues contentValues = new ContentValues();
        OrderProducto oe = (OrderProducto) obj;
        contentValues.put("idOrder" , oe.getIdOrder());
        contentValues.put("idProduct",oe.getIdProducto());

        String condicion = "idOrder=" + String.valueOf(oe.getIdOrder());

        int resultado = MiBD.getDB().update("orderProducts", contentValues, condicion, null);

        return resultado;
    }

    public void delete(Object obj) {
        OrderProducto oe = (OrderProducto) obj;
        String condicion = "idOrder=" + String.valueOf(oe.getIdOrder());

        //Se borra el event indicado en el campo de texto
        MiBD.getDB().delete("orderProducts", condicion, null);
    }
    //SEARCH ORDERPRODUCTS WITH THE ID FROM THE ORDER PASSED WITH ARGS
    public Object search(Object obj) throws ParseException {
        OrderProducto oe = (OrderProducto) obj;

        String condicion = "idOrder=" + String.valueOf(oe.getIdOrder());

        String[] columnas = {
                "idOrder","idProduct"
        };


        Cursor cursor = MiBD.getDB().query("orderProducts", columnas, condicion, null, null, null, null);
        OrderProducto nuevoOrderProducto = null;

        if (cursor.moveToFirst()) {
         //   Log.d("nuevo","cursor"+cursor.getInt(0)+"");
            nuevoOrderProducto = new OrderProducto();
            nuevoOrderProducto.setIdOrder(cursor.getInt(0));
            nuevoOrderProducto.setIdProducto(cursor.getInt(1));

        }
        Log.d("nuevo","ud"+nuevoOrderProducto.getIdOrder()+"  "+nuevoOrderProducto.getIdProducto());
        return nuevoOrderProducto;
    }
    //SEARCH ALL ORDERPRODUCTS
    public ArrayList getAll() throws ParseException {
        ArrayList<OrderProducto> listaOrderProductos = new ArrayList<OrderProducto>();
        String[] columnas = {
                "idOrder","idProduct"
        };
        Cursor cursor = MiBD.getDB().query("orderProducts", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                OrderProducto nuevoOrderProductos = new OrderProducto();
                nuevoOrderProductos.setIdOrder(cursor.getInt(0));
                nuevoOrderProductos.setIdProducto(cursor.getInt(1));

                listaOrderProductos.add(nuevoOrderProductos);
            } while(cursor.moveToNext());
        }
      //  Log.d("User",listaOrderProductos.size()+"");
        return listaOrderProductos;
    }
    //SEARCH ORDERPRODUCTS WITH THE ID FROM THE PRODUCT PASSED WITH ARGS
    public ArrayList getOrderProductos_P(Producto producto) {
        ArrayList<OrderProducto> listaOrderProducto = new ArrayList<OrderProducto>();
        String condicion = "idProduct=" + String.valueOf(producto.getId());
        String[] columnas = {
                "idOrder","idProduct"
        };
        Cursor cursor = MiBD.getDB().query("orderProducts", columnas, condicion, null, null, null, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                OrderProducto nuevoOrderProducto = new OrderProducto();
                nuevoOrderProducto.setIdOrder(cursor.getInt(0));
                nuevoOrderProducto.setIdProducto(cursor.getInt(1));

                listaOrderProducto.add(nuevoOrderProducto);

            } while (cursor.moveToNext());
        }
        return listaOrderProducto;
    }
    //SEARCH ORDERPRODUCTS WITH THE ID FROM THE ORDER PASSED WITH ARGS
    public ArrayList getOrderProductos_O(Pedido pedido) {


        ArrayList<OrderProducto> listaOrderProducto = new ArrayList<OrderProducto>();
        String condicion = "idOrder=" + String.valueOf(pedido.getId());
        String[] columnas = {
                "idOrder","idProduct"
        };
        Cursor cursor = MiBD.getDB().query("orderProducts", columnas, condicion, null, null, null, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                OrderProducto nuevoOrderProducto = new OrderProducto();
                nuevoOrderProducto.setIdOrder(cursor.getInt(0));
                nuevoOrderProducto.setIdProducto(cursor.getInt(1));
               // Log.d("usuario",nuevoOrderProducto.getIdOrder()+"");
                listaOrderProducto.add(nuevoOrderProducto);

            } while (cursor.moveToNext());
        }
        return listaOrderProducto;
    }



    public ArrayList getOrders(Producto producto) throws ParseException {
        ArrayList<OrderProducto> listaOrderProducto = getOrderProductos_P(producto);
        ArrayList<Pedido> listaPedido = new ArrayList<Pedido>();

        for (OrderProducto es : listaOrderProducto){
            Pedido e = new Pedido();
            e.setId(es.getIdOrder());
            e = (Pedido) MiBD.getInstance(null).getOrderDAO().search(e);
            listaPedido.add(e);
        }

        return listaPedido;
    }

    public ArrayList getProducts(Pedido pedido) throws ParseException {
        ArrayList<OrderProducto> listaOrderProducto = getOrderProductos_O(pedido);
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();

        for (OrderProducto es : listaOrderProducto){
            Producto p = new Producto();
            p.setId(es.getIdProducto());
            p = (Producto) MiBD.getInstance(null).getProductDAO().searchAlt(p);
            listaProductos.add(p);
        }

        return listaProductos;
    }
}
