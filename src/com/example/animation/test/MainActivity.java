package com.example.animation.test;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

public class MainActivity extends Activity {

    private WindowManager mWindowManager;
    private Button mButton;
    
    private WindowManager.LayoutParams mParams;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        mWindowManager = this.getWindowManager();
        
        mButton = new Button(this);
        mButton.setText("Test");
        
        mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 40, PixelFormat.RGBA_8888);
        mParams.width = 100;
        mParams.height = 100;
        mParams.x = 100;
        mParams.y = 100;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager.addView(mButton, mParams);
        
        TopViewAnimator anim = new TopViewAnimator(500, 500);
        anim.setDuration(1000);
        anim.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public class TopViewAnimator
    {
        public int mDx;
        public int mDy;
        
        public float mOriginX;
        public float mOriginY;
        
        public float mRemainderX;
        public float mRemainderY;
        
        public float mDistX;
        public float mDistY;
        public float mTargetX;
        public float mTargetY;
        
        public long mDuration = 300;
        public long mRate = 10;
        public int mFrame;
        public int mTotalFrame;
        public float mTimeStep;
        public float mTimeProgress;
        public float mDistProgress;
        
        public Handler mHandler = new Handler();
        
        public Interpolator mInterpolater = new AccelerateDecelerateInterpolator();
        
        public Runnable mRunnable = new Runnable()
        {
            @Override
            public void run() {
                if (mFrame++ == mTotalFrame) {
                    return;
                }
                mTimeProgress += mTimeStep;
                mDistProgress = mInterpolater.getInterpolation(mTimeProgress);
                mDistX = mDx * mDistProgress + mRemainderX;
                mDistY = mDy * mDistProgress + mRemainderY;
                mTargetX = mOriginX + mDistX;
                mTargetY = mOriginY + mDistY;
                mParams.x = (int)(mTargetX);
                mParams.y = (int)(mTargetY);
                mRemainderX = mTargetX - mParams.x;
                mRemainderY = mTargetY - mParams.y;
                mWindowManager.updateViewLayout(mButton, mParams);
                mHandler.postDelayed(mRunnable, mRate);
            }
        };
        
        public TopViewAnimator(final int dx, final int dy)
        {
            mDx = dx;
            mDy = dy;
        }
        
        public void start()
        {
            mTotalFrame = (int)(mDuration / mRate);
            mTimeStep = 1 / (float)mTotalFrame;
            mOriginX = mParams.x;
            mOriginY = mParams.y;
            mHandler.postDelayed(mRunnable, mRate);
        }
        
        public void setDuration(final long duration)
        {
            mDuration = duration;
        }
    }
}
