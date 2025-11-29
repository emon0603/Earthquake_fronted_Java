package com.emon.earthquake.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.emon.earthquake.Worker.WorkerStarter;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Restart worker after phone reboot
            new WorkerStarter().startWorker(context);
        }
    }
}
