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
    public static final String START_IN = "ar.com.tzulberti.bowtraining.startIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        int seriesAmount = sharedPref.getInt(SERIES_AMOUNT, -1);
        int seriesSleepTime = sharedPref.getInt(SERIES_SLEEP_TIME, -1);
        int repetitionsAmount = sharedPref.getInt(REPETITIONS_AMOUNT, -1);
        int repetitionsDuration = sharedPref.getInt(REPETITIONS_DURATION, -1);
        int startIn = sharedPref.getInt(START_IN, -1);


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

        if (startIn >= 0) {
            EditText startInEditText = (EditText) findViewById(R.id.startIn);
            startInEditText.setText(Integer.toString(startIn));
        }

    }

    /** Called when the user taps the Send button */
    public void start(View view) {
        EditText seriesAmountEditText = (EditText) findViewById(R.id.seriesAmount);
        EditText repetitionsAmountEditText = (EditText) findViewById(R.id.repetitionsAmount);
        EditText seriesSleepTimeEditText = (EditText) findViewById(R.id.seriesSleepTime);
        EditText repetitionDurationEditText = (EditText) findViewById(R.id.repetitionsDuration);
        EditText startInEditText = (EditText) findViewById(R.id.startIn);

        boolean foundError = false;
        String seriesAmountInputValue = seriesAmountEditText.getText().toString();
        String repetitionsAmountInputValue = repetitionsAmountEditText.getText().toString();
        String seriesSleepTimeInputValue = seriesSleepTimeEditText.getText().toString();
        String repetitionDurationInputValue = repetitionDurationEditText.getText().toString();
        String startInInputValue = startInEditText.getText().toString();

        String requiredValueError = getResources().getString(R.string.requiredValue);

        if (seriesAmountInputValue.equals("")) {
            seriesAmountEditText.setError(requiredValueError);
            foundError = true;
        }

        if (repetitionsAmountInputValue.equals("")) {
            repetitionsAmountEditText.setError(requiredValueError);
            foundError = true;
        }

        if (seriesSleepTimeInputValue.equals("")) {
            seriesSleepTimeEditText.setError(requiredValueError);
            foundError = true;
        }

        if (repetitionDurationInputValue.equals("")) {
            repetitionDurationEditText.setError(requiredValueError);
            foundError = true;
        }

        if (startInInputValue.equals("")) {
            startInEditText.setError(requiredValueError);
            foundError = true;
        }

        if (foundError) {
            return;
        }

        int seriesAmount = Integer.parseInt(seriesAmountInputValue);
        int repetitionsAmount = Integer.parseInt(repetitionsAmountInputValue);
        int seriesSleepTime = Integer.parseInt(seriesSleepTimeInputValue);
        int repetitionDuration = Integer.parseInt(repetitionDurationInputValue);
        int startIn = Integer.parseInt(startInInputValue);

        if (seriesAmount <= 0) {
            seriesAmountEditText.setError(requiredValueError);
            foundError = true;
        }

        if (repetitionsAmount <= 0) {
            repetitionsAmountEditText.setError(requiredValueError);
            foundError = true;
        }

        if (seriesSleepTime <= 0) {
            seriesSleepTimeEditText.setError(requiredValueError);
            foundError = true;
        }

        if (repetitionDuration <= 0) {
            repetitionDurationEditText.setError(requiredValueError);
            foundError = true;
        }

        if (startIn <= 0) {
            startInEditText.setError(startInEditText.getHint());
            foundError = true;
        }

        if (foundError) {
            return;
        }

        // save the preferences for the next execution
        // TODO maybe create a dict with the values to prevent copy&paste
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SERIES_AMOUNT, seriesAmount);
        editor.putInt(REPETITIONS_AMOUNT, repetitionsAmount);
        editor.putInt(SERIES_SLEEP_TIME, seriesSleepTime);
        editor.putInt(REPETITIONS_DURATION, repetitionDuration);
        editor.putInt(START_IN, startIn);
        editor.commit();

        // start the real exercise
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(SERIES_AMOUNT, seriesAmount);
        intent.putExtra(REPETITIONS_AMOUNT, repetitionsAmount);
        intent.putExtra(SERIES_SLEEP_TIME, seriesSleepTime);
        intent.putExtra(REPETITIONS_DURATION, repetitionDuration);
        intent.putExtra(START_IN, startIn);
        startActivity(intent);
    }
}
