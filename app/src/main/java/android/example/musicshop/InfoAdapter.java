package android.example.musicshop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    ArrayList<InfoItem> infoItems;
    Context context;

    public InfoAdapter(ArrayList<InfoItem> infoItems, Context context){
        this.infoItems = infoItems;
        this.context = context;

    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_item,
                viewGroup,false);
        InfoViewHolder infoViewHolder = new InfoViewHolder(view);
        return infoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder infoViewHolder, int i) {
        InfoItem infoItem = infoItems.get(i);
        infoViewHolder.titleTextView.setText(infoItem.getTitle());
    }


    @Override
    public int getItemCount() {
        return infoItems.size();
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       public TextView titleTextView;

       public InfoViewHolder(@NonNull View itemView) {
           super(itemView);
           itemView.setOnClickListener(this);

           titleTextView = itemView.findViewById(R.id.titleTextView);
       }

        @Override
        public void onClick(View v) {

           int position = getAdapterPosition();
           InfoItem infoItem = infoItems.get(position);

            Intent intent = new Intent(context, InfoActivity.class );
            intent.putExtra("title",  infoItem.getTitle());
            intent.putExtra("info",  infoItem.getInformation());

            context.startActivity(intent);
        }
    }

}
