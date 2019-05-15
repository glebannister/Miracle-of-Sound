package android.example.musicshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int quantity = 1;
    Spinner spinner;
    ArrayList spinnerArrayList;
    ArrayAdapter spinnerAdapter;
    HashMap instrumentsMap;
    String instrumentsName;
    double price;
    String userEMail;
    TextView UserMail;
    TextView quantityTextView;
    Button byButton;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference usersReference;
    ChildEventListener usersChildEventListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("USERS");


        setTitle("Music Shop");

        UserMail = findViewById(R.id.nameEditText);

        usersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users user = dataSnapshot.getValue(Users.class);
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    userEMail = user.getEmail();
                    UserMail.setText(userEMail);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        usersReference.addChildEventListener(usersChildEventListener);


        Intent receivedMainIntent = getIntent();
        userEMail = receivedMainIntent.getStringExtra("UserEMail");

        UserMail.setText(userEMail);

        createSpinner();
        createMap();

        ArrayList<InfoItem> infoItems = new ArrayList<>();

        infoItems.add(new InfoItem(Text.TITLE, Text.INFO));

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        adapter = new InfoAdapter(infoItems, this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        byButton = findViewById(R.id.addButton);
        quantityTextView = findViewById(R.id.quantity_TextView);


        quantityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (quantity == 0){
                    byButton.setEnabled(false);
                } else {
                    byButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sing_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SingInActivity.class));
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    void createSpinner(){
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        spinnerArrayList = new ArrayList();
        spinnerArrayList.add("Guitar");
        spinnerArrayList.add("Drams");
        spinnerArrayList.add("Keyboard");
        spinnerArrayList.add("Violin");
        spinnerArrayList.add("Microphone");

        spinnerAdapter = new ArrayAdapter( this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    void createMap(){
        instrumentsMap = new HashMap();
        instrumentsMap.put("Guitar", 250.0);
        instrumentsMap.put("Drams", 330.0);
        instrumentsMap.put("Keyboard", 500.0);
        instrumentsMap.put("Violin", 400.0);
        instrumentsMap.put("Microphone", 50.0);
    }

    public void increaseQuantity(View view) {
        quantity = quantity + 1;
        TextView QuantityTextView = findViewById(R.id.quantity_TextView);
        QuantityTextView.setText("" + quantity);
        TextView priceTextView = findViewById(R.id.price_TextView);
        priceTextView.setText("" + quantity * price);
    }

    public void decreaseQuantity(View view) {
        quantity = quantity - 1;
        if (quantity < 0){
            quantity = 0;
        }
        TextView QuantityTextView = findViewById(R.id.quantity_TextView);
        QuantityTextView.setText("" + quantity);
        TextView priceTextView = findViewById(R.id.price_TextView);
        priceTextView.setText("" + quantity * price);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        instrumentsName = spinner.getSelectedItem().toString();
        price = (double) instrumentsMap.get(instrumentsName);
        TextView priceTextView = findViewById(R.id.price_TextView);
        priceTextView.setText("" + quantity * price);

        ImageView goodsImageView = findViewById(R.id.instrumentImageView);
        goodsImageView.animate().alpha(1).rotation(360).setDuration(3000);


        switch (instrumentsName){
            case "Guitar":
                goodsImageView.setImageResource(R.drawable.guitar);
                break;
            case "Drams":
                goodsImageView.setImageResource(R.drawable.dra_m);

                break;
            case "Keyboard":
                goodsImageView.setImageResource(R.drawable.piano);
                break;
            case "Violin":
                goodsImageView.setImageResource(R.drawable.violin);
                break;
            case "Microphone":
                goodsImageView.setImageResource(R.drawable.microphone);
                break;

                default:
                    goodsImageView.setImageResource(R.drawable.guitar);
                    break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void addToCart(View view) {


        Order order = new Order();

        order.userMail = userEMail;

        order.instrumentsName = instrumentsName;

        order.quantity = quantity;

        order.price = price;

        order.orderPrice = quantity * price;


        Intent orderIntend = new Intent(MainActivity.this, OrderActivity.class);
        orderIntend.putExtra("UserMail", order.userMail);
        orderIntend.putExtra("InstrumentName", order.instrumentsName);
        orderIntend.putExtra("Quantity", order.quantity);
        orderIntend.putExtra("Price", order.price);
        orderIntend.putExtra("OrderPrice", order.orderPrice);

        startActivity(orderIntend);

    }

}

