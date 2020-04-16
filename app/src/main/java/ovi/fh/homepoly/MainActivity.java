package ovi.fh.homepoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    // For randomly selecting number
    Random random = new Random();
    int dice;

    // Setting up the playerMovement class for player movement
    PlayerMovement playerMovement = new PlayerMovement();
    PlayerMovement playerMovementTwo = new PlayerMovement();

    // creating view for every properties available in the board
    ImageView propertyClicked;
    // player Icons
    ImageView goPlayerOne;

    // textView that will show elements of the properties
    TextView propertyText, rentText, houseCost,oneHouseRent,
            twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, money;

    // The button that will enable player to make a move (diceRollButton)
    // doneIndicator will help player to select when is move is done
    Button diceRollButton, doneIndicator;

    // gridLayout that contains the game board
    androidx.gridlayout.widget.GridLayout gridLayout;
    int stepCounter = 0;

    // variables that will indicates the player
    int turn = 1;

    //
    int[] propertiesCanBuy = {1,3,6,8,9,11,13,14,16,17,19,21,22,24,25,26,27,28,29,31,32,34,35,37,38};
    List<Integer> arrayList;

    int totalAmount = 1500;
    int pulledPrice;
    boolean playerOneTurn = true;

    ArrayList<Integer> playerOneProperties = new ArrayList<>();
    ArrayList<Integer> playerTwoProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.monopolyGridLayout);
//        Log.i("index of gridLayout", Integer.toString(gridLayout.));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Properties");
        query.getInBackground("Ga5tOxH7Wm", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null){

                }else {

                    e.printStackTrace();

                }
            }
        });

    }

    public void rollDice(View view){

        diceRollButton = findViewById(R.id.diceRollButton);
        dice = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);

        int diceInsideTurn = dice;

        multiPlayerInIt();

        //goPlayerOne = findViewById(R.id.goPlayerOne);
        //playerMovement.movePlayer(goPlayerOne,dice);

        Log.i("Resultant Array: " , Arrays.toString(propertiesCanBuy));
        changeBackground(diceInsideTurn);
    }

    public void multiPlayerInIt(){
        if(turn == 1){
            diceRollButton.setVisibility(View.INVISIBLE);
            goPlayerOne = findViewById(R.id.goPlayerOne);
            playerMovement.movePlayer(goPlayerOne,dice);
            propertiesCanBuy = buyProperties(propertiesCanBuy,playerMovement.yStepCounter);
            turn = 0;
        }else{
            diceRollButton.setVisibility(View.VISIBLE);
            dice = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);
            goPlayerOne = findViewById(R.id.goPlayerTwo);
            //movePlayer(goPlayerOne);
            playerMovementTwo.movePlayer(goPlayerOne,dice);
            propertiesCanBuy = buyProperties(propertiesCanBuy,playerMovementTwo.yStepCounter);
            turn = 1;
        }
    }

    //this method will change the background according to the
    public void changeBackground(int diceInsideTurn){

        Log.i("dice roll inside turn", Integer.toString(diceInsideTurn));
        int j = stepCounter + diceInsideTurn;

        //Log.i("dice roll", Integer.toString(dice));
        if (stepCounter <= 38 && j < 39) {
            stepCounter += diceInsideTurn;
            changeBackgroundColor(stepCounter);
        }else {
            int i = stepCounter + diceInsideTurn;
            if (i <= 39 ){
                changeBackgroundColor(i);
                stepCounter = -1;
            }else {
                int lastMove = 39 - stepCounter;
                diceInsideTurn = diceInsideTurn - lastMove;
                stepCounter = -1;
                stepCounter += diceInsideTurn;
                changeBackgroundColor(stepCounter);
            }
        }
    }

    public void changeBackgroundColor(int stepCounter){
        gridLayout.getChildAt(stepCounter).setBackgroundColor(Color.BLACK);
        Log.i("index", "ssd" + gridLayout.getChildAt(stepCounter).getId() );

    }

    public void dropIn(View view) {

        propertyClicked = (ImageView) view;

        propertyText = findViewById(R.id.propertyText);
        rentText = findViewById(R.id.rentText);
        houseCost = findViewById(R.id.houseCost);
        oneHouseRent = findViewById(R.id.oneHouseRent);
        twoHouseRent = findViewById(R.id.twoHouseRent);
        threeHouseRent = findViewById(R.id.threeHouseRent);
        fourHouseRent = findViewById(R.id.fourHouseRent);
        hotelRent = findViewById(R.id.hotelRent);

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Properties");
        parseQuery.whereEqualTo("diceRoll", Integer.parseInt(propertyClicked.getTag().toString()));

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){

                        for (ParseObject object : objects){
                            propertyText.setText(object.getString("propertiesNames"));
                            rentText.setText("RENT " + object.getInt("rent") + "$");
                            houseCost.setText( "PER HOUSE COST " + object.getInt("houseCost") + "$");
                            oneHouseRent.setText( object.getInt("houseOneRent") + "$");
                            twoHouseRent.setText( object.getInt("houseTwoRent") + "$");
                            threeHouseRent.setText( object.getInt("houseThreeRent") + "$");
                            fourHouseRent.setText( object.getInt("houseFourRent") + "$");
                            hotelRent.setText( object.getInt("hotelCost") + "$");
                        }
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });

        Log.i("index of gridLayout", "");

        //doSomething();
    }

    public void viewProperties(View view){

        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        for (int i : playerOneProperties){
            gridLayout.getChildAt(i).startAnimation(animation);
        }

    }

    public int[] buyProperties(int[] arr, int valueToCheck){

        Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        if (arr == null || valueToCheck < 0 || valueToCheck >= 39) {

            return arr;
        }
        arrayList = IntStream.of(arr).boxed().collect(Collectors.toList());

        if (arrayList.contains(valueToCheck) == true){

            dialog.setContentView(R.layout.buy_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buyProperties = dialog.findViewById(R.id.buyProperties);

            int i = arrayList.indexOf(valueToCheck);
            arrayList.remove(i);

            buyProperties.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    buyPropertiesButton(valueToCheck);

                    dialog.dismiss();
                }
            });
            dialog.show();

            //moneyLeft.setText();
        }

        return arrayList.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public void buyPropertiesButton(int valueToCheck){

        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Properties");
        //ImageView imageView = (R.id.oldKentIndicator);
        money = findViewById(R.id.moneyLefts);

        gridLayout = findViewById(R.id.monopolyGridLayout);
        int m;
        if (playerOneTurn){
            playerOneProperties.add(valueToCheck);
            m = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());
            Log.i("Player one", "is" + playerOneProperties);

        }else {
            playerTwoProperties.add(valueToCheck);
            m = Integer.parseInt(gridLayout.getChildAt(playerMovementTwo.yStepCounter).getTag().toString());
            Log.i("Player Two", "is" + playerTwoProperties);
            playerOneTurn = true;
        }


        Log.i("inside", "is " + m);
        parseQuery.whereEqualTo("diceRoll", m);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            pulledPrice =  object.getInt("price");
                            Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                }else {
                    e.printStackTrace();
                }

                totalAmount = totalAmount - pulledPrice;
                //imageView.setVisibility(View.VISIBLE);
                money.setText(Integer.toString(totalAmount));
            }
        });

    }

    public void phaseFinish(View view){
        doneIndicator =  findViewById(R.id.doneIndicator);
        playerOneTurn = false;
        multiPlayerInIt();
    }
}
