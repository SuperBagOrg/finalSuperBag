package com.example.k.superbag2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by K on 2016/8/15.
 */
public class RecImage extends ImageView {

    private Paint paint;
    private Rect rect = new Rect();


    public RecImage(Context context) {
        super(context);
    }

    public RecImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        canvas.getClipBounds(rect);
        canvas.drawRect(rect,paint);
    }
}
