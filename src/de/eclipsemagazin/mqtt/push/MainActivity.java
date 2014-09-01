package de.eclipsemagazin.mqtt.push;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	Date date;
    Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
    int today = localCalendar.get(Calendar.DAY_OF_WEEK);
    TimeImage timeImage = new TimeImage(0);
	    
    ImageView imageView;
    TextView time;
	static TextView day;
	
	GridAdapter grid;

    public void onCreate(Bundle savedInstanceState){
    	
	
    	grid = GridAdapter.getInstance();
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
				
		time=(TextView)findViewById(R.id.txtTime);
		
		
		time.setText(grid.getNextTime());
		time.setTextSize(TypedValue.COMPLEX_UNIT_DIP,screenSize());
		day = (TextView)findViewById(R.id.txtDay);
		day.setText(getTime());

		//create todays pills view
		GridView dayView = (GridView) findViewById(R.id.dayGridView);
		dayView.setAdapter(new PillClick(this, timeImage.getArrayList()));
		GridView pillGridView = (GridView) findViewById(R.id.pillGridView);
		pillGridView.setAdapter(new PillClick(this, grid.getTodayPills(today)));
		
		
    }

	public void viewSchedule(View v){
    	Intent intent = new Intent(this, Schedule.class);
    	startActivity(intent);
    }
    
    public static void setText(String t1){
    	day.setText(t1);
   }
	public String getTime(){
    	String[] days = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    	return days[today-1];
   }
	/*
	 * checks the screen size so text is at the correct size
	 * 
	 * @return size Size the text should be
	 */
	private int screenSize() {
		int size = 40;
		int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		switch(screenSize) {
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				size = 220;
			    break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	size = 140;
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	size = 100;
		        break;
		}
		
		return size;
	}
	
}
