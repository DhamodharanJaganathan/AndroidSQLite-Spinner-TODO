package com.inducesmile.androidsqliteexample.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.inducesmile.androidsqliteexample.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

  public TextView name, quan;
  public ImageView deleteProduct;
  public ImageView editProduct;

  public ProductViewHolder(View itemView) {
    super(itemView);
    name = itemView.findViewById(R.id.product_name);
    quan = itemView.findViewById(R.id.product_quan);
    deleteProduct = itemView.findViewById(R.id.delete_product);
    editProduct = itemView.findViewById(R.id.edit_product);
  }
}
