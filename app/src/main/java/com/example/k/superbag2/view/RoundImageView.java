package com.example.k.superbag2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by K on 2016/8/20.
 */
public class RoundImageView extends ImageView {

    private Matrix matrix;
    private BitmapShader shader;
    private Paint paint;
    //半径
    private int radius;
    //
    private int width;

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        paint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //强制改变view的宽高一致，避免拉伸或无法完全填充
        width = Math.min(getMeasuredWidth(),getMeasuredHeight());
        radius = width / 2;
    }

    private void setShader(){
        Drawable drawable = getDrawable();
        if (drawable == null){
            return;
        }
        Bitmap bitmap = drawable2Bitmap(drawable);
        shader = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP);
        float scale = 1.0f;
        //获取到Bitmap宽高最小值
        int size = Math.min(bitmap.getWidth(),bitmap.getHeight());
        scale = width * 1.0f / size;
        matrix.setScale(scale,scale);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (getDrawable() == null){
            return;
        }
        setShader();
        canvas.drawCircle(radius,radius,radius,paint);

        //画外围圆圈
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(3);

        int w = getPaddingLeft() + getPaddingRight();
        int r = (getWidth() - w) / 2;

        canvas.drawCircle(getWidth()/2,getHeight()/2,r,circlePaint);
    }

    private Bitmap drawable2Bitmap(Drawable drawable){
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            return bitmapDrawable.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }
}
