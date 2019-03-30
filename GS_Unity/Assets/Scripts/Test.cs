// mMySql
using UnityEngine;
using System;
using System.Collections;
using System.Data;
using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.Text;
using UnityEngine.UI;

public class Test : MonoBehaviour
{

    public Button btn1, btn2, btn3;
    public Text txtTopic;
    MySqlConnection dbConnection;
    mMySql mMySql;
    // db_pwd db_pwd=new db_pwd();
    static string dbName = db_pwd.getDatabase();
    static string player = "user_a";
    static string topic_id = "";
    List<List<string>> topicset = new List<List<string>>();
    List<List<string>> optionset = new List<List<string>>();
    void Start()
    {
        dbConnection = new MySqlConnection(string.Format("Server = {0}; database = {1}; User ID = {2}; Password = {3};", db_pwd.getServer(), dbName, db_pwd.getUserId(), db_pwd.getPwd()));
        mMySql = new mMySql(dbConnection);
        mMySql.setDBName(dbName);
        dbConnection.Open();
        //! check
        // mMySql.doQuery("call m_reset;");

        setTopics();
        btn1.onClick.AddListener(setTopic);
        btn1.onClick.AddListener(setOptions);
        // btn1.onClick.AddListener(() => votingOption(1));
    }
    void Update(){
    
    }
    void setTopics()
    {
        IDataReader reader = mMySql.doQuery("Select * from topic;");
        //`topic_id`,`topic`,`user_id`,`last_update`
        while (reader.Read())
        {
            List<string> l=new List<string>();
            l.Add(reader.GetString(0));
            l.Add(reader.GetString(1));
            topicset.Add(l);
        }
        reader.Close();
    }
    void setTopic()
    {
        if (topicset.Count > 0)
        {
            int i = new System.Random().Next(topicset.Count - 1);
            topic_id = topicset[i][0];
            txtTopic.text=topicset[i][1];
            topicset.RemoveAt(i);
        }
    }

    void setOptions()
    {
        optionset.Clear();
        IDataReader reader = mMySql.doQuery("select * from option_vote where topic_id="+topic_id+" group by flag1,flag2,flag3,flag4 order by rand() limit 3;");
        // `topic_id`,`topic`,`option_id`,`option`,`flag1`,`flag2`,`flag3`,`flag4`,`vote`
        while (reader.Read())
        {
            List<string> l=new List<string>();
            l.Add(reader.GetString(2));
            l.Add(reader.GetString(3));
            optionset.Add(l);
        }
        reader.Close();
        btn1.GetComponentInChildren<Text>().text = optionset[0][1];
        btn2.GetComponentInChildren<Text>().text = optionset[1][1];
        btn3.GetComponentInChildren<Text>().text = optionset[2][1];
        Debug.Log("option cnt:"+optionset.Count);
    }
    void votingOption(int choose)
    {
        mMySql.insertInto("user_option", new string[] { "'" + player + "'", "'" + topic_id + "'", "'" + optionset[choose][0] + "'", "now()", "now()" });
    }
    
    void OnDestroy()
    {
        dbConnection.Close();
    }

}