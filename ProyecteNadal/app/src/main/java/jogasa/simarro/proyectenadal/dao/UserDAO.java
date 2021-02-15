package jogasa.simarro.proyectenadal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.autofill.UserData;
import android.text.TextUtils;

import com.firebase.ui.auth.data.model.User;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import jogasa.simarro.proyectenadal.bd.MiBD;
import jogasa.simarro.proyectenadal.pojo.Usuario;

public class UserDAO  {
    private MiBD dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public UserDAO(){}
    public UserDAO(Context con){
        this.context=con;
    }
    public long add(Object obj){

        ContentValues contentValues = new ContentValues();
        Usuario c = (Usuario) obj;
        contentValues.put("nombre" , c.getNombre());
        contentValues.put("contraseña", c.getContraseña());
        contentValues.put("correo", c.getContraseña());

        return MiBD.getDB().insert("users",null,contentValues);

    }
    public void close(){
        MiBD.getDB().close();
    }
    public int update(Object obj){
        ContentValues contentValues = new ContentValues();
        Usuario c = (Usuario) obj;
        contentValues.put("nombre" , c.getNombre());
        contentValues.put("contraseña", c.getContraseña());
        contentValues.put("correo", c.getContraseña());

        String condicion = "id=" + String.valueOf(c.getId());

        int resultado = MiBD.getDB().update("users", contentValues, condicion, null);

        return resultado;
    }

    public void delete(Object obj) {
        Usuario c = (Usuario) obj;
        String condicion = "id=" + String.valueOf(c.getId());

        //Se borra el user indicado en el campo de texto
       MiBD.getDB().delete("users", condicion, null);
    }
    public Object search(Object obj) throws ParseException {
        Usuario c = (Usuario) obj;
        String condicion;
        if(TextUtils.isEmpty(c.getEmail())){
            condicion = "id=" + String.valueOf(c.getId());
        }else{
            condicion = "correo=" + "'" + c.getEmail() + "'";
        }

        ;

        String[] columnas = {
                "_id","nombre","contraseña","correo"
        };

        Cursor cursor = MiBD.getDB().query("users", columnas, condicion, null, null, null, null);
        Usuario nuevoUsuario = null;
        if (cursor.moveToFirst()) {
            nuevoUsuario = new Usuario();
            nuevoUsuario.setId(cursor.getInt(0));
            nuevoUsuario.setNombre(cursor.getString(1));
            nuevoUsuario.setContraseña(cursor.getString(2));
            nuevoUsuario.setEmail(cursor.getString(3));


            nuevoUsuario.setPedidos(MiBD.getInstance(null).getOrderDAO().getPedidos(nuevoUsuario));


        }
        return nuevoUsuario;
    }
    public ArrayList getAll() throws ParseException {
        ArrayList<Usuario> listaStudios = new ArrayList<Usuario>();
        String[] columnas = {
                "_id","nombre","contraseña","correo"
        };
        Cursor cursor = MiBD.getDB().query("users", columnas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Usuario u = new Usuario();
                u = new Usuario();
                u.setId(cursor.getInt(0));
                u.setNombre(cursor.getString(1));
                u.setContraseña(cursor.getString(2));
                u.setEmail(cursor.getString(3));


                u.setPedidos(MiBD.getInstance(null).getOrderDAO().getPedidos(u));

                listaStudios.add(u);

            } while(cursor.moveToNext());
        }
        return listaStudios;
    }





}
