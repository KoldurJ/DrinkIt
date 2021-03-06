package koldur.losversados;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Juan on 13/06/2017.
 */

public class DaActivity extends AppCompatActivity {

    private HashMap<Integer,String> listaPreg = new HashMap<Integer,String>();
    private HashMap<Integer,String> listaCastigos = new HashMap<Integer,String>();

    Button siguiente;
    TextView descr, id, titulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        leerArchivoDAPreg(listaPreg);
        leerArchivoDARet(listaCastigos);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da);

        siguiente = (Button)findViewById(R.id.siguienteDA);
        titulo = (TextView)findViewById(R.id.titleDA);
        descr = (TextView)findViewById(R.id.DescripDA);
        id = (TextView)findViewById(R.id.identificadorDA);

        siguiente.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Random random1 = new Random();
                int randomNumber1 = random1.nextInt(listaPreg.size());
                titulo.setText(listaPreg.get(randomNumber1));
                id.setText(Integer.toString(randomNumber1));

                Random random2 = new Random();
                int randomNumber2 = random2.nextInt(listaCastigos.size());
                descr.setText(listaCastigos.get(randomNumber2));

                titulo.refreshDrawableState();
                descr.refreshDrawableState();
                id.refreshDrawableState();
            }
        });
    }

    private void leerArchivoDAPreg(HashMap lista){

        InputStream inputStream = null;
        String text;
        String[] lines;
        int last = 0;
        try{
            inputStream = this.getResources().openRawResource(R.raw.dedoacus);
            text = btoString(inputStream);
            lines = text.split("\n");

            for (int i = 0; i < lines.length; i++){
                last = i;
                System.out.println("iniciando carga" + i);
                String linea[] = lines[i].split(";");
                lista.put(i,linea[1]);
            }
            System.err.println("carga completa");
        }catch (IOException e){
            System.err.println("last " + last);
        } finally{
            try{
                inputStream.close();
            }catch(IOException e){
                System.err.println("Error al cerrar");
            }
        }
    }

    private void leerArchivoDARet(HashMap lista){

        InputStream inputStream = null;
        String text;
        String[] lines;
        SharedPreferences sh = getSharedPreferences(getString(R.string.DA_COnfiguration_file), Context.MODE_PRIVATE);
        Boolean ext = sh.getBoolean("rhighexpl",false);
        try{
            int lastId = 0;
            if(sh.getBoolean("rhigh",false)) {
                inputStream = this.getResources().openRawResource(R.raw.rethighfile);
                text = btoString(inputStream);
                lines = text.split("\n");

                int counter = 0;
                for (int i = 0; i < lines.length; i++){
                    String linea[] = lines[i].split(";");
                    if (linea[1].equals("Reto")) {
                        lista.put(counter, linea[2]);
                        counter++;
                    }
                    else if(ext && (linea[1].equals("EXPL"))) {
                        lista.put(counter, linea[2]);
                        counter++;
                    }
                }
                lastId = counter;
            }
            if(sh.getBoolean("rmed",false)) {
                inputStream = this.getResources().openRawResource(R.raw.retmedfile);
                text = btoString(inputStream);
                lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String linea[] = lines[i].split(";");
                    lista.put(i + lastId, linea[2]);
                }
                lastId += lines.length;
            }
            if(sh.getBoolean("rlow",false)) {
                inputStream = this.getResources().openRawResource(R.raw.retlowfile);
                text = btoString(inputStream);
                lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String linea[] = lines[i].split(";");
                    lista.put(i + lastId, linea[2]);
                }
                lastId += lines.length;
            }
            if(sh.getBoolean("vhigh",false)){
                inputStream = this.getResources().openRawResource(R.raw.verdhighfile);
                text = btoString(inputStream);
                lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String linea[] = lines[i].split(";");
                    lista.put(i + lastId, linea[2]);
                }
                lastId += lines.length;
            }
            if(sh.getBoolean("vmed",false)){
                inputStream = this.getResources().openRawResource(R.raw.verdmedfile);
                text = btoString(inputStream);
                lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String linea[] = lines[i].split(";");
                    lista.put(i + lastId, linea[2]);
                }
                lastId += lines.length;
            }
            if(sh.getBoolean("vlow",true)){
                inputStream = this.getResources().openRawResource(R.raw.verdlowfile);
                text = btoString(inputStream);
                lines = text.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    String linea[] = lines[i].split(";");
                    lista.put(i + lastId, linea[2]);
                }
            }

        }catch (IOException e){
            // FALTA POR DECLARAR QUÉ PASA SI NO SE PUEDE LEER
        } finally{
            try{
                inputStream.close();
            }catch(IOException e){
                // FALTA POR DECLARAR QUÉ PASA SI NO SE PUEDE LEER
            }
        }
    }


    public String btoString( InputStream inputStream ) throws IOException
    {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ( (len=inputStream.read(bytes))>0 )
        {
            b.write(bytes,0,len);
        }
        return new String( b.toByteArray(),"UTF8");
    }
}
