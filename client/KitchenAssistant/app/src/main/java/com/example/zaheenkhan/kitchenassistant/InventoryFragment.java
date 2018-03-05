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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by zaheenkhan on 2/13/18.
 */

public class InventoryFragment extends Fragment implements View.OnClickListener{

    ArrayList<Row> rows = new ArrayList<Row>();
    ArrayList<Inventory> items = new ArrayList<Inventory>();
    ArrayList<Ingredient> dropList = new ArrayList<Ingredient>();
    String[] dropArray = new String[]{};
    ArrayAdapter<String> dropAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_inventory,container,false);
        Button b = (Button) view.findViewById(R.id.b_addItem);
        b.setOnClickListener(this);
        b = (Button)view.findViewById(R.id.b_save);
        b.setOnClickListener(this);
        b = (Button)view.findViewById(R.id.b_deleteItems);
        b.setOnClickListener(this);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RequestParams params = new RequestParams();
        params.add("id", "1");
        dropAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line);
        final TextView tt = view.findViewById(R.id.tt1);
        //tt.setText("before");
        HttpUtils.get("/api/inventory/1", params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonObject) {
                ObjectMapper mapper = new ObjectMapper();

                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                try {


                    Inventory[] response = mapper.readValue(jsonObject.toString(), Inventory[].class);
                    items.addAll(Arrays.asList(response));
                    int index = 0;
//                    tt.setText(response.getIngredient().getName());
//                    addRow(getView());
//                        rows.get(index).actv.setText(response.getIngredient().getName());
//                        rows.get(index).et.setText(response.getQuantity()+"");
                    for(Inventory item: response){
                        addRow(getView(),item);
                        rows.get(index).actv.setText(item.getIngredient().getName());
                        dropAdapter.remove(item.getIngredient().getName());
                        dropAdapter.notifyDataSetChanged();
                        rows.get(index).et.setText(item.getQuantity()+"");
                        rows.get(index).tv.setText(item.getIngredient().getMeasurementType());
                        //rows.get(index).it = item;
                        index++;
                    }
//                    if(index < 3){
//                        addRow(getView());
//                        index++;
//                    }


//                    addRow(getView());
//                        rows.get(0).actv.setText(jsonObject.getJSONObject(0).getString("id"));
//                        rows.get(0).et.setText(jsonObject.getJSONObject(0).getString("Quantity")+"");
                    //tt.setText(String.format("Index : %d ",index));
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

    public void addRow(View v,Inventory it){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.ll_itemsList);
        final LinearLayout row = new LinearLayout(getActivity());
        row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        row.setLongClickable(true);
        AutoCompleteTextView at = getAutoCompleteTextView();
        final TextView tv = new TextView(getActivity());
        at.setWidth(500);
        at.setHint("Item");
        EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setWidth(250);
        et.setHint("Qty");
        tv.setWidth(250);
        row.addView(at);
        row.addView(et);
        row.addView(tv);
        ll.addView(row);
        rows.add(new Row(at, et,tv,it));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_addItem:
                Inventory newItem = new Inventory();
                //items.add(newItem);
                addRow(view,newItem);
                break;
            case R.id.b_save:
                deleteItems(true);
                save();
                break;
            case R.id.b_deleteItems:
                deleteItems(false);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    public void deleteItems(boolean submit){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.ll_itemsList);
        final Button delButton = getView().findViewById(R.id.b_deleteItems);
        final TextView tt = getView().findViewById(R.id.tt1);
        boolean flag = true;
        if (delButton.getText().equals("DELETE ITEMS"))
            delButton.setText("CANCEL");
        else {
            delButton.setText("DELETE ITEMS");
            flag = false;
        }
        if (submit)
        {
            if (flag) {
                //This means the submit button was pressed but delete items checkbox was not active
                //reset button text back to normal and return nothing to delete
                delButton.setText("DELETE ITEMS");
                return;
            }
            ArrayList<Inventory> deleteList = new ArrayList<>();
            try {
                for (int i = 0; i < ll.getChildCount(); i++) {
                    LinearLayout v = (LinearLayout) ll.getChildAt(i);
                    CheckBox delB = (CheckBox) v.getChildAt(0);
                    if (delB.isChecked())
                    {
                        if (!rows.get(i).it.getId().contentEquals("-1")){
                            deleteList.add(rows.get(i).it);
                        }
                        ll.removeViewAt(i);
                        rows.remove(i);
                        //items.remove(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Could not save changes to Inventory", Toast.LENGTH_LONG).show();
                tt.setText(e.toString());
                //tt.setText(String.format("index: %d exc: %d  %s", index, exc, e.getStackTrace()[1].toString()));
            }
            if (!deleteList.isEmpty()) {
                Gson gson = new GsonBuilder().create();
                JsonArray itemsList = gson.toJsonTree(deleteList).getAsJsonArray();
                StringEntity params = null;
                try {
                    params = new StringEntity(itemsList.toString());
                    //tt.setText(params.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpUtils.delete(getContext(),"/api/inventory/1", params, new JsonHttpResponseHandler(){
                    public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, Objects response) {
                        Toast.makeText(getActivity(), "Successfully deleted items", Toast.LENGTH_LONG).show();
                    }
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Toast.makeText(getActivity(), "Could not delete items", Toast.LENGTH_LONG).show();
                        tt.setText("failure");
                    }

                });
            }
            else
                Toast.makeText(getActivity(), "Nothing to delete", Toast.LENGTH_LONG).show();
        }
        for (int i = 0; i < ll.getChildCount(); i++) {
            LinearLayout v = (LinearLayout) ll.getChildAt(i);
            if (flag) {
                CheckBox del = new CheckBox(getActivity());
                v.addView(del, 0);
            }
            else
            {
                v.removeViewAt(0);
            }
        }

    }

    @SuppressLint({"ResourceType", "DefaultLocale"})
    public void save(){
        final TextView tt = getView().findViewById(R.id.tt1);
        int exc = 0;
        ArrayList<Inventory> saveItems = new ArrayList<>();
        try {
            for(Row row: rows){
                if(row.actv.getText().toString().trim().length() != 0
                        &&
                        row.et.getText().toString().trim().length() != 0){
                    //items.add(new Inventory(row.actv.getText().toString(), row.et.getText().toString()));
                    if (!Objects.equals(row.it.getQuantity(), row.et.getText().toString().trim())) {

                        row.it.setQuantity(row.et.getText().toString().trim());
                        row.it.setUpdatedAt(Calendar.getInstance().getTime().toString());

                        saveItems.add(row.it);
                        exc++;
                        //tt.setText(String.format("updated on index: %d exc: %d, value: %s", index, exc,saveItems.get(index-1-exc).toString()));
                    }
                }
            }
            //tt.setText(String.format("index: %d exc: %d", index, exc));
            if (exc==0)
            {
                Toast.makeText(getActivity(), "Inventory already upto date. Nothing to save", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Could not save changes to Inventory", Toast.LENGTH_LONG).show();
            tt.setText(e.toString());
            //tt.setText(String.format("index: %d exc: %d  %s", index, exc, e.getStackTrace()[1].toString()));
        }
        Gson gson = new GsonBuilder().create();
        JsonArray itemsList = gson.toJsonTree(saveItems).getAsJsonArray();
        /*RequestParams params = new RequestParams();
        params.add("id", "1");
        params.add("inventory", itemsList.toString());*/
        StringEntity params = null;
        try {
            params = new StringEntity(itemsList.toString());
            //tt.setText(params.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpUtils.put(getContext(),"/api/inventory/1", params, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, String response) {
                TextView tt = getView().findViewById(R.id.tt1);
                tt.setText(response);
                Toast.makeText(getActivity(), "Successfully saved", Toast.LENGTH_LONG).show();
            }
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getActivity(), "Could not read your data", Toast.LENGTH_LONG).show();
                tt.setText("failure");
            }

        });

    }

    public AutoCompleteTextView getAutoCompleteTextView(){

        final TextView tt = getView().findViewById(R.id.tt1);
        final AutoCompleteTextView textView = new AutoCompleteTextView(getActivity());
        RequestParams params = new RequestParams();
        if (dropList.isEmpty()) {
            if (items.size()>0)
            dropList.add(items.get(0).getIngredient());
            HttpUtils.get("/api/ingredients", params, new JsonHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, JSONArray jsonObject) {
                            ObjectMapper mapper = new ObjectMapper();

                            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                            try {
                                Ingredient[] response = mapper.readValue(jsonObject.toString(), Ingredient[].class);
                                dropList.clear();
                                dropList.addAll(Arrays.asList(response));
                                ArrayList<String> arry = new ArrayList<>();
                                int index=0;
                                for (Ingredient item: response
                                     ) {
                                    arry.add(item.getName());
                                }
                                dropArray = arry.toArray(new String[arry.size()]);



                                dropAdapter.addAll(dropArray);
                                textView.setAdapter(dropAdapter);
                                dropAdapter.notifyDataSetChanged();

                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), "Could not read your saved grocery"+e.toString(), Toast.LENGTH_LONG).show();
                                tt.setText(e.toString());
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            textView.setAdapter(dropAdapter);
            dropAdapter.notifyDataSetChanged();
        }
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<rows.size();i++){
                    if (rows.get(i).actv.getOnItemClickListener() == this)
                    {
                        if (rows.get(i).it.getItemid().contentEquals("-1")){
                            rows.get(i).it.setCreatedAt(Calendar.getInstance().getTime().toString());
                            rows.get(i).it.setUserId("1");
                        }
                        String selected = (String) parent.getItemAtPosition(position);
                        position = Arrays.asList(dropArray).indexOf(selected);
                        rows.get(i).it.setUpdatedAt(Calendar.getInstance().getTime().toString());
                        rows.get(i).it.setItemid(dropList.get(position).getId());
                        rows.get(i).it.setIngredientId(dropList.get(position).getId());
                        rows.get(i).it.setIngredient(dropList.get(position));

                        //changing quantity value to trigger a save action of the ingredient when the button is pressed.
                        //this needs to be changed. not a good way to detect change in the ingredient type
                        rows.get(i).it.setQuantity(String.valueOf(Integer.parseInt(rows.get(i).it.getQuantity())-1));

                        rows.get(i).tv.setText(dropList.get(position).getMeasurementType());
                        TextView tt = getView().findViewById(R.id.tt1);
                        tt.setText(String.format("The call to onItemClick. position:%d, i:%d",position,i));
                        break;
                    }
                }
                //tv.setText(dropList.get(position).getMeasurementType());
                //items.get(items.size()-1).setIngredient(dropList.get(position));

            }
        });
        textView.setThreshold(0);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                textView.showDropDown();
            }
        });
        return textView;
    }
}
class Row{
    AutoCompleteTextView actv;
    EditText et;
    TextView tv;
    Inventory it;
    Row(AutoCompleteTextView p_actv, EditText p_et, TextView p_tv,Inventory p_it){
        actv = p_actv;
        et = p_et;
        tv = p_tv;
        it = p_it;
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

    private Ingredient Ingredient;

    private String UserId;

    Inventory()
    {
        setId("-1");
        setCreatedAt(Calendar.getInstance().getTime().toString());
        setQuantity("0");
        setItemid("-1");
        setIngredientId("-1");
        setIngredient(null);
        setUserId("-1");
    }

    public Ingredient getIngredient() {
        return Ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        Ingredient = ingredient;
    }

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