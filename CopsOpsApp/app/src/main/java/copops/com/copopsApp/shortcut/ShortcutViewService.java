package copops.com.copopsApp.shortcut;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import copops.com.copopsApp.R;
import copops.com.copopsApp.activity.DashboardActivity;
import copops.com.copopsApp.utils.AppSession;

public class ShortcutViewService extends Service implements View.OnClickListener {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private View btnreportofincident, btnassignedintervation, btnchat, btnmain,btnreportanincident;
    private AppSession mSession;
    private final static float CLICK_DRAG_TOLERANCE = 10;
    private WindowManager.LayoutParams params;

    public ShortcutViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSession = AppSession.getInstance(getApplicationContext());

        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.frag_shortcut_design, null);


        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        if (mSession.getData("shortcutlocationx").equals("") && mSession.getData("shortcutlocationy").equals("")) {

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {

            Float fx = Float.valueOf(mSession.getData("shortcutlocationx"));
            Float fy = Float.valueOf(mSession.getData("shortcutlocationy"));

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.x = Math.round(fx);
            params.y = Math.round(fy);
        }

        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);
        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);

        btnreportofincident = mFloatingView.findViewById(R.id.btnreportofincident);
        btnmain = mFloatingView.findViewById(R.id.btnmain);
        btnassignedintervation = mFloatingView.findViewById(R.id.btnassignedintervation);
        btnchat = mFloatingView.findViewById(R.id.btnchat);
        btnreportanincident = mFloatingView.findViewById(R.id.btnreportanincident);

        //adding click listener to close button and expanded view
        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        expandedView.setOnClickListener(this);
        btnchat.setOnClickListener(this);
        btnassignedintervation.setOnClickListener(this);
        btnreportofincident.setOnClickListener(this);
        btnmain.setOnClickListener(this);
        btnreportanincident.setOnClickListener(this);

        //adding an touchlistener to make drag movement of the floating widget
        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        //when the drag is ended switching the state of the widget

                        float upRawX = event.getRawX();
                        float upRawY = event.getRawY();

                        Log.e("============", "params.x ==" + params.x);
                        Log.e("============", "params.y ==" + params.y);

                        float upDX = upRawX - initialTouchX;
                        float upDY = upRawY - initialTouchY;

                        mSession.saveData("shortcutlocationx", String.valueOf(params.x));
                        mSession.saveData("shortcutlocationy", String.valueOf(params.y));

                       /* mSession.saveData("shortcutlocationx", "");
                        mSession.saveData("shortcutlocationy", "");*/

                        if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);// WE HAVE A CLICK!!
                        } else {
                            Log.e("DRag===", "==" + Math.abs(upDX));
                        }

                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //this code is helping the widget to move around the screen with fingers
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.layoutExpanded:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;*/

            case R.id.btnmain:
                //switching views
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.btnassignedintervation:
                //switching views
                mSession.saveData("shortcutscreentype", "assignedintervation");
                Intent ia = new Intent(getApplicationContext(), DashboardActivity.class);
                ia.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ia);
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.btnchat:
                //switching views
              /*  mSession.saveData("shortcutscreentype", "chat");
                Intent ic = new Intent(getApplicationContext(), DialogsActivity.class);
                ic.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ic);*/
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.btnreportofincident:
                //switching views
                Log.e("getgrade==", "" + mSession.getData("operatorgrade"));
             //   if (mSession.getData("operatorgrade").equals("Grade II")) {
                if (mSession.getData("operatorgrade").equalsIgnoreCase(" II")) {
                    mSession.saveData("shortcutscreentype", "reportanincident");
                    Intent ir = new Intent(getApplicationContext(), DashboardActivity.class);
                    ir.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(ir);
                }
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;


            case R.id.buttonClose:
                //closing the widget
                stopSelf();
                break;

            case R.id.btnreportanincident:
                mSession.saveData("shortcutscreentype", "operatorreportanincident");
                Intent ir = new Intent(getApplicationContext(), DashboardActivity.class);
                ir.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ir);
                break;
        }
    }
}
