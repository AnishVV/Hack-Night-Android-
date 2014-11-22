package com.night.hack.hacknightproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class TransparentActvity extends Activity {

    private ArrayList<Point> points;
    private HashMap<Character, String> swipe_map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        points = new ArrayList<Point>();
        swipe_map = new HashMap<Character, String>();
        swipe_map.put('h', "com.onelouder.baconreaader");
        swipe_map.put('v', "com.chrome.beta");
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, "Swipe for an App", duration);
        toast.show();
    }
    private void startExternalApp(char seq){
        PackageManager p = getPackageManager();
        //List<ApplicationInfo> ps = p.getInstalledApplications(PackageManager.GET_META_DATA);
        Intent t = p.getLaunchIntentForPackage(swipe_map.get(seq));
        startActivity(t);
    }
    public boolean onTouchEvent(android.view.MotionEvent event){
        //int action = MotionEventCompat.getActionMasked(event);


        switch(event.getAction()) {
            case (MotionEvent.ACTION_DOWN) :
                points.add(new Point((int)event.getX(0),(int)event.getY(0)));
                return true;
            case (MotionEvent.ACTION_MOVE) :
                points.add(new Point((int)event.getX(0),(int)event.getY(0)));
                if(points.size() > 2000){
                    points = new ArrayList<Point>();
                }
                return true;
            case (MotionEvent.ACTION_UP) :
                parse();
                return true;
            case (MotionEvent.ACTION_CANCEL) :parse();       return true;
            case (MotionEvent.ACTION_OUTSIDE) :parse();
                return true;
            default : parse();
                return false;
        }
    }
    private void parse(){
        char val = 0;


        //h means horizontal line
        //v means vertical line
        Point p0 = points.get(0);
        int minX = p0.x, maxX = p0.x, minY = p0.y, maxY = p0.y;
        for(Point p : points){
            Log.d("", "" + p.toString());
            if(p.x < minX)
                minX = p.x;
            else if(p.x > maxX)
                maxX = p.x;
            if(p.y < minY)
                minY = p.y;
            else if(p.y > maxY)
                maxY = p.y;
        }
        if(maxY - minY < 2*(maxX - minX)/3 && maxX - minX > 200)
            val = 'h';
        else if(maxY - minY > 200 && maxX - minX < 2*(maxY - minY)/3)
            val = 'v';



        points = new ArrayList<Point>();
        if(val > 0)
            startExternalApp(val);
    }
    public void mapSwipe(char swipe, String appPackageName){
        swipe_map.put(swipe, appPackageName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transparent_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
