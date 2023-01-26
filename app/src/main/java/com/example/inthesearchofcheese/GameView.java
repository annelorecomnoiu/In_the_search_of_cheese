package com.example.inthesearchofcheese;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class GameView extends View {

    Bitmap background, ground, mouse;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    float mouseX, mouseY;
    float oldX;
    float oldMouseX;
    ArrayList<MouseTrap> mouseTraps;
    ArrayList<Explosion> explosions;

    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(),R.drawable.ground);
        mouse = BitmapFactory.decodeResource(getResources(), R.drawable.mouse2);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;

        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenney_blocks));
        healthPaint.setColor(Color.GREEN);
        mouseX = dWidth / 2 - mouse.getWidth() / 2;
        mouseY = dHeight - ground.getHeight() - mouse.getHeight();

        mouseTraps = new ArrayList<>();
        explosions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MouseTrap mouseTrap = new MouseTrap(context);
            mouseTraps.add(mouseTrap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground,null, rectGround,null);
        canvas.drawBitmap(mouse, mouseX, mouseY, null);

        for(int i=0; i<mouseTraps.size(); i++) {

            canvas.drawBitmap(mouseTraps.get(i).getMouseTrap(mouseTraps.get(i).mouseTrapFrame), mouseTraps.get(i).mouseTrapX,
                    mouseTraps.get(i).mouseTrapY, null);

            mouseTraps.get(i).mouseTrapFrame++;
            if (mouseTraps.get(i).mouseTrapFrame > 2) {
                mouseTraps.get(i).mouseTrapFrame = 0;
            }

            mouseTraps.get(i).mouseTrapY += mouseTraps.get(i).mouseTrapVelocity;

            if (mouseTraps.get(i).mouseTrapY + mouseTraps.get(i).getMouseTrapHeight() >= dHeight - ground.getHeight()) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = mouseTraps.get(i).mouseTrapX;
                explosion.explosionY = mouseTraps.get(i).mouseTrapY;
                explosions.add(explosion);
                mouseTraps.get(i).resetPosition();
            }

            if (mouseTraps.get(i).mouseTrapX + mouseTraps.get(i).getMouseTrapWidth() >= mouseX
                    && mouseTraps.get(i).mouseTrapX <= mouseX + mouse.getWidth()
                    && mouseTraps.get(i).mouseTrapY + mouseTraps.get(i).getMouseTrapHeight() >= mouseY) {
                life--;
                mouseTraps.get(i).resetPosition();
                if (life == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for(int i=0; i<explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 2){
                explosions.remove(i);
            }
        }

        if(life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if(life == 1){
            healthPaint.setColor(Color.RED);
        }

        canvas.drawRect(dWidth-200, 30, dWidth-200+60*life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        if(touchY >= mouseY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldMouseX = mouseX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float distance = oldX - touchX;
                float newMouseX = oldMouseX - distance;
                if(newMouseX <=0 )
                    mouseX = 0;
                else if(newMouseX >= dWidth - mouse.getWidth())
                    mouseX = dWidth - mouse.getWidth();
                else
                    mouseX = newMouseX;
            }
        }
        return true;
    }


}
