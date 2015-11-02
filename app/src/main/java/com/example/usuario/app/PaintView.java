package com.example.usuario.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.Serializable;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by Marina on 15/07/2015.
 */
public class PaintView extends View implements Serializable {

    private Paint paint = new Paint();
    //private Paint canvasPaint;
    private Path path = new Path();
    private int paintColor;
    //private Canvas drawCanvas;
    private Bitmap bitmapImg, bitmapResized;
    final Point point = new Point();
    private boolean init = true;
    private boolean painted = false;

    public PaintView(Context context,AttributeSet attrs){

        super(context, attrs);

        init = true;

        //Suaviza as bordas do toque
        paint.setAntiAlias(true);
        //Seta a largura da linha a ser desenhada
        paint.setStrokeWidth(6);
        //Estilo da linha
        paint.setStyle(Paint.Style.STROKE);
        //Configuracao para quando as linhas se juntarem no desenho
        paint.setStrokeJoin(Paint.Join.ROUND);

        //Instancia um objeto do tipo Paint
        //canvasPaint = new Paint(Paint.DITHER_FLAG);

        //File file = new File("/storage/sdcard0/BlueTree", "imgPainted.png");
        File file = new File(Environment.getExternalStorageDirectory()+"/BlueTree", "imgPainted.png");
        //Environment.getExternalStorageDirectory()+"/BlueTree"
        if(file.exists()){

            bitmapImg = BitmapFactory.decodeFile(file.getAbsolutePath()).copy(Bitmap.Config.ARGB_8888, true);

            if(bitmapImg.getHeight()<0){
                //Bitmap com copia da imagem a ser colorida
                bitmapImg = BitmapFactory.decodeResource(getResources(),
                        R.drawable.img2).copy(Bitmap.Config.ARGB_8888, true);
            }

        }else{

            //Bitmap com copia da imagem a ser colorida
            bitmapImg = BitmapFactory.decodeResource(getResources(),
                    R.drawable.img2).copy(Bitmap.Config.ARGB_8888, true);
        }


       // this.getBitmap();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        //view given size
        super.onSizeChanged(w, h, oldw, oldh);

       // canvasBitmap = createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //drawCanvas = new Canvas(canvasBitmap);

    }

    /**
     * Metodo chamado toda vez que toca na tela
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if(init){

            int height, width;
            width = this.getWidth();
            height = this.getHeight();
            bitmapResized = Bitmap.createScaledBitmap(bitmapImg, width, height, true);
            init = false;
        }

        canvas.drawBitmap(bitmapResized, 0, 0, paint);

    }


    /**
     * Metodo que ira desenhar usando as configuracoes definidas na classe Paint
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                painted = true;
                point.x = (int) x;
                point.y = (int) y;

                /**
                 * Ira chamar o metodo de colorir so' se clicar dentro da imagem
                 */
                if(x < bitmapResized.getWidth() && y < bitmapResized.getHeight()){

                    final int sourceColor = bitmapResized.getPixel((int)x,(int) y);
                    final int targetColor = paint.getColor();

                    new FloodFill(bitmapResized, point, sourceColor, targetColor).run();

                    invalidate();
                }

        }

        return true;
    }

    /**
     * Metodo que configura a cor utilizada para colorir o desenho
     * @param newColor
     */
    public void setColor(String newColor){
        //invalida a view
        invalidate();

        paintColor = Color.parseColor(newColor);
        paint.setColor(paintColor);
    }

    public Bitmap getBitmapImage(){
        return bitmapResized;
    }

    public boolean getInfoPaint(){
        return painted;
    }

    public  void resetBitmapImage(){

        bitmapImg = BitmapFactory.decodeResource(getResources(),
                R.drawable.img2).copy(Bitmap.Config.ARGB_8888, true);
        init = true;

        invalidate();
        painted = false;
    }

}
