package com.example.zaheenkhan.kitchenassistant;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zaheenkhan on 2/13/18.
 */

public class DashboardFragment extends Fragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try{
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if(getView() != null){
                    final LinearLayout ll = (LinearLayout) getView().findViewById(R.id.ll_itemsLayout);
                    ll.removeAllViews();
                    onViewCreated(getView(), null);
                }
            }
        }
        catch (Exception ex){
            String x = ex.toString();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboardfragment,container,false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final LinearLayout ll_itemsLayout = view.findViewById(R.id.ll_itemsLayout);
        HttpUtils.get("/api/items/recipesuggestion/1", null, new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, final JSONArray jsonObject) {
                ObjectMapper mapper = new ObjectMapper();

                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                try {
                    Item[] response = mapper.readValue(jsonObject.toString(), Item[].class);

                    for(final Item item: response)
                        HttpUtils.get("/api/itemrecipes/" + item.getId(), null, new JsonHttpResponseHandler() {
                                    public void onSuccess(int statusCode, Header[] headers, final JSONArray innerJsonObject) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

                                        try {
                                            ItemRecipe[] itemRecipes = mapper.readValue(innerJsonObject.toString(), ItemRecipe[].class);
                                            item.setItemRecipe(itemRecipes);
                                            final TextView tv = new TextView(getActivity());
                                            tv.setText(item.getName());
                                            tv.setHeight(100);
                                            tv.setTextSize(20);
                                            tv.setTypeface(null, Typeface.BOLD);
                                            tv.setTextColor(Color.BLACK);
                                            LinearLayout title = new LinearLayout(getActivity());
                                            title.setOrientation(LinearLayout.HORIZONTAL);
                                            title.addView(tv);
                                            final ImageView expand = new ImageView(getActivity());
                                            expand.setBackgroundResource(R.drawable.expand);
                                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                                            expand.setLayoutParams(layoutParams);
                                            title.addView(expand);
                                            ll_itemsLayout.addView(title);
                                            final TextView innerTextView = new TextView(getActivity());
                                            for (ItemRecipe recipe : item.getItemRecipe()) {

                                                innerTextView.setText(innerTextView.getText() +
                                                        recipe.getStepNumber() + ". " + recipe.getStepName() + ": " + recipe.getStepDescription() + "\n");

                                            }
                                            innerTextView.setVisibility(View.VISIBLE);
                                            ll_itemsLayout.addView(innerTextView);
                                            Button b_delete = new Button(getActivity());
                                            b_delete.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                            b_delete.setTextColor(Color.WHITE);
                                            b_delete.setOnClickListener(new View.OnClickListener(){
                                                @Override
                                                public void onClick(View v){
                                                    RequestParams params = new RequestParams();
                                                    params.add("item", item.getId());
                                                    HttpUtils.get("/api/items/completecooking/1", params, new TextHttpResponseHandler() {
                                                        public void onSuccess(int statusCode, Header[] headers, String response) {
                                                            Toast.makeText(getActivity(), "Congratulations, your inventory has been updated accordingly!", Toast.LENGTH_LONG).show();
                                                        }
                                                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                                            Toast.makeText(getActivity(), "Sorry, we could not adjust your inventory!", Toast.LENGTH_LONG).show();
                                                        }
                                                });

                                            }});
                                            b_delete.setText("I cooked this!");
                                            ll_itemsLayout.addView(b_delete);
                                            Space s = new Space(getActivity());
                                            LinearLayout.LayoutParams slayoutParams = new LinearLayout.LayoutParams(100, 30);
                                            s.setLayoutParams(slayoutParams);
                                            ll_itemsLayout.addView(s);

                                            expand.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (expand.getRotation() == 0) {
                                                        innerTextView.setVisibility(View.GONE);
                                                        expand.setRotation(180);
                                                    } else {
                                                        expand.setRotation(0);
                                                        innerTextView.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                        } catch (Exception ex) {

                                        }

                                    }
                                }
                        );

                }
                catch (Exception ex){
                    Toast.makeText(getActivity(), "Seems like you do not have enough in your inventory! Please add grocery.", Toast.LENGTH_LONG).show();
                }
            }
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getActivity(), "Seems like you do not have enough in your inventory! Please add grocery.", Toast.LENGTH_LONG).show();
            }
        });
    }

}



//Classes!
class Item
{
    private String Salad;

    private String breakfast;

    private String Desserts;

    private String brunch;

    private String id;

    private String Name;

    private String updatedAt;

    private String dinner;

    private String Appetizer;

    private String Soup;

    private String createdAt;

    @JsonProperty("Fast-Food")
    private String FastFood;

    private String calories;

    private String timeToPrepare;

    private ItemRecipe[] itemRecipe;

    public ItemRecipe[] getItemRecipe(){
        return itemRecipe;
    }

    public void setItemRecipe(ItemRecipe[] itemRecipe){
        this.itemRecipe = itemRecipe;
    }

    public String getSalad ()
    {
        return Salad;
    }

    public void setSalad (String Salad)
    {
        this.Salad = Salad;
    }

    public String getBreakfast ()
    {
        return breakfast;
    }

    public void setBreakfast (String breakfast)
    {
        this.breakfast = breakfast;
    }

    public String getDesserts ()
    {
        return Desserts;
    }

    public void setDesserts (String Desserts)
    {
        this.Desserts = Desserts;
    }

    public String getBrunch ()
    {
        return brunch;
    }

    public void setBrunch (String brunch)
    {
        this.brunch = brunch;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getUpdatedAt ()
{
    return updatedAt;
}

    public void setUpdatedAt (String updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getDinner ()
    {
        return dinner;
    }

    public void setDinner (String dinner)
    {
        this.dinner = dinner;
    }

    public String getAppetizer ()
    {
        return Appetizer;
    }

    public void setAppetizer (String Appetizer)
    {
        this.Appetizer = Appetizer;
    }

    public String getSoup ()
    {
        return Soup;
    }

    public void setSoup (String Soup)
    {
        this.Soup = Soup;
    }

    public String getCreatedAt ()
{
    return createdAt;
}

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getFastFood ()
{
    return FastFood;
}

    public void setFastFood (String FastFood)
{
    this.FastFood = FastFood;
}

    public String getCalories ()
    {
        return calories;
    }

    public void setCalories (String calories)
    {
        this.calories = calories;
    }

    public String getTimeToPrepare ()
    {
        return timeToPrepare;
    }

    public void setTimeToPrepare (String timeToPrepare)
    {
        this.timeToPrepare = timeToPrepare;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Salad = "+Salad+", breakfast = "+breakfast+", Desserts = "+Desserts+", brunch = "+brunch+", id = "+id+", Name = "+Name+", updatedAt = "+updatedAt+", dinner = "+dinner+", Appetizer = "+Appetizer+", Soup = "+Soup+", createdAt = "+createdAt+", Fast-Food = "+FastFood+", calories = "+calories+", timeToPrepare = "+timeToPrepare+"]";
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