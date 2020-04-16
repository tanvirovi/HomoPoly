package ovi.fh.homepoly;

import android.os.Handler;
import android.util.Log;
import android.view.View;

public class PlayerMovement {

    Handler h;
    int stepCounter;
    int j,k;
    int yStepCounter = 0;
    int tempStepCounter;


    public void movePlayer(View goPlayerOne, int dice){

        stepCounter = dice;
        Log.i("dice roll move", Integer.toString(stepCounter));

        tempStepCounter = yStepCounter + stepCounter;

        h =new Handler();

        //checking the total steps that y can contain

        if (tempStepCounter <= 10){

            goPlayerOne.animate().translationYBy(135 * stepCounter).setDuration(1000).start();
            yStepCounter += stepCounter;
            Log.i("dice yStepCounter", Integer.toString(yStepCounter));
            //diceRollButton.setVisibility(View.VISIBLE);

        }else if (tempStepCounter > 10 && tempStepCounter <= 20){

            k = stepCounter;

            if (yStepCounter <= 10) {
                j = 10 - yStepCounter;
                goPlayerOne.animate().translationYBy(135 * j).setDuration(10).start();
                stepCounter = stepCounter - j;
            }

            h.postDelayed(new Runnable() {
                public void run() {
                    goPlayerOne.animate().translationXBy(120 * stepCounter).setDuration(10).start();
                }
            }, 15);

            yStepCounter += k;
            Log.i("dice xStepCounter", Integer.toString(yStepCounter));


        }else if (tempStepCounter > 20 && tempStepCounter <= 30){

            k = stepCounter;

            if (yStepCounter <= 20) {
                j = 20 - yStepCounter;
                goPlayerOne.animate().translationXBy(120 * j).setDuration(1000).start();
                stepCounter = stepCounter - j;
            }


            h.postDelayed(new Runnable() {
                public void run() {
                    goPlayerOne.animate().translationYBy(-135 * stepCounter).setDuration(1000).start();
                }
            }, 1500);

            yStepCounter += k;
            Log.i("dice yStepCounter", Integer.toString(yStepCounter));

        }else if (tempStepCounter > 30 && tempStepCounter <= 40){

            k = stepCounter;

            if (yStepCounter <= 30) {
                j = 30 - yStepCounter;
                goPlayerOne.animate().translationYBy(-135 * j).setDuration(1000).start();
                stepCounter = stepCounter - j;
            }

            h.postDelayed(new Runnable() {
                public void run() {
                    goPlayerOne.animate().translationXBy(-120 * stepCounter).setDuration(1000).start();
                }
            }, 1500);

            yStepCounter += k;
            Log.i("dice -xStepCounter", Integer.toString(yStepCounter));

        }else{

            k = stepCounter;
            if (yStepCounter <= 40) {
                j = 40 - yStepCounter;
                goPlayerOne.animate().translationXBy(-120 * j).setDuration(1000).start();
                stepCounter = stepCounter - j;
            }

            h.postDelayed(new Runnable() {
                public void run() {
                    goPlayerOne.animate().translationYBy(135 * stepCounter).setDuration(1000).start();
                }
            }, 1500);

            yStepCounter = stepCounter;
        }


    }
}
