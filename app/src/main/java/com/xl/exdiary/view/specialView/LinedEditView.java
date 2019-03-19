package com.xl.exdiary.view.specialView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;


@SuppressLint("AppCompatCustomView")
public class LinedEditView extends EditText {
    public LinedEditView(Context context) {
        super(context);
    }

    public LinedEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinedEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
//       mPaint.setColor(0x80000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.BLACK);

        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int spacingHeight = (int) Math.round(getResources().getDisplayMetrics().density * 2);



        if (Build.VERSION.SDK_INT >= 16) {
            spacingHeight = (int) getLineSpacingExtra();
        }
        height = height + spacingHeight;//把最后一个行间距也计算进去
        int count = (height - paddingTop - paddingBottom) / lineHeight;


        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i + 1) + paddingTop - spacingHeight / 2;
            canvas.drawLine(0 + paddingLeft, baseline, right - paddingRight, baseline, mPaint);
        }
        super.onDraw(canvas);
    }

}