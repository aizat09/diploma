package com.example.aizat.smartcar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener {
    public static final String TAG = "MainActivity";
    private SectionsPageAdapter sectionsPageAdapter;

    private ViewPager viewPager;

    private static final int REQUEST_RECOGNITION = 1;

    private FloatingActionButton startRecognizer;
    private TextToSpeech tts;

    private ListenerClass listenerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: Starting");

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        startRecognizer = (FloatingActionButton) findViewById(R.id.fab);
//        startRecognizer.setEnabled(false);
        tts = new TextToSpeech(this, this);

        listenerClass = new ListenerClass();
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConditionerFragment(),"Conditioner");
        adapter.addFragment(new LightsFragment(),"Lights");
        adapter.addFragment(new OpenFragment(),"Open");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bt_settings) {
            Intent intent = new Intent(this, BluetoothActivity.class);
            //startActivityForResult(intent,REQUEST_CODE_BLUETOOTH);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onInit(int arg0) {
//        startRecognizer.setEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if ((requestCode == REQUEST_RECOGNITION) & (resultCode == RESULT_OK)) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            analyzeSpeech(adapter);
        }

    }
    public void analyzeSpeech(ArrayAdapter<?> adapter){
        int count = adapter.getCount();
        try {
            Activities activity = null;
            Buttons key = null;
            String keyString = null,activityString = null,strOut = "";
            boolean activityFound = false, keyFound = false;
            for (int i = 0; i < count; i++) {
                String sr[] = adapter.getItem(i).toString().split(" ");
                for(int k = 0;k < sr.length;k++){
                    String word = sr[k];
                    if(listenerClass.getKeyMap().containsKey(word)){
                        key = listenerClass.getKeyMap().get(word);
                        keyString = word;
                        keyFound = true;
                    }
                    if(listenerClass.getActivityMap().containsKey(word)){
                        activity = listenerClass.getActivityMap().get(word);
                        activityString = word;
                        activityFound = true;
                    }
                }
            }
            if(!keyFound || !activityFound) {
                msg("Не распознано");
                return;
            }
            msg("Выполняется: "+activityString+" " +keyString);
            listenerClass.listener(activity,key);
            ToggleButton tgl = (ToggleButton) findViewById(listenerClass.getButtonMap().get(key));
            Log.d("Button","tgl: " + (tgl == null) + " | id: " + listenerClass.getButtonMap().get(key));
            if(tgl != null){
                if(activity == Activities.OPEN || activity == Activities.TURN_ON){
                    tgl.setChecked(true);

                }
                if(activity == Activities.CLOSE || activity == Activities.TURN_OFF){
                    tgl.setChecked(false);
                }
            }
//                Toast.makeText(getApplicationContext(),"Выполняется: "+activityString+" " +keyString,Toast.LENGTH_SHORT);


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void speechButton(View v){
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Recognize");
//        startActivityForResult(intent, REQUEST_RECOGNITION);
        try{
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
            startActivityForResult(intent, REQUEST_RECOGNITION);
        }
        catch(ActivityNotFoundException e)
        {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW,   Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
//            startActivity(browserIntent);
            msg("Your phone does not have voice recognition library.");

        }
    }
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


    public class ListenerClass {

        private Map<Buttons,String> onMap;
        private Map<String,Activities> activityMap;
        private Map<String,Buttons> keyMap;
        public Map<Buttons,Integer> buttonMap;
        public ListenerClass(){
            onMap = new HashMap<>();
            activityMap = new HashMap<>();
            keyMap = new HashMap<>();
            initMaps();
            initButtonMap();
        }
        private void initMaps(){

            keyMap.put("кондиционер",Buttons.CONDITIONER);
            keyMap.put("фары",Buttons.HEAD_LAMPS);
            keyMap.put("противотуманки",Buttons.FOG_LAMP);
            keyMap.put("левый",Buttons.LEFT_LAMPS);
            keyMap.put("правый",Buttons.RIGHT_LAMPS);
            keyMap.put("аварийки",Buttons.EMERGENCY);
            keyMap.put("салон",Buttons.SALON);

            activityMap.put("включить",Activities.TURN_ON);
            activityMap.put("выключить",Activities.TURN_OFF);
            activityMap.put("открыть",Activities.OPEN);
            activityMap.put("закрыть",Activities.CLOSE);

            onMap.put(Buttons.CONDITIONER,"C");
            onMap.put(Buttons.HEAD_LAMPS,"H");
            onMap.put(Buttons.FOG_LAMP,"G");
            onMap.put(Buttons.LEFT_LAMPS,"T");
            onMap.put(Buttons.RIGHT_LAMPS,"R");
            onMap.put(Buttons.EMERGENCY,"P");
            onMap.put(Buttons.SALON,"S");

        }
        private void initButtonMap(){
            buttonMap = new HashMap<>();
            buttonMap.put(Buttons.CONDITIONER,R.id.condStateToggle);
            buttonMap.put(Buttons.HEAD_LAMPS,R.id.headLampStateToggle);
            buttonMap.put(Buttons.FOG_LAMP,R.id.fogLampsStateToggle);
            buttonMap.put(Buttons.LEFT_LAMPS,R.id.leftLampStateToggle);
            buttonMap.put(Buttons.RIGHT_LAMPS,R.id.rightLampStateToggle);
            buttonMap.put(Buttons.EMERGENCY,R.id.stopLampStateToggle);
            buttonMap.put(Buttons.SALON,R.id.salonLightToggle);

        }
        public void listener(Activities activity, Buttons key)
                throws Exception{
            String sendCode = onMap.get(key);

            if(activity == Activities.OPEN || activity == Activities.TURN_ON){
                sendCode = sendCode.toUpperCase();

            }
            if(activity == Activities.CLOSE || activity == Activities.TURN_OFF){
                sendCode = sendCode.toLowerCase();
            }
            BluetoothActivity.outputStream.write(sendCode.getBytes());

        }

        public Map<String, Activities> getActivityMap() {
            return activityMap;
        }

        public Map<String, Buttons> getKeyMap() {
            return keyMap;
        }

        public Map<Buttons, Integer> getButtonMap() {
            return buttonMap;
        }
    }
    enum Buttons{
        CONDITIONER,HEAD_LAMPS,FOG_LAMP,LEFT_LAMPS,RIGHT_LAMPS,EMERGENCY,SALON;
    }

    enum Activities{
        TURN_ON,TURN_OFF,OPEN,CLOSE
    }

}
