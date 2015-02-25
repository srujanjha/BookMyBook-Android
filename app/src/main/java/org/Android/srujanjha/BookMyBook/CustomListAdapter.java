package org.android.srujanjha.bookmybook;

/**
 * Created by Srujan Jha on 01-01-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Search> searchItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Search> searchItems) {
        this.activity = activity;
        this.searchItems = searchItems;
    }

    @Override
    public int getCount() {
        return searchItems.size();
    }

    @Override
    public Object getItem(int location) {
        return searchItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView author = (TextView) convertView.findViewById(R.id.author);
        TextView publisher = (TextView) convertView.findViewById(R.id.publisher);

        // getting movie data for the row
        Search m = searchItems.get(position);

        // thumbnail image
        try{thumbNail.setImageUrl(m.getImage(), imageLoader);}
        catch(Exception e){thumbNail.setImageUrl("http://images3.uread.com/productimages/images200/notavailable.gif",imageLoader);}

        // title
        title.setText(m.getTitle());

        // price
        price.setText("Price: " + String.valueOf(m.getPrice()));

        // author
        author.setText("Author:"+m.getAuthor());

        // release year
        publisher.setText(String.valueOf(m.getPublisher()));

        return convertView;
    }


}