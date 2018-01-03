package com.example.alexandremguay.DDAfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.AbsListView.CHOICE_MODE_SINGLE;

/**
 * Created by AMG on 2018-01-02.
 */

public class HomeworkTracker extends AppCompatActivity {

    protected final static String ACTIVITY_NAME = "HomeworkTracker";
    protected AlertDialog.Builder builder;
    protected Locale locale = Locale.getDefault();

    protected int k;
    protected int checked;
    protected String weekday;
    protected Toolbar toolbar;
    protected TextView date;
    //change the variable name after
    protected ArrayList<String> mytasks;
    protected ProgressBar progressBar;
    protected FloatingActionButton fab;
    protected ListView tasksList;
    protected EditText addText;
    protected Button add;
    protected Button delete;
    protected Cursor z;
    protected CheckBox cb;
    private SQLiteDatabase dbase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_main);
        mytasks = new ArrayList<>();
        toolbar = findViewById(R.id.maintoolbar);
        addText = findViewById(R.id.addtext);
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        fab = findViewById(R.id.fab);
//        fab.setImageDrawable(ContextCompat.getDrawable(HomeworkTracker.this, R.drawable.capture));
        fab.setImageBitmap(textAsBitmap("Cheer Up!", 60, Color.BLACK));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("D.D.A");
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        final ListAdapter1 adapter1 = new ListAdapter1(this);
        tasksList = findViewById(R.id.list_tasks);
        tasksList.setAdapter(adapter1);
        tasksList.setChoiceMode(CHOICE_MODE_SINGLE);
        final ListDatabaseHelper dbHelper = new ListDatabaseHelper(this);
        dbase = dbHelper.getWritableDatabase();
        z = dbase.query(false, ListDatabaseHelper.c, new String[]{ListDatabaseHelper.m}, null, null, null, null, null, null);
        int columnIndex = z.getColumnIndex(ListDatabaseHelper.m);
        z.moveToFirst();
        while (!z.isAfterLast()) {
            String msg = z.getString(columnIndex);
            mytasks.add(msg);
            z.moveToNext();
        }

        getDate();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                checked = tasksList.getCheckedItemPosition();
            }
        });

        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addText.setText("");
                fab.hide();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addText.getText().length() == 0) {
                    snacker("Field Empty");
                } else {
                    String x = addText.getText().toString().trim();
                    mytasks.add(x);
                    Log.i(ACTIVITY_NAME, mytasks.toString());
                    ContentValues cValues = new ContentValues();
                    cValues.put(ListDatabaseHelper.m, x);
                    dbase.insert(ListDatabaseHelper.c, "NullColumnName", cValues);
                    adapter1.notifyDataSetChanged(); //this restarts the process of getCount()/ getView()
                    setOnAddedTask();
                    addText.setText("");
                    addText.clearFocus();
                    toolbar.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(tasksList.getWindowToken(), 0);
                    fab.show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//how to delete from database using listview item number!?!?
                dbase.delete("TASKS" ,"KEY_TASK=?", new String[]{adapter1.getItem(checked)});
                mytasks.remove(checked);

                Log.i(ACTIVITY_NAME, Integer.toString(checked));
                Log.i(ACTIVITY_NAME, mytasks.toString());

                setOnDeletedTask();
                adapter1.notifyDataSetChanged();

               //http://androidsbs.blogspot.ca/2013/06/remove-selected-item-from-listview.html
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheerOn(weekday);
            }
        });

        new ProgressUpdate().execute();
    }


    private class ListAdapter1 extends ArrayAdapter<String> {

        public ListAdapter1(Context ctx) {
            super(ctx, 0);
        }

        @Override //This returns the number of rows that will be in your listView
        public int getCount() {
            return mytasks.size();
        }

        @Override //This returns the item to show in the list at the specified position
        public String getItem(int position) {
            return mytasks.get(position);
        }

        @Override //This returns the layout that will be positioned at the specified row in the list
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View result;
            result = inflater.inflate(R.layout.tasks_listview, null);

            TextView name = result.findViewById(R.id.task);
            name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            name.setBackgroundColor(Color.parseColor("#F7DC6F"));
            name.setText(getItem(position)); // get the string at position

            return result;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {

        k = mi.getItemId();
        builder = new AlertDialog.Builder(this);

        switch (k) {
            case R.id.action_one:
                Intent intent = new Intent(HomeworkTracker.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.about:
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.about_dialog, null)).setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
                break;

            default:
                return super.onOptionsItemSelected(mi);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbase.close();
        z.close();
    }

    public void getDate() {
        Calendar c;
        String today;
        String fullDate;
        date = findViewById(R.id.todayDate);
        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        weekday = new DateFormatSymbols().getWeekdays()[dayOfWeek];
        today = day + "/" + month + "/" + year;
        fullDate = weekday + " - " + today;
        date.setText(fullDate);
    }

    protected void setOnAddedTask() {
// need to check database if add occured!!
        CharSequence text;
        int duration;

        if (locale.getDisplayLanguage().equals("français")) {
            text = "Tâche AJOUTÉE!";
        } else {
            text = "Task ADDED Successfully";
        }

        duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

    }

    protected void setOnDeletedTask() {
// need to check database if delete occured!!
        CharSequence text;
        int duration;

        if (locale.getDisplayLanguage().equals("français")) {
            text = "Tâche SUPPRIMÉE!";
        } else {
            text = "Task DELETED Successfully";
        }
        duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

    }

    private class ProgressUpdate extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            pbUpdate(weekday);
            return "";
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        protected void pbUpdate(String w) {
            switch (w) {
                case "Monday":
                    publishProgress(15);
                    break;

                case "Tuesday":
                    publishProgress(30);
                    break;

                case "Wednesday":
                    publishProgress(45);
                    break;

                case "Thursday":
                    publishProgress(60);
                    break;

                case "Friday":
                    publishProgress(75);
                    break;

                case "Saturday":
                    publishProgress(90);
                    break;

                default:
                    publishProgress(105);
                    break;
            }

        }
    }

    public void cheerOn(String w) {

        switch (w) {
            case ("Monday"):
                snacker("Start of the week... lots to do!");
                break;

            case "Tuesday":
                snacker("Just getting started... don't despair!");
                break;

            case "Wednesday":
                snacker("HumpDay... halfway there!");
                break;

            case "Thursday":
                snacker("Keep up the grind, weekend is coming!");
                break;

            case "Friday":
                snacker("Last day of the week... Good job!");
                break;

            case "Saturday":
                snacker("Saturday woohoo... wrap everything up!");
                break;

            case "Sunday":
                snacker("Sunday... take a day off, you've earned it!!!!");
                break;

            case ("lundi"):
                snacker("Début de semaine, il reste beaucoup de travaux!");
                break;

            case "mardi":
                snacker("On commence bien... ne lâche pas!");
                break;

            case "mercredi":
                snacker("Moitié chemin, continue fort!");
                break;

            case "jeudi":
                snacker("Bientôt le weekend!");
                break;

            case "vendredi":
                snacker("Dernière journée de la semaine, beau travail!");
                break;

            case "samedi":
                snacker("Samedi... woohoo, on finit tout!");
                break;

            case "dimanche":
                snacker("Dimanche... prends la journée de congé!!!!");
                break;

            default:
                snacker("???");
                break;
        }
    }

    public void snacker(String msgs) {
        Snackbar snack = Snackbar.make(this.findViewById(R.id.fab), msgs, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(HomeworkTracker.this, R.color.white));
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextSize(26);
        tv.setTypeface(null, Typeface.ITALIC);
        snack.show();
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    //https://stackoverflow.com/questions/33671196/floatingactionbutton-with-text-instead-of-image

}
