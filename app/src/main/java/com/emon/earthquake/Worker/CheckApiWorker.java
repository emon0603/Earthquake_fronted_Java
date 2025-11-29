package com.emon.earthquake.Worker;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckApiWorker extends Worker {

    public CheckApiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // এখানে ১৫ মিনিট পর পর API hit হবে
        Log.d("API_WORKER", "Worker Running… API checking…");

        /*String apiUrl = "https://yourserver.com/api/check";  // এখানে আপনার API link দিন

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            Log.d("API_RESPONSE", response.toString());

            // আপনার JSON parse করুন
            JSONObject json = new JSONObject(response.toString());
            boolean hasNewData = json.getBoolean("new_data");

            if (hasNewData) {
                Log.d("API_WORKER", "New data found!");
                // এখানে notification, database save etc. করতে পারবেন
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return Result.success();
    }
}

