package android.example.musicshop;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderActivity extends AppCompatActivity {

    //String addresses;
    //String subject = "Order from Miracle of Sound";
    String emailText;

    FirebaseDatabase database;
    DatabaseReference MessageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setTitle("Your Order");

        database = FirebaseDatabase.getInstance();
        MessageReference = database.getReference().child("ORDERS");


        Intent receivedOrderIntent = getIntent();
        String userMail = receivedOrderIntent.getStringExtra("UserMail");
        String instrumentsName = receivedOrderIntent.getStringExtra("InstrumentName");
        int quantity = receivedOrderIntent.getIntExtra("Quantity", 0);
        double price = receivedOrderIntent.getDoubleExtra("Price", 0);
        double orderPrice = receivedOrderIntent.getDoubleExtra("OrderPrice", 0);

        emailText = "Customer Email:" + userMail + "\n" +
                "Instrument:" + instrumentsName + "\n" +
                "Quantity:" + quantity + "\n" +
                "Price:" + price + "\n" +
                "Order price:" + orderPrice;
        TextView orderTextView = findViewById(R.id.orderTextView);
        orderTextView.setText(emailText);
    }

    public void submitOrder(View view) {
        /*Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        //Intent intent = new Intent(Intent.ACTION_SEND); для всех приложений
        //intent.setType("*message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, emailText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }*/
        MessageReference.push().setValue(emailText);
        Toast.makeText(OrderActivity.this, "Your have submit order, the seller will contact with you on E-mail." +
                        "Wait please",
                Toast.LENGTH_LONG).show();

    }

}
