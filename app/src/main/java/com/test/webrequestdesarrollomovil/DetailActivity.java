package com.test.webrequestdesarrollomovil;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;

import org.w3c.dom.Text;


public class DetailActivity extends ActionBarActivity {

    Button addcomment;
    DatabaseHelper db;
    EditText comment;
    TextView cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        addcomment = (Button) findViewById(R.id.addComent);
        comment = (EditText) findViewById(R.id.commenttoadd);
        cm = (TextView) findViewById(R.id.comment);

        db = new DatabaseHelper(getApplicationContext());


       // Bundle myExtras;
         String myItemId = "";

        Intent myExtras = getIntent();

       // Object myExtras = getIntent().getSerializableExtra("item_id");

        if (myExtras != null) {
            myItemId = myExtras.getStringExtra("item_id");

        }

      TextView tittle = (TextView) findViewById(R.id.tvTitule);

      tittle.setText(myItemId);
        final String item = myItemId;

        String[] aux = item.split("\n");


        cm.setText(db.getComment(aux[0],"comentarios"));


        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] aux2 = item.split("\n");


                db.insertTable("comentarios","idpeliculas",aux2[0],"comentario", comment.getText().toString());



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
