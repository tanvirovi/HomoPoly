package ovi.fh.homepoly;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MoneyDealings extends Activity {

    // this will contain the predefined amount of the properties
    int pulledPrice;
    int colorCode;

    // Total amount of money playerOne have
    int totalAmount = 1500;

    ParseQuery<ParseObject> parseQuery;

    TextView money;

    public Activity activity;

    public MoneyDealings( Activity activity){
        this.activity = activity;
    }

    public void deductMoney(int tag, boolean playerTurn, String s, ArrayList<Integer> integerArrayList){

        if (playerTurn){
            money = activity.findViewById(R.id.moneyLefts);
        }else {
            money = activity.findViewById(R.id.moneyLeftsTwo);
        }

        parseQuery = ParseQuery.getQuery("Properties");

        Log.i("inside", "is " + tag);
        parseQuery.whereEqualTo("diceRoll", tag);


        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null){
                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            pulledPrice =  object.getInt(s);
                            colorCode = object.getInt("colorSet");
                            //Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                }else {
                    e.printStackTrace();
                }
                propertiesSet(integerArrayList, colorCode);

                totalAmount = totalAmount - pulledPrice;
                money.setText(Integer.toString(totalAmount));
                Log.i("Total ", "- " + totalAmount);
            }
        });
    }

    public void deductMoney(int tag, boolean playerTurn, int bidAmount){

        if (playerTurn){
            money = activity.findViewById(R.id.moneyLefts);
        }else {
            money = activity.findViewById(R.id.moneyLeftsTwo);
        }

        parseQuery = ParseQuery.getQuery("Properties");

        Log.i("inside", "is " + tag);

        totalAmount = totalAmount - bidAmount;
        money.setText(Integer.toString(totalAmount));
        Log.i("Total ", "- " + totalAmount);

    }

    public void deductMoney(int tag, boolean playerTurn, String s){

        if (playerTurn){
            money = activity.findViewById(R.id.moneyLefts);
        }else {
            money = activity.findViewById(R.id.moneyLeftsTwo);
        }

        parseQuery = ParseQuery.getQuery("Properties");

        Log.i("inside", "is " + tag);
        parseQuery.whereEqualTo("diceRoll", tag);

        if (tag == 2){
            totalAmount = totalAmount - 200;
            money.setText(Integer.toString(totalAmount));
            Log.i("Totalll ", "- " + tag);
        }

        else if (tag == 36){
            totalAmount = totalAmount - 100;
            money.setText(Integer.toString(totalAmount));
            Log.i("Total ", "- " + totalAmount);
        }
        else {
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                pulledPrice = object.getInt(s);
                                //Log.i("PulledPrice", "is " + pulledPrice);
                            }
                        }
                    } else {
                        e.printStackTrace();
                    }

                    totalAmount = totalAmount - pulledPrice;
                    money.setText(Integer.toString(totalAmount));
                    Log.i("Total ", "- " + totalAmount);
                }
            });
        }
    }

    public void addMoney(int tag, boolean playerTurn,String s, ArrayList<Integer> integerArrayList){

        if (playerTurn){
            money = activity.findViewById(R.id.moneyLefts);
        }else {
            money = activity.findViewById(R.id.moneyLeftsTwo);
        }

        parseQuery = ParseQuery.getQuery("Properties");

        Log.i("inside", "is " + tag);
        parseQuery.whereEqualTo("diceRoll", tag);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            pulledPrice =  object.getInt(s);
                            colorCode = object.getInt("colorSet");
                            //Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                }else {
                    e.printStackTrace();
                }

                propertiesSet(integerArrayList, colorCode);

                totalAmount = totalAmount + pulledPrice;
                money.setText(Integer.toString(totalAmount));
                Log.i("Total", "add " + totalAmount);
            }
        });
    }

    public void addMoney(int tag, boolean playerTurn,String s){

        if (playerTurn){
            money = activity.findViewById(R.id.moneyLefts);
        }else {
            money = activity.findViewById(R.id.moneyLeftsTwo);
        }

        parseQuery = ParseQuery.getQuery("Properties");

        Log.i("inside", "is " + tag);
        parseQuery.whereEqualTo("diceRoll", tag);

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            pulledPrice =  object.getInt(s);
                            //Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                }else {
                    e.printStackTrace();
                }

                totalAmount = totalAmount + pulledPrice;
                money.setText(Integer.toString(totalAmount));
                Log.i("Total", "add " + totalAmount);
            }
        });
    }

    public void simpleAddMoney(int add, boolean b){
        if (b){
            totalAmount = totalAmount + add;
            money = activity.findViewById(R.id.moneyLefts);
            money.setText(Integer.toString(totalAmount));
        }else {
            totalAmount = totalAmount + add;
            money = activity.findViewById(R.id.moneyLeftsTwo);
            money.setText(Integer.toString(totalAmount));
        }


    }

    public void propertiesSet(ArrayList<Integer> integerArrayList, int m){

        switch (m){
            case 1:
                if(integerArrayList.contains(1) && integerArrayList.contains(3)){
                    pulledPrice = pulledPrice * 2;
                }
            case 2:
                if(integerArrayList.contains(6) && integerArrayList.contains(8) && integerArrayList.contains(9)){
                    pulledPrice = pulledPrice * 2;
                }
            case 3:
                if(integerArrayList.contains(11) && integerArrayList.contains(13) && integerArrayList.contains(14)){
                    pulledPrice = pulledPrice * 2;
                }
            case 4:
                if(integerArrayList.contains(16) && integerArrayList.contains(18) && integerArrayList.contains(19)){
                    pulledPrice = pulledPrice * 2;
                }
            case 5:
                if(integerArrayList.contains(21) && integerArrayList.contains(23) && integerArrayList.contains(24)){
                    pulledPrice = pulledPrice * 2;
                }
            case 6:
                if(integerArrayList.contains(26) && integerArrayList.contains(27) && integerArrayList.contains(29)){
                    pulledPrice = pulledPrice * 2;
                }
            case 7:
                if(integerArrayList.contains(31) && integerArrayList.contains(32) && integerArrayList.contains(34)){
                    pulledPrice = pulledPrice * 2;
                }
            case 8:
                if(integerArrayList.contains(37) && integerArrayList.contains(39)){
                    pulledPrice = pulledPrice * 2;
                }

        }
    }

    public void auctionProperties(boolean b, int tag){
        parseQuery = ParseQuery.getQuery("Properties");
        parseQuery.whereEqualTo("diceRoll", tag);
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            pulledPrice = object.getInt("price");
                            //Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
