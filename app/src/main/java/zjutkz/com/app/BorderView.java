package zjutkz.com.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zjutkz.annotation.Knight;
import com.zjutkz.utils.KnightUtil;

/**
 * Created by kangzhe on 16/4/4.
 */
public class BorderView extends View {

    private Paint insidePaint;
    private Paint borderPaint;

    public BorderView(Context context) {
        this(context, null);
    }

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    private void initPaint() {
        insidePaint = new Paint();
        insidePaint.setColor(Color.BLUE);

        borderPaint = new Paint();
        borderPaint.setColor(Color.GREEN);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0,getWidth(),getHeight(),borderPaint);
        canvas.drawRect(15, 15, getWidth() - 15, getHeight() - 15, insidePaint);
    }

    @Knight(nightResId = Color.GREEN,dayResId = Color.RED)
    void setBorderColor(int color){
        borderPaint.setColor(color);
        invalidate();
    }
}
