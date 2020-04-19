package ovi.fh.homepoly;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import java.util.Random;

public class PlayerMovement {

    Handler h;
    int stepCounter;
    int j,k;
    int yStepCounter = 0;
    int tempStepCounter;
    Random boxRandom = new Random();

    public void movePlayer(View goPlayerOne, int dice){

        stepCounter = dice;

        tempStepCounter = yStepCounter + stepCounter;

        Log.i("dice roll move", Integer.toString(tempStepCounter));
        h =new Handler();

        //checking the total steps that y can contain

        if (tempStepCounter == 4 || tempStepCounter == 7 || tempStepCounter == 18 ||
                tempStepCounter == 23 || tempStepCounter == 33 || tempStepCounter == 38){

            int randomNumber = boxRandom.nextInt(5)+1;
            pickChance(randomNumber);
        }

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

    private void pickChance(int randomNumber){
        switch(randomNumber){
            case 1:
                Log.i("chance","go to jail " + randomNumber);
                break;
            case 2:
                Log.i("chance","go to pallMall " + randomNumber);
                break;
            case 3:
                Log.i("chance","go to start " + randomNumber);
                break;
            case 4:
                Log.i("chance","go to parking " + randomNumber);
                break;
            case 5:
                Log.i("chance","go to Mayfair " + randomNumber);
                break;
            default:
                Log.i("chance","error " + randomNumber);

        }
    }
}
