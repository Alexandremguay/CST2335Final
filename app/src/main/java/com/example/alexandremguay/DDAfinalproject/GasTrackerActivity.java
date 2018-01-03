package com.example.alexandremguay.DDAfinalproject;

/**
 * Created by Danny on 2018-01-02.
 */

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.alexandremguay.DDAfinalproject.NoteHelper.KEY_ID;

public class GasTrackerActivity extends AppCompatActivity {

    ListView list;
    EditText enterGas;
    Button gasButton;
    Button deleteButton;
    ArrayList<String> gasPrices = new ArrayList();
    ChatAdapter chatAdapter;
    NoteHelper dataBase;
    AlertDialog.Builder builder;
    String id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chatAdapter = new ChatAdapter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_tracker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D.D.A");

        enterGas = findViewById(R.id.myEditText);
        list = findViewById(R.id.list);
        gasButton = findViewById(R.id.btnSimple);
        deleteButton = findViewById(R.id.btnDelete);

        dataBase = new NoteHelper(this);
        final SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();

        Cursor c = sqLiteDatabase.query(false, NoteHelper.TABLE_NAME, new String[]{KEY_ID, NoteHelper.KEY_MESSAGE}, null, null, null, null, null, null);
        c.moveToFirst();

        int numRows = c.getCount();
        int MI = c.getColumnIndex(NoteHelper.KEY_MESSAGE);


        for (int i = 0; i < numRows; i++) {

            gasPrices.add(c.getString(MI));
            c.moveToNext();


        }
        c.moveToFirst();

        gasButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {


                ContentValues j = new ContentValues();
                j.put(NoteHelper.KEY_MESSAGE, enterGas.getText().toString());
                sqLiteDatabase.insert(NoteHelper.TABLE_NAME, "", j);

                gasPrices.add(enterGas.getText().toString());
                chatAdapter.notifyDataSetChanged();
                enterGas.setText("");
                Toast.makeText(getApplicationContext(), "Gas Price Added :)", Toast.LENGTH_SHORT).show();

            }});



        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(GasTrackerActivity.this);
                builder.setMessage(R.string.dialog_message);
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String[] args = {"1"};
                        sqLiteDatabase.delete("Notes2", "_id=?", args);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onResume();

                    }
                }).show();

            }});

        list.setAdapter(chatAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showHelp() {
        setContentView(R.layout.help_menu_layout);
    }


    public class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return gasPrices.size();
        }

        public String getItem(int position) {
            return gasPrices.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = GasTrackerActivity.this.getLayoutInflater();
            View result = null;

            result = inflater.inflate(R.layout.gas_prices_list, null);

            TextView text = result.findViewById(R.id.price_text);
            text.setText(getItem(position)); // get the string at position
            return result;
        }
    }

}