package com.emon.earthquake.Worker;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkerHelper {

    public static void startMyWorker(Context context) {

        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.Builder(CheckApiWorker.class, 15, TimeUnit.MINUTES)
                        .addTag("auto_api_worker")
                        .build();

        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork("auto_api_worker",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        workRequest);
    }
}
