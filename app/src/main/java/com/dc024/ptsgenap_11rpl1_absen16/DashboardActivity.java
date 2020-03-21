package com.dc024.ptsgenap_11rpl1_absen16;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dc024.ptsgenap_11rpl1_absen16.adapter.rv_adapter;
import com.dc024.ptsgenap_11rpl1_absen16.model.rv_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private List<rv_model> rv_modelArrayList = new ArrayList<>();
    private rv_adapter rv_adapter1;
    private EditText nama,jenis,text;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout ;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("Tugas PTS", MODE_PRIVATE);
        final String username = sharedPreferences.getString("id", "");
        recyclerView = findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        btn = findViewById(R.id.btnadd);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv_modelArrayList.clear();
                HashMap<String, String> body = new HashMap<>();
                body.put("username", username);
                AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/barang.php")
                        .addBodyParameter(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("GZS", "respon : " + response);

                                    String status = response.optString("STATUS");
                                    String message = response.optString("MESSAGE");
                                    if (status.equalsIgnoreCase("SUCCESS")) {
                                        JSONArray orders = response.optJSONObject("PAYLOAD").optJSONArray("INVEN_LAB");

                                        if (orders == null) return;
                                        System.out.println(orders.length()+"gzs");

                                        for (int i = 0; i < orders.length(); i++) {
                                            final JSONObject aData = orders.optJSONObject(i);
                                            System.out.println(aData.get("b_id")+"ayo ojo error :(");
                                            rv_model item = new rv_model();
                                            item.setId(aData.getString("b_id"));
                                            item.setJenis(aData.getString("b_jenis"));
                                            item.setKode(aData.getString("b_kode"));
                                            item.setNama(aData.getString("b_nama"));

                                            rv_modelArrayList.add(item);
                                        }rv_adapter1.notifyDataSetChanged();



                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
                                Log.d("GZS", "onError: " + anError.getErrorBody());
                                Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                                Log.d("GZS", "onError: " + anError.getErrorDetail());
                                Log.d("GZS", "onError: " + anError.getResponse());
                                Log.d("GZS  ", "onError: " + anError.getErrorCode());
                            }
                        });

                 rv_adapter1 = new rv_adapter(getApplicationContext(), rv_modelArrayList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(rv_adapter1);
                rv_adapter1.setOnItemClickCallback(new rv_adapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(rv_model data) {
                        showSelectedHero(data);
                    }
                });

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        HashMap<String, String> body = new HashMap<>();
        body.put("username", username);
        AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/barang.php")
                .addBodyParameter(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("GZS", "respon : " + response);

                            String status = response.optString("STATUS");
                            String message = response.optString("MESSAGE");
                            if (status.equalsIgnoreCase("SUCCESS")) {
                                JSONArray orders = response.optJSONObject("PAYLOAD").optJSONArray("INVEN_LAB");

                                if (orders == null) return;
                                System.out.println(orders.length()+"gzs");

                                for (int i = 0; i < orders.length(); i++) {
                                    final JSONObject aData = orders.optJSONObject(i);
                                    System.out.println(aData.get("b_id")+"ayo ojo error :(");
                                    rv_model item = new rv_model();
                                    item.setId(aData.getString("b_id"));
                                    item.setJenis(aData.getString("b_jenis"));
                                    item.setKode(aData.getString("b_kode"));
                                    item.setNama(aData.getString("b_nama"));

                                    rv_modelArrayList.add(item);
                                }rv_adapter1.notifyDataSetChanged();



                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
                        Log.d("GZS", "onError: " + anError.getErrorBody());
                        Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                        Log.d("GZS", "onError: " + anError.getErrorDetail());
                        Log.d("GZS", "onError: " + anError.getResponse());
                        Log.d("GZS  ", "onError: " + anError.getErrorCode());
                    }
                });
        System.out.println(rv_modelArrayList.size()+"guttt");

        rv_adapter1 = new rv_adapter(getApplicationContext(), rv_modelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(rv_adapter1);
        rv_adapter1.setOnItemClickCallback(new rv_adapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(rv_model data) {
                showSelectedHero(data);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Dialog dialog = new Dialog(getApplicationContext());
//                dialog.setContentView(R.layout.dialog_adddata);
//                final EditText text = (EditText) dialog.findViewById(R.id.etKode1);
//                final EditText nama = (EditText) dialog.findViewById(R.id.etNamaUser1);
//                final EditText jenis = (EditText) dialog.findViewById(R.id.etJenis1);
//
//                LinearLayout divSimpan = dialog.findViewById(R.id.divTambah);
//                divSimpan.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        HashMap<String, String> body = new HashMap<>();
//                        body.put("nama", nama.getText().toString());
//                        body.put("jenis", jenis.getText().toString());
//                        body.put("kode", text.getText().toString());
//                        AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/add.php")
//                                .addBodyParameter(body)
//                                .setPriority(Priority.MEDIUM)
//                                .build()
//                                .getAsJSONObject(new JSONObjectRequestListener() {
//                                    @Override
//                                    public void onResponse(JSONObject response) {
//                                        Log.d("GZS", "respon : " + response);
//
//                                        String status = response.optString("STATUS");
//                                        String message = response.optString("MESSAGE");
//                                        if (status.equalsIgnoreCase("SUCCESS")) {
//                                            JSONObject payload = response.optJSONObject("PAYLOAD");
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                        }
//                                        else {
//                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onError(ANError anError) {
//                                        Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
//                                        Log.d("GZS", "onError: " + anError.getErrorBody(    ));
//                                        Log.d("GZS", "onError: " + anError.getLocalizedMessage());
//                                        Log.d("GZS", "onError: " + anError.getErrorDetail());
//                                        Log.d("GZS", "onError: " + anError.getResponse());
//                                        Log.d("GZS  ", "onError: " + anError.getErrorCode());
//                                    }
//                                });
//                    }
//                });
//                LinearLayout divHapus = dialog.findViewById(R.id.divCancel);
//                divHapus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.show();

                View dlgView = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.dialog_adddata, null);
                final Dialog dialog = new Dialog(DashboardActivity.this, android.R.style.Theme_Material_Dialog);
                 text = (EditText) dlgView.findViewById(R.id.etKode1);
                 nama = (EditText) dlgView.findViewById(R.id.etNamaUser1);
                 jenis = (EditText) dlgView.findViewById(R.id.etJenis1);


                ((LinearLayout) dlgView.findViewById(R.id.divTambah)).setOnClickListener(new View.OnClickListener() {
                    private void doNothing() {

                    }

                    @Override
                    public void onClick(View view) {
                        AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/add.php")
                                .addBodyParameter("nama", nama.getText().toString().trim().toUpperCase())
                                .addBodyParameter("jenis", jenis.getText().toString().trim().toUpperCase())
                                .addBodyParameter("kode", text.getText().toString().trim().toUpperCase())
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("GZS", "respon : " + response);

                                        String status = response.optString("STATUS");
                                        String message = response.optString("MESSAGE");
                                        if (status.equalsIgnoreCase("SUCCESS")) {
                                            JSONObject payload = response.optJSONObject("PAYLOAD");
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
                                        Log.d("GZS", "onError: " + anError.getErrorBody(    ));
                                        Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                                        Log.d("GZS", "onError: " + anError.getErrorDetail());
                                        Log.d("GZS", "onError: " + anError.getResponse());
                                        Log.d("GZS  ", "onError: " + anError.getErrorCode());
                                    }
                                });
                    }
                });

                dialog.setContentView(dlgView);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }
    private void showSelectedHero(rv_model hero) {
        Toast.makeText(this, "Kamu memilih " + hero.getNama(), Toast.LENGTH_SHORT).show();
//        LayoutInflater inflater = (LayoutInflater) DashboardActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View dlgView = inflater.from(DashboardActivity.this).inflate(R.layout.dialog_detail, null);
        View dlgView = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.dialog_detail, null);
        final Dialog dialog = new Dialog(DashboardActivity.this, android.R.style.Theme_Material_Dialog);


        text = dlgView.findViewById(R.id.etKode);
        text.setText(hero.getKode());
        nama = dlgView.findViewById(R.id.etNama);
        nama.setText(hero.getNama());
        jenis = dlgView.findViewById(R.id.etJenis);
        jenis.setText(hero.getJenis());
        final String id = hero.getId();


        ((LinearLayout) dlgView.findViewById(R.id.divSave)).setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/update.php")
                        .addBodyParameter("id", id)
                        .addBodyParameter("nama", nama.getText().toString().trim().toUpperCase())
                        .addBodyParameter("jenis", jenis.getText().toString().trim().toUpperCase())
                        .addBodyParameter("kode", text.getText().toString().trim().toUpperCase())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("GZS", "respon : " + response);

                                String status = response.optString("STATUS");
                                String message = response.optString("MESSAGE");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    JSONObject payload = response.optJSONObject("PAYLOAD");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    rv_adapter1.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
                                Log.d("GZS", "onError: " + anError.getErrorBody(    ));
                                Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                                Log.d("GZS", "onError: " + anError.getErrorDetail());
                                Log.d("GZS", "onError: " + anError.getResponse());
                                Log.d("GZS  ", "onError: " + anError.getErrorCode());
                            }
                        });

            }
        });

        ((LinearLayout) dlgView.findViewById(R.id.divDelete)).setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }

            @Override
            public void onClick(View view) {
                AndroidNetworking.post("http://192.168.43.153/TugasPTS/api/delete.php")
                        .addBodyParameter("id", id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("GZS", "respon : " + response);

                                String status = response.optString("STATUS");
                                String message = response.optString("MESSAGE");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    JSONObject payload = response.optJSONObject("PAYLOAD");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    rv_adapter1.notifyDataSetChanged();
                                    dialog.dismiss();

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "ERROR LUR", Toast.LENGTH_SHORT).show();
                                Log.d("GZS", "onError: " + anError.getErrorBody(    ));
                                Log.d("GZS", "onError: " + anError.getLocalizedMessage());
                                Log.d("GZS", "onError: " + anError.getErrorDetail());
                                Log.d("GZS", "onError: " + anError.getResponse());
                                Log.d("GZS  ", "onError: " + anError.getErrorCode());
                            }
                        });
            }
        });

        dialog.setContentView(dlgView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

}
