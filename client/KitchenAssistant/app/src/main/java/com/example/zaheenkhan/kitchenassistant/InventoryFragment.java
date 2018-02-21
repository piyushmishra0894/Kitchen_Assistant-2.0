package com.example.zaheenkhan.kitchenassistant;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.BinderThread;
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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zaheenkhan on 2/13/18.
 */

public class InventoryFragment extends Fragment implements View.OnClickListener{

    ArrayList<Row> rows = new ArrayList<Row>();
    ArrayList<Item> items = new ArrayList<Item>();

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
        HttpUtils.get("userinventory", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    InventoryModel response = mapper.readValue(jsonObject.toString(), InventoryModel.class);
                    int index = 0;
                    for(Item item: response.inventory){
                        addRow(getView());
                        rows.get(index).actv.setText(item.name);
                        rows.get(index).et.setText(item.qty+"");
                        index++;
                    }
                    if(index < 3){
                        addRow(getView());
                        index++;
                    }
                    Toast.makeText(getActivity(), "Check the inventory tab for your saved grocery!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Could not read your saved grocery", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getActivity(), "Could not read your data", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addRow(View v){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.ll_itemsList);
        LinearLayout row = new LinearLayout(getActivity());
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        AutoCompleteTextView at = getAutoCompleteTextView();
        at.setWidth(700);
        at.setHint("Item");
        EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setWidth(300);
        et.setHint("Qty");
        row.addView(at);
        row.addView(et);
        ll.addView(row);
        rows.add(new Row(at, et));
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

    public void save(){
        for(Row row: rows){
            if(row.actv.getText().toString().trim().length() != 0
                    &&
                    row.et.getText().toString().trim().length() != 0){
                items.add(new Item(row.actv.getText().toString(), Double.parseDouble(row.et.getText().toString())));
            }
        }
        Gson gson = new GsonBuilder().create();
        JsonArray itemsList = gson.toJsonTree(items).getAsJsonArray();
        RequestParams params = new RequestParams();
        params.add("id", "1");
        params.add("inventory", itemsList.toString());
        HttpUtils.post("userinventory", params, new JsonHttpResponseHandler(){
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
    Row(AutoCompleteTextView p_actv, EditText p_et){
        actv = p_actv;
        et = p_et;
    }
}
class Item{
    String name;
    double qty;
    Item(String p_name, double p_qty){
        name = p_name;
        qty = p_qty;
    }
    Item(){

    }
}
class InventoryModel
{
    protected Item[] inventory;
    protected int userId;
}