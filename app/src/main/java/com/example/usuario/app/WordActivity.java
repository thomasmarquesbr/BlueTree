package com.example.usuario.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class WordActivity extends ActionBarActivity {

    private ImageView img1,img2,img3;
    private RelativeLayout container1,container2,container3;
    private int countMatchs = 0, countFullChild=0;
    private int totalContainers, exclusionElem = 1;
    private RelativeLayout rlRaiz;
    private ImageButton btnNext;
    private boolean matching = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("Etapa", "  --  " + this.getIntent().getIntExtra("Etapa", 0));

        //seleciona o activity layout adequada
        switch (this.getIntent().getIntExtra("id",0)){
            case 1:
                setContentView(R.layout.activity_word);
                break;
            case 2:
                setContentView(R.layout.activity_word2);
                break;
            case 3:
                setContentView(R.layout.activity_word3);
                break;
        }



        rlRaiz = (RelativeLayout) findViewById(R.id.rLayoutWordActivity);
        totalContainers = rlRaiz.getChildCount() - exclusionElem;

        btnNext = (ImageButton) findViewById(R.id.btnConfirm);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordActivity.this,WaitingActivity.class);
                startActivity(intent);
            }
        });

        //adicionando eventos de ontouch e ondrag as views
        for(int i=0;i < totalContainers;i++){

            RelativeLayout container = (RelativeLayout) rlRaiz.getChildAt(i);
            container.setOnDragListener(new MyDragListener(i));

            //containers bottom com img
            if (i >= totalContainers/2){
                ImageView img = (ImageView) container.getChildAt(0);
                img.setOnTouchListener(new MyTouchListener());
            }
        }

        new Thread(new Runnable() {//encerra a propria activity depois de x segundos
            @Override
            public void run() {
                try { Thread.sleep(WordActivity.this.getIntent().getIntExtra("duration",0) * 1000); } catch (InterruptedException e) {  e.printStackTrace(); }
                WordActivity.this.finish();
            }
        }).start();

    }

    /*@Override
    protected void onPause(){

    }*/

    class MyTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                return true;
            } else {
                return false;

            }
        }
    }


    class MyDragListener implements View.OnDragListener{

        private int id;

        MyDragListener(int id){

            this.id = id;

        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();

            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    //Log.i("Script", id + " - ACTION_DRAG_STARTED");
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //Log.i("Script", id+" - ACTION_DRAG_ENTERED");
                    //v.setBackgroundColor(Color.YELLOW);
                    if(v.getTag().equals("null")) {
                        v.setBackground(getResources().getDrawable(R.drawable.backgwordslargefocus));
                    }else{
                        v.setBackground(getResources().getDrawable(R.drawable.backwordsfocus));
                    }
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    //Log.i("Script",id+" - ACTION_DRAG_LOCATION");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundColor(Color.BLACK);
                    if(v.getTag().equals("null")) {
                        v.setBackground(getResources().getDrawable(R.drawable.backgwordslarge));
                    }else{
                        v.setBackground(getResources().getDrawable(R.drawable.backgwords));
                    }
                    //Log.i("Script",id+" - ACTION_DRAG_EXITED");
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    ViewGroup from = (ViewGroup) view.getParent();
                    RelativeLayout to = (RelativeLayout) v;

                    if (to.getChildCount() > 0){
                        //existe view no container destino, então é feito o swap das imagens
                        View viewSwap = to.getChildAt(0);
                        from.removeView(view);
                        to.addView(view);
                        view.setVisibility(View.VISIBLE);

                        to.removeView(viewSwap);
                        from.addView(viewSwap);
                        viewSwap.setVisibility(View.VISIBLE);
                    }else{
                        //não existe img no container destino, então é adicionado a img normalmente
                        from.removeView(view);
                        to.addView(view);
                        view.setVisibility(View.VISIBLE);
                    }
                    Log.i("Script",id+" - ACTION_DROP");

                    //Faz a contagem de acertos
                    countMatchs = 0;
                    countFullChild = 0;
                    for(int i=0; i < totalContainers/2; i++) {
                        RelativeLayout containerTemp = (RelativeLayout) rlRaiz.getChildAt(i);
                        //Log.i("Script", "container: " +containerTemp.getTag());

                        //verifica se existe img no container
                        if (containerTemp.getChildCount() > 0) {
                            countFullChild++;
                            ImageView imgTemp = (ImageView) containerTemp.getChildAt(0);
                            //verifica se houve acerto
                            if(imgTemp.getTag().toString().equals(containerTemp.getTag().toString())) {
                                //Log.i("Script", "View: " + imgTemp.getTag().toString() + " - Container: " + containerTemp.getTag().toString());
                                countMatchs++;
                            }
                        }
                    }
                    //Verifica se terminou de montar o caça-palavras
                    if(countFullChild == totalContainers/2){
                        btnNext.setVisibility(View.VISIBLE);
                    }else{
                        btnNext.setVisibility(View.INVISIBLE);
                    }
                    //verifica se acertou todas as silabas
                    if(countMatchs == totalContainers/2) {
                        //*****************
                        //gravar acerto
                        //******************
                        Log.d("Info Debug", "Numero de acertos: " + countMatchs);
                        Log.d("Info Debug", "Gravar acerto de palavras");
                        XMLUtil.insertActivityReleased("paint", 1);

                    }

                    //Log.i("View"," -- "+view.getTag().toString()+" >>> "+to.getTag().toString());
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.i("Script",id+" - ACTION_DRAG_ENDED");
                    //v.setBackgroundColor(Color.BLACK);
                    if(v.getTag().equals("null")) {
                        v.setBackground(getResources().getDrawable(R.drawable.backgwordslarge));
                    }else{
                        v.setBackground(getResources().getDrawable(R.drawable.backgwords));
                    }
                    break;
            };

            return false;
        }
    }

}