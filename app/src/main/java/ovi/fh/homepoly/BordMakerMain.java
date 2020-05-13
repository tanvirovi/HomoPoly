package ovi.fh.homepoly;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.FontsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.Date;
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
    ArrayList<Integer> playerOneMortgageProperties = new ArrayList<>();
    ArrayList<Integer> playerTwoProperties = new ArrayList<>();

    // creating HashMap for the index pair of Indicators
    HashMap<Integer, Integer> indexPairs = new HashMap<>();

    // creating different objects for each player
    MoneyDealings moneyDealings = new MoneyDealings(this);
    MoneyDealings moneyDealings1 = new MoneyDealings(this);

    // creating objects of bidText
    BidNumber bidNumber;

    // tagIndex for gridLayout child index
    int tagIndex, clickIndex;

    // variable for storing the bid amount
    int bidAmount;

    ArrayList<BidNumber> stringArrayList;

    Boolean mortgageModeIsOn = false;
    Boolean redeemModeIsOn = false;
    Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.monopolyGridLayout);

        // Map for keeping index and indicators in a match
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
        doneIndicator = findViewById(R.id.doneIndicator);
        if(turn == 1){
            diceRollButton.setVisibility(View.INVISIBLE);
            doneIndicator.setVisibility(View.VISIBLE);
            goPlayerOne = findViewById(R.id.goPlayerOne);
            playerMovement.movePlayer(goPlayerOne,dice,moneyDealings,playerOneTurn);
            propertiesCanBuy = buyProperties(propertiesCanBuy,playerMovement.yStepCounter);
            turn = 0;
        }else{
            diceRollButton.setVisibility(View.VISIBLE);
            doneIndicator.setVisibility(View.INVISIBLE);
            dice = (random.nextInt(6) + 1) + (random.nextInt(6) + 1);
            goPlayerOne = findViewById(R.id.goPlayerTwo);
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

        gridLayout.getChildAt(40).setBackgroundColor(Color.BLACK);

        Log.i("backGroundColorOfChildIndex ", "Setting the backGround Color of " + gridLayout.getChildAt(stepCounter).getId() );
    }

    // pulling information from parse
    public void dropIn(View view) {

        propertyClicked = (ImageView) view;

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.show_properties_layout);

        propertyText = dialog.findViewById(R.id.propertyText);
        rentText = dialog.findViewById(R.id.rentText);
        houseCost = dialog.findViewById(R.id.houseCost);
        oneHouseRent = dialog.findViewById(R.id.oneHouseRent);
        twoHouseRent = dialog.findViewById(R.id.twoHouseRent);
        threeHouseRent = dialog.findViewById(R.id.threeHouseRent);
        fourHouseRent = dialog.findViewById(R.id.fourHouseRent);
        hotelRent = dialog.findViewById(R.id.hotelRent);

        clickIndex = Integer.parseInt((String) propertyClicked.getTag());

        if (mortgageModeIsOn){

            Log.i("playerOneProperties", " is" + playerOneProperties.size());

            if (playerOneProperties.size() > 0) {

                if (playerOneProperties.contains(clickIndex)) {

                    Toast.makeText(BordMakerMain.this, "i have the properties" + clickIndex, Toast.LENGTH_SHORT).show();

                    playerOneProperties.remove(Integer.valueOf(clickIndex));

                    moneyDealings.addMoney(clickIndex,true,"mortgageValue");

                    playerOneMortgageProperties.add(Integer.valueOf(clickIndex));

                    animation.cancel();
                    animationCreation(playerOneProperties);

                }

                else {
                    Toast.makeText(BordMakerMain.this, "click on a blinking properties", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (redeemModeIsOn){

            Log.i("playerOneProperties", " is" + playerOneMortgageProperties.size());

            if (playerOneMortgageProperties.size() > 0) {

                if (playerOneMortgageProperties.contains(clickIndex)) {

                    Toast.makeText(BordMakerMain.this, "i have the properties" + clickIndex, Toast.LENGTH_SHORT).show();

                    playerOneMortgageProperties.remove(Integer.valueOf(clickIndex));

                    moneyDealings.deductMoney(clickIndex,true,"mortgageReturn");

                    playerOneProperties.add(Integer.valueOf(clickIndex));

                    animation.cancel();
                    animationCreation(playerOneMortgageProperties);

                }

                else {
                    Toast.makeText(BordMakerMain.this, "click on a blinking properties", Toast.LENGTH_SHORT).show();
                }
            }
        }

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
        dialog.show();

        //doSomething();
    }

    // animation of listed properties
    public void viewProperties(View view){
        mortgageModeIsOn = true;

        RelativeLayout relativeLayout = findViewById(R.id.mortGageLayout);
        relativeLayout.setVisibility(View.VISIBLE);
        Button button = findViewById(R.id.closeButton);

        TextView textView = findViewById(R.id.mortgageView);
        textView.setText("click the blinking properties to mortgage");

        animationCreation(playerOneProperties);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.INVISIBLE);
                animation.cancel();
                mortgageModeIsOn = false;
                redeemModeIsOn = false;
            }
        });

    }

    public void animationCreation(ArrayList<Integer> integerArrayList){
        animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        for (int i : integerArrayList){
            gridLayout.getChildAt(i).startAnimation(animation);
        }
    }

    // adding properties to the ArrayList and the total amount update
    public int[] buyProperties(int[] arr, int valueToCheck){

        Dialog buyPropertiesDialog = new Dialog(this);
        Dialog auctionDialogSelection = new Dialog(this);

        buyPropertiesDialog.setCanceledOnTouchOutside(false);
        auctionDialogSelection.setCanceledOnTouchOutside(false);

        if (arr == null || valueToCheck < 0 || valueToCheck >= 40) {
            return arr;
        }

        arrayList = IntStream.of(arr).boxed().collect(Collectors.toList());

        int gridChildIndex,taxIndex;

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
                gridChildIndex = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());

                moneyDealings.deductMoney(gridChildIndex,playerOneTurn,"rent",playerTwoProperties);
                moneyDealings1.addMoney(gridChildIndex,false,"rent",playerTwoProperties);
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
                gridChildIndex = Integer.parseInt(gridLayout.getChildAt(playerMovementTwo.yStepCounter).getTag().toString());

                moneyDealings1.deductMoney(gridChildIndex,playerOneTurn,"rent",playerOneProperties);
                moneyDealings.addMoney(gridChildIndex,true,"rent",playerOneProperties);
            }
        }

        if (arrayList.contains(valueToCheck)){

            buyPropertiesDialog.setContentView(R.layout.buy_popup);

            Objects.requireNonNull(buyPropertiesDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button buyProperties = buyPropertiesDialog.findViewById(R.id.buyProperties);

            auctionDialogSelection.setContentView(R.layout.auction_popup);

            Button auctionProperties = buyPropertiesDialog.findViewById(R.id.auctionProperties);

            arrayList.remove((Integer) valueToCheck);

            auctionProperties.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buyPropertiesDialog.dismiss();

                    auctionDialogSelection.show();
                }
            });

            Button bidButton = auctionDialogSelection.findViewById(R.id.bidButton);
            TextView manualText = auctionDialogSelection.findViewById(R.id.manualNumber);

            ListView listView =  auctionDialogSelection.findViewById(R.id.showBidList);
            stringArrayList = new ArrayList<>();


            bidButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dialog1.dismiss();

                    if (playerOneTurn){

                        tagIndex = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());
                        moneyDealings.auctionProperties(false,tagIndex);

                        String s = String.valueOf(manualText.getText());

                        if (Integer.parseInt(s) > bidAmount) {

                            bidNumber = new BidNumber(s + "player 1 bid");
                            stringArrayList.add(bidNumber);

                            BidListAdapter arrayAdapter = new BidListAdapter(BordMakerMain.this,R.layout.newlist, stringArrayList);

                            listView.setAdapter(arrayAdapter);
                            // problem exist
                            if (Integer.parseInt(s) <= 60 || Integer.parseInt(s) <= moneyDealings.pulledPrice) {

                                Log.i("bid", "is" + moneyDealings.pulledPrice);

                                bidAmount = Integer.parseInt(s) + 20;

                                bidNumber = new BidNumber(bidAmount + "player 2 bid");
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

                                auctionDialogSelection.dismiss();
                            }

                        }

                        else {
                            Toast.makeText(BordMakerMain.this,"make a higher bid",Toast.LENGTH_LONG).show();
                        }

                    }else {

                    }
                }
            });

            Button foldButton = auctionDialogSelection.findViewById(R.id.foldButton);


            foldButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    auctionDialogSelection.dismiss();
                }
            });

            buyProperties.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    buyPropertiesButton(valueToCheck);

                    buyPropertiesDialog.dismiss();
                }
            });

            buyPropertiesDialog.show();

        }

        return arrayList.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    // adding properties to the player ArrayList and the total amount update
    public void buyPropertiesButton(int valueToCheck){

        gridLayout = findViewById(R.id.monopolyGridLayout);

        int broughtPropertiesChildIndex;
        String price = "price";

        //money = findViewById(R.id.moneyLefts);
        if (playerOneTurn){
            playerOneProperties.add(valueToCheck);

            broughtPropertiesChildIndex = Integer.parseInt(gridLayout.getChildAt(playerMovement.yStepCounter).getTag().toString());
            Log.i("Player m", "is" + broughtPropertiesChildIndex);

            int index = indexPairs.get(broughtPropertiesChildIndex);

            gridLayout.getChildAt(index).setBackgroundResource(R.drawable.car);
            Log.i("Player one", "is" + playerOneProperties);

            moneyDealings.deductMoney(broughtPropertiesChildIndex,playerOneTurn,price);

        }else {
            playerTwoProperties.add(valueToCheck);

            broughtPropertiesChildIndex = Integer.parseInt(gridLayout.getChildAt(playerMovementTwo.yStepCounter).getTag().toString());
            int index = indexPairs.get(broughtPropertiesChildIndex);

            gridLayout.getChildAt(index).setBackgroundResource(R.drawable.moto);
            Log.i("Player Two", "is" + playerTwoProperties);

            moneyDealings1.deductMoney(broughtPropertiesChildIndex,playerOneTurn,price);
        }

    }

    // executing the Done button
    public void phaseFinish(View view){
        //doneIndicator =  findViewById(R.id.doneIndicator);
        playerOneTurn = false;

        multiPlayerInIt();
    }

    // putting information into HashMap for the property indicator
    public void populatingHashMap(){
        // key = ChildIndexValueOf GridLayout, value = ChildIndexValueOf GridLayout indicator
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

    public void redeemProperties(View view) {
        redeemModeIsOn = true;
        RelativeLayout relativeLayout = findViewById(R.id.mortGageLayout);
        relativeLayout.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.mortgageView);
        textView.setText("click the blinking properties to unmortgage");

        animationCreation(playerOneMortgageProperties);

    }
}
