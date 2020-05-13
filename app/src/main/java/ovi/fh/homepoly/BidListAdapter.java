package ovi.fh.homepoly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BidListAdapter extends ArrayAdapter<BidNumber> {

    private Context mContext;
    int reAnInt;

    public BidListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BidNumber> objects) {
        super(context, resource, objects);
        mContext = context;
        reAnInt = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String s = getItem(position).getS();
        BidNumber bidNumber  = new BidNumber(s);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(reAnInt, parent, false);

        TextView textView = convertView.findViewById(R.id.auctionText);

        textView.setText(s);
        return convertView;
    }
}
