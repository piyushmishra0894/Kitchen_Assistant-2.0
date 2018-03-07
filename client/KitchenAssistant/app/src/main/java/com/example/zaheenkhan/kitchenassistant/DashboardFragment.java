package com.example.zaheenkhan.kitchenassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zaheenkhan on 2/13/18.
 */

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboardfragment,container,false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LinearLayout ll_itemsLayout = view.findViewById(R.id.ll_itemsLayout);
        HttpUtils.get("/api/items", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, final JSONArray jsonObject) {
                ObjectMapper mapper = new ObjectMapper();

                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                try {
                    Item[] response = mapper.readValue(jsonObject.toString(), Item[].class);

                    for(final Item item: response) {
                        HttpUtils.get("/api/itemrecipes/"+item.getId(), null, new JsonHttpResponseHandler() {
                            public void onSuccess(int statusCode, Header[] headers, final JSONArray innerJsonObject) {
                                ObjectMapper mapper = new ObjectMapper();
                                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                                try{
                                    ItemRecipe[] itemRecipes = mapper.readValue(innerJsonObject.toString(), ItemRecipe[].class);
                                    item.setItemRecipes(itemRecipes);
                                    final TextView tv = new TextView(getActivity());
                                    tv.setText(item.getName() + "+");
                                    tv.setHeight(100);
                                    tv.setTextSize(20);
                                    ll_itemsLayout.addView(tv);
                                    final TextView innerTextView = new TextView(getActivity());
                                    for(ItemRecipe recipe: item.getItemRecipes()){

                                        innerTextView.setText(innerTextView.getText() +
                                                recipe.getStepNumber() + ". " + recipe.getStepName()+":" + recipe.getStepDescription() + "\n");

                                    }
                                    innerTextView.setVisibility(View.GONE);
                                    ll_itemsLayout.addView(innerTextView);
                                    tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(tv.getText().toString().endsWith("+")){
                                                tv.setText(item.getName() + "-");
                                                innerTextView.setVisibility(View.VISIBLE);
                                            }
                                            else{
                                                tv.setText(item.getName() + "+");
                                                innerTextView.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                                catch (Exception ex){

                                }

                            }
                        });

                    }

                }
                catch (Exception ex){

                }
            }
        });
    }

}



//Classes!
class Item
{
    private String updatedAt;

    private String Name;

    private String id;

    private String createdAt;

    private ItemRecipe[] itemRecipes;

    public ItemRecipe[] getItemRecipes() {return itemRecipes; }

    public void setItemRecipes(ItemRecipe[] itemRecipes) {this.itemRecipes = itemRecipes; }

    public String getUpdatedAt ()
{
    return updatedAt;
}

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
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

    @Override
    public String toString()
    {
        return "ClassPojo [updatedAt = "+updatedAt+", Name = "+Name+", id = "+id+", createdAt = "+createdAt+"]";
    }
}


class ItemRecipe
{
    private String updatedAt;

    private String id;

    private String ItemId;

    private String StepDescription;

    private String createdAt;

    private String StepName;

    private String StepNumber;

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

    public String getItemId ()
    {
        return ItemId;
    }

    public void setItemId (String ItemId)
    {
        this.ItemId = ItemId;
    }

    public String getStepDescription ()
    {
        return StepDescription;
    }

    public void setStepDescription (String StepDescription)
    {
        this.StepDescription = StepDescription;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getStepName ()
    {
        return StepName;
    }

    public void setStepName (String StepName)
    {
        this.StepName = StepName;
    }

    public String getStepNumber ()
    {
        return StepNumber;
    }

    public void setStepNumber (String StepNumber)
    {
        this.StepNumber = StepNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", ItemId = "+ItemId+", StepDescription = "+StepDescription+", createdAt = "+createdAt+", StepName = "+StepName+", StepNumber = "+StepNumber+"]";
    }
}