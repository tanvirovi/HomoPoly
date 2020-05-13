package ovi.fh.homepoly;


import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerMovement extends Activity{

    Handler h;
    int stepCounter;
    int j,k;
    int yStepCounter = 0;
    int tempStepCounter;
    Random boxRandom = new Random();

    int o = 0;
    int[] stepArray = {9,19,29,39};
    List<Integer> arrayList;

    TextView simpleTextView;

    public Activity activity;

    public PlayerMovement( Activity activity){
        this.activity = activity;
    }

    public void moveLeftY(int moveBy, View view){
        view.animate().translationYBy(135 * moveBy).setDuration(1000).start();
    }
    public void moveDownX(int moveBy, View view){
        view.animate().translationXBy(120 * moveBy).setDuration(1000).start();
    }
    public void moveRightY(int moveBy, View view){
        view.animate().translationYBy(-135 * moveBy).setDuration(1000).start();
    }
    public void moveUpX(int moveBy, View view){
        view.animate().translationXBy(-120 * moveBy).setDuration(1000).start();
    }


    public void movePlayer(View goPlayerOne, int dice, MoneyDealings object, boolean b){
        //dice = 7;

        arrayList = IntStream.of(stepArray).boxed().collect(Collectors.toList());
        int[] i = {3,3,2,1,12,12,2,3,2,1,12,10,10,5};

        stepCounter = i[o];

        //stepCounter = dice;

        o = o + 1;

        tempStepCounter = yStepCounter + stepCounter;

        Log.i("dice roll tempStepCounter", Integer.toString(tempStepCounter));
        Log.i("dice roll yStepCounter", Integer.toString(yStepCounter));
        Log.i("dice roll stepCounter", Integer.toString(stepCounter));
        h =new Handler();

        //checking the total steps that y can contain if the yStepCounter is 9 19 29 39 and dice roll is 12
        if (arrayList.contains(yStepCounter) && stepCounter == 12){

            if (tempStepCounter >= 40) {
                chanceBonusFines(object, b, 200);
            }

            // one element is 9
            if (yStepCounter == 9) {
                goPlayerOne.animate().translationYBy(135).setDuration(1000).start();

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationXBy(120 * 10).setDuration(1000).start();
                    }
                }, 1500);

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationYBy(-135).setDuration(1000).start();
                    }
                }, 2500);
            }

            else if (yStepCounter == 19) {

                moveDownX(1,goPlayerOne);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveRightY(10,goPlayerOne);

                    }
                }, 1500);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveUpX(1,goPlayerOne);

                    }
                }, 2500);
            }

            else if (yStepCounter == 29) {

                moveRightY(1,goPlayerOne);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveUpX(10,goPlayerOne);

                    }
                }, 1500);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveLeftY(1,goPlayerOne);

                    }
                }, 2500);
            }

            else {

                moveUpX(1,goPlayerOne);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveLeftY(10,goPlayerOne);

                    }
                }, 1500);

                h.postDelayed(new Runnable() {
                    public void run() {

                        moveDownX(1,goPlayerOne);

                    }
                }, 2500);
            }

            yStepCounter += stepCounter;

        }

        else {

            if (tempStepCounter == 4 || tempStepCounter == 7 || tempStepCounter == 18
                    || tempStepCounter == 47 || tempStepCounter == 44 || tempStepCounter == 23
                    || tempStepCounter == 33 || tempStepCounter == 38) {

                int randomNumber = boxRandom.nextInt(1) + 1;

                //int randomNumber = 2;

                pickChance(randomNumber, goPlayerOne, object, b);

                if (randomNumber == 1) {

                    if (tempStepCounter <= 10) {

                        goPlayerOne.animate().translationYBy(135 * stepCounter).setDuration(1000).start();

                    } else if (tempStepCounter > 10 && tempStepCounter <= 20) {

                        if (yStepCounter <= 10) {
                            j = 10 - yStepCounter;
                            goPlayerOne.animate().translationYBy(135 * j).setDuration(1000).start();
                            stepCounter = stepCounter - j;
                        }

                        h.postDelayed(new Runnable() {
                            public void run() {
                                goPlayerOne.animate().translationXBy(120 * stepCounter).setDuration(1000).start();
                            }
                        }, 1500);

                    } else if (tempStepCounter >= 40) {

                        if (yStepCounter <= 40) {
                            j = 40 - yStepCounter;
                            goPlayerOne.animate().translationXBy(-120 * j).setDuration(1000).start();
                            stepCounter = stepCounter - j;
                        }

                    } else if (tempStepCounter > 20 && tempStepCounter <= 30) {

                        if (yStepCounter <= 20) {
                            j = 20 - yStepCounter;
                            goPlayerOne.animate().translationXBy(120 * j).setDuration(1000).start();
                            stepCounter = stepCounter - j;
                            Log.i("in tempStepCounter" , " here " + tempStepCounter);
                            Log.i("in yStepCounter" , " here " + yStepCounter);
                            Log.i("in stepCounter" , " here " + stepCounter);
                        }

                        h.postDelayed(new Runnable() {
                            public void run() {
                                Log.i("in tempStepCounter" , " here " + tempStepCounter);
                                Log.i("in yStepCounter" , " here " + yStepCounter);
                                Log.i("in stepCounter" , " here " + stepCounter);
                                goPlayerOne.animate().translationYBy(-135 * stepCounter).setDuration(1000).start();
                            }
                        }, 1500);

                    } else if (tempStepCounter > 30 && tempStepCounter <= 40) {

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
                    }
                    yStepCounter = 10;
                }

                else if (randomNumber == 2){
                    simpleMove(goPlayerOne, object, b);
                }
            }

            // checking point if the player is exactly at 30
            else if (tempStepCounter == 30) {

                goTOJail(goPlayerOne);

            }

            // general cycle of.. movement of the player throughout the board
            else {
                simpleMove(goPlayerOne, object, b);
            }
        }
    }

    public void simpleMove(View goPlayerOne, MoneyDealings moneyDealings, boolean b){
        // 1 to 10 can be controlled by this portion
        if (tempStepCounter <= 10) {

            goPlayerOne.animate().translationYBy(135 * stepCounter).setDuration(1000).start();
            yStepCounter += stepCounter;
            Log.i("dice yStepCounter", Integer.toString(yStepCounter));

        }

        // moving into 11 to 20 player movement
        else if (tempStepCounter > 10 && tempStepCounter <= 20) {

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

        }

        // moving into 21 to 30 player movement (19 and 30 is not included)
        else if (tempStepCounter > 20 && tempStepCounter <= 30) {

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
            }, 1100);

            yStepCounter += k;
            Log.i("dice yStepCounter", Integer.toString(yStepCounter));

        }

        // moving into 31 to 40 player movement (49 is not included)
        else if (tempStepCounter > 30 && tempStepCounter <= 40) {

            k = stepCounter;

            if (tempStepCounter == 40) {
                chanceBonusFines(moneyDealings, b, 200);
            }

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

        }

        // resetting the previous parameters so the cycle can start again
        else if (tempStepCounter > 40 && tempStepCounter <= 52){


            Log.i("check", " tempStepCounter" + tempStepCounter);
            k = stepCounter;
            if (tempStepCounter == 51){

                goPlayerOne.animate().translationYBy(135 * 10).setDuration(1000).start();

                if (tempStepCounter >= 40) {
                    chanceBonusFines(moneyDealings, b, 200);
                }

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationXBy(120).setDuration(1000).start();
                    }
                }, 1500);

            }
            else if (tempStepCounter == 52){

                goPlayerOne.animate().translationYBy(135 * 10).setDuration(1000).start();

                if (tempStepCounter >= 40) {
                    chanceBonusFines(moneyDealings, b, 200);
                }

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationXBy(120 * 2).setDuration(1000).start();
                    }
                }, 1500);

            }
            else {
                if (yStepCounter <= 40) {
                    j = 40 - yStepCounter;
                    goPlayerOne.animate().translationXBy(-120 * j).setDuration(1000).start();
                    stepCounter = stepCounter - j;
                }

                if (tempStepCounter >= 40) {
                    chanceBonusFines(moneyDealings, b, 200);
                }

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationYBy(135 * stepCounter).setDuration(1000).start();
                    }
                }, 1500);
            }
            yStepCounter = stepCounter;
        }
    }

    private void pickChance(int randomNumber,View view, MoneyDealings o, boolean b){

        Dialog dialog = new Dialog(activity);

        switch(randomNumber){
            case 1:

                dialog.setContentView(R.layout.chest_chance);
                simpleTextView = dialog.findViewById(R.id.simpleText);
                simpleTextView.setText("Go to Jail");
                dialog.show();

                h.postDelayed(new Runnable() {
                    public void run() {
                       dialog.dismiss();
                    }
                }, 1000);

                Log.i("chance","go to jail " + randomNumber);

                goTOJail(view);

                break;
            case 2:

                Log.i("chance","your building loan matures get 150 " + randomNumber);

                dialog.setContentView(R.layout.chest_chance);
                simpleTextView = dialog.findViewById(R.id.simpleText);
                simpleTextView.setText("your building loan" +"\n"+ "matures get 150 ");
                dialog.show();

                h.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1500);

                chanceBonusFines(o,b, 150);
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

    public void goTOJail(View goPlayerOne) {

        if (tempStepCounter == 4 || tempStepCounter == 7 || tempStepCounter == 18
                || tempStepCounter == 47 || tempStepCounter == 44 || tempStepCounter == 23
                || tempStepCounter == 33 || tempStepCounter == 38) {

            if (tempStepCounter == 44 || tempStepCounter == 4) {

                int yy = tempStepCounter-40;

                if (yy == 4) {
                    h.postDelayed(new Runnable() {
                        public void run() {
                            goPlayerOne.animate().translationYBy(135 * yy).setDuration(1000).start();
                        }
                    }, 1500);
                }

                Log.i("dice yStepCounter", Integer.toString(yStepCounter));

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationYBy(135 * 6).setDuration(1000).start();
                    }
                }, 2500);

            }

            else if (tempStepCounter == 47 || tempStepCounter == 7){

                int yy = tempStepCounter-40;

                if (yy == 7) {
                    h.postDelayed(new Runnable() {
                        public void run() {
                            goPlayerOne.animate().translationYBy(135 * yy).setDuration(1000).start();
                        }
                    }, 1500);
                }

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationYBy(135 * 3).setDuration(1000).start();
                    }
                }, 1500);

            }

            else if (tempStepCounter == 18){

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationXBy(-120 * 8).setDuration(1000).start();
                    }
                }, 2500);
            }

            else if (tempStepCounter == 23){

                h.postDelayed(new Runnable() {
                    public void run() {
                moveLeftY(3,goPlayerOne);
                    }
                }, 2500);

                h.postDelayed(new Runnable() {
                    public void run() {
                        moveUpX(10,goPlayerOne);
                    }
                }, 3500);

            }

            else if (tempStepCounter == 33){
                h.postDelayed(new Runnable() {
                    public void run() {
                        moveUpX(7,goPlayerOne);
                    }
                }, 2500);

                h.postDelayed(new Runnable() {
                    public void run() {
                        moveLeftY(10,goPlayerOne);
                    }
                }, 3500);
            }

            else if (tempStepCounter == 38){
                h.postDelayed(new Runnable() {
                    public void run() {
                        moveUpX(2,goPlayerOne);
                    }
                }, 2500);

                h.postDelayed(new Runnable() {
                    public void run() {
                        moveLeftY(10,goPlayerOne);
                    }
                }, 3500);
            }

            h.postDelayed(new Runnable() {
                public void run() {
                    yStepCounter = 10;
                }
            }, 4500);

            // if the player has been sent to "goto Jail"
        }

        else if (tempStepCounter == 30) {

            if (tempStepCounter > 20 && tempStepCounter <= 30) {

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

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationXBy(-120 * 10).setDuration(1000).start();
                    }
                }, 2600);

                h.postDelayed(new Runnable() {
                    public void run() {
                        goPlayerOne.animate().translationYBy(135 * 10).setDuration(1000).start();
                    }
                }, 3700);

                yStepCounter = 10;

            }
        }

    }

    public void chanceBonusFines(MoneyDealings o, boolean b, int addMoney){

            o.simpleAddMoney(addMoney,b);

    }
}
