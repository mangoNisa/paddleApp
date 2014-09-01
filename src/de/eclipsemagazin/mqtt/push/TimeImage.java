package de.eclipsemagazin.mqtt.push;

import java.util.ArrayList;
import java.util.Arrays;

public class TimeImage{
	
	ArrayList<ArrayList<Integer>> arrayList = new ArrayList<ArrayList<Integer>>();
	Integer[][] time1 = {{-1,R.drawable.morn}, {-1,R.drawable.afternoon}, {-1,R.drawable.evening}, {-1,R.drawable.even}};
	Integer[][] time2 = {{-1,R.drawable.blank},{-1,R.drawable.morn}, {-1,R.drawable.afternoon}, {-1,R.drawable.evening}, {-1,R.drawable.even}};
	static Integer[][] weekday = {{-1,R.drawable.mon},{-1,R.drawable.tues},{-1,R.drawable.wed},{-1,R.drawable.thur},{-1,R.drawable.fri},{-1,R.drawable.sat},{-1,R.drawable.sun}};

	/*
	 * Constructor checks if its creating the images for schedule page
	 * or main activity
	 * 
	 * @param i 1 if main pages else can be any other number 
	 */
	public TimeImage(int i){
		if(i != 1)arrayList = new ArrayList<ArrayList<Integer>>(getTime(time1));
		else arrayList = new ArrayList<ArrayList<Integer>>(getTime(time2));
	}
	
	/*
	 * Converts an nested array to an arraylist to create
	 * an arraylist that has the images of times of the day in order 
	 * 
	 * @param array Nested arraylist of images
	 * 
	 * @return current Arraylist of images 
	 */
	private ArrayList<ArrayList<Integer>> getTime(Integer[][] array) {
		ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<array.length; i++){
			ArrayList<Integer> c = new ArrayList<Integer>();
			c.addAll(Arrays.asList(array[i]));
			current.add(c);
		}

		return current;
	}
	
	/*
	 * Creates an nested arraylist of the days of the week
	 * 
	 */
	public ArrayList<ArrayList<Integer>> getWeek() {
		ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<weekday.length; i++){
			ArrayList<Integer> c = new ArrayList<Integer>();
			c.addAll(Arrays.asList(weekday[i]));
			current.add(c);
		}

		return current;
	}
	
	public ArrayList<ArrayList<Integer>> getArrayList(){
		return arrayList;
	}
	
}
