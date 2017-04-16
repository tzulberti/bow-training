package ar.com.tzulberti.bowtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String CANT_SERIES = "ar.com.tzulberti.bowtraining.cantseries";
    public static final String CANT_REPETICIONES = "ar.com.tzulberti.bowtraining.cantrepeticiones";
    public static final String TIEMPO_SERIES = "ar.com.tzulberti.bowtraining.timeseries";
    public static final String TIEMPO_REPETICIONES = "ar.com.tzulberti.bowtraining.timeRepeticiones";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int cantSeries = sharedPref.getInt(CANT_SERIES, -1);
        int cantRepetitions = sharedPref.getInt(CANT_REPETICIONES, -1);
        int seriesTime = sharedPref.getInt(TIEMPO_SERIES, -1);
        int repetitionTime = sharedPref.getInt(TIEMPO_REPETICIONES, -1);

        if (cantSeries >= 0) {
            EditText cantSeriesEditText = (EditText) findViewById(R.id.cantSeries);
            cantSeriesEditText.setText(Integer.toString(cantSeries));
        }
    }

    /** Called when the user taps the Send button */
    public void start(View view) {



        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText cantSeriesEditText = (EditText) findViewById(R.id.cantSeries);
        EditText cantRepeticionesEditText = (EditText) findViewById(R.id.cantRepeticiones);
        EditText tiempoSeriesEditText = (EditText) findViewById(R.id.tiempoSeries);
        EditText tiempoRepeticionesEditText = (EditText) findViewById(R.id.tiempoRepeticiones);


        boolean foundError = false;
        String cantSeries = cantSeriesEditText.getText().toString();
        String cantRepetitions = cantRepeticionesEditText.getText().toString();
        String seriesTime = tiempoSeriesEditText.getText().toString();
        String repetitionTime = tiempoRepeticionesEditText.getText().toString();


        if (cantSeries.equals("")) {
            cantSeriesEditText.setError(cantSeriesEditText.getHint());
            foundError = true;
        }

        if (cantRepetitions.equals("")) {
            cantRepeticionesEditText.setError(cantRepeticionesEditText.getHint());
            foundError = true;
        }

        if (seriesTime.equals("")) {
            tiempoSeriesEditText.setError(tiempoSeriesEditText.getHint());
            foundError = true;
        }

        if (repetitionTime.equals("")) {
            tiempoRepeticionesEditText.setError(tiempoRepeticionesEditText.getHint());
            foundError = true;
        }

        if (foundError) {
            return;
        }

        intent.putExtra(CANT_SERIES, cantSeries);
        intent.putExtra(CANT_REPETICIONES, cantRepetitions);
        intent.putExtra(TIEMPO_SERIES, seriesTime);
        intent.putExtra(TIEMPO_REPETICIONES, repetitionTime);



        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(CANT_SERIES, Integer.parseInt(cantSeries));
        editor.putInt(CANT_REPETICIONES, Integer.parseInt(cantRepetitions));
        editor.putInt(TIEMPO_SERIES, Integer.parseInt(seriesTime));
        editor.putInt(TIEMPO_REPETICIONES, Integer.parseInt(repetitionTime));
        editor.commit();

        startActivity(intent);
    }
}
