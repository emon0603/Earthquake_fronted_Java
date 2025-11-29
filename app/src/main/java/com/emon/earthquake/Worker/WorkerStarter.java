package com.emon.earthquake.Worker;

import android.content.Context;

import com.emon.earthquake.MainActivity;

public class WorkerStarter {

    public void startWorker(Context context) {
        new WorkerHelper().startMyWorker(context);
    }
}

