package jogasa.simarro.proyectenadal.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class ProductDAO{
//EVENTO _id,nombre,price,productLimit,cantity,isFav
    public long add(Object obj) {
        ContentValues contentValues = new ContentValues();
        Producto e = (Producto) obj;
        contentValues.put("nombre" , e.getNombre());
        contentValues.put("descripcion",e.getDescripcion());
        contentValues.put("price", e.getPrecio());
        contentValues.put("productLimit", e.getLimiteProducto());
        contentValues.put("cantity", e.getCantidad());
        contentValues.put("isFav", e.isFav() ? 1 : 0);


        return MiBD.getDB().insert("products", null, contentValues);
    }

    public int update(Object obj) {

        ContentValues contentValues = new ContentValues();
        Producto p = (Producto) obj;
        contentValues.put("nombre" , p.getNombre());
        contentValues.put("descripcion",p.getDescripcion());
        contentValues.put("price", p.getPrecio());
        contentValues.put("productLimit", p.getLimiteProducto());
        contentValues.put("cantity", p.getCantidad());
        contentValues.put("isFav", p.isFav() ? 1 : 0);

        String condicion = "id=" + String.valueOf(p.getId());

        int resultado = MiBD.getDB().update("products", contentValues, condicion, null);

        return resultado;
    }

    public void delete(Object obj) {
        Producto p = (Producto) obj;
        String condicion = "id=" + String.valueOf(p.getId());

        //Se borra el event indicado en el campo de texto
        MiBD.getDB().delete("products", condicion, null);
    }

    public Object search(Object obj) throws ParseException {
        Producto p = (Producto) obj;

        String condicion = "id=" + String.valueOf(p.getId());

        String[] columnas = {
                "_id","nombre","descripcion","price","productLimit","cantity","isFav"
        };

        Cursor cursor = MiBD.getDB().query("products", columnas, condicion, null, null, null, null);
        Producto nuevoProducto = null;
        if (cursor.moveToFirst()) {
            nuevoProducto = new Producto();
            nuevoProducto.setId(cursor.getInt(0));
            nuevoProducto.setNombre(cursor.getString(1));
            nuevoProducto.setDescripcion(cursor.getString(2));
            nuevoProducto.setPrecio(cursor.getFloat(3));
            nuevoProducto.setLimiteProducto(cursor.getInt(4));
            nuevoProducto.setCantidad(cursor.getInt(5));
            nuevoProducto.setFav(cursor.getInt(6)!=0);

        }
        return nuevoProducto;
    }

    public Object searchAlt(Object obj) throws ParseException {
        Producto p = (Producto) obj;

        String condicion = "id=" + String.valueOf(p.getId());

        String[] columnas = {
                "_id","nombre","descripcion","price","productLimit","cantity","isFav"
        };

        Cursor cursor = MiBD.getDB().query("products", columnas, condicion, null, null, null, null);
        Producto nuevoProducto = null;
        if (cursor.moveToFirst()) {
            nuevoProducto = new Producto();
            nuevoProducto.setId(cursor.getInt(0));
            nuevoProducto.setNombre(cursor.getString(1));
            nuevoProducto.setDescripcion(cursor.getString(2));
            nuevoProducto.setPrecio(cursor.getFloat(3));
            nuevoProducto.setLimiteProducto(cursor.getInt(4));
            nuevoProducto.setCantidad(cursor.getInt(5));
            nuevoProducto.setFav(cursor.getInt(6)!=0);
        }
        return nuevoProducto;
    }

    public ArrayList getAll() throws ParseException {
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        String[] columnas = {
                "_id","nombre","descripcion","price","productLimit","cantity","isFav"
        };
        Cursor cursor = MiBD.getDB().query("products", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Producto nuevoProducto = new Producto();
                nuevoProducto.setId(cursor.getInt(0));
                nuevoProducto.setNombre(cursor.getString(1));
                nuevoProducto.setDescripcion(cursor.getString(2));
                nuevoProducto.setPrecio(cursor.getFloat(3));
                nuevoProducto.setLimiteProducto(cursor.getInt(4));
                nuevoProducto.setCantidad(cursor.getInt(5));
                nuevoProducto.setFav(cursor.getInt(6)!=0);




                listaProductos.add(nuevoProducto);
            } while(cursor.moveToNext());
        }
        return listaProductos;
    }


}
