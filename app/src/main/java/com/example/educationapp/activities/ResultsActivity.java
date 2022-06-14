package com.example.educationapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.educationapp.R;

public class ResultsActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_results);

        setUpResults();


    }

    private void setUpResults() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(75, 75, 75, 75);

        LinearLayout layout = findViewById(R.id.layout_results);

        CardView res = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res.setLayoutParams(lp);

        CardView res2 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res2.setLayoutParams(lp);

        CardView res3 = (CardView) View.inflate(layout.getContext(), R.layout.statistic_data, null);
        res3.setLayoutParams(lp);

        float c = getIntent().getExtras().getInt("correct");
        float t = getIntent().getExtras().getInt("total");
        float p = c / t * 100;

        TextView r = res.findViewById(R.id.result);
        r.setText("Spravne");

        TextView v = res.findViewById(R.id.value);
        v.setText(String.valueOf((int) c));

        TextView r2 = res2.findViewById(R.id.result);
        r2.setText("Celkem");

        TextView v2 = res2.findViewById(R.id.value);
        v2.setText(String.valueOf((int) t));

        TextView r3 = res3.findViewById(R.id.result);
        r3.setText("Uspesnost");

        TextView v3 = res3.findViewById(R.id.value);
        v3.setText(p + " % ");


        layout.addView(res, 0);
        layout.addView(res2, 1);
        layout.addView(res3, 2);

    }

    public void switchBackToMain(){

        Intent switchActivityIntent = new Intent(this,MainActivity.class);
        startActivity(switchActivityIntent);
        finish();
    }


    /**
     * If the back button is pressed, predefined behavior is
     * that it switch to previous activity, with this method
     * I overwrite the behavior and it will switch to main activity
     */
    @Override
    public void onBackPressed()
    {
        switchBackToMain();

    }

}
