package ar.com.tzulberti.bowtraining;

import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Display the excercise information.
 *
 * The code has a few functions calling each other because the CountDownTimer isn't blocking
 * the normal execution. So it must use the transition once that the timer has finished.
 */
public class DisplayMessageActivity extends AppCompatActivity {

    private final static int MIN_WARNING_START = 1;

    private TextToSpeech textToSpeech;
    private boolean shouldStop;

    private int seriesAmount;
    private int seriesSleepTime;
    private int repetitionsAmount;
    private int repetitionsDuration;

    private CountDownTimer currentTimer;

    private int currentSerie;
    private int currentRepetition;

    private TextView timeLeftText;
    private TextView statusText;


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

    @Override
    protected void onPause() {
        super.onPause();

        this.currentTimer.cancel();
        this.shouldStop = true;
    }

    public void stop(View view) {
        this.statusText.setText(R.string.stoppedStatus);
        this.shouldStop = true;
        this.currentTimer.cancel();
    }


    public void start() {
        Intent intent = getIntent();
        int startIn = intent.getIntExtra(MainActivity.START_IN, 1);

        this.seriesAmount = intent.getIntExtra(MainActivity.SERIES_AMOUNT, 0);
        this.repetitionsAmount = intent.getIntExtra(MainActivity.REPETITIONS_AMOUNT, 0);
        this.repetitionsDuration = intent.getIntExtra(MainActivity.REPETITIONS_DURATION, 0) + 1;
        this.seriesSleepTime = intent.getIntExtra(MainActivity.SERIES_SLEEP_TIME, 0) + 1 ;

        this.timeLeftText = (TextView) findViewById(R.id.textTimeLeft);
        this.statusText = (TextView) findViewById(R.id.textStatus);

        TextView totalSeriesText = (TextView) findViewById(R.id.textTotalSeries);
        totalSeriesText.setText(Integer.toString(seriesAmount));

        TextView totalRepetitionsText = (TextView) findViewById(R.id.textTotalRepetitions);
        totalRepetitionsText.setText(Integer.toString(repetitionsAmount));

        this.statusText.setText(R.string.waitingToStart);
        this.timeLeftText.setBackgroundResource(R.color.colorBlue);
        this.timeLeftText.setText(Integer.toString(startIn));


        this.currentRepetition = 1;
        this.currentSerie = 1;

        // start the count down before starting to do the exercise
        this.currentTimer = new CountDownTimer(startIn * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsMissing = millisUntilFinished / 1000;
                timeLeftText.setText(Long.toString(secondsMissing));
                if (secondsMissing <= MIN_WARNING_START) {
                    timeLeftText.setBackgroundResource(R.color.colorYellow);
                }
            }

            public void onFinish() {
                // after that time start the exercise
                startSeries();
            }
        }.start();
    }


    /**
     * Runn the current serie (if it should or if the program hasn' been stopped).
     *
     * Also, it will rest if running between series
     */
    private void startSeries() {
        final Resources resources = getResources();
        if (this.shouldStop) {
            return ;
        }

        if (this.currentSerie > this.seriesAmount) {
            // finished reading all the series
            this.readValue(resources.getText(R.string.finishedStatus));
            this.statusText.setText(R.string.finishedStatus);
            this.timeLeftText.setBackgroundResource(R.color.colorBlue);
            this.timeLeftText.setText("");
            return;
        }

        TextView currentSerieText = (TextView) findViewById(R.id.textCurrentSerie);
        currentSerieText.setText(Integer.toString(this.currentSerie));

        // make sure of reseting this value for the different series
        this.currentRepetition = 1;

        if (this.currentSerie == 1) {
            this.startRepetition();
        } else {
            // it has finished the first serie so it should go to the next one
            // after sleeping a given number of seconds
            this.readValue(resources.getText(R.string.finishedSerie));
            this.timeLeftText.setText(Integer.toString(seriesSleepTime));
            this.timeLeftText.setBackgroundResource(R.color.colorGreen);
            this.statusText.setText(resources.getText(R.string.restStatus));

            this.currentTimer = new CountDownTimer(seriesSleepTime * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long secondsMissing = millisUntilFinished / 1000;
                    timeLeftText.setText(Long.toString(secondsMissing));
                    if (secondsMissing <= MIN_WARNING_START) {
                        timeLeftText.setBackgroundResource(R.color.colorYellow);
                    }
                }

                public void onFinish() {
                    startRepetition();
                }
            }.start();
        }

    }


    private void startRepetition() {
        final Resources resources = getResources();
        if (this.shouldStop) {
            return ;
        }

        if (this.currentRepetition > this.repetitionsAmount) {
            // finished the current serie and it is time to go to the next one
            this.currentSerie += 1;
            this.startSeries();
            return ;
        }

        TextView currentRepetitionText = (TextView) findViewById(R.id.textCurrentRepetition);
        currentRepetitionText.setText(Integer.toString(currentRepetition));

        // set the values because on the first execution of onTick will be
        // executed after one second
        this.readValue(Integer.toString(repetitionsDuration));
        this.statusText.setText(resources.getText(R.string.runningStatus));
        timeLeftText.setText(Integer.toString(repetitionsDuration));
        timeLeftText.setBackgroundResource(R.color.colorRed);

        // do the current repetition


        this.currentTimer = new CountDownTimer(repetitionsDuration * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsMissing = millisUntilFinished / 1000;
                timeLeftText.setText(Long.toString(secondsMissing));
                readValue(Long.toString(secondsMissing));
            }

            public void onFinish() {
                //readValue("0");
                readValue(resources.getText(R.string.downBow));
                currentRepetition += 1;
                statusText.setText(resources.getText(R.string.restStatus));
                timeLeftText.setBackgroundResource(R.color.colorGreen);
                timeLeftText.setText(Integer.toString(repetitionsDuration));

                // if this is the last repetiton of the serie there is no need
                // to sleep because that is going to be given by the serie
                if (currentRepetition > repetitionsAmount) {
                    currentSerie += 1;
                    startSeries();
                    return ;
                }

                // sleep between the repetitions
                currentTimer = new CountDownTimer(repetitionsDuration * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long secondsMissing = millisUntilFinished / 1000;
                        timeLeftText.setText(Long.toString(secondsMissing));

                        if (secondsMissing <= MIN_WARNING_START) {
                            timeLeftText.setBackgroundResource(R.color.colorYellow);
                        }
                    }

                    public void onFinish() {
                        readValue(resources.getText(R.string.upBow));
                        startRepetition();
                    }
                }.start();
            }
        }.start();

    }


    public void readValue(CharSequence text) {
        if (this.shouldStop) {
            return;
        }

        int res = this.textToSpeech.speak(String.valueOf(text), TextToSpeech.QUEUE_FLUSH, null);
        if (res != TextToSpeech.SUCCESS) {
            // TODO check what we can do in this case
            System.err.println("Error while reading value: " + text);
        }
    }
}
