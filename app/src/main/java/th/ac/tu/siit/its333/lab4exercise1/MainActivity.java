package th.ac.tu.siit.its333.lab4exercise1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    CourseDBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new CourseDBHelper(this);
        db = helper.getReadableDatabase();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // This method is called when this activity is put foreground.


        Cursor c = db.rawQuery("SELECT SUM(credit) AS TotalCredit, SUM(credit*value) AS TGP FROM course", null);
        c.moveToFirst();

        int totalCredit = c.getInt(c.getColumnIndex("TotalCredit"));
        double TGP = c.getDouble(c.getColumnIndex("TGP"));
        double GPA = 0;

        if(totalCredit !=0 ){
            GPA = TGP / totalCredit;
        }

        String totalCredit_s = Integer.toString(totalCredit);
        String TGP_s = String.format("%.2f",TGP);
        String GPA_s = String.format("%.2f",GPA);

        TextView tvGP = (TextView)findViewById(R.id.tvGP);
        TextView tvCR = (TextView)findViewById(R.id.tvCR);
        TextView tvGPA = (TextView)findViewById(R.id.tvGPA);

        tvGP.setText(TGP_s);
        tvCR.setText(totalCredit_s);
        tvGPA.setText(GPA_s);


    }

    public void buttonClicked(View v) {
        int id = v.getId();
        Intent i;

        switch(id) {
            case R.id.btAdd:
                i = new Intent(this, AddCourseActivity.class);
                startActivityForResult(i, 88);
                break;

            case R.id.btShow:
                i = new Intent(this, ListCourseActivity.class);
                startActivity(i);
                break;

            case R.id.btReset:

                db.delete("course","",null);
                onResume();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 88) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("code");
                int credit = data.getIntExtra("credit", 0);
                String grade = data.getStringExtra("grade");

                helper = new CourseDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues r = new ContentValues();

                r.put("code",code);
                r.put("credit",credit);
                r.put("grade",grade);
                r.put("value",gradeToValue(grade));

                long new_id= db.insert("course",null,r);

                if(new_id == -1){
                    Log.d("Insert Status","Fail");
                }else{
                    Log.d("Insert Status","Success");
                }


                db.close();
            }
        }

        Log.d("course", "onActivityResult");
    }

    double gradeToValue(String g) {
        if (g.equals("A"))
            return 4.0;
        else if (g.equals("B+"))
            return 3.5;
        else if (g.equals("B"))
            return 3.0;
        else if (g.equals("C+"))
            return 2.5;
        else if (g.equals("C"))
            return 2.0;
        else if (g.equals("D+"))
            return 1.5;
        else if (g.equals("D"))
            return 1.0;
        else
            return 0.0;
    }

    void calculateGPA(){





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
