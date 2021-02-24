package com.example.myapplication.extension;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

public class ShimmerView extends View implements ValueAnimator.AnimatorUpdateListener {

    private static final int CORNER_RADIUS_DP = 9;
    private static final int EDGE_COLOR = Color.TRANSPARENT;

    private static final int ANIMATION_DURATION = 1500;
    private float cornerRadius;

    private Bitmap viewGradientPattern;
    private Paint paint;
    private Paint shaderPaint;
    private int[] shaderColors;

    private ValueAnimator animator;

    public ShimmerView(Context context) {
        super(context);
        init(context);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initialize components required to setup ShimmerView
     *
     * @param context Context
     */
    private void init(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        cornerRadius = dpToPixels(metrics, CORNER_RADIUS_DP);
        int centerColor = ContextCompat.getColor(context, R.color.colorWhiteWithAlpha);

        paint = new Paint();
        shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        shaderColors = new int[]{EDGE_COLOR, centerColor, EDGE_COLOR};

        animator = ValueAnimator.ofFloat(-1f, 2f);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (isAttachedToWindow()) {
            float f = (float) valueAnimator.getAnimatedValue();
            updateShader(getWidth(), f);
            invalidate();
        } else {
            animator.cancel();
        }
    }

    /**
     * Set up LinearGradient to be animated
     *
     * @param w width
     * @param f offset
     */
    private void updateShader(float w, float f) {
        float left = w * f;
        LinearGradient shader = new LinearGradient(left, 0f, left + w, 0f,
                shaderColors, new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP);
        shaderPaint.setShader(shader);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        switch (visibility) {
            case VISIBLE:
                animator.start();
                break;
            case INVISIBLE:
            case GONE:
                animator.cancel();
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateShader(w, -1f);
        if (h > 0 && w > 0) {
            preDrawItemPattern(w, h);
        } else {
            viewGradientPattern = null;
            animator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw gradient background
        canvas.drawRect(0, 0, getWidth(), getHeight(), shaderPaint);
        if (viewGradientPattern != null) {
            // draw bitmap with gradient pattern
            canvas.drawBitmap(viewGradientPattern, 0, 0, paint);
        }
    }

    /**
     * Creates canvas holding our gradient bitmap
     *
     * @param w width
     * @param h height
     */
    private void preDrawItemPattern(int w, int h) {
        viewGradientPattern = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        // draw gradient item in canvas
        Canvas canvas = new Canvas(viewGradientPattern);
        Bitmap item = getItemBitmap(w, h);
        int top = 0;
        do {
            canvas.drawBitmap(item, 0, top, paint);
            top = top + item.getHeight();
        } while (top < canvas.getHeight());

        // give the canvas a transparent background color
        canvas.drawColor(EDGE_COLOR, PorterDuff.Mode.SRC_IN);
    }


    /**
     * Creates bitmap which hold the animated gradient
     *
     * @param w width
     * @param h height
     * @return bitmap used to gradient
     */
    private Bitmap getItemBitmap(int w, int h) {
        // we only need Alpha value in this bitmap
        Bitmap item = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);

        Canvas canvas = new Canvas(item);
        canvas.drawColor(Color.argb(255, 0, 0, 0));

        Paint itemPaint = new Paint();
        itemPaint.setAntiAlias(true);
        itemPaint.setColor(Color.argb(0, 0, 0, 0));
        itemPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        RectF rectF = new RectF(0, 0, w, h);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, itemPaint);

        return item;
    }

    public static float dpToPixels(DisplayMetrics metrics, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }
}
