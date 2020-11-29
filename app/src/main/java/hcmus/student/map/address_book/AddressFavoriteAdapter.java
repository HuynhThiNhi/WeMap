package hcmus.student.map.address_book;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import hcmus.student.map.database.Database;
import hcmus.student.map.database.Place;
import hcmus.student.map.R;

public class AddressFavoriteAdapter extends BaseAdapter {
    Database mDatabase;
    Context context;
    List<Place> places;


    public AddressFavoriteAdapter(Context context) {
        this.context = context;
        this.mDatabase = new Database(context);
        this.places = new ArrayList<>();
    }

    public void getUpdate() {
        places = mDatabase.getPlacesFavorite();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Place getItem(int position) {
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.row_place, null);
        TextView txtListItemName = convertView.findViewById(R.id.txt_list_item_name);
        TextView txtListItemLatLng = convertView.findViewById(R.id.txt_list_item_lat_lng);
        final Place place = getItem(position);

        txtListItemName.setText(place.getName());
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        LatLng location = place.getLocation();
        txtListItemLatLng.setText(formatter.format("(%.2f, %.2f)", location.latitude, location.longitude).toString());
        if (place.getAvatar() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(place.getAvatar(), 0, place.getAvatar().length);
            ImageView ivAvatar = convertView.findViewById(R.id.ivAvatar);
            ivAvatar.setBackground(new BitmapDrawable(context.getResources(), bmp));

        }

        final Button btnFavorite = convertView.findViewById(R.id.btnFavorite);

        if (place.getFavorite().equals("1"))
            btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (place.getFavorite().equals("0")) {
                    place.setFavorite("1");
                    LatLng location = place.getLocation();
                    mDatabase.addFavorite(location.latitude, location.longitude);
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_red);
                } else {
                    place.setFavorite("0");
                    LatLng location = place.getLocation();
                    mDatabase.removeFavorite(location.latitude, location.longitude);
                    btnFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite);
                }
            }
        });
        return convertView;
    }
}