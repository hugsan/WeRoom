package com.itcom202.weroom.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.itcom202.weroom.R;

public class ListItem extends RecyclerView.ViewHolder {
  TextView textView;

  public ListItem(View itemView) {
    super(itemView);
    textView = itemView.findViewById(R.id.text);
  }

  public void bind(int i) {
    textView.setText(String.valueOf(i));
  }
}
