package transapps.gpxfitness.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.FavoritesAccessor;
import transapps.gpxfitness.db.METAccessor;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ExerciseListViewActivity extends Activity {
	 
	List<String> categoryList;
    List<String> childList;
    Map<String, List<String>> exerciseCollection;
    Map<String, Integer> iconCollection;
    ExpandableListView expListView;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_list);

        categoryList = new ArrayList<String>();
        createCategoryList(categoryList);
        iconCollection = new LinkedHashMap<String, Integer>();
        createIconCollection(categoryList, iconCollection);
        createExerciseCollection();
 
        expListView = (ExpandableListView) findViewById(R.id.category_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, categoryList, exerciseCollection, iconCollection);
        expListView.setAdapter(expListAdapter);
        int[] colors = {0, 0xFF363C3B, 0}; // red for the example
        expListView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        expListView.setChildDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        expListView.setDividerHeight(1);
        setGroupIndicatorToRight();
 
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("exercise",selected);
                returnIntent.putExtra("icon", iconCollection.get(groupPosition));
                setResult(RESULT_OK,returnIntent);     
                finish();
                return true;
            }
        });
    }
 
    public static void createCategoryList(List<String> listCategories) {
    	if(FavoritesAccessor.getFavorites()!=null)
    	{
        	listCategories.add("Favorites");
    	}
        String[] categories = METAccessor.getCategories();
        for(String s: categories){
        	s = s.substring(0,1).toUpperCase() + s.substring(1);
        	listCategories.add(s);
        }
        
        
    }
    public static void createIconCollection(List<String> listCategories, Map<String, Integer> iconCollect) {
    	 
    	 for(String category: listCategories){
    		if(category.equals("Conditioning"))
    			iconCollect.put(category, R.drawable.ic_cat_conditioning);
    		else if(category.equals("Military"))
    			iconCollect.put(category, R.drawable.ic_cat_military);
    		else if(category.equals("Occupational"))
    			iconCollect.put(category, R.drawable.ic_cat_occupational);
    		else if(category.equals("Running"))
    			iconCollect.put(category, R.drawable.ic_cat_running);
    		else if(category.equals("Sports"))
    			iconCollect.put(category, R.drawable.ic_cat_sports);
    		else if(category.equals("Walking"))
    			iconCollect.put(category, R.drawable.ic_cat_walking);
    		else if(category.equals("Weight training"))
    			iconCollect.put(category, R.drawable.ic_cat_lifting);
    		else if(category.equals("Favorites"))
    			iconCollect.put(category, R.drawable.ic_cat_favorites);
    		else //running man default
    			iconCollect.put(category, R.drawable.ic_cat_running);
    	 }
    	 
    }
    private void createExerciseCollection() {
    	Log.d("TEXT", "CREATED EXERCISE COLLECTION");
        exerciseCollection = new LinkedHashMap<String, List<String>>();
        for (String category : categoryList) {
        	if(category.toLowerCase().equals("favorites"))
        		loadChild(FavoritesAccessor.getFavorites());
        	else
        		loadChild(METAccessor.getExercises(category.toLowerCase()));
            exerciseCollection.put(category, childList);
        }
    }
 
    private void loadChild(String[] exercises) {
        childList = new ArrayList<String>();
        for (String ex : exercises)
            childList.add(ex);
    }
 
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(60), width
                - getDipsFromPixel(5));
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
 
}