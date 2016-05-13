package com.whipped.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void whipSelected(View v) {
        String sound = (String) v.getTag();
        int image = 0;

        switch (v.getId()) {
            case R.id.whip_pic_1:
                image = R.drawable.whip_1;
                break;
            case R.id.whip_pic_2:
                image = R.drawable.whip_2;
                break;
            case R.id.whip_pic_3:
                image = R.drawable.whip_3;
                break;
            case R.id.whip_pic_4:
                image = R.drawable.whip_4;
                break;
            case R.id.whip_pic_5:
                image = R.drawable.whip_5;
                break;
            case R.id.whip_pic_6:
                image = R.drawable.whip_6;
                break;
        }

        Intent i = new Intent(this, WhipActivity.class);
        i.putExtra(WhipActivity.STRING_EXTRA_SOUND, sound);
        i.putExtra(WhipActivity.INT_EXTRA_IMAGE, image);
        startActivity(i);
    }
}
