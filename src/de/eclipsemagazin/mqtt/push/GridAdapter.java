package de.eclipsemagazin.mqtt.push;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
/*************************************************************
 * 
 * GridAdapter produces the pill images for the corresponding
 * pills boxes on level of importance and has methods to 
 * manipulate and get data from the Map 
 * 
 * @author Nisa Nabi
 * 
 *************************************************************/

public class GridAdapter extends Activity {
	
	private static GridAdapter instance;
	 
	Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
	
	static ArrayList<ArrayList<Integer>> pillImage = new ArrayList<ArrayList<Integer>>();
	
	String[] medication;
	int theKey;
	String nextPillTime;
	private GridAdapter(){}
	MQTTMessage m = MQTTMessage.getInstance();
	
	public static GridAdapter getInstance(){
		if(instance == null){
			instance = new GridAdapter();
		}
		return instance;
	}
	
	/*
	 * setPills parses the json dictionary and then adds the the appropriate
	 * pill image colour by called the pillColour method. The key and the 
	 * image are added to an array list which are nested into another
	 * array list
	 * 
	 * @author Nisa
	 * 
	 * @param message JSON dictionary that needs to be parsed 
	 * 
	 * @return nothing
	 */
	public void setPills(String message) {
		System.out.println(message);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
        map = gson.fromJson(message, type);
        pillImage = new ArrayList<ArrayList<Integer>>();
		Integer pill;
		try{
        for(int key=0; key<28 ; key++ ){
    		System.out.println(map);

        	/*if(map.get(key+"").get("state").equals(1.0)){pill = R.drawable.taken;
        	else if (map.get(key+"").get("state").equals(2.0)) pill = R.drawable.gfade;
        	else if (map.get(key+"").get("state").equals(0.0)) pill = R.drawable.warning;
        	else*/ pill = pillColour( (Double) map.get(key+"").get("importance"));
        	
        	ArrayList<Integer> current = new ArrayList<Integer>();
    		current.add(key);
    		current.add(pill);
    		pillImage.add(current);
        }     
		}catch(Exception e){
			System.out.println(e.getMessage().toString());
		}
	}
	
	/*
	 * pillColour checks what the importance is and assigns the 
	 * corresponding coloured pill to the image.
	 * 
	 * @param impor Value of importance key from map 
	 * 
	 * @return image Yellow, green or red pill image
	 */
	private int pillColour(double impor) {
		int image = 0 ;
		if( impor == 0.0) image = R.drawable.gor;
		else if( impor == 1.0) image = R.drawable.yor;
		else if( impor == 2.0) image = R.drawable.ror;
		return image;		
	}
	
	public ArrayList<ArrayList<Integer>> getPills(){
		return pillImage;
	}	
	
	/*
	 * @return list of pills in each pill box
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPillList() {
		return (ArrayList<String>) map.get(getKey()+"").get("pills");
	}

	/*
	 * @param today Integer value representing day of week
	 * 
	 * @return list 4 pills in todays pill schedule
	 */
	public ArrayList<ArrayList<Integer>> getTodayPills(int today) {
		int day = (today-2) * 4; // row of day in grid
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		for(int i = day; i < day+4 ; i++){
			list.add(getPills().get(i));
		}
		return list;		
	}	
	/*
	 * keyTime gets the time the pills must be taken for
	 * the key that will have been already set
	 * 
	 * @return time Time pills must be taken
	 */
	public String keyTime(){
		double s =  (Double) map.get(getKey()+"").get("time");
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(0);
		long time = Long.valueOf(df.format(s));
		Date date = new Date(time);
		SimpleDateFormat sd = new SimpleDateFormat("hh:mm");
		String t = sd.format(date);
		return t;

	}
	
	/*
	 * pillColour checks what the importance is and assigns the 
	 * corresponding coloured pill to the image.
	 * 
	 * @return imp Importance of pill
	 */
	public String getImportance(){

		double i = (Double) map.get(getKey()+"").get("importance");
		String imp = "invalid";

		if(i == 2.0){
			imp = "Very Important";
		}else if(i == 1.0){
			imp = "Important";
		}else if(i == 0.0){
			imp = "Take as needed";
		}
		
		return imp;
	}
	/*
	 * Changes the pill image depending on the change integer 
	 * 
	 * @param key Key of pill in array list
	 * @param change Integer of change type	
	 * 
	 * 0 = skip
	 * 1 = taken
	 * 2 = warning
	 * 
	 * @return nothing
	 */
	public void changePill(int key, int change) {
		switch(change){
		case 2:		
			pillImage.get(key).set(1, R.drawable.warning);
			break;
		case 1:		
			pillImage.get(key).set(1, R.drawable.taken);
			break;
		case 0:		
			pillImage.get(key).set(1, R.drawable.gfade);
			break;
		}
	}
	
	/*
	 * Called when snooze has been pressed on a device
	 * Saves the number of snooze's for the given pill 
	 * 
	 * @param message Json array with key and number of snooze's left
	 * 
	 * @return nothing
	 */
	public void saveSnooze(String message) {
		Map<String, Integer> pillMap = createMap(message);
		double snoozes =  pillMap.get("snoozes");
		int index =  pillMap.get("cellIndex");
		map.get(index + "").put("snoozes", snoozes);
	}
	
	/*
	 * Called when user presses snooze in alert notification
	 * sets snooze to snooze - 1 and sends MQTT message of snooze
	 * 
	 * @param key Index of item in dictionary
	 */
	public void snooze() {
		
		double s = (Double) map.get(getKey()+"").get("snoozes");
		if(s < 4 ){
			double snooze = s + 1;
			Map<String, Integer> myMap = new HashMap<String, Integer>();
			myMap.put("cellIndex", Integer.parseInt(getKey()+""));
			myMap.put("snoozes", (int) snooze);
			try {
				m.sendMessage("snooze", myMap.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * Parses json map
	 * 
	 * @param message Json message to be parsed 
	 * 
	 * @return myMap the parsed map
	 */
	public Map<String, Integer> createMap(String message) {
		Map<String, Integer> myMap = new HashMap<String, Integer>();
		Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Integer>>(){}.getType();
        myMap = gson.fromJson(message, type); 

        return myMap;
	}
	
	public void setKey(int theKey){
		this.theKey = theKey;
	}

	public int getKey(){
		return theKey;
	}

	/*
	 * alertPill checks what time of day a pill is due
	 * 
	 * @param pill Key of pill in dictionary 
	 * 
	 * @return time Time of pill due
	 */
	public String alertPill(int pill) {
		int pillTime = pill%4;
		String time = null;
		switch(pillTime){
		case 0: time = "Morning";
				break;
		case 1: time = "Afternoon";
				break;
		case 2: time = "Evening";
				break;
		case 3: time = "Night";
				break;
		}
		return time;		
	}

	/*
	 * Sets the time of the next pill. 
	 * Correctly commented out because the request/response for nextPill does not work
	 * 
	 * @param key Key of the next pill to be taken
	 * 
	 */
	public void setNextTime(String key) {
		/*double s =  (Double) map.get(key+"").get("time");
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(0);
		long time = Long.valueOf(df.format(s));
		Date date = new Date(time);
		SimpleDateFormat sd = new SimpleDateFormat("hh:mm");
		String t = sd.format(date);
		System.out.println(t + "&&&&%%%%%%%%%%");
		this.nextPillTime = t;*/
		this.nextPillTime = "12:00";
	}
	
	public String getNextTime(){
		return this.nextPillTime;
	}
	
}