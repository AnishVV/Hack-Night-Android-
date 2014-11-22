package com.night.hack.hacknightproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by Chocolatesauce on 11/21/2014.
 */
public class ButtonControlReceiver extends BroadcastReceiver{

    ButtonCallback bService;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("WE GOT A BUTTON CLICK!");
        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
        //System.out.println(volume);
        bService.updateVolume(volume);
    }

    public void registerCallback(BackgroundService service){
        bService = service;
    }
}
