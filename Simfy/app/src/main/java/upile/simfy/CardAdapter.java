package upile.simfy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Upile.Milanzi on 7/18/2016.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public List<ListItem> items;
    Context context;
    MediaPlayer mediaPlayer;

    public CardAdapter(List<ListItem> items) {
        super();
        this.items = new ArrayList<>();
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ListItem list = items.get(position);
        Picasso.with(context).load(list.getImageUrl()).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {

            }
        });
        holder.textViewName.setText(list.getDescription());
        holder.imageView.setTag(list);
        holder.imageView.setOnClickListener(imageClickListener);
    }

    public View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ListItem listItem = (ListItem) v.getTag();

            String message = listItem.getDescription();
            String play = context.getResources().getString(R.string.play);
            String close = context.getResources().getString(R.string.close);
            String title = listItem.getName();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title).setMessage(message).setCancelable(false)
                    .setPositiveButton(play, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog alert = builder.create();
            alert.show();

            final Uri audio = Uri.parse(String.format("android.resource://" + context.getPackageName() + "/raw/%s", listItem.getName().toLowerCase()));
              mediaPlayer = new MediaPlayer();

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer = MediaPlayer.create(context, audio);
                            mediaPlayer.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    stopMediaPlayer();
                }
            });

        }
    };

    public void stopMediaPlayer(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewName;
        public ProgressBar progressbar;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            progressbar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            progressbar.setVisibility(View.VISIBLE);

        }
    }
}