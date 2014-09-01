package de.eclipsemagazin.mqtt.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

public class Schedule extends Activity{
	
	private TimeImage timeImage = new TimeImage(1);
	private GridAdapter grid = GridAdapter.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		GridView dayView = (GridView) findViewById(R.id.dayGridView);
		dayView.setAdapter(new PillClick(this, timeImage.getArrayList()));
		GridView weekView = (GridView) findViewById(R.id.weekDayView);
		weekView.setAdapter(new PillClick(this, timeImage.getWeek()));
		GridView gridview = (GridView) findViewById(R.id.pillView);
		gridview.setAdapter(new PillClick(this,grid.getPills()));
	}
	
	@Override
	public void onBackPressed() {
	    Intent intent = new Intent(this, Login.class);
	    startActivity(intent);
	}
}