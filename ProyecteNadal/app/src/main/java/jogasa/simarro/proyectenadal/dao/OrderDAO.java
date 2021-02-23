package jogasa.simarro.proyectenadal.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Pedido;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class OrderDAO {


    public long add(Object obj) {
        ContentValues contentValues = new ContentValues();
        Pedido o = (Pedido) obj;
        contentValues.put("nombre" , o.getNombre());
        contentValues.put("paymentMethod", o.getMetodoFacturacion());
        contentValues.put("shipmentAddress", o.getDireccionEnvio());
        contentValues.put("price", o.getPrecio());
        contentValues.put("isFinished", o.isFinished() ? 1 : 0);
        contentValues.put("cantity",o.getCantidadPedido());
        contentValues.putNull("idUser");
        return MiBD.getDB().insert("orders", null, contentValues);
    }

    public long insertOrderToClient(Pedido p , Usuario u) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre" , p.getNombre());
        contentValues.put("isFinished", p.isFinished() ? 1 : 0);
        contentValues.put("idUser",u.getId());
        contentValues.put("cantity",p.getCantidadPedido());
        contentValues.put("date",p.getFechacreacionPedido());
        return MiBD.getDB().insert("orders", null, contentValues);
    }


    public int update(Object obj) {

        ContentValues contentValues = new ContentValues();
        Pedido p = (Pedido) obj;
        contentValues.put("nombre" , p.getNombre());
        contentValues.put("paymentMethod", p.getMetodoFacturacion());
        contentValues.put("shipmentAddress", p.getDireccionEnvio());
        contentValues.put("isFinished", p.isFinished() ? 1 : 0);
        contentValues.put("cantity",p.getCantidadPedido());


        String condicion = "_id=" + String.valueOf(p.getId());

        int resultado = MiBD.getDB().update("orders", contentValues, condicion, null);

        return resultado;
    }
    public void delete(Object obj) {
        Pedido c = (Pedido) obj;
        String condicion = "_id=" + String.valueOf(c.getId());

        //Se borra el user indicado en el campo de texto
        MiBD.getDB().delete("orders", condicion, null);
    }


    public Object search(Object obj) throws ParseException {
        Pedido c = (Pedido) obj;

        String condicion = "";
        condicion = "_id=" + String.valueOf(c.getId());


        String[] columnas = {
                "_id","nombre","paymentMethod","shipmentAddress","price","isFinished","idUser","cantity","date"
        };

        Cursor cursor = MiBD.getDB().query("orders", columnas, condicion, null, null, null, null);
        Pedido p = null;

        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            p = new Pedido();

            p.setId(cursor.getInt(0));
            p.setNombre(cursor.getString(1));
            p.setMetodoFacturacion(cursor.getString(2));
            p.setDireccionEnvio(cursor.getString(3));
            p.setPrecio(cursor.getFloat(4));
            p.setFinished(cursor.getInt(5) != 0);
            p.setCantidadPedido(cursor.getInt(7));
            p.setFechacreacionPedido(cursor.getString(8));


            // Obtenemos el studio y lo asignamos
            Usuario a = new Usuario();
            a.setId(cursor.getInt(6));
            a = (Usuario) MiBD.getInstance(null).getUserDAO().search(a);
            p.setUsuarioCreador(a);

            // Obtenemos la lista de events que tiene el studio
            p.setProductos(MiBD.getInstance(null).getOrderProductsDAO().getProducts(p));
        }
        return p;
    }

    public ArrayList getAll() throws ParseException {
        ArrayList<Pedido> listaUsers = new ArrayList<Pedido>();
        String[] columnas = {
                "_id","nombre","paymentMethod","shipmentAddress","price","isFinished","idUser","cantity","date",
        };
        Cursor cursor = MiBD.getDB().query("orders", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Pedido nuevoPedido = new Pedido();
                nuevoPedido = new Pedido();
                nuevoPedido.setId(cursor.getInt(0));
                nuevoPedido.setNombre(cursor.getString(1));
                nuevoPedido.setMetodoFacturacion(cursor.getString(2));
                nuevoPedido.setDireccionEnvio(cursor.getString(3));
                nuevoPedido.setPrecio(cursor.getFloat(4));
                nuevoPedido.setFinished(cursor.getInt(5) != 0);
                nuevoPedido.setCantidadPedido(cursor.getInt(7));
                nuevoPedido.setFechacreacionPedido(cursor.getString(8));

                Usuario a = new Usuario();
                a.setId(cursor.getInt(6));
                a = (Usuario) MiBD.getInstance(null).getUserDAO().search(a);
                nuevoPedido.setUsuarioCreador(a);

                // Obtenemos la lista de events que tiene el studio
                nuevoPedido.setProductos(MiBD.getInstance(null).getOrderProductsDAO().getProducts(nuevoPedido));

                listaUsers.add(nuevoPedido);

            } while(cursor.moveToNext());
        }
        return listaUsers;
    }

    public ArrayList getPedidos(Usuario usuario) {

        //Log.d("NUEVO","idU"+usuario.getId()+"");
        ArrayList<Pedido> listaPedidos = new ArrayList<Pedido>();
        String condicion = "idUser=" + String.valueOf(usuario.getId());
        String[] columnas = {
                "_id","nombre","paymentMethod","shipmentAddress","price","isFinished","idUser","cantity","date",
        };
        Cursor cursor = MiBD.getDB().query("orders", columnas, condicion, null, null, null, null);
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setId(cursor.getInt(0));
                nuevoPedido.setNombre(cursor.getString(1));
                nuevoPedido.setMetodoFacturacion(cursor.getString(2));
                nuevoPedido.setDireccionEnvio(cursor.getString(3));
                nuevoPedido.setPrecio(cursor.getFloat(4));
                nuevoPedido.setFinished(cursor.getInt(5) != 0);
                nuevoPedido.setUsuarioCreador(usuario);
                nuevoPedido.setCantidadPedido(cursor.getInt(7));
                nuevoPedido.setFechacreacionPedido(cursor.getString(8));

                listaPedidos.add(nuevoPedido);

            } while (cursor.moveToNext());
        }
        return listaPedidos;
    }
}
