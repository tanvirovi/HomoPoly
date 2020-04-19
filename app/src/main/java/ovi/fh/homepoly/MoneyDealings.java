package ovi.fh.homepoly;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MoneyDealings extends Activity {

    // this will contain the predefined amount of the properties
    int pulledPrice;
    // Total amount of money playerOne have
    int totalAmount = 1500;

    ParseQuery<ParseObject> parseQuery;

    TextView money;
    public Activity activity;

    public MoneyDealings( Activity activity){

        this.activity = activity;

    }
    public void deductMoney(int tag, boolean playerTurn){
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
                            pulledPrice =  object.getInt("price");
                            //Log.i("PulledPrice", "is " + pulledPrice);
                        }
                    }
                }else {
                    e.printStackTrace();
                }

                totalAmount = totalAmount - pulledPrice;
                money.setText(Integer.toString(totalAmount));
                Log.i("Total", "is " + totalAmount);
            }
        });

    }
}
