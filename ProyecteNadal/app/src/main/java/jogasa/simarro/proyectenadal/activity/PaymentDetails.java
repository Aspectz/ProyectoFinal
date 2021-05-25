package jogasa.simarro.proyectenadal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import jogasa.simarro.proyectenadal.R;

public class PaymentDetails extends AppCompatActivity {
    private TextView txtId,txtAmount,txtStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId=(TextView)findViewById(R.id.txtId);
        txtAmount=(TextView)findViewById(R.id.txtAmount);
        txtStatus=(TextView)findViewById(R.id.txtStatus);

        //get intent
        Intent intent=getIntent();

        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try{
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(response.getString(String.format("$%s",paymentAmount)));
        } catch (JSONException e) {
                e.printStackTrace();
        }

    }
}