package ar.com.tzulberti.bowtraining;

import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class DisplayMessageActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private boolean shouldStop;

    private int seriesAmount;
    private int seriesSleepTime;
    private int repetitionsAmount;
    private int repetitionsDuration;

    private int currentSerie;
    private int currentRepetition;


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
        this.seriesAmount = intent.getIntExtra(MainActivity.SERIES_AMOUNT, 0);
        this.repetitionsAmount = intent.getIntExtra(MainActivity.REPETITIONS_AMOUNT, 0);
        this.repetitionsDuration = intent.getIntExtra(MainActivity.REPETITIONS_DURATION, 0);
        this.seriesSleepTime = intent.getIntExtra(MainActivity.SERIES_SLEEP_TIME, 0);


        this.currentRepetition = 1;
        this.currentSerie = 1;

        System.out.println("On start method");
        System.out.println(this.seriesAmount);
        System.out.println(this.repetitionsAmount);
        System.out.println(this.repetitionsDuration);
        System.out.println(this.seriesSleepTime);

        TextView totalSeriesText = (TextView) findViewById(R.id.textTotalSeriesValue);
        totalSeriesText.setText(Integer.toString(seriesAmount));

        TextView totalRepetitionsText = (TextView) findViewById(R.id.textTotalRepetitionsValue);
        totalRepetitionsText.setText(Integer.toString(repetitionsAmount));

        this.startSeries();
    }


    private void startSeries() {
        if (this.shouldStop) {
            return ;
        }

        if (this.currentSerie > this.seriesAmount) {
            // finished reading all the series
            this.readValue("Finished excercise");
            return;
        }

        TextView currentSerieText = (TextView) findViewById(R.id.textCurrentSerieValue);
        currentSerieText.setText(Integer.toString(this.currentSerie));
        this.currentRepetition = 1;

        if (this.currentSerie == 1) {
            this.startRepetition();
        } else {
            // it has finished the first serie so it should go to the next one
            // after sleeping a given number of seconds
            CountDownTimer serieSleepTimer = new CountDownTimer(seriesSleepTime * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (shouldStop) {
                        this.cancel();
                    }

                    long secondsMissing = millisUntilFinished / 1000;
                    if (secondsMissing < 3) {
                        readValue(Long.toString(secondsMissing));
                    }
                }

                public void onFinish() {
                    // TODO ver como sacar este valor del strings.xml
                    readValue("Up");
                    startRepetition();
                }
            }.start();
        }

    }

    private void startRepetition() {
        if (this.shouldStop) {
            return ;
        }

        if (this.currentRepetition > this.repetitionsAmount) {
            // finished the current serie and it is time to go to the next one
            this.readValue("Finished current serie");
            this.currentSerie += 1;
            this.startSeries();
            return ;
        }

        TextView cantRepetitionText = (TextView) findViewById(R.id.textCurrentRepetitionValue);
        cantRepetitionText.setText(Integer.toString(currentRepetition));

        // sleep between the series
        CountDownTimer excerciseTimer = new CountDownTimer(repetitionsDuration * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (shouldStop) {
                    this.cancel();
                }

                readValue(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                // TODO ver como sacar este valor del strings.xml
                readValue("Down");

                currentRepetition += 1;

                // sleep between the repetitions
                CountDownTimer timerDescansoSeries = new CountDownTimer(repetitionsDuration * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        if (shouldStop) {
                            this.cancel();
                        }

                        long secondsMissing = millisUntilFinished / 1000;
                        if (secondsMissing < 3) {
                            readValue(Long.toString(secondsMissing));
                        }
                    }

                    public void onFinish() {
                        // TODO ver como sacar este valor del strings.xml
                        readValue("Up");
                        startRepetition();
                    }
                }.start();
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
