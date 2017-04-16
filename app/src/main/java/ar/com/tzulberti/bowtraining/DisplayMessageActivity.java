package ar.com.tzulberti.bowtraining;

import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;


public class DisplayMessageActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private boolean shouldStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        this.shouldStop = false;
        this.textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    // TODO check what we can do in this case
                }
                start();
            }
        });
    }

    public void stop(View view) {
        this.shouldStop = true;
    }


    public void start() {
        Intent intent = getIntent();
        int cantSeries = intent.getIntExtra(MainActivity.CANT_SERIES, 0);
        int cantRepeticiones = intent.getIntExtra(MainActivity.CANT_REPETICIONES, 0);
        int timepoRepeticiones = intent.getIntExtra(MainActivity.TIEMPO_REPETICIONES, 0);
        int tiempoSeries = intent.getIntExtra(MainActivity.TIEMPO_SERIES, 0);

        final DisplayMessageActivity self = this;
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (self.shouldStop) {
                    this.cancel();
                }
                readValue(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                readValue("done");
            }
        }.start();
    }


    public void readValue(String text) {
        int res = this.textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        if (res != TextToSpeech.SUCCESS) {
            // TODO check what we can do in this case
        }
    }
}
