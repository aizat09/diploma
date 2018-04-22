package com.example.aizat.smartcar;

import android.content.Intent;
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


public class MainActivity extends AppCompatActivity  implements TextToSpeech.OnInitListener {
    public static final String TAG = "MainActivity";
    private SectionsPageAdapter sectionsPageAdapter;

    private ViewPager viewPager;

    private static final int REQUEST_RECOGNITION = 1;

    private FloatingActionButton startRecognizer;
    private TextToSpeech tts;

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
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConditionerFragment(),"Conditioner");
        adapter.addFragment(new InnerLightFragment(),"Inner Lights");
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
        ToggleButton tb;
        boolean flag =false;
        try {
            for (int i = 0; i < count; i++) {
                String sr = adapter.getItem(i).toString();
                if (sr.equals("зелёный") || sr.equals("зеленый")) {
                    msg("Green");
                    flag = true;
                    break;
                }
                if (sr.equals("желтый") || sr.equals("жёлтый")) {

                    msg("yellow");
                    flag = true;
                    break;
                }
                if (sr.equals("красный")) {
                    msg("red");
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                msg("Не распознано");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void speechButton(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Recognize");
        startActivityForResult(intent, REQUEST_RECOGNITION);
    }
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
