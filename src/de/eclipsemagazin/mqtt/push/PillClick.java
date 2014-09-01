package de.eclipsemagazin.mqtt.push;

import java.util.ArrayList;


import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class PillClick extends BaseAdapter {

	private Context mContext;
	private ArrayList<ArrayList<Integer>> image;
	GridAdapter grid = GridAdapter.getInstance();
	MQTTMessage m = MQTTMessage.getInstance();

	public PillClick(Context c, ArrayList<ArrayList<Integer>> pillImage) {
		mContext = c;
		this.image = pillImage;
	}

	public int getCount() {
		return image.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	/*
	 * Create a new ImageView for each item referenced by the Adapter
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {	
			
			//Set size of image depending on size of screen
			Resources r = Resources.getSystem();
			float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, screenSize(), r.getDisplayMetrics());
		    imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((int) px, (int) px));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);		
			
		}else{
			imageView = (ImageView) convertView;			
		}
		
		imageView.setImageResource(image.get(position).get(1));
		
		// Do not display dialog box day of weekday images or daytime images
		if (image.get(position).get(0).equals(-1)) return imageView; 

		imageView.setOnClickListener(new OnClickListener() {

			public void onClick(View args0) {

				grid.setKey(image.get(position).get(0));
				if (grid.getImportance().equals("Very Important")) dialogAlert(position, "Very Important", Color.RED);
				else if (grid.getImportance().equals("Important")) dialogAlert(position, "Important", Color.YELLOW);
				else dialogAlert(position, "Take as needed", Color.GREEN);
			}
		});

		return imageView;
	}

	/*
	 * Checks screen size to allocate correct size for image
	 * 
	 * @return size Size of image
	 */
	private int screenSize() {
		int size = 1;
		int screenSize = mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch(screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				size = 100;
			    break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	size = 60;
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	size = 30;
		        break;
		}
		
		return size;
	}

	/*
	 * Creates the alert box for each pill
	 * 
	 * @param position Index of item being worked on
	 * 
	 * @param message States importance of the pill
	 * 
	 * @param colour Colour of the pill
	 */
	public void dialogAlert(final int position, String message, int colour) {
		
		SpannableStringBuilder builder = new SpannableStringBuilder();
		SpannableStringBuilder build = new SpannableStringBuilder(message);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		build.setSpan(new ForegroundColorSpan(colour), 0, message.length(), 0);

		
		alertDialogBuilder.setTitle(build);
		builder.append("Due: " + grid.keyTime() + "\n");

		for (String i : grid.getPillList()) {
			if(grid.getPillList().indexOf(i) == 0){
				builder.append("Pills: " + i + "\n");
			}
			else{
				for(int j = 0; j<10; j++){
					builder.append(" ");
				}
				builder.append(i + "\n");
			}
		}

		alertDialogBuilder.setMessage(builder);
		alertDialogBuilder.setCancelable(false);

		alertDialogBuilder.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		
		//if the pill is green
		if(message.equals("Take as needed")){
			alertDialogBuilder.setNegativeButton("Skip",
					new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							grid.changePill(image.get(position).get(0), 0);
							dialog.cancel();
							
							try {
								m.sendMessage("skip", image.get(position).get(0).toString());
							} catch (Exception e) {
								Toast.makeText(mContext, "Unable to skip pill", Toast.LENGTH_LONG);
							}
							
							
							Intent i = new Intent(mContext, Schedule.class); 
							i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mContext.startActivity(i); //To refresh the page so changes will appear
							((Activity) mContext).overridePendingTransition(0,0);
							
						}
					});
		}


		// create alert dialog and show
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


}
