package com.example.inthesearchofcheese;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class MouseTrap {

    Bitmap mouseTrap[] = new Bitmap[3];
    int mouseTrapFrame = 0;
    int mouseTrapX, mouseTrapY, mouseTrapVelocity;
    Random random;

    public MouseTrap(Context context){
        mouseTrap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mousetrap0);
        mouseTrap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mousetrap1);
        mouseTrap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mousetrap2);
        random = new Random();
        resetPosition();
    }

    public Bitmap getMouseTrap(int trapFrame){
        return mouseTrap[trapFrame];
    }

    public int getMouseTrapWidth(){
        return mouseTrap[0].getWidth();
    }

    public int getMouseTrapHeight(){
        return mouseTrap[0].getHeight();
    }

    public void resetPosition(){
        mouseTrapX = random.nextInt(GameView.dWidth - getMouseTrapWidth());
        mouseTrapY = random.nextInt(100);
        mouseTrapVelocity = 30 + random.nextInt(10);
    }
}
