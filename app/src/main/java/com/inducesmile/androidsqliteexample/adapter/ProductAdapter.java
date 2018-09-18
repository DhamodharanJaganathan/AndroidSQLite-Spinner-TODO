package com.inducesmile.androidsqliteexample.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.inducesmile.androidsqliteexample.Product;
import com.inducesmile.androidsqliteexample.R;
import com.inducesmile.androidsqliteexample.database.SqliteDatabase;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

  private Context context;
  private List<Product> listProducts;

  private SqliteDatabase mDatabase;

  public ProductAdapter(Context context, List<Product> listProducts) {
    this.context = context;
    this.listProducts = listProducts;
    mDatabase = new SqliteDatabase(context);
  }

  @Override
  public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.product_list_layout, parent, false);
    return new ProductViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ProductViewHolder holder, int position) {
    final Product singleProduct = listProducts.get(position);

    holder.name.setText(singleProduct.getName());
    holder.quan.setText(String.valueOf(singleProduct.getQuantity()));

    holder.editProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //editTaskDialog(singleProduct);
        showDialog(singleProduct);
      }
    });

    holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //delete row from database

        mDatabase.deleteProduct(singleProduct.getId());

        //refresh the activity page.
        ((Activity) context).finish();
        context.startActivity(((Activity) context).getIntent());
      }
    });
  }

  @Override
  public int getItemCount() {
    return listProducts.size();
  }


  private void editTaskDialog(final Product product) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View subView = inflater.inflate(R.layout.add_product_layout, null);

    final EditText nameField = (EditText) subView.findViewById(R.id.enter_name);
    final EditText quantityField = (EditText) subView.findViewById(R.id.enter_quantity);

    if (product != null) {
      nameField.setText(product.getName());
      quantityField.setText(String.valueOf(product.getQuantity()));
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Edit product");
    builder.setView(subView);
    builder.create();

    builder.setPositiveButton("EDIT PRODUCT", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        final String name = nameField.getText().toString();
        final int quantity = Integer.parseInt(quantityField.getText().toString());

        if (TextUtils.isEmpty(name) || quantity <= 0) {
          Toast
              .makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG)
              .show();
        } else {
          mDatabase.updateProduct(new Product(product.getId(), name, quantity));
          //refresh the activity
          ((Activity) context).finish();
          ((Activity) context).overridePendingTransition(0, 0);
          context.startActivity(((Activity) context).getIntent());
          ((Activity) context).overridePendingTransition(0, 0);

        }
      }
    });

    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
      }
    });
    builder.show();
  }

  private int getIndex(Spinner spinner, String myString) {

    int index = 0;

    for (int i = 0; i < spinner.getCount(); i++) {
      if (spinner.getItemAtPosition(i).equals(myString)) {
        index = i;
      }
    }
    return index;
  }


  public void showDialog(final Product singleProduct) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View alertLayout = inflater.inflate(R.layout.custom_layout_dialog, null);
    final EditText etName = (EditText) alertLayout.findViewById(R.id.etName);
    final Spinner spSex = (Spinner) alertLayout.findViewById(R.id.spSex);

    if (singleProduct != null) {
      etName.setText(String.valueOf(singleProduct.getQuantity()));
      spSex.setSelection(getIndex(spSex, singleProduct.getName()));
    }

    AlertDialog.Builder alert = new AlertDialog.Builder(context);
    alert.setTitle("Add fund");
    alert.setView(alertLayout);
    alert.setCancelable(false);
    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show();
      }
    });

    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        final int quantity = Integer.parseInt(etName.getText().toString());
        String domin = String.valueOf(spSex.getSelectedItem());

        if (TextUtils.isEmpty(singleProduct.getName()) || singleProduct.getQuantity() <= 0) {
          Toast
              .makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG)
              .show();
        } else {
          mDatabase.updateProduct(new Product(singleProduct.getId(), domin, quantity));
          //refresh the activity
          ((Activity) context).finish();
          ((Activity) context).overridePendingTransition(0, 0);
          context.startActivity(((Activity) context).getIntent());
          ((Activity) context).overridePendingTransition(0, 0);
        }
      }
    });
    AlertDialog dialog = alert.create();
    dialog.show();
  }


}
