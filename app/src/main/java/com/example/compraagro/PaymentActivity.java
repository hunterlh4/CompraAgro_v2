package com.example.compraagro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compraagro.Culqi.Card;
import com.example.compraagro.Culqi.Token;
import com.example.compraagro.Culqi.TokenCallback;
import com.example.compraagro.Validation.Validation;
import com.example.compraagro.model.Transaction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import org.json.JSONObject;

import java.util.UUID;

public class PaymentActivity extends AppCompatActivity {

    Validation validation;

    ProgressDialog progress;

    CreditCardView creditCardView;

    TextView txtcardnumber, txtcvv, txtmonth, txtyear, txt_cantidad, kind_card, result;
    Button btnPay;
    String monto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        monto=getIntent().getExtras().getString("monto");
        validation = new Validation();
//
        progress = new ProgressDialog(this);
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//
//        txtcardnumber = (TextView) findViewById(R.id.txt_cardnumber);
//
//        txtcvv = (TextView) findViewById(R.id.txt_cvv);
//
//        txtmonth = (TextView) findViewById(R.id.txt_month);
//
//        txtyear = (TextView) findViewById(R.id.txt_year);
//
        txt_cantidad = (TextView) findViewById(R.id.monto);
//
//        kind_card = (TextView) findViewById(R.id.kind_card);
//
//        result = (TextView) findViewById(R.id.token_id);
//
        btnPay = (Button) findViewById(R.id.btn_pay);
//
//        txtcvv.setEnabled(false);
//
        txt_cantidad.setText(monto);
        txt_cantidad.setEnabled(false);

        creditCardView = findViewById(R.id.card1);
        creditCardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                String bin = creditCardView.getCardNumber();

                validation.bin(bin,creditCardView);

//                if(bin.length() > 1) {
//                    if(Integer.valueOf(bin.substring(0,2)) == 41) {
//                        creditCardView.setType(CardType.VISA);
//                    } else if (Integer.valueOf(bin.substring(0,2)) == 51) {
//                        creditCardView.setType(CardType.MASTERCARD);
//                    } else {
//                    }
//                } else {
//                    creditCardView.setType(CardType.DISCOVER);
//                }
            }
        });


        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                progress.show();

                Card card = new Card(creditCardView.getCardNumber(), creditCardView.getExpiryDate(), 9, 2020, txt_cantidad.getText().toString());

                Token token = new Token("sk_test_yXVtdMtTz76KR4VH");

                token.createToken(getApplicationContext(), card, new TokenCallback() {
                    @Override
                    public void onSuccess(JSONObject token) {
                        try {
                            result.setText(token.get("id").toString());

                        } catch (Exception ex){
                            progress.hide();
                        }
                        progress.hide();

                    }

                    @Override
                    public void onError(Exception error) {
                        progress.hide();
                        pay();
                        Toast.makeText(getApplication(),"Pago exitoso ",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });


    }

    public void pay(){

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

        String transaction0=getIntent().getExtras().getString("transaction0");
        String transaction1=getIntent().getExtras().getString("transaction1");
        String transaction2=getIntent().getExtras().getString("transaction2");
        String transaction3=getIntent().getExtras().getString("transaction3");
        String transaction4=getIntent().getExtras().getString("transaction4");
        String transaction5=getIntent().getExtras().getString("transaction5");
        String transaction6=getIntent().getExtras().getString("transaction6");
        String transaction7=getIntent().getExtras().getString("transaction7");
        String transaction8=getIntent().getExtras().getString("transaction8");

        com.example.compraagro.model.Transaction transaction= new Transaction();
        transaction.setIdTransaction(transaction0);
        transaction.setIdProduct(transaction1);
        transaction.setIdBuyer(transaction2);
        transaction.setIdSeller(transaction3);
        transaction.setDate(transaction4);
        transaction.setPrice(transaction5);
        transaction.setWeight(transaction6);
        transaction.setState(transaction7);
        transaction.setNameProduct(transaction8);

        mDatabase.child("Transactions").child(transaction.getIdTransaction()).setValue(transaction);
    }


}