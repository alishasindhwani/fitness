/*
 * Copyright 2013 Ken Yang
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package transapps.gpxfitness.piechart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import transapps.gpxfitness.R;
import transapps.gpxfitness.db.METAccessor;
import transapps.gpxfitness.obj.Exercise;
import transapps.gpxfitness.ui.ExerciseListViewActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PieChart extends View {

	public interface OnSelectedLisenter{
		public abstract void onSelected(Exercise iSelectedExercise);
	}

	private OnSelectedLisenter onSelectedListener = null;

	private static final String TAG = PieChart.class.getName();
	public static final String ERROR_NOT_EQUAL_TO_100 = "NOT_EQUAL_TO_100";
	private static final int DEGREE_360 = 360;
	private static String[] PIE_COLORS 	= null;
	private static int iColorListSize 	= 0;

	private Paint paintPieFill;
	private Paint paintPieBorder;
	private ArrayList<Float> alPercentage = new ArrayList<Float>();

	private int iDisplayWidth, iDisplayHeight;
	private int iSelectedIndex 	= -1;
	private int iCenterWidth 	= 0;
	private int iShift			= 0;
	private int iMargin 		= 0;  // margin to left and right, used for get Radius
	private int iDataSize		= 0;

	private RectF r 			= null;

	private float fDensity 		= 0.0f;
	private float fStartAngle 	= 0.0f;
	private float fEndAngle 	= 0.0f;

	private HashMap<Exercise, Float> data;

	private ArrayList<Exercise> indexExercises;

	private Map<String, Integer> icons;

	public PieChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		PIE_COLORS = getResources().getStringArray(R.array.colors);
		iColorListSize = PIE_COLORS.length;

		fnGetDisplayMetrics(context);
		iShift 	= (int) fnGetRealPxFromDp(40);
		iMargin = (int) fnGetRealPxFromDp(60); //FIXME: changed this from 25

		// used for paint circle
		paintPieFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintPieFill.setStyle(Paint.Style.FILL);

		// used for paint border
		paintPieBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintPieBorder.setStyle(Paint.Style.STROKE);
		paintPieBorder.setStrokeWidth(fnGetRealPxFromDp(3));
		paintPieBorder.setColor(Color.WHITE);
		Log.i(TAG, "PieChart init");
	}

	// set listener
	public void setOnSelectedListener(OnSelectedLisenter listener){
		this.onSelectedListener = listener;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(TAG, "onDraw");

		for (int i = 0; i < iDataSize; i++) {

			// check whether the data size larger than color list size
			if (i>=iColorListSize){
				paintPieFill.setColor(Color.parseColor(PIE_COLORS[i%iColorListSize]));
			}else{
				paintPieFill.setColor(Color.parseColor(PIE_COLORS[i]));
			}

			fEndAngle = alPercentage.get(i);

			// convert percentage to angle
			fEndAngle = fEndAngle / 100 * DEGREE_360;

			// if the part of pie was selected then change the coordinate
			if (iSelectedIndex == i) {
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				float fAngle = fStartAngle + fEndAngle / 2;
				double dxRadius = Math.toRadians((fAngle + DEGREE_360) % DEGREE_360);
				float fY = (float) Math.sin(dxRadius);
				float fX = (float) Math.cos(dxRadius);
				canvas.translate(fX * iShift, fY * iShift);
			}

			canvas.drawArc(r, fStartAngle, fEndAngle, true, paintPieFill);

			// if the part of pie was selected then draw a border
			if (iSelectedIndex == i) {
				canvas.drawArc(r, fStartAngle, fEndAngle, true, paintPieBorder);
				canvas.restore();
			}
			fStartAngle = fStartAngle + fEndAngle;
		}
				
		int iR = iCenterWidth-iMargin;
		for (int i = 0; i < iDataSize; i++) {
			fEndAngle = alPercentage.get(i);

			// convert percentage to angle
			fEndAngle = fEndAngle / 100 * DEGREE_360;
			if (alPercentage.get(i)>8) {
			
				float centerY = (float) (0.75*iR*Math.cos(Math.toRadians(fStartAngle) + 0.5*Math.toRadians(fEndAngle))) + iCenterWidth;
				float centerX = (float) (0.75*iR*Math.sin(Math.toRadians(fStartAngle) + 0.5*Math.toRadians(fEndAngle))) + iCenterWidth;
				
				float length = 40;

				float top = centerX-length;
				if (top<0) top = 0;
				float bottom = centerX+length;
				if (bottom>iDisplayHeight) bottom = iDisplayHeight;
				float left = centerY-length;
				if (left<0) left = 0;
				float right = centerX-length;
				if (right>iDisplayWidth) right = iDisplayWidth;
				RectF rw = new RectF(centerY-length, centerX-length, centerY+length, centerX+length);
				
				if (!(indexExercises.get(i).getType().equals("empty"))) { 
					String category = indexExercises.get(i).getCategory();
					if (icons==null) {
						Log.i("PIE", "icons collection is null?!");
						makeIconCollection();
					}
					
					int id;
					if (icons.get(category)==null) id = icons.get("Running");
					else id = icons.get(category);
					
					Bitmap pic =BitmapFactory.decodeResource(getResources(), id);
	
					canvas.drawBitmap(pic, null, rw, null);
				}
			}
			fStartAngle = fStartAngle + fEndAngle;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// get screen size
		iDisplayWidth = (int)(MeasureSpec.getSize(widthMeasureSpec)); //FIXME: changed these from .8
		iDisplayHeight =(int)(MeasureSpec.getSize(heightMeasureSpec));
//		iDisplayWidth = 800;
//		iDisplayHeight = 811;
		if (iDisplayWidth>iDisplayHeight){
			iDisplayWidth = iDisplayHeight;
		}

		/*
		 *  determine the rectangle size
		 */
		iCenterWidth = iDisplayWidth / 2;
		int iR = iCenterWidth-iMargin;
		if (r == null) {
			r = new RectF(iCenterWidth-iR,  // top
					iCenterWidth-iR,  		// left
					iCenterWidth+iR,  		// rights
					iCenterWidth+iR); 		// bottom
		}
		//Log.i("IMPORTANT", "iDisplayWidth:"+iDisplayWidth+", iDisplayHeight:"+iDisplayHeight);
		setMeasuredDimension(iDisplayWidth, iDisplayWidth);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// get degree of the touch point
		double dx = Math.atan2(event.getY() - iCenterWidth, event.getX() - iCenterWidth);
		float fDegree = (float) (dx / (2 * Math.PI) * DEGREE_360);
		fDegree = (fDegree + DEGREE_360) % DEGREE_360;

		// get the percent of the selected degree
		float fSelectedPercent = fDegree * 100 / DEGREE_360;
		
		Exercise iSelectedExercise = null;
		
		// check which pie was selected
		float fTotalPercent = 0;
		for (int i=0; i<iDataSize; i++) {
			fTotalPercent += alPercentage.get(i); //the percentage
			if (fTotalPercent > fSelectedPercent) {
				if(iSelectedIndex==i)
					iSelectedIndex=-1;
				
				else
				{
				iSelectedIndex = i;
				iSelectedExercise = indexExercises.get(i);
				}
				break;
			}
		}
		if (onSelectedListener != null){
			onSelectedListener.onSelected(iSelectedExercise);
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	private void fnGetDisplayMetrics(Context cxt){
		final DisplayMetrics dm = cxt.getResources().getDisplayMetrics();
		fDensity = dm.density;
	}

	private float fnGetRealPxFromDp(float fDp){
		return (fDensity!=1.0f) ? fDensity*fDp : fDp;
	}

	public void setAdapter(HashMap<Exercise, Float> dat) throws Exception {
		if (dat==null) {
			this.data = dat;
			Exercise empty = new Exercise("empty", 0);
			this.indexExercises = new ArrayList<Exercise>();
			this.indexExercises.add(empty);
			this.alPercentage = new ArrayList<Float>();
			this.alPercentage.add(100f);
			iDataSize = 1;
		}
		else {
			this.data = dat;
			ArrayList<Float> alPercentage = new ArrayList<Float>();
			ArrayList<Exercise> indexExercises = new ArrayList<Exercise>();
			for (Exercise e : dat.keySet()) {
				alPercentage.add(dat.get(e));
				indexExercises.add(e);
			}
			this.indexExercises = indexExercises;
			this.alPercentage = alPercentage;
			iDataSize = alPercentage.size();
		}
		
		float fSum = 0;
		for (int i = 0; i < iDataSize; i++) {
			fSum+=alPercentage.get(i);
		}
		
		if (Math.abs(100-fSum)<2) {
			alPercentage.set(0, alPercentage.get(0)+Math.abs(100-fSum));
		}
		
		if (fSum!=100){
			Log.e("PIE",ERROR_NOT_EQUAL_TO_100+" diff: "+Math.abs(100-fSum));
			iDataSize = 0;
			throw new Exception(ERROR_NOT_EQUAL_TO_100);
		}
		
		makeIconCollection();
	}
	
	public Map<String, Integer> makeIconCollection() {
		List<String> listCategories = new ArrayList<String>();
        String[] categories = METAccessor.getCategories();
        for(String s: categories){
        	s = s.substring(0,1).toUpperCase() + s.substring(1);
        	listCategories.add(s);
        }
        listCategories.add("GPS");
        Map<String, Integer> iconCollect = new HashMap<String, Integer>();
        
        for(String category: listCategories){
    		if(category.equals("Conditioning"))
    			iconCollect.put(category, R.drawable.ic_cat_conditioning_gray);
    		else if(category.equals("Military"))
    			iconCollect.put(category, R.drawable.ic_cat_military_gray);
    		else if(category.equals("Occupational"))
    			iconCollect.put(category, R.drawable.ic_cat_occupational_gray);
    		else if(category.equals("Running"))
    			iconCollect.put(category, R.drawable.ic_cat_running_gray);
    		else if(category.equals("Sports"))
    			iconCollect.put(category, R.drawable.ic_cat_sports_gray);
    		else if(category.equals("Walking"))
    			iconCollect.put(category, R.drawable.ic_cat_walking_gray);
    		else if(category.equals("GPS"))
    			iconCollect.put(category, R.drawable.ic_gpxinput_gray);
    		else //running man default
    			iconCollect.put(category, R.drawable.ic_cat_running_gray);
    	 }
        
        for (String cat : iconCollect.keySet()) {
        	Log.i("PIE", "icon key: "+cat+", val: "+iconCollect.get(cat));
        }
        
		this.icons = iconCollect;
		return iconCollect;
	}
}
