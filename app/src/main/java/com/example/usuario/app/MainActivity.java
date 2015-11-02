package com.example.usuario.app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class MainActivity extends ActionBarActivity {

    private ImageButton btnPlay;
   // private Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btnPlayOnClick();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void btnPlayOnClick() throws ParserConfigurationException{

        //Diretorio funcional no dispositivo
        File directory = new File("/storage/sdcard0/BlueTree");
        //Diretorio que funciona no emulador
        //File directory = new File(Environment.getExternalStorageDirectory()+"/BlueTree");

        if(!directory.exists()){

            if(directory.mkdirs())
                Log.d("Info Debug","Diretorio da aplicacao criado com sucesso");
            else
                Log.d("Info Debug","Nao foi possivel criar o diretorio da aplicacao");
        }


        //Inserir o seu ip aqui!
        String fileURL = "http://192.168.0.105/sync/metadata.xml";
        String saveDir = "/storage/sdcard0/BlueTree";

        try{

            Boolean returnConnection ;

            returnConnection = new HTTPDownloadUtility().execute(fileURL, saveDir).get();

            if(!returnConnection){
                Log.d("Info Debug", "Erro na conexao");
                Toast savedToast = Toast.makeText(getApplicationContext(), "Erro ao baixar arquivo de sincronizacao!",
                        Toast.LENGTH_SHORT);
                savedToast.show();
            }

        }catch (Exception ex){

            ex.printStackTrace();

        }

        Intent intent = new Intent(MainActivity.this, WaitingActivity.class);
        startActivity(intent);
    }

}
