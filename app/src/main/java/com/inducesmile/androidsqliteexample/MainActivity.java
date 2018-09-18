package com.inducesmile.androidsqliteexample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;
import com.inducesmile.androidsqliteexample.adapter.ProductAdapter;
import com.inducesmile.androidsqliteexample.database.SqliteDatabase;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private SqliteDatabase mDatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FrameLayout fLayout = (FrameLayout) findViewById(R.id.activity_to_do);

    RecyclerView productView = (RecyclerView) findViewById(R.id.product_list);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    productView.setLayoutManager(linearLayoutManager);
    productView.setHasFixedSize(true);

    mDatabase = new SqliteDatabase(this);
    List<Product> allProducts = mDatabase.listProducts();

    if (allProducts.size() > 0) {
      productView.setVisibility(View.VISIBLE);
      ProductAdapter mAdapter = new ProductAdapter(this, allProducts);
      productView.setAdapter(mAdapter);
      int sum = 0;
      for (int i = 0; i < allProducts.size(); i++) {
        sum = sum + allProducts.get(i).getQuantity();
      }

      System.out.println("Sum after adding  is : " + sum);

      Toast.makeText(this, String.valueOf("Total : " + sum), Toast.LENGTH_SHORT).show();

    } else {
      productView.setVisibility(View.GONE);
      Toast.makeText(this, "There is no product in the database. Start adding now",
          Toast.LENGTH_LONG).show();
    }

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // add new quick task
        //addTaskDialog();

        showDialog();
      }
    });

    FloatingActionButton fab_1 = (FloatingActionButton) findViewById(R.id.fab_1);
    fab_1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mDatabase.deleteAll();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

      }
    });

  }

  private void addTaskDialog() {
    LayoutInflater inflater = LayoutInflater.from(this);
    View subView = inflater.inflate(R.layout.add_product_layout, null);

    final EditText nameField = (EditText) subView.findViewById(R.id.enter_name);
    final EditText quantityField = (EditText) subView.findViewById(R.id.enter_quantity);

    String[] s = {"India ", "Arica", "India ", "Arica", "India ", "Arica",
        "India ", "Arica", "India ", "Arica"};
    final ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this,
        android.R.layout.simple_spinner_item, s);
    final Spinner sp = new Spinner(MainActivity.this);
    sp.setLayoutParams(
        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    sp.setAdapter(adp);

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Add new product");
    builder.setView(subView);
    builder.setView(sp);
    builder.create();

    builder.setPositiveButton("ADD PRODUCT", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        final String name = nameField.getText().toString();
        final int quantity = Integer.parseInt(quantityField.getText().toString());

        if (TextUtils.isEmpty(name) || quantity <= 0) {
          Toast.makeText(MainActivity.this, "Something went wrong. Check your input values",
              Toast.LENGTH_LONG).show();
        } else {
          Product newProduct = new Product(name, quantity);
          mDatabase.addProduct(newProduct);

          //refresh the activity
          finish();
          overridePendingTransition(0, 0);
          startActivity(getIntent());
          overridePendingTransition(0, 0);
        }
      }
    });

    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(MainActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
      }
    });
    builder.show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mDatabase != null) {
      mDatabase.close();

    }
  }


  public void showDialog() {
    LayoutInflater inflater = getLayoutInflater();
    View alertLayout = inflater.inflate(R.layout.custom_layout_dialog, null);
    final EditText etName = (EditText) alertLayout.findViewById(R.id.etName);
    final Spinner spSex = (Spinner) alertLayout.findViewById(R.id.spSex);

    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    alert.setTitle("Add fund");
    alert.setView(alertLayout);
    alert.setCancelable(false);
    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
      }
    });

    alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        final int quantity = Integer.parseInt(etName.getText().toString());
        String domin = String.valueOf(spSex.getSelectedItem());

        if (TextUtils.isEmpty(domin) || quantity <= 0) {
          Toast.makeText(MainActivity.this, "Something went wrong. Check your input values",
              Toast.LENGTH_LONG).show();
        } else {
          Product newProduct = new Product(domin, quantity);
          mDatabase.addProduct(newProduct);

          //refresh the activity
          finish();
          overridePendingTransition(0, 0);
          startActivity(getIntent());
          overridePendingTransition(0, 0);
        }
      }
    });
    AlertDialog dialog = alert.create();
    dialog.show();
  }
}
