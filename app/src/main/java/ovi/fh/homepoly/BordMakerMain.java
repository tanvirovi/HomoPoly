package ovi.fh.homepoly;

import android.app.Dialog;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BordMakerMain extends AppCompatActivity {

    // For randomly selecting number
    Random random = new Random();
    int dice;

    // Setting up the playerMovement class for player movement
    PlayerMovement playerMovement = new PlayerMovement(this);
    PlayerMovement playerMovementTwo = new PlayerMovement(this);

    // creating view for every properties available in the board
    ImageView propertyClicked;
    // player Icons
    ImageView goPlayerOne;

    // textView that will show elements of the properties
    TextView propertyText, rentText, houseCost,oneHouseRent,
            twoHouseRent, threeHouseRent, fourHouseRent, hotelRent, money,money1;

    // The button that will enable player to make a move (diceRollButton)
    // doneIndicator will help player to select when is move is done
    Button diceRollButton, doneIndicator;

    // gridLayout that contains the game board
    androidx.gridlayout.widget.GridLayout gridLayout;

    // variable that used in changing the background
    int stepCounter = 0;

    // variables that will indicates the player
    int turn = 1;

    // this is an array off all the index of all the properties
    int[] propertiesCanBuy = {1,3,6,8,9,11,13,14,16,17,19,21,22,24,25,26,27,28,29,31,32,34,35,37,39};

    // arrayList is created to convert the above array propertiesCanBuy
    List<Integer> arrayList;


    // this will check who's turn at the moment + in buyPropertiesButton()
    boolean playerOneTurn = true;

    // Saves the brought properties of both player
    ArrayList<Integer> playerOneProperties = new ArrayList<>();
    ArrayList<Integer> playerTwoProperties = new ArrayList<>();

    HashMap<Integer, Integer> indexPairs = new HashMap<>();

    MoneyDealings moneyDealings = new MoneyDealings(this);
    MoneyDealings moneyDealings1 = new MoneyDealings(this);

    BidNumber bidNumber;

    int tagIndex;
    int bid,bid1;
    Handler h;
    ArrayList<BidNumber> stringArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.monopolyGridLayout);
        populatingHashMap();
    }

    // Randomly generating two number
    public void rollDice(View view){
        // Buttons to roll the dice
        diceRollButton = findViewById(R.id.diceRollButton);
        dice = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);

        playerOneTurn = true;

        // the value of dice can change that is why assign it to a new int
        int diceInsideTurn = dice;

        // This method will determine who is rolling
        multiPlayerInIt();

        //printing the whole properties array
        Log.i("Resultant Array: " , Arrays.toString(propertiesCanBuy));

        // Only to track the movement
        changeBackground(diceInsideTurn);
    }

    // controlling which player turn it is
    public void multiPlayerInIt(){
        if(turn == 1){
            diceRollButton.setVisibility(View.INVISIBLE);
            goPlayerOne = findViewById(R.id.goPlayerOne);
            playerMovement.movePlayer(goPlayerOne,dice,moneyDealings,playerOneTurn);
            propertiesCanBuy = buyProperties(propertiesCanBuy,playerMovement.yStepCounter);
            turn = 0;
        }else{
            diceRollButton.setVisibility(View.VISIBLE);
            dice = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);
            goPlayerOne = findViewById(R.id.goPlayerTwo);
            //movePlayer(goPlayerOne);
            playerMovementTwo.movePlayer(goPlayerOne,dice,moneyDealings1,playerOneTurn);
            propertiesCanBuy = buyProperties(propertiesCanBuy,playerMovementTwo.yStepCounter);
            turn = 1;
        }
    }

    // this method will change the background according to the dice variable
    public void changeBackground(int diceInsideTurn){

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

    // changing the background of gridLayout by index
    public void changeBackgroundColor(int stepCounter){
        gridLayout.getChildAt(44).setBackgroundColor(Color.BLACK);
        Log.i("index", "ssd" + gridLayout.getChildAt(stepCounter).getId() );
    }

    // pulling information from parse
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

    // animation of listed properties
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

    // adding properties to the ArrayList and the total amount update
    public int[] buyProperties(int[] arr, int valueToCheck){

        Dialog dialog = new Dialog(this);
        Dialog dialog1 = new Dialog(this);

        dialog.setCanceledOnTouchOutside(false);
        dialog1.setCanceledOnTouchOutside(false);


        if (arr == null || valueToCheck < 0 || valueToCheck >= 40) {

            return arr;
        }

        arrayList = IntStream.of(arr).boxed().collect(Collectors.toList());

        int m,taxIndex;

        if (playerOneTurn){

            taxIndex = playerMovement.yStepCounter;

            if (taxIndex == 2){
                moneyDealings.deductMoney(taxIndex,playerOneTurn,"rent");
                Log.i("taxIndex ", "- " + taxIndex);
            }

            else if (taxIndex == 36){
                moneyDealings.deductMoney(taxIndex,playerOneTurn,"rent");
                Log.i("taxIndex ", "- " + taxIndex);
            }

            if (playerTwoProperties.contains(valueToCheck)){

                Log.i("playerOneTurn", "if is " + playerOneTurn);
                m = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());

                moneyDealings.deductMoney(m,playerOneTurn,"rent",playerTwoProperties);
                moneyDealings1.addMoney(m,false,"rent",playerTwoProperties);
            }
        }

        else {

            taxIndex = playerMovementTwo.yStepCounter;

            if (taxIndex == 2){
                moneyDealings1.deductMoney(taxIndex,playerOneTurn,"rent");
                Log.i("taxIndex ", "- " + taxIndex);
            }

            else if (taxIndex == 36){
                moneyDealings1.deductMoney(taxIndex,playerOneTurn,"rent");
                Log.i("taxIndex ", "- " + taxIndex);
            }

            if (playerOneProperties.contains(valueToCheck)){

                Log.i("playerOneTurn", " else is " + playerOneTurn);
                m = Integer.parseInt(gridLayout.getChildAt(playerMovementTwo.yStepCounter).getTag().toString());

                moneyDealings1.deductMoney(m,playerOneTurn,"rent",playerOneProperties);
                moneyDealings.addMoney(m,true,"rent",playerOneProperties);
            }
        }

        if (arrayList.contains(valueToCheck)){

            dialog.setContentView(R.layout.buy_popup);

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buyProperties = dialog.findViewById(R.id.buyProperties);

            dialog1.setContentView(R.layout.auction_popup);

            Button auctionProperties = dialog.findViewById(R.id.auctionProperties);

            arrayList.remove((Integer) valueToCheck);

            auctionProperties.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    dialog1.show();
                }
            });

            Button bidButton = dialog1.findViewById(R.id.bidButton);
            TextView manualText = dialog1.findViewById(R.id.manualNumber);

            ListView listView =  dialog1.findViewById(R.id.showBidList);
            stringArrayList = new ArrayList<>();


            bidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dialog1.dismiss();

                    if (playerOneTurn){

                        tagIndex = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());
                        moneyDealings.auctionProperties(false,tagIndex);

                        String s = String.valueOf(manualText.getText());

                        if (Integer.parseInt(s) > bid1) {

                            bidNumber = new BidNumber(s + "player 1 bid");
                            stringArrayList.add(bidNumber);

                            BidListAdapter arrayAdapter = new BidListAdapter(BordMakerMain.this,R.layout.newlist, stringArrayList);

                            listView.setAdapter(arrayAdapter);
                            // problem exist
                            if (Integer.parseInt(s) <= 60 || Integer.parseInt(s) <= moneyDealings.pulledPrice) {

                                Log.i("bid", "is" + moneyDealings.pulledPrice);

                                bid1 = Integer.parseInt(s) + 20;

                                bidNumber = new BidNumber(bid1 + "player 2 bid");
                                stringArrayList.add(bidNumber);
                                listView.setAdapter(arrayAdapter);

                            }else {

                                bidNumber = new BidNumber("player 2 folded");

                                stringArrayList.add(bidNumber);
                                listView.setAdapter(arrayAdapter);

                                playerOneProperties.add(tagIndex);
                                moneyDealings.deductMoney(tagIndex,playerOneTurn,Integer.parseInt(s));
                                int index = indexPairs.get(tagIndex);
                                gridLayout.getChildAt(index).setBackgroundResource(R.drawable.car);

                                dialog1.dismiss();
                            }

                        }

                        else {
                            Toast.makeText(BordMakerMain.this,"make a higher bid",Toast.LENGTH_LONG).show();
                        }

                    }else {

                    }
                }
            });

            buyProperties.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    buyPropertiesButton(valueToCheck);

                    dialog.dismiss();
                }
            });

            dialog.show();

        }

        return arrayList.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    // adding properties to the player ArrayList and the total amount update
    public void buyPropertiesButton(int valueToCheck){

        gridLayout = findViewById(R.id.monopolyGridLayout);

        int m;
        String price = "price";

        //money = findViewById(R.id.moneyLefts);
        if (playerOneTurn){
            playerOneProperties.add(valueToCheck);

            m = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());
            Log.i("Player m", "is" + m);

            int index = indexPairs.get(m);

            gridLayout.getChildAt(index).setBackgroundResource(R.drawable.car);
            Log.i("Player one", "is" + playerOneProperties);

            moneyDealings.deductMoney(m,playerOneTurn,price);

        }else {
            playerTwoProperties.add(valueToCheck);

            m = Integer.parseInt(gridLayout.getChildAt(playerMovementTwo.yStepCounter).getTag().toString());
            int index = indexPairs.get(m);

            gridLayout.getChildAt(index).setBackgroundResource(R.drawable.moto);
            Log.i("Player Two", "is" + playerTwoProperties);

            moneyDealings1.deductMoney(m,playerOneTurn,price);
        }

    }

    // executing the Done button
    public void phaseFinish(View view){
        doneIndicator =  findViewById(R.id.doneIndicator);
        playerOneTurn = false;

        multiPlayerInIt();
    }

    // putting information into HashMap for the property indicator
    public void populatingHashMap(){
        indexPairs.put(1,44);
        indexPairs.put(3,45);
        indexPairs.put(5,46);
        indexPairs.put(6,47);
        indexPairs.put(8,48);
        indexPairs.put(9,49);
        indexPairs.put(11,50);
        indexPairs.put(12,51);
        indexPairs.put(13,52);
        indexPairs.put(14,53);
        indexPairs.put(15,54);
        indexPairs.put(16,55);
        indexPairs.put(17,56);
        indexPairs.put(19,57);

        Log.i("HashMap " ,"is " + indexPairs);
    }

}
