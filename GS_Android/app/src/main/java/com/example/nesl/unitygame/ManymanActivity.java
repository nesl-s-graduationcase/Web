package com.example.nesl.unitygame;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ManymanActivity extends AppCompatActivity {
    private int position = 0;
    private ArrayList<String> topics = new ArrayList<String>();
    private ArrayList<Integer> affectIndices =new ArrayList<Integer>();
    private ArrayList<String> voteItems = new ArrayList<String>();
    private String topic;
    private int [] imgIds={R.drawable.a,R.drawable.b,R.drawable.c,
            R.drawable.d,R.drawable.e,R.drawable.f,
            R.drawable.g,R.drawable.h,R.drawable.i,
            R.drawable.j,R.drawable.k,R.drawable.l};
    private TextView txtTopic;
    private ListView lst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manyman);
        txtTopic=findViewById(R.id.txtTopic);
        lst=findViewById(R.id.lst);
        final Intent itIn=getIntent();
        position=itIn.getIntExtra("position",0);
        topics=itIn.getStringArrayListExtra("topics");
        affectIndices=itIn.getIntegerArrayListExtra("affectIndices");

        final GridView gridCard=findViewById(R.id.gridCard);
        final GridView gridCard2=findViewById(R.id.gridCard2);
        gridCard.setAdapter(new ManymanActivity.MyAdapter2());
        gridCard2.setAdapter(new ManymanActivity.MyAdapter2());

        if(topics!= null){
            Log.e("manyone","topics != null");
        }else{
            position=0;
            creatTopics();
        }

        if(creatVoteItems())creatInterface();

        findViewById(R.id.btnQ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ManymanActivity.this)
                        .setTitle("隨機任務")
                        .setMessage("1.對手的One指標維持在5\n獎勵:成就-環保戰士")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                        .show();
            }
        });

    }
    private boolean creatVoteItems(){
        voteItems.clear();
        try {
            SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            Cursor csr= db.rawQuery("select * from voteItems where topic ='" + topic+"' ", null);
            while (csr.moveToNext()) {
                voteItems.add(csr.getString(csr.getColumnIndex("voteItem")));
            }
            db.close();
        }catch (SQLiteException e){
            Toast.makeText(ManymanActivity.this,"先開vote寫DB",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void creatTopics(){
        topics=new ArrayList<String>();
        try {
            SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            Cursor csr = db.rawQuery("select topic from voteItems group by topic", null);
            while (csr.moveToNext()) {
                topics.add(csr.getString(0));
            }
            db.close();
            topic=topics.get(position);
        }catch (SQLiteException e){
            Toast.makeText(ManymanActivity.this,"先開vote寫DB",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void creatAffectIndices(String voteItem) {
        try {
            SQLiteDatabase db = openOrCreateDatabase("my.db", MODE_PRIVATE, null);
            Cursor csr= db.rawQuery("select flagOne,flagTwo,flagThree,flagFour from voteItems where topic ='"+topic+"' and voteItem='"+voteItem+"'", null);
            while (csr.moveToNext()) {
                affectIndices=new ArrayList<Integer>();
                affectIndices.add(csr.getInt(0));
                affectIndices.add(csr.getInt(1));
                affectIndices.add(csr.getInt(2));
                affectIndices.add(csr.getInt(3));
            }
            db.close();
            topic=topics.get(position);
        }catch (SQLiteException e){
            Toast.makeText(ManymanActivity.this,"先開vote寫DB",Toast.LENGTH_LONG).show();
            finish();
        }


    }

    public void creatInterface() {
        txtTopic.setText(topics.get(position));
        lst.setAdapter(new ManymanActivity.MyAdapter());
        if (affectIndices != null){
            TextView txtOtherFlagOne = findViewById(R.id.txtOtherFlagOne);
            TextView txtOtherFlagTwo = findViewById(R.id.txtOtherFlagTwo);
            TextView txtOtherFlagThree = findViewById(R.id.txtOtherFlagThree);
            TextView txtOtherFlagFour = findViewById(R.id.txtOtherFlagFour);
            TextView txtMyFlagOne = findViewById(R.id.txtMyFlagOne);
            TextView txtMyFlagTwo = findViewById(R.id.txtMyFlagTwo);
            TextView txtMyFlagThree = findViewById(R.id.txtMyFlagThree);
            TextView txtMyFlagFour = findViewById(R.id.txtMyFlagFour);
            txtOtherFlagOne.setText((Integer.parseInt(txtOtherFlagOne.getText().toString()) + affectIndices.get(0)) + "");
            txtOtherFlagTwo.setText((Integer.parseInt(txtOtherFlagTwo.getText().toString()) + affectIndices.get(1)) + "");
            txtOtherFlagThree.setText((Integer.parseInt(txtOtherFlagThree.getText().toString()) + affectIndices.get(2)) + "");
            txtOtherFlagFour.setText((Integer.parseInt(txtOtherFlagFour.getText().toString()) + affectIndices.get(3)) + "");
            txtMyFlagOne.setText((Integer.parseInt(txtMyFlagOne.getText().toString()) + affectIndices.get(0)) + "");
            txtMyFlagTwo.setText((Integer.parseInt(txtMyFlagTwo.getText().toString()) + affectIndices.get(1)) + "");
            txtMyFlagThree.setText((Integer.parseInt(txtMyFlagThree.getText().toString()) + affectIndices.get(2)) + "");
            txtMyFlagFour.setText((Integer.parseInt(txtMyFlagFour.getText().toString()) + affectIndices.get(3)) + "");
            affectIndices.clear();
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return voteItems.size();
        }

        @Override
        public Object getItem(int i) {
            return voteItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View v, ViewGroup vg) {
            v= LayoutInflater.from(ManymanActivity.this).inflate(R.layout.manyman_item,null);
            Button btn=v.findViewById(R.id.btn_manyone);
            final String voteItem=voteItems.get(i);
            btn.setText(voteItem);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    creatAffectIndices(voteItem);
//                    Intent inOut = new Intent(ManymanActivity.this, ManymanActivity.class);
                    if (position == topics.size() - 1) {
                        position = 0;
                    } else {
                        position = position + 1;
                    }
                    topic=topics.get(position);
//                    inOut.putExtra("topics", topics)
//                            .putExtra("position", position)
//                            .putExtra("affectIndices", affectIndices);
//                    finish();
//                    startActivity(inOut);
                    creatInterface();
                    creatVoteItems();
                }
            });
            return v;
        }
    }
    class MyAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return imgIds.length;
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView iv = new ImageView(ManymanActivity.this);
            iv.setImageResource(imgIds[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setLayoutParams(new GridView.LayoutParams(150, 150));
            return iv;
        }
    }

}