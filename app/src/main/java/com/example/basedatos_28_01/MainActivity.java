package com.example.basedatos_28_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etCodigo,etNombre,etPrecio;
    public final static String BBDD="Tienda";
    String nombre,codigo,precio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarViews();
    }
    public void cargarViews(){
        etCodigo=findViewById(R.id.etCodigo);
        etNombre=findViewById(R.id.etNombre);
        etPrecio=findViewById(R.id.etPrecio);
    }

    public void limpiar(){
        etCodigo.setText("");
        etNombre.setText("");
        etPrecio.setText("");
        etCodigo.requestFocus();
    }
    public void limpiar1(){

        etNombre.setText("");
        etPrecio.setText("");
        etCodigo.requestFocus();
    }
    //Codigo boton nuevo
    public void crearArticulo(View v){
        ClaseConexion miCon=new ClaseConexion(this,BBDD,null,1);
        SQLiteDatabase base = miCon.getWritableDatabase();
        if(etCodigo.getText().toString().trim().length()==0|| etNombre.getText().toString().trim().length()==0 || etCodigo.getText().toString().trim().length()==0){
            Toast.makeText(this,"Rellena los Campos",Toast.LENGTH_SHORT).show();
            return;
        }
        codigo=etCodigo.getText().toString().trim();
        nombre=etNombre.getText().toString().trim();
        precio=etPrecio.getText().toString().trim();

        //Me creo un registro
        ContentValues registro=new ContentValues();
        registro.put("nombre",nombre);
        registro.put("codigo",codigo);
        registro.put("pvp",precio);
        //Inserto los valores
        base.insert(ClaseConexion.TABLA, null, registro);
        //Cierro la conexion Muy importante
        base.close();
        limpiar();
        Toast.makeText(this, "Articulo Guardado", Toast.LENGTH_SHORT).show();
    }
    //Codigoboton buscar
    public void buscarRegistro(View v){
        limpiar1();
        ClaseConexion miCon=new ClaseConexion(this,BBDD,null,1);
        SQLiteDatabase base = miCon.getReadableDatabase();
        if(etCodigo.getText().toString().trim().length()==0){
            Toast.makeText(this,"Rellena el codigo",Toast.LENGTH_SHORT).show();
            return;
        }
        codigo=etCodigo.getText().toString().trim();
        Cursor fila = base.rawQuery("select nombre,pvp from articulos where codigo="+codigo, null);
        //este cursor tendra un registro o ninguno
        if(fila.moveToFirst()) {
            etNombre.setText(fila.getString(0));
            etPrecio.setText(fila.getString(1));
        }else{
            Toast.makeText(this,"No encontre ningun articulo",Toast.LENGTH_SHORT).show();
        }
        base.close();
    }
    //Codigoboton modificar
    public void modificarArticulo(View v){
        ClaseConexion miCon=new ClaseConexion(this, BBDD,null,1);
        SQLiteDatabase base=miCon.getWritableDatabase();
        if(etCodigo.getText().toString().trim().length()==0||etNombre.getText().toString().trim().length()==0||etPrecio.getText().toString().trim().length()==0){
            Toast.makeText(this,"Rellene los campos", Toast.LENGTH_LONG).show();
            return;
        }
        codigo=etCodigo.getText().toString().trim();
        nombre=etNombre.getText().toString().trim();
        precio=etPrecio.getText().toString().trim();
        ContentValues registro=new ContentValues();
        registro.put("nombre",nombre);
        registro.put("pvp",precio);
           int i= base.update("articulos",registro,"codigo" + "=?", new String[]{String.valueOf(codigo)});
           if (i>0){
               Toast.makeText(this,"Articulo modificado", Toast.LENGTH_LONG).show();
           }else {
               Toast.makeText(this,"Articulo no encontrado", Toast.LENGTH_LONG).show();
           }

        base.close();
    }
    //Codigoboton borrar
    public void borrarArticulo(View v){
        ClaseConexion miCon=new ClaseConexion(this, BBDD,null,1);
        SQLiteDatabase base=miCon.getWritableDatabase();
        if(etCodigo.getText().toString().trim().length()==0){
            Toast.makeText(this,"Rellene el codigo", Toast.LENGTH_LONG).show();
            return;
        }
        codigo=etCodigo.getText().toString().trim();
        base.execSQL("DELETE FROM " + "articulos"+ " WHERE "+"codigo="+codigo);
        Toast.makeText(this,"Articulo borrado", Toast.LENGTH_LONG).show();
        base.close();
    }
}
