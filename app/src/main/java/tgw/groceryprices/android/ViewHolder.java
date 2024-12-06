package tgw.groceryprices.android;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import tgw.groceryprices.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final ImageButton btn;
    public final TextView tvName;

    public ViewHolder(View view) {
        super(view);
        this.tvName = view.findViewById(R.id.tvItemName);
        this.btn = view.findViewById(R.id.btnView);
    }
}
