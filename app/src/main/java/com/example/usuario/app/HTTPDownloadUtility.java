package com.example.usuario.app;


import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Marina on 02/09/2015.
 */
public class HTTPDownloadUtility extends AsyncTask<String, Boolean, Boolean> {

   private static final int BUFFER_SIZE = 4096;


    @Override
    protected Boolean doInBackground(String... params){

        String fileURL = params[0];
        String saveDir = params[1];

        try{
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            //Checa a resposta HTTP
            if(responseCode == HttpURLConnection.HTTP_OK){
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if(disposition != null){
                    //extrai o nome do arquivo do cabecalho
                    int index = disposition.indexOf("filename=");
                    if(index > 0){
                        fileName = disposition.substring(index+10, disposition.length() - 1);
                    }
                }else{
                    //extrai o nome do arquivo da URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/")+1,fileURL.length());
                }

                InputStream inputStream = httpConn.getInputStream();

                String saveFilePath = saveDir + File.separator + fileName;

                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while((bytesRead = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer,0,bytesRead);
                }

                outputStream.close();
                inputStream.close();
                Log.d("DEBUUUG", "Arquivo "+fileName+ "baixado");
                httpConn.disconnect();
                return true;

            }else{
                Log.d("DEBUUUG", "Erro ao baixar arquivo. Servidor retornou codigo HTTP: " + responseCode);
                httpConn.disconnect();
                return false;
            }

        }catch (Exception e) {

            Log.d("DEBUUUG","Erro ao enviar requisicao = "+e);
            return false;

        }

    }

}
