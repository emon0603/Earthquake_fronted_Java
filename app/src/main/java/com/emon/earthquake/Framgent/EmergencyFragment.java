package com.emon.earthquake.Framgent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emon.earthquake.R;


public class EmergencyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View helpView = inflater.inflate(R.layout.fragment_emergency, container, false);
        TextView textView = helpView.findViewById(R.id.helpText);

        TextColorSet(textView);
        CallingMethod(helpView);


        return helpView;
    }


    void CallingMethod(View view){
        ImageView helpBtn = view.findViewById(R.id.image_bt);

        helpBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:999"));
            startActivity(intent);

        });



    }

    void TextColorSet(TextView textView){

        String text = "Click the help button to call the help";
        SpannableString spannable = new SpannableString(text);

        int start = text.indexOf("help button");
        int end = start + "help button".length(); // <-- এখানে () খুব জরুরি

        spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#D6453E")),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView.setText(spannable);
    }
}