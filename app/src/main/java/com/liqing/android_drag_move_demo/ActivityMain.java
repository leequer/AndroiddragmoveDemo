package com.liqing.android_drag_move_demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * 单击，弹出提示。
 * 长按，缩小，跟着指头移动。
 * 松手恢复到原位和原来大小
 * <p>
 * +换位置
 */
public class ActivityMain extends Activity {
    private ImageView mImage;
    private String tag = "ActivityMain";
    private long mBeagin;
    private long mEnd;
    private boolean mdispatchTouchEvent = false;
    private int mCurrentX;
    private int mCurrentY;
    private int mImageOrginY;
    private int mImageOrginX;
    private ImageView mImage1;
    private int mImage1OrginX;
    private int mImage1OrginY;
    private RelativeLayout mlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImage = findViewById(R.id.imageView);
        mImageOrginX = (int) mImage.getX();
        mImageOrginY = (int) mImage.getY();
        mImage.setOnTouchListener(mImageViewOnTouchListener);
        mImage.setOnClickListener(mImageViewOnclickListener);

        mImage1 = findViewById(R.id.imageView2);
        mImage1OrginX = (int) mImage1.getX();
        mImage1OrginY = (int) mImage1.getY();

        mlayout = findViewById(R.id.id_root);
        // mlayout.setOnTouchListener(mLayoutOnTouch);
    }


    View.OnClickListener mImageViewOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ActivityMain.this, "请选择照片", Toast.LENGTH_SHORT).show();
        }
    };

//    private View.OnTouchListener mLayoutOnTouch = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            int x = (int) event.getX();
//            int y = (int) event.getY();
//            if (inRangeOfView(mImage1,event)){
//                Log.e(tag,"onclick mimage 1 ");
//            }
//            return false;
//        }
//    };

    View.OnTouchListener mImageViewOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            Log.e(tag, "mImage ontouch");
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:


                    mdispatchTouchEvent = false;
                    Log.e(tag, "mImage down");
                    mBeagin = System.currentTimeMillis();
                    mCurrentX = x;
                    mCurrentY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e(tag, "mImage move");
                    mEnd = System.currentTimeMillis();
                    long _duration = mEnd - mBeagin;
                    if (_duration >= 600) {

                        if (!mdispatchTouchEvent) {
                            ScaleAnimation _scale = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, mCurrentX, mCurrentY);
                            _scale.setDuration(200);
                            _scale.setFillAfter(true);
                            mImage.startAnimation(_scale);
                        }

                        mdispatchTouchEvent = true;

                        mImage.offsetLeftAndRight(x - mCurrentX);
                        mImage.offsetTopAndBottom(y - mCurrentY);

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e(tag, "mImage up");


                    if (mdispatchTouchEvent) {
                        //放大
                        ScaleAnimation _scale = new ScaleAnimation(0.5f, 1f, 0.5f, 1f);
                        _scale.setDuration(200);
                        _scale.setFillAfter(true);
                        mImage.startAnimation(_scale);

                        if (!inRangeOfView(mImage1, event)) {
                            Log.e(tag, "if");
                            //覆盖image1
                            mImage.layout((int) mImage1.getX(), (int) mImage1.getY(), (int) mImage1.getX() + mImage1.getWidth(), mImage1.getHeight());
                        } else {
                            Log.e(tag, "else");
                            //回到原始位置
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImage.getLayoutParams();
                            //导包要导相对布局的包x
                            params.leftMargin = mImageOrginX;
                            params.topMargin = mImageOrginY;
                            mImage.setLayoutParams(params);
                            mImage.layout(x, y, mImage.getWidth(), mImage.getHeight());
                        }
                    }
                    return mdispatchTouchEvent;
            }

            return false
                    ;
        }
    };

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
