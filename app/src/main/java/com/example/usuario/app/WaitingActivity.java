package com.example.usuario.app;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

import javax.xml.parsers.ParserConfigurationException;


public class WaitingActivity extends ActionBarActivity {

    ImageButton btnPaint, btnVideo;
    Boolean paintReleased = false;
    Boolean videoReleased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        verifyReleasedActivities();

        if(paintReleased) {

            btnPaint = (ImageButton) findViewById(R.id.imageButton_paint);

            btnPaint.setVisibility(View.VISIBLE);

            btnPaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Info debug", "Clicou no botao de colorir");
                    Intent intent = new Intent(WaitingActivity.this, PaintActivity.class);
                    startActivity(intent);
                    // btnPaint.setBackgroundResource(R.drawable.ic_paint_pressed);
                }

            });
        }

        if(videoReleased){

            btnVideo = (ImageButton) findViewById(R.id.imageButton_video);

            btnVideo.setVisibility(View.VISIBLE);

            btnVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Info debug", "Clicou no botao de video");
                    Intent intent = new Intent(WaitingActivity.this, VideoActivity.class);
                    startActivity(intent);
                    // btnPaint.setBackgroundResource(R.drawable.ic_paint_pressed);
                }

            });
        }

        Synchronizer sync = new Synchronizer(this);
        sync.start();

    }

    public void callActivityWord(int id,int duration){

        Intent intent = new Intent(this, WordActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("duration", duration);
        startActivity(intent);

    }

    public void callActivityPuzzle(int duration){
        Intent intent = new Intent(this, PuzzleActivity.class);
        intent.putExtra("duration",duration);
        startActivity(intent);
    }


    private void verifyReleasedActivities(){

        //releasedActivities.xml
        //Environment.getExternalStorageDirectory()+"/BlueTree"
        String fileName = "/storage/sdcard0/BlueTree/releasedActivities.xml";
        File fileReleased =  new File(fileName);
        ApplicationXML app;

        if(fileReleased.exists()){
            //Verifica o arquivo em busca de activities liberadas
            app = XMLUtil.parseFileReleased();

            for(int i=0;i<app.getListActivity().size();i++){
               Log.d("Info Debug", "Activity Liberada: " + app.getListActivity().get(i).getType());
                String type = app.getListActivity().get(i).getType();
                switch (type){
                    case "paint":
                        paintReleased = true;
                        break;
                    case "video":
                        videoReleased = true;
                        break;
                }


            }

        }else{
            Log.d("Info Debug", "Nao tem nenhuma atividade liberada");
        }

    }


}