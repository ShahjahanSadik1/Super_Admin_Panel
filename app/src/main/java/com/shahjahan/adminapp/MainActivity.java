package com.shahjahan.adminapp;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    EditText search_bar;


    ListView list_item;
    ImageView manu,load_data;

    ProgressBar progress_bar;


    ArrayList<HashMap<String,String>>arrayList = new ArrayList<>();
    HashMap<String,String>hashMap;
    MYADAPTER myadapter = new MYADAPTER();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_item = findViewById(R.id.list_item);
        manu = findViewById(R.id.manu);
        load_data = findViewById(R.id.load_data);
        progress_bar = findViewById(R.id.progress_bar);

        search_bar = findViewById(R.id.search_bar);


       internetchack();



        Toast.makeText(MainActivity.this, "Assalam Walekum", Toast.LENGTH_SHORT).show();




        //:::::::::::::::::::::::::::::::::::::::::::::::
        manu.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_edit_dialog);


            TextInputEditText ed_input_text = dialog.findViewById(R.id.ed_input_text);
            TextView button_post = dialog.findViewById(R.id.button_post);

            button_post.setOnClickListener(v1 -> {



                    String stri_ed_input_text = ed_input_text.getText().toString();


                progress_bar.setVisibility(View.VISIBLE);
                String url ="http://192.168.0.102:8080/Apps/data_insart.php/?n="+stri_ed_input_text ;






            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    display_data();
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            });





            if (stri_ed_input_text.length()>0){

                RequestQueue queue = Volley.newRequestQueue(this);
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                progress_bar.setVisibility(View.VISIBLE);

                dialog.dismiss();
            }else {
                progress_bar.setVisibility(View.GONE);
                Toast.makeText(this, "error error", Toast.LENGTH_SHORT).show();
            }


            });





            dialog.show();

        });//:::::::::::::::::::::::::::::::::::::::::::::::



        display_data();




       search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    search_data();


                   return true;
                }
                return false;
            }
        });



    }//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public class MYADAPTER extends BaseAdapter{



        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custom_list_design,parent,false);

            TextView text_view = view.findViewById(R.id.text_view);
            LinearLayout layout = view.findViewById(R.id.layout);




        HashMap<String,String>hashMap = arrayList.get(position);


        String stri_id = hashMap.get("has_id");
        String status_text = hashMap.get("has_name");




           text_view.setText(status_text);


            //yyyyyyyyyyyyyyyyyyyyyyyyyyyy


            //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            layout.setOnClickListener(v -> {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_toest_yes_na);

                TextInputEditText ed_update_text  = dialog.findViewById(R.id.ed_update_text);
                TextView update_button = dialog.findViewById(R.id.update_button);
                TextView delete_button = dialog.findViewById(R.id.delete_button);

                ed_update_text.setText(status_text);


                update_button.setOnClickListener(v1 -> {

                   String stri_ststus =ed_update_text.getText().toString();

                    update_data(stri_ststus,stri_id);

                    dialog.dismiss();

                });



                delete_button.setOnClickListener(v1 -> {



                    delete_data(stri_id);

                    dialog.dismiss();

                });









                dialog.show();
            });//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            return view;
        }
    }//base adapter base adapter base adapter base adapter base adapter base adapter base adapter





    //display_data display_data display_data display_data display_data display_data display_data display_data
    public void display_data (){


        arrayList = new ArrayList<>();
        String url = "http://192.168.0.102:8080/Apps/data_view_data.php";
       // String url = "https://shahjahansadik.000webhostapp.com/apps/data_view_data.php";


        progress_bar.setVisibility(View.VISIBLE);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {






                progress_bar.setVisibility(View.GONE);
                for (int x=0; x<response.length(); x++){

                    try {


                        JSONObject jsonObject = response.getJSONObject(x);

                        String stri_id = jsonObject.getString("id");
                        String statuss = jsonObject.getString("status_text");




                        hashMap = new HashMap<>();
                        hashMap.put("has_id",stri_id);
                        hashMap.put("has_name",statuss);
                        arrayList.add(hashMap);


                        list_item.setAdapter(myadapter);


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progress_bar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Internet not connccted", Toast.LENGTH_SHORT).show();


            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);

    }//display_data display_data display_data display_data display_data display_data display_data




    //update data //update data //update data //update data //update data //update data //update data
    public void update_data(String ed_text ,String ed_id){

        String url = "http://192.168.0.102:8080/Apps/update.php/?status_text="+ed_text+"&id="+ed_id;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("updateRes" , response.toString());
                Toast.makeText(MainActivity.this, "Update data", Toast.LENGTH_SHORT).show();
                display_data();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("updat" , error.toString());

                Toast.makeText(MainActivity.this, "Internet not connccted", Toast.LENGTH_SHORT).show();


            }
        });


        if (ed_text.length()>0){
            RequestQueue queue = Volley.newRequestQueue(this);
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }else {
            Toast.makeText(this, "Update Data", Toast.LENGTH_SHORT).show();
        }






    }//update data //update data //update data //update data //update data //update data //update data



    //delete data //delete data //delete data //delete data //delete data //delete data //delete data

    public void delete_data(String id_is){




        String url = "http://192.168.0.102:8080/Apps/delete.php/?id="+id_is;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("deleteRes" , response.toString());
                Toast.makeText(MainActivity.this, "Delete data", Toast.LENGTH_SHORT).show();
                display_data();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("updat" , error.toString());


                Toast.makeText(MainActivity.this, "Internet not connccted", Toast.LENGTH_SHORT).show();

            }
        });



        RequestQueue queue = Volley.newRequestQueue(this);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);





    }

    //delete data //delete data //delete data //delete data //delete data //delete data //delete data



    //search_view //search_view //search_view //search_view //search_view //search_view //search_view

    public void search_data(){



        String stri_ed_search = search_bar.getText().toString();

        String url = "http://192.168.0.102:8080/Apps/search.php/?s="+stri_ed_search;



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {



                for (int x=0; x<response.length(); x++){

                    try {
                        JSONObject jsonObject = response.getJSONObject(x);

                        String stri_id = jsonObject.getString("id");
                        String statuss = jsonObject.getString("status_text");




                        hashMap = new HashMap<>();
                        hashMap.put("ha_id",stri_id);
                        hashMap.put("has_e",statuss);
                        arrayList.add(hashMap);


                        Toast.makeText(MainActivity.this, ""+hashMap.get("has_e"), Toast.LENGTH_LONG).show();




                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }





                display_data();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("dat" , error.toString());
                Toast.makeText(MainActivity.this, "Internet not connccted", Toast.LENGTH_SHORT).show();


            }
        });




            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest);







    }





    //search_view //search_view //search_view //search_view //search_view //search_view //search_view






    //network chack//network chack//network chack//network chack//network chack//network chack//network chack
    public void internetchack (){
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = con.getActiveNetworkInfo();

        if (net!= null && net.isConnected()){
            Toast.makeText(MainActivity.this, "Internet connccted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "Internet not connccted", Toast.LENGTH_SHORT).show();
        }

    }
     //network chack//network chack//network chack//network chack//network chack//network chack//network chack









}