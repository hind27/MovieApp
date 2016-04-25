package com.example.hend.Themovieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hend.Themovieapp.Models.Film;
import com.example.hend.Themovieapp.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter
{
 private  Context context;
 private  ArrayList<Film> films = new ArrayList<Film>();
    LayoutInflater inflater;
    public String base_images_url="http://image.tmdb.org/t/p/w185//";

    public GridViewAdapter(Context context,  ArrayList<Film> films)

        {
            this.context = context;
            this.films = films;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //ImageView view = ( ImageView) convertView; //coverview like itemlist inflete
            ViewHolder holder = new ViewHolder();
            //ImageView img;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_item_layout, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                //img= (ImageView) view.findViewById( R.id.image );
                //view.setScaleType(CENTER_CROP);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();

            }



            Picasso.with(context)
                    .load(base_images_url + films.get(position).getPoster_path())
                     .tag(context)
                   //.resize( holder.imageView.getMeasuredWidth(),  holder.imageView.getMeasuredHeight())
                    //.centerCrop()
                    .into(holder.imageView);

            //  android:layout_width="160dp"
            //android:layout_height="160dp"



//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent i = new Intent(context, MoviedetailsActivity.class);
//                    i.putExtra("Id",films.get(position).getId());
//                    i.putExtra("poster_path",films.get(position).getPoster_path());
//                    i.putExtra("backdrop_path",films.get(position).getBackdrop_path());
//                    i.putExtra("overview",films.get(position).getOverview());
//                    context.startActivity(i);
//
//
//                }
//            });


            //convertView.setOnClickListener(new OnItemClickListener(position));
            return convertView;
        }

        @Override
        public int getCount() {
            return films.size();
        }

        @Override public Object getItem(int position) {
            return films.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }
        }

class ViewHolder {
 ImageView imageView;
}

