package com.example.tailorapp.tabLayout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tailorapp.R;
import com.example.tailorapp.contants.Api;
import com.example.tailorapp.contants.AppController;
import com.example.tailorapp.tabLayout.category.Adapter_Category;
import com.example.tailorapp.tabLayout.category.Model_Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabsFragment extends Fragment {

    private RecyclerView categoryList;
    private Adapter_Category adapter;
    private List<Model_Category> list;
    private String cat_id;

    public static TabsFragment newInstance() {
        return new TabsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        if (getArguments() != null){
            cat_id = getArguments().getString("cat_id");
        }

        categoryList = view.findViewById(R.id.categoryList);
        categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        list = new ArrayList<>();
        adapter = new Adapter_Category(list, getContext());
        categoryList.setAdapter(adapter);
        getCategoryList(cat_id);
    }

    private void getCategoryList(final String cat_id){

        StringRequest req = new StringRequest(Request.Method.POST, Api.CategoryListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("success");
                            if (status){

                                JSONArray jsonArray = jsonObject.getJSONArray("products");
                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String id = innerObj.getString("id");
                                    String name = innerObj.getString("name");
                                    String price = innerObj.getString("pirce");
                                    String image = innerObj.getString("thumb");

                                    Model_Category item = new Model_Category(
                                            id,
                                            name,
                                            price,
                                            image
                                    );

                                    list.add(item);
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("error"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("category_id", cat_id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(req);

    }

}
