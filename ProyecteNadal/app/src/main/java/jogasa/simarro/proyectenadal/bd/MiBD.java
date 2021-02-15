package jogasa.simarro.proyectenadal.bd;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

import jogasa.simarro.proyectenadal.R;
import jogasa.simarro.proyectenadal.dao.OrderDAO;
import jogasa.simarro.proyectenadal.dao.OrderProductsDAO;
import jogasa.simarro.proyectenadal.dao.ProductDAO;
import jogasa.simarro.proyectenadal.dao.UserDAO;
import jogasa.simarro.proyectenadal.pojo.Producto;

public class MiBD extends SQLiteOpenHelper {

   private static int version=2;
    private static String nombreDB="usuarios";
    private static SQLiteDatabase.CursorFactory factory=null;
    private static SQLiteDatabase db;
    private Context context;




    private String sqlCreacionTablaUsuarios="CREATE TABLE users(_id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT NOT NULL,contraseña TEXT NOT NULL,correo TEXT NOT NULL);";
    private String sqlCreacionTablaProductos="CREATE TABLE products(_id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT NOT NULL,descripcion TEXT NOT NULL,price REAL NOT NULL,productLimit INTEGER NOT NULL, cantity INTEGER NOT NULL,isFav INTEGER NOT NULL);";
    private String sqlCreacionTablaPedidos="CREATE TABLE orders(_id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT NOT NULL,paymentMethod TEXT,shipmentAddress TEXT,price REAL,isFinished INTEGER, idUser INTEGER );";

    private String sqlCreacionProductoPedido="CREATE TABLE orderProducts(idOrder INTEGER, idProduct INTEGER);";

    private String[] sqlInsertarProductos={"INSERT INTO products(_id,nombre,descripcion,price,productLimit,cantity,isFav) VALUES(1,'Fresa','Una simple fresa',0.89,10,10,0)",
                                            "INSERT INTO products(_id,nombre,descripcion,price,productLimit,cantity,isFav) VALUES(2,'Banana','Una simple Banana',1.05,5,8,1)"};

    private static MiBD instance = null;

    private static UserDAO userDAO;
    private static ProductDAO productDAO;
    private static OrderDAO orderDAO;
    private static OrderProductsDAO orderProductsDAO;


    public UserDAO getUserDAO() {
        return userDAO;
    }

    public  ProductDAO getProductDAO() {
        return productDAO;
    }

    public  OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public  OrderProductsDAO getOrderProductsDAO() {
        return orderProductsDAO;
    }
    public static MiBD getInstance(Context context) {
        if(instance == null) {
            instance = new MiBD(context);
            db = instance.getWritableDatabase();
            userDAO = new UserDAO();
            orderDAO = new OrderDAO();
            productDAO = new ProductDAO();
            orderProductsDAO = new OrderProductsDAO();
        }
        return instance;
    }

    public static SQLiteDatabase getDB(){
        return db;
    }
    public static void closeDB(){db.close();};




    public MiBD(Context context){super(context,nombreDB,factory,version);this.context=context;}
    public MiBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreacionTablaUsuarios);
        db.execSQL(sqlCreacionProductoPedido);
        db.execSQL(sqlCreacionTablaPedidos);
        db.execSQL(sqlCreacionTablaProductos);

        for(int i=0;i<sqlInsertarProductos.length;i++){
            db.execSQL(sqlInsertarProductos[i]);
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ( newVersion > oldVersion )
        {
            //elimina tabla
            db.execSQL( "DROP TABLE IF EXISTS users" );
            db.execSQL( "DROP TABLE IF EXISTS orders" );
            db.execSQL( "DROP TABLE IF EXISTS orderProducts" );
            db.execSQL( "DROP TABLE IF EXISTS products" );


            //y luego creamos la nueva tabla
            db.execSQL(sqlCreacionTablaUsuarios);
            db.execSQL(sqlCreacionProductoPedido);
            db.execSQL(sqlCreacionTablaPedidos);
            db.execSQL(sqlCreacionTablaProductos);

            for(int i=0;i<sqlInsertarProductos.length;i++){
                db.execSQL(sqlInsertarProductos[i]);
            }
            Log.i("SQLite", "Se actualiza versión de la base de datos, New version= " + newVersion  );
        }
    }


}
