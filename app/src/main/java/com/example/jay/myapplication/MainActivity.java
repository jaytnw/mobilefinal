package com.example.jay.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jay.myapplication.app.AppController;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText edname, edlastname;
    RadioButton rb;
    RadioGroup rg;
    Button btnAdd,btView;
    String name, lastname,gen,university ;

    private String urlForAddingProduct = AppController.baseUrl+"create_product.php";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        edname = (EditText) findViewById(R.id.edname);
        edlastname = (EditText) findViewById(R.id.edlastname);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Adding the product to our database");
        pDialog.setCancelable(false);

        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        btnAdd = (Button) findViewById(R.id.btAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radio = rg.getCheckedRadioButtonId();

                rb = (RadioButton) findViewById(radio);

//                Toast.makeText(getBaseContext(),rb.getText(),Toast.LENGTH_LONG).show();

                gen = rb.getText().toString();

                if(validate()){
                    addNewProductToDatabase();
                }


            }
        });

        btView = (Button) findViewById(R.id.btNew);
        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewProductsActivity.class);
                startActivity(intent);
            }
        });

    }

        private boolean validate() {

            name = edname.getText().toString();
            lastname = edlastname.getText().toString();


            if (name.length() == 0 ) {
                edname.setError("Please enter correct name for the product.");
                return false;
            } else if(lastname.length()==0){
                edlastname.setError("Please enter correct price for the product.");
                return false;
            }
            else{
                return true;
            }
        }

    private void addNewProductToDatabase(){

        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlForAddingProduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resetFields();
                        hidepDialog();
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("lastname",lastname);
                params.put("university", university);
                params.put("gen", gen);
                return params;
            }
        };
        AppController.getmInstance().addToRequestQueue(stringRequest);

    }

    private void resetFields() {
        edname.setText("");
        edlastname.setText("");

    }


    private void showpDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hidepDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        university = text;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
