package com.example.nesl.unitygame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class VoteActivity extends AppCompatActivity {
    private String topic;
    private ArrayList<String> topics = new ArrayList<String>();
    private ArrayList<ArrayList> voteItems = new ArrayList<ArrayList>();
    //    private ArrayList<Integer> votes = new ArrayList<Integer>();
    private ArrayList<Integer> chooesPositions = new ArrayList<Integer>();
    private String[] spns=new String[]{"One+","One-","Two+","Two-","Three+","Three-","Four+","Four-"};
    private String[] spns2=new String[]{"voteTop","voteLast"};
    private int position,spn_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        final TextView txtTopic = findViewById(R.id.txtTopic);
        final ListView lst = findViewById(R.id.lst);
        final Spinner spn=findViewById(R.id.spn);
        final Spinner spn2=findViewById(R.id.spn2);
        final Intent itIn = getIntent();
        topics = itIn.getStringArrayListExtra("topics");
        position = itIn.getIntExtra("position", 0);

        try{
            if(topics.size() != 0){
                Log.e("vote","topics.size() != 0");
            }
        }catch(Exception e) {
            //
            //todo: no data,creat to topics
            //
            topics=new ArrayList<String>();
            position = 0;
            SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS voteItems (_id integer primary key ,topic text,voteItem text,flagOne integer,flagTwo integer,flagThree integer,flagFour integer,vote integer DEFAULT 0);");
            String queryTopicSql = "select topic from voteItems group by topic";
            Log.e("vote","topics.size() == 0");
            if (db.rawQuery(queryTopicSql, null).getCount() == 0) {
                Log.e("vote", "db中無topic資料，正在建立...");
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        ContentValues cv = new ContentValues();
//                        cv.put("topic", "topicTest" + i);
//                        cv.put("voteItem", "topicTest"+i+"-voteItem" + j);
//                        cv.put("flagOne", (int)(2-Math.random() * 5)+"");
//                        cv.put("flagTwo", (int)(2-Math.random() * 5)+"");
//                        cv.put("flagThree",(int)(2-Math.random() * 5)+"");
//                        cv.put("flagFour", (int)(2-Math.random() * 5)+"");
//                        db.insert("voteItems", null, cv);
//                    }
//                }
                for (int i = 0; i < 3; i++) {
                    for (int a = -2; a <= 2; a++) {
                        for (int b = -2; b <= 2; b++) {
                            for (int c = -2; c <= 2; c++) {
                                for (int d = -2; d <= 2; d++) {
                                    int ee = 0, f = 0;
                                    if (a > 0) {
                                        ee += a;
                                    }
                                    if (b > 0) {
                                        ee += b;
                                    }
                                    if (c > 0) {
                                        ee += c;
                                    }
                                    if (d > 0) {
                                        ee += d;
                                    }
                                    if (a < 0) {
                                        f += a;
                                    }
                                    if (b < 0) {
                                        f += b;
                                    }
                                    if (c < 0) {
                                        f += c;
                                    }
                                    if (d < 0) {
                                        f += d;
                                    }
                                    if (f != 0 && ((double) ee / f == 0.5 || (double) ee / f == -0.5)) {
                                        System.out.print(a + " " + b + " " + c + " " + d);
                                        System.out.println("");
                                        ContentValues cv = new ContentValues();
                                        cv.put("topic", "topicTest" + i);
                                        cv.put("voteItem", "topicTest" + i + ",One:" + a+ ",Two:"+b+ ",Three:"+c+ ",Four:"+d);
                                        cv.put("flagOne", a + "");
                                        cv.put("flagTwo", b + "");
                                        cv.put("flagThree", c + "");
                                        cv.put("flagFour", d + "");
                                        cv.put("vote", (int)(Math.random() * 1000));
                                        db.insert("voteItems", null, cv);
                                        Log.e("insert","topicTest" + i +":"+a + " " + b + " " + c + " " + d);
                                    }
                                }
                            }

                        }

                    }
                }
            }
            Cursor csr = db.rawQuery(queryTopicSql, null);
            while (csr.moveToNext()) {
                topics.add(csr.getString(0));
            }
            db.close();
        }

        topic = topics.get(position);
        txtTopic.setText(topic);

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            EditText editText = findViewById(R.id.editText);
            EditText editFlag = findViewById(R.id.editFlag);
            EditText editFlag2 = findViewById(R.id.editFlag2);
            EditText editFlag3 = findViewById(R.id.editFlag3);
            EditText editFlag4 = findViewById(R.id.editFlag4);
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
                ContentValues cv = new ContentValues();
                cv.put("topic", topic);
                cv.put("voteItem", editText.getText().toString());
                cv.put("flagOne", editFlag.getText().toString());
                cv.put("flagTwo", editFlag2.getText().toString());
                cv.put("flagThree", editFlag3.getText().toString());
                cv.put("flagFour", editFlag4.getText().toString());
                db.insert("voteItems", null, cv);
                db.close();
                recreate();
            }
        });
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spns);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(arrayAdapter);
        spn.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spn_i=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spns2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(arrayAdapter2);
        spn2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //
                //todo: add to voteItems from db
                //
                SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
                String s="";
                String s2="";
                switch (spn_i){
                    case 0 :s="flagOne>0";break;
                    case 1 :s="flagOne<0";break;
                    case 2 :s="flagTwo>0";break;
                    case 3 :s="flagTwo<0";break;
                    case 4 :s="flagThree>0";break;
                    case 5 :s="flagThree<0";break;
                    case 6 :s="flagFour>0";break;
                    case 7 :s="flagFour<0";break;
                }
                switch (i){
                    case 0 :s2="order by vote desc";break;
                    case 1 :s2="order by vote";break;
                }
                Log.e("vote,spnType",s);
                Cursor csr= db.rawQuery("select * from voteItems where topic='"+topic+"' and "+s+" "+s2, null);
                if (csr.getCount() == 0) {
                    Toast.makeText(VoteActivity.this, "vote,null voteItems,please create", Toast.LENGTH_LONG).show();
                } else {
                    voteItems.clear();
                    while (csr.moveToNext()) {
                        ArrayList a=new ArrayList();
                        a.add(csr.getString(2));
                        a.add((csr.getInt(csr.getColumnIndex("vote"))));
                        voteItems.add(a);
                    }
                }
                db.close();
                lst.setAdapter(new VoteActivity.MyAdapter());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
                for(int i:chooesPositions){
                    int vote=(int)(voteItems.get(i).get(1))+1;
                    db.execSQL("UPDATE voteItems SET vote = "+vote+" WHERE topic = '"+topic+"' and voteItem='"+voteItems.get(i).get(0)+"';");
                }
                Toast.makeText(VoteActivity.this,
                        "UPDATE OK", Toast.LENGTH_LONG).show();
                Intent inOut = new Intent(VoteActivity.this, VoteActivity.class);
                if (position == topics.size() - 1) {
                    position = 0;
                } else {
                    position = position + 1;
                }
                inOut.putExtra("topic", topics.get(position))
                        .putExtra("topics", topics)
                        .putExtra("position", position);
                finish();
                startActivity(inOut);

            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return voteItems.size();
        }

        @Override
        public Object getItem(int i) {
            return voteItems.get(i).get(0);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View v, ViewGroup vg) {
            v= LayoutInflater.from(VoteActivity.this).inflate(R.layout.vote_item,null);
//            ImageView img = v.findViewById(R.id.img);
//            img.setImageBitmap(imgs.get(i));

            TextView txt=v.findViewById(R.id.txt);
            TextView txtSub=v.findViewById(R.id.txtSub);
            final CheckBox chk=v.findViewById(R.id.chk);

            txt.setText(voteItems.get(i).get(0).toString());
            txtSub.setText("投票數:"+voteItems.get(i).get(1));

            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    if(chk.isChecked()){
                        chooesPositions.add(i);
                    }
                }});

            return v;


        }
    }
}