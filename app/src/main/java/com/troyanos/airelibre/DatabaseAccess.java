package com.troyanos.airelibre;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

   /* private DatabaseAccess (Context context){

        this.openHelper= new DatabaseHelper(context, null, 0);
    }*/

    /*public static DatabaseAccess getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAccess(context);
        }
        return instance;
    }*/

    public void open (){
        this.db=openHelper.getWritableDatabase();
    }

    public void close(){
        if (db!=null){
            this.db.close();
        }
    }
    public String getInfo (String nombre_espacio){
        c=db.rawQuery("select direc_espacio from espacios_espacios where nombre_espacio = '"+nombre_espacio+"'", new String [] {});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String direcion_espacio = c.getString(0);
            buffer.append ("" +direcion_espacio);
            toString();
        }
        return buffer.toString();
}
}
