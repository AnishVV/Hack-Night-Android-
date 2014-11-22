package com.night.hack.hacknightproject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class BackgroundService extends Service implements ButtonCallback{

    ButtonControlReceiver receiver;
    static final String TAG = "Background_Service";

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ButtonControlReceiver();
        receiver.registerCallback(this);
        Log.d(TAG, "Service created");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(receiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        return super.onStartCommand(intent, flags, startId);

    }

    public BackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    LinkedList<Boolean> volList = new LinkedList<Boolean>();
    LinkedList<Long> timeList = new LinkedList<Long>();
    LinkedList<Boolean> sequence = new LinkedList<Boolean>(Arrays.asList(true, false, true, false));
    int pastVol = 0;

    int timeInSec = 20;

    @Override
    public void updateVolume(int newVol) {
        Log.d(TAG, new String("" + newVol));

        if(pastVol == newVol){ return;}
        else if(pastVol < newVol){
            Log.d(TAG, "true");
            volList.add(true);
        }
        else{
            Log.d(TAG, "false");
            volList.add(false);
        }

        pastVol = newVol;
        timeList.addLast(System.currentTimeMillis());
        Log.d(TAG, volList.size() + "");
        if (volList.size() < 4){ return; }
        Long time = (timeList.peekLast() - timeList.peekFirst())/1000;
        Log.d(TAG, time.toString());
        if(time < timeInSec){
            if(checkSequence()){
                Log.d(TAG, "SEQUENCE ACHIEVED");
                Intent dialogIntent = new Intent(this, TransparentActvity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(dialogIntent);
            }
        }

        volList.removeFirst();
        timeList.removeFirst();
    }

    public boolean checkSequence(){
        Iterator<Boolean> it = volList.listIterator();
        Iterator<Boolean> it2 = sequence.listIterator();
        while(it.hasNext() && it2.hasNext()){
            if(it.next().equals(it2.next())){}
            else{return false;}
        }
        return true;
    }
}
