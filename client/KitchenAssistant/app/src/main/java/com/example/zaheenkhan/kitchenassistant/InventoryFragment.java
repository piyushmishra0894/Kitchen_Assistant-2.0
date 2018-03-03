package com.example.zaheenkhan.kitchenassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import junit.runner.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by zaheenkhan on 2/13/18.
 */

public class InventoryFragment extends Fragment implements View.OnClickListener{

    ArrayList<Row> rows = new ArrayList<Row>();
    ArrayList<Inventory> items = new ArrayList<Inventory>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inventory,container,false);
        Button b = (Button) view.findViewById(R.id.b_addItem);
        b.setOnClickListener(this);
        b = (Button)view.findViewById(R.id.b_save);
        b.setOnClickListener(this);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RequestParams params = new RequestParams();
        params.add("id", "1");
        final TextView tt = view.findViewById(R.id.tt1);
        //tt.setText("before");
        HttpUtils.get("/api/inventory/1", params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonObject) {
                ObjectMapper mapper = new ObjectMapper();

                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                try {


                    Inventory[] response = mapper.readValue(jsonObject.toString(), Inventory[].class);
                    items.addAll(Arrays.asList(response));
                    //tt.setText(response.getIngredient().toString());




                    int index = 0;
//                    tt.setText(response.getIngredient().getName());
//                    addRow(getView());
//                        rows.get(index).actv.setText(response.getIngredient().getName());
//                        rows.get(index).et.setText(response.getQuantity()+"");
                    for(Inventory item: response){
                        addRow(getView());
                        rows.get(index).actv.setText(item.getIngredient().getName());
                        rows.get(index).et.setText(item.getQuantity()+"");
                        rows.get(index).tv.setText(item.getIngredient().getMeasurementType());
                        index++;
                    }
                    if(index < 3){
                        addRow(getView());
                        index++;
                    }


//                    addRow(getView());
//                        rows.get(0).actv.setText(jsonObject.getJSONObject(0).getString("id"));
//                        rows.get(0).et.setText(jsonObject.getJSONObject(0).getString("Quantity")+"");

                    Toast.makeText(getActivity(), "Check the inventory tab for your saved grocery!", Toast.LENGTH_LONG).show();
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Could not read your saved grocery"+e.toString(), Toast.LENGTH_LONG).show();
                    tt.setText(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getActivity(), "Could not read your data", Toast.LENGTH_LONG).show();
                tt.setText("failure");
            }
        });

    }

    public void addRow(View v){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.ll_itemsList);
        LinearLayout row = new LinearLayout(getActivity());
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        AutoCompleteTextView at = getAutoCompleteTextView();
        TextView tv = new TextView(getActivity());
        at.setWidth(550);
        at.setHint("Item");
        EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setWidth(300);
        et.setHint("Qty");
        tv.setWidth(250);
        row.addView(at);
        row.addView(et);
        row.addView(tv);
        ll.addView(row);
        rows.add(new Row(at, et,tv));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_addItem:
                addRow(view);
                break;
            case R.id.b_save:
                save();
                break;
        }
    }

    @SuppressLint("ResourceType")
    public void save(){
        final TextView tt = getView().findViewById(R.id.tt1);
        int index =0;
        for(Row row: rows){
            if(row.actv.getText().toString().trim().length() != 0
                    &&
                    row.et.getText().toString().trim().length() != 0){
                //items.add(new Inventory(row.actv.getText().toString(), row.et.getText().toString()));
                items.get(index++).setQuantity(row.et.getText().toString().trim());
            }
        }
        Gson gson = new GsonBuilder().create();
        JsonArray itemsList = gson.toJsonTree(items).getAsJsonArray();
        /*RequestParams params = new RequestParams();
        params.add("id", "1");
        params.add("inventory", itemsList.toString());*/
        StringEntity params = null;
        try {
            params = new StringEntity(itemsList.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //tt.setText(params.toString());
        HttpUtils.put(getContext(),"/api/inventory/1", params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, byte[] response) {
                Toast.makeText(getActivity(), "Successfully saved", Toast.LENGTH_LONG).show();            }
        });

    }

    public AutoCompleteTextView getAutoCompleteTextView(){
        final String[] ITEMS = new String[] {
                "Milk", "Eggs", "Bread", "Banana", "Oil"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, ITEMS);
        final AutoCompleteTextView textView = new AutoCompleteTextView(getActivity());
        textView.setThreshold(0);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                textView.showDropDown();
            }
        });
        textView.setAdapter(adapter);
        return textView;
    }
}
class Row{
    AutoCompleteTextView actv;
    EditText et;
    TextView tv;
    Row(AutoCompleteTextView p_actv, EditText p_et, TextView p_tv){
        actv = p_actv;
        et = p_et;
        tv = p_tv;
    }
}

class Ingredient
{
    private String updatedAt;

    private String id;

    private String createdAt;

    private String measurementType;

    private String name;

    public String getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getMeasurementType ()
    {
        return measurementType;
    }

    public void setMeasurementType (String measurementType)
    {
        this.measurementType = measurementType;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", createdAt = "+createdAt+", measurementType = "+measurementType+", name = "+name+"]";
    }
}
@JsonIgnoreProperties(value = { "IngredientObj" })
class Inventory
{
    private String IngredientId;

    private String updatedAt;

    private String id;

    private String Quantity;

    private String createdAt;

    private String Itemid;

    public Ingredient getIngredient() {
        return Ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        Ingredient = ingredient;
    }

    private Ingredient Ingredient;

    public Ingredient getIngredientObj() {
        return IngredientObj;
    }

    public void setIngredientObj(Ingredient ingredientObj) {
        IngredientObj = ingredientObj;
    }

    private Ingredient IngredientObj;

    private String UserId;

    public String getIngredientId ()
    {
        return IngredientId;
    }

    public void setIngredientId (String IngredientId)
    {
        this.IngredientId = IngredientId;
    }

    public String getUpdatedAt ()
    {
        return updatedAt;
    }

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getQuantity ()
    {
        return Quantity;
    }

    public void setQuantity (String Quantity)
    {
        this.Quantity = Quantity;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getItemid ()
    {
        return Itemid;
    }

    public void setItemid (String Itemid)
    {
        this.Itemid = Itemid;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [IngredientId = "+IngredientId+", updatedAt = "+updatedAt+", id = "+id+", Quantity = "+Quantity+", createdAt = "+createdAt+", Itemid = "+Itemid+" , UserId = "+UserId+"]";
    }
}

class InventoryModel
{
    protected Inventory[] inventory;
    //protected int userId;
}