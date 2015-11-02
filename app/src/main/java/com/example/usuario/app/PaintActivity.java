package com.example.usuario.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * Activity responsavel pela aplicaçao de colorir desenhos
 */

public class PaintActivity extends Activity implements OnTouchListener {

    private ImageButton currentColor;
    private PaintView paintView;
 //   private Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // setContentView(R.layout.activity_paint);
        setContentView(R.layout.activity_paint2);

        //Recupera o Layout que contem o primeiro botao de cor exibido na paleta
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors1);
       // LinearLayout layoutImg = (LinearLayout)findViewById(R.id.layoutImg);

        //Recupera a view para colorir
        paintView = (PaintView)findViewById(R.id.desenho);

        currentColor = (ImageButton)paintLayout.getChildAt(0);
        //Seta uma imagem para mostrar que a cor esta sendo selecionada atualmente
        currentColor.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        this.paintView = (PaintView)findViewById(R.id.desenho);

        //Seta a cor inicial para colorir
        paintView.setColor(currentColor.getTag().toString());

        paintView.requestFocus();
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d("Info Debug", "onPause");
        Bitmap bmp = paintView.getBitmapImage();

        if(paintView.getInfoPaint()){
            try{

                //Diretorio que funciona no emulador
                File directory = new File(Environment.getExternalStorageDirectory()+"/BlueTree");
                //File file = new File("/storage/sdcard0/BlueTree");

                if(!directory.exists()){
                    directory.mkdirs();
                }

                //Diretorio que funciona no dispositivo
                //File fileImg= new File("/storage/sdcard0/BlueTree", "imgPainted.png");
                //Diretorio que funciona no emulador
                File fileImg= new File(Environment.getExternalStorageDirectory()+"/BlueTree", "imgPainted.png");
                Log.d("Info Debug","Salvar imagem em: "+fileImg.getAbsolutePath());
                FileOutputStream outStream = new FileOutputStream(fileImg);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();

            }

            catch(Exception e){
                Log.e("Could not save", e.toString());
            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;

    }

    /**
     *  Metodo chamado quando o usuario pressiona uma cor da paleta
     */
    public void paintClicked(View view){

        //verifica se a cor selecionada e' diferente da cor atual
        if(view != currentColor){

            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            paintView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currentColor.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currentColor = (ImageButton) view;

        }

    }

    /**
       Metodo chamado quando o usuario seleciona o botao com o desenho de "borracha"
     */
    public void eraseClicked(View view){
        Log.d("DEBUUUG","eraseClicked");

        String color = "#FFFFFF";
        paintView.setColor(color);

    }

    /**
     * Metodo chamado quando o usuario seleciona o botao com o desenho de "novo"
     */
    public void resetClicked(View view){
        Log.d("DEBUUUG","resetClicked");
        paintView.resetBitmapImage();
    }

    /**
     * Metodo chamado quando o usuario seleciona o botao com o desenho de "salvar arquivo"
     */
    public void saveClicked(View view){
        Log.d("DEBUUUG","saveClicked");
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Salvar");
        saveDialog.setMessage("Deseja salvar a imagem na Galeria?");
        saveDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //salvar desenho
                paintView.setDrawingCacheEnabled(true);

                Bitmap bmp = paintView.getBitmapImage();
               // bmp.compress(Bitmap.CompressFormat.PNG,100,)

                String imgSaved = MediaStore.Images.Media.insertImage(PaintActivity.this.getContentResolver(), bmp,
                       "Imagem.png", "drawing");

                if (imgSaved != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(), "Desenho salvo na Galeria!",
                            Toast.LENGTH_SHORT);
                    savedToast.show();
                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(), "Erro ao salvar imagem.",
                            Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }

                paintView.destroyDrawingCache();
            }
        });

        saveDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }


}
