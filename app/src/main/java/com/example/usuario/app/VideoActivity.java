package com.example.usuario.app;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;

import java.io.File;


public class VideoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        if(DownloadVideoFile()){

            VideoView videoView =(VideoView)findViewById(R.id.videoView1);

            //Creating MediaController
            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);

            //specify the location of media file
            //String pathVideo = "/storage/sdcard0/BlueTree/video.mp4";
            String pathVideo = Environment.getExternalStorageDirectory()+"/BlueTree/video.mp4";
            Uri uri=Uri.parse(pathVideo);

            //Setting MediaController and URI, then starting the videoView
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
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

    private boolean DownloadVideoFile(){

        Log.d("Info Debug", "Download video bonus");

        //Diretorio funcional no dispositivo
        File directory = new File("/storage/sdcard0/BlueTree");

        if(!directory.exists()){

            if(directory.mkdirs())
                Log.d("Info Debug", "Diretorio da aplicacao criado com sucesso");
            else {
                Log.d("Info Debug", "Nao foi possivel criar o diretorio da aplicacao");
                return false;
            }
        }

        //Inserir o seu ip aqui!
        String fileURL = "http://192.168.0.105/sync/video.mp4";
        String saveDir = "/storage/sdcard0/BlueTree";

        try{

            Boolean returnConnection ;

            returnConnection = new HTTPDownloadUtility().execute(fileURL, saveDir).get();

            if(!returnConnection){
                Log.d("Info Debug", "Erro na conexao");
                Toast savedToast = Toast.makeText(getApplicationContext(), "Erro ao baixar video bonus!",
                        Toast.LENGTH_SHORT);
                savedToast.show();
                return false;
            }

        }catch (Exception ex){

            ex.printStackTrace();
            return false;
        }
        return true;

    }
}
