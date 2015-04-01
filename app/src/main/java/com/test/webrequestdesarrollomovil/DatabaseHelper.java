package com.test.webrequestdesarrollomovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // toda tabla que se cree va a tener un primary key conformado por la palabra id mas el nombre de la tabla
    private final String table1 = "peliculas";

    //la columna 1 se crea junto con la tabla, por eso se comienza con la columna 2
    private final String table1Col2 = "titulo";
    private final String table1Col3 = "ano";
    private final String table1Col4 = "imdb";
    private final String table1Col5 = "tipo";

    private  final String table2 = "comentarios";
    private final String table2Col2 = "idpeliculas";
    private final String table2Col3 = "comentario";



    //En este constructor definimos el context, el nombre de la BBDD y la version de la misma
    public DatabaseHelper(Context context) {
        super(context, "Peliculas", null, 1);
    }

    //metodo heredado de SQLiteOpenHelper
    //Este se ejecuta cuando se crea la BBDD
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creando tabla de movies
        createTable(db,this.table1);

        //agregando las columnas (la col con el PK se genero junto con la tabla)
        addTableColumn(db,this.table1,this.table1Col2,"TEXT");
        addTableColumn(db,this.table1,this.table1Col3,"TEXT");
        addTableColumn(db,this.table1,this.table1Col4,"TEXT");
        addTableColumn(db,this.table1,this.table1Col5,"TEXT");

        createTable(db,this.table2);
        addTableColumn(db,this.table2,this.table2Col2,"TEXT");
        addTableColumn(db,this.table2,this.table2Col3,"TEXT");
    }

    //metodo heredado de SQLiteOpenHelper
    //Este se ejecuta cuando se actualiza algun componente de la BBDD
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(SQLiteDatabase db, String tableName){
        db.execSQL("CREATE TABLE " +tableName+"(id"+tableName+" Integer PRIMARY KEY);");
    }

    public void addTableColumn(SQLiteDatabase db, String tableName, String columnName, String typeColumn){
        db.execSQL("ALTER TABLE " +tableName+" ADD COLUMN "+columnName+" "+typeColumn+";");
    }

    public void insertTable(String tableName, String column1, String data1){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column1, data1);

        db.insert(tableName, null, values);
        db.close();
    }

    public void insertTable(String tableName, String column1, String data1,String column2, String data2,
                            String column3, String data3,String column4, String data4){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column1, data1);
        values.put(column2, data2);
        values.put(column3, data3);
        values.put(column4, data4);

        db.insert(tableName, null, values);
        db.close();
    }

    public void insertTable(String tableName, String column1, String data1,String column2, String data2){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column1, data1);
        values.put(column2, data2);

        db.insert(tableName, null, values);
        db.close();
    }

    public Cursor getAll(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db. rawQuery("SELECT id"+table+" as _id, titulo ||' '||ano as titulo  FROM "+table+";", null);
        return c;
    }
    public Cursor getByID (long id,String table){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT id"+table+" as _id, titulo ||' '||ano as titulo  FROM "+table+" where idpeliculas="+id+";", null);
        return c;
    }

    public String getComment (String id,String table){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT comentario FROM "+table+" where idpeliculas= '"+id+"' ;", null);

        String str = "";


        if (c.moveToFirst()) {
          str = c.getString(c.getColumnIndex("comentario"));
        }

        return str;
    }

}
