package ar.com.tzulberti.bowtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String SERIES_AMOUNT = "ar.com.tzulberti.bowtraining.seriesAmount";
    public static final String SERIES_SLEEP_TIME = "ar.com.tzulberti.bowtraining.seriesSleepTime";
    public static final String REPETITIONS_AMOUNT = "ar.com.tzulberti.bowtraining.repetitionsAmount";
    public static final String REPETITIONS_DURATION = "ar.com.tzulberti.bowtraining.repetitionsDuration";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        int seriesAmount = sharedPref.getInt(SERIES_AMOUNT, -1);
        int seriesSleepTime = sharedPref.getInt(SERIES_SLEEP_TIME, -1);
        int repetitionsAmount = sharedPref.getInt(REPETITIONS_AMOUNT, -1);
        int repetitionsDuration = sharedPref.getInt(REPETITIONS_DURATION, -1);

        // TODO maybe create a map with this values to prevent doing copy&paste?
        if (seriesAmount >= 0) {
            EditText cantSeriesEditText = (EditText) findViewById(R.id.seriesAmount);
            cantSeriesEditText.setText(Integer.toString(seriesAmount));
        }

        if (seriesSleepTime >= 0) {
            EditText cantSeriesEditText = (EditText) findViewById(R.id.seriesSleepTime);
            cantSeriesEditText.setText(Integer.toString(seriesSleepTime));
        }

        if (repetitionsAmount >= 0) {
            EditText cantSeriesEditText = (EditText) findViewById(R.id.repetitionsAmount);
            cantSeriesEditText.setText(Integer.toString(repetitionsAmount));
        }
        if (repetitionsDuration >= 0) {
            EditText cantSeriesEditText = (EditText) findViewById(R.id.repetitionsDuration);
            cantSeriesEditText.setText(Integer.toString(repetitionsDuration));
        }

    }

    /** Called when the user taps the Send button */
    public void start(View view) {
        EditText seriesAmountEditText = (EditText) findViewById(R.id.seriesAmount);
        EditText repetitionsAmountEditText = (EditText) findViewById(R.id.repetitionsAmount);
        EditText seriesSleepTimeEditText = (EditText) findViewById(R.id.seriesSleepTime);
        EditText repetitionDurationEditText = (EditText) findViewById(R.id.repetitionsDuration);


        boolean foundError = false;
        String seriesAmount = seriesAmountEditText.getText().toString();
        String repetitionsAmount = repetitionsAmountEditText.getText().toString();
        String seriesSleepTime = seriesSleepTimeEditText.getText().toString();
        String repetitionDuration = repetitionDurationEditText.getText().toString();


        if (seriesAmount.equals("")) {
            seriesAmountEditText.setError(seriesAmountEditText.getHint());
            foundError = true;
        }

        if (repetitionsAmount.equals("")) {
            repetitionsAmountEditText.setError(repetitionsAmountEditText.getHint());
            foundError = true;
        }

        if (seriesSleepTime.equals("")) {
            seriesSleepTimeEditText.setError(seriesSleepTimeEditText.getHint());
            foundError = true;
        }

        if (repetitionDuration.equals("")) {
            repetitionDurationEditText.setError(repetitionDurationEditText.getHint());
            foundError = true;
        }

        if (foundError) {
            return;
        }

        // save the preferences for the next execution
        // TODO maybe create a dict with the values to prevent copy&paste
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SERIES_AMOUNT, Integer.parseInt(seriesAmount));
        editor.putInt(REPETITIONS_AMOUNT, Integer.parseInt(repetitionsAmount));
        editor.putInt(SERIES_SLEEP_TIME, Integer.parseInt(seriesSleepTime));
        editor.putInt(REPETITIONS_DURATION, Integer.parseInt(repetitionDuration));
        editor.commit();

        // start the real exercise
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(SERIES_AMOUNT, Integer.parseInt(seriesAmount));
        intent.putExtra(REPETITIONS_AMOUNT, Integer.parseInt(repetitionsAmount));
        intent.putExtra(SERIES_SLEEP_TIME, Integer.parseInt(seriesSleepTime));
        intent.putExtra(REPETITIONS_DURATION, Integer.parseInt(repetitionDuration));
        startActivity(intent);
    }
}
