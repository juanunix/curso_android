package com.paola.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MiPrimeraAplicacion extends AppCompatActivity {
private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_relative_layout);
        textViewEmail = (TextView) findViewById(R.id.textView2);

      //  if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                textViewEmail.setText(extras.getString(Intent.EXTRA_TEXT));
            }
    /*    } else {
            textViewEmail.setText((String) savedInstanceState.getSerializable(Intent.EXTRA_TEXT));
        }*/
    }

    public void onClickButton(View view){
        switch(view.getId()) {

            case R.id.my_button:
                Toast.makeText(view.getContext(), R.string.toast_paola, Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageButton1:
                Toast.makeText(view.getContext(), R.string.toast_imgBut1, Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageButton2:
                Toast.makeText(view.getContext(), R.string.toast_imgBut2, Toast.LENGTH_SHORT).show();
                break;

            case R.id.imageButton3:
                Toast.makeText(view.getContext(), R.string.toast_imgBut3, Toast.LENGTH_SHORT).show();
                break;

        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      //if(textViewEmail !=null)
   //     savedInstanceState.putString(Intent.EXTRA_TEXT, textViewEmail.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
