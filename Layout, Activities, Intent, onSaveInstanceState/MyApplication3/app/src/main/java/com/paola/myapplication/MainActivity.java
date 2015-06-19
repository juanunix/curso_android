package com.paola.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

private String status_message;

private final static String INFO_TAG = "Ciclo de vida Activity";

private EditText user_email;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            status_message = "Estado: onCreate. La Activity se ha creado. ";
            Log.i(INFO_TAG, status_message);

            user_email = (EditText) findViewById(R.id.editText);

            /* EditText saves its state by default so it's not necessary to get it here again
            * Anyway we are doing it because it can be needed for the other TextViews that
            * loose their texts when change the orientation by default. So I would like you to see how
            * it's done (You will need to override onSaveInstanceState(Bundle savedInstanceState) to be
            * able to recover the key:value here. See it below.
            */

            if (savedInstanceState != null) {
                String email = (String) savedInstanceState.getString(Intent.EXTRA_TEXT);
                user_email.setText(email);
            }

        }


        @Override
        protected void onStart(){
            super.onStart();
            status_message = "Estado: onStart. La actividad está visible en la pantalla";
            Log.i(INFO_TAG,status_message);
        }

        @Override
        protected void onRestart(){
            super.onRestart();
            status_message = "Estado: onRestart. La actividad estaba invisible y se va a iniciar otra vez";
            Log.i(INFO_TAG,status_message);
        }


        @Override
        protected void onResume(){
            super.onResume();
            status_message = "Estado: onResume. El usuario puede interactuar con la actividad";
            Log.i(INFO_TAG,status_message);
        }

        @Override
        protected void onPause(){
            super.onPause();
            status_message = "Estado: onPause. El usuario no puede interactuar con la actividad ";
            Log.i(INFO_TAG,status_message);
        }

        @Override
        protected void onStop(){
            super.onStop();
            status_message = "Estado: onStop. Actividad invisible para el usuario";
            Log.i(INFO_TAG,status_message);
        }

        @Override
        protected void onDestroy(){
            super.onDestroy();
            status_message = "Estado: onDestroy. Salgo de la aplicación. ";
            Log.i(INFO_TAG,status_message);
        }


    public void onClickButton(View view){
        String extra = user_email.getText().toString();
        Intent intentNewActivity = new Intent(getApplicationContext(), MiPrimeraAplicacion.class)
                .putExtra(Intent.EXTRA_TEXT, extra);
        startActivity(intentNewActivity);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

        /*EditText saves its state by default so it's not necessary to save it again but it can be needed for other TextViews
        * that don't save their text by default so we do it in order you to see how to save something in the Bundle saveInstaceState
        * and recover it in onCreate().
        */

        if(user_email !=null)
            savedInstanceState.putString(Intent.EXTRA_TEXT, user_email.getText().toString());

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







