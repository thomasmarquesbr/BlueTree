package com.example.usuario.app;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Marina on 16/07/2015.
 */
public class FloodFill extends Thread  {

    Bitmap mBitmap;
    int               mTargetColor;
    int            mNewColor;
    Point mPoint;
 //   Runnable       mCallback;
   // ProgressDialog mProgressDialog;

    //public FloodFill(ProgressDialog pd, Runnable callback,
    //                       Bitmap bitmap, Point pt, int targetColor, int newColor)
    public FloodFill(Bitmap bitmap, Point pt, int targetColor, int newColor)

    {
        Log.d("DEBUG", "NO CONSTRUTOR");
        mBitmap          = bitmap;
        mPoint           = pt;
        mTargetColor     = targetColor;
        mNewColor          = newColor;
       // mProgressDialog = pd;
      //  mCallback       = callback;
    }

    @Override
    public void run()
    {
        QueueLinearFloodFiller filler =
                new QueueLinearFloodFiller(mBitmap, mTargetColor, mNewColor);

        filler.setTolerance(10);

        filler.floodFill(mPoint.x, mPoint.y);

        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            //mProgressDialog.dismiss();
            //mCallback.run();
        }
    };

}
