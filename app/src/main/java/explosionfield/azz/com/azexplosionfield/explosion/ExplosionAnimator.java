package explosionfield.azz.com.azexplosionfield.explosion;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by azz on 15/11/19.
 */
public class ExplosionAnimator extends ValueAnimator{
    public static final int PART_WH = 8; //默认小球宽高
    private Particle[][] particles;
    private Paint mPaint;
    private View mContainer;

    public ExplosionAnimator(View view, Bitmap bitmap, Rect bound) {
        mPaint = new Paint();
        mContainer = view;

        int w = bound.width();
        int h = bound.height();

        int partW_Count = w / PART_WH; //横向个数
        int partH_Count = h / PART_WH; //竖向个数

        int bitmap_part_w = bitmap.getWidth() / partW_Count;
        int bitmap_part_h = bitmap.getHeight() / partH_Count;

        particles = new Particle[partH_Count][partW_Count];
        Point point = null;
        for (int row = 0; row < partH_Count; row ++) { //行
            for (int column = 0; column < partW_Count; column ++) { //列
                //取得当前粒子所在位置的颜色
                int color = bitmap.getPixel(column * bitmap_part_w, row * bitmap_part_h);

                point = new Point(column, row); //x是列，y是行

                particles[row][column] = Particle.generateParticle(color, bound, point);
            }
        }
    }

    public void draw(Canvas canvas) {
        if(!isStarted()) { //动画结束时停止
            return;
        }
        for (Particle[] particle : particles) {
            for (Particle p : particle) {
                p.advance((Float) getAnimatedValue());
                mPaint.setColor(p.color);
//                mPaint.setAlpha((int) (255 * p.alpha)); //只是这样设置，透明色会显示为黑色
                mPaint.setAlpha((int) (Color.alpha(p.color) * p.alpha)); //这样透明颜色就不是黑色了
                canvas.drawCircle(p.cx, p.cy, p.radius, mPaint);
            }
        }

        mContainer.postInvalidate();
    }

    @Override
    public void start() {
        super.start();
        mContainer.postInvalidate();
    }
}
