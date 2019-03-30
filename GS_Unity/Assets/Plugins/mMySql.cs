// mysql
using UnityEngine;
using System;
using System.Collections;
using System.Data;
using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.Text;

public class mMySql
{

    public static MySqlConnection dbConnection;//Just like MyConn.conn in StoryTools before 
    public mMySql(){}
    public mMySql(MySqlConnection c){
        dbConnection=c;
    }
    public static string db;
    public string getDBName() {return db;}
    public void setDBName(string s) {db=s;}

    // MySQL Query  
    public IDataReader doQuery(string sqlQuery)
    {
        IDbCommand dbCommand = dbConnection.CreateCommand();
        dbCommand.CommandText = sqlQuery;
        IDataReader dr = dbCommand.ExecuteReader();
        dbCommand.Dispose();
        dbCommand = null;

        return dr;
    }

    // 寫入資料表
    public void insertInto(string table, string[] data)
    {
        StringBuilder query = new StringBuilder();
        // IDataReader dr=doQuery("select COLUMN_name from INFORMATION_SCHEMA.COLUMNS where table_schema='"+db+"' and table_name='"+table+"'");
        // List<string> arr=new List<string>();
        // while(dr.Read()){
        //     arr.Add(dr.GetString(0));
        // }
        
        // query.Append(string.Format("Insert into {0}(", table));
        // for (int i = 1; i < arr.Count; i++)
        // {
        //     query.Append(", " + arr[i]);
        // }
        // query.Append(string.Format(") value ({0}", data[0]));
        // for (int i = 1; i < data.Length; i++)
        // {
        //     query.Append(", " + data[i]);
        // }
        query.Append(string.Format("Insert into {0} value (null,{1}", table, data[0]));
        for (int i = 1; i < data.Length; i++)
        {
            query.Append(", " + data[i]);
        }
        query.Append("');");
        Debug.Log(query);
        doQuery(query.ToString()).Close();
        Debug.Log("載入資料成功");
    }
    public void insertBigData(string table, List<string[]> datalist)
    {
        StringBuilder query = new StringBuilder();
        // query.Append(string.Format("Insert into {0} value (null,'{1}", table, data[0][0]));
        query.Append(string.Format("Insert into {0} value (null", table));
        for(int i=0;i<datalist.Count-1;i++){
            foreach (string s in datalist[i]){
                query.Append(","+s);
            }
            if (i==datalist.Count-2)query.Append(");");
            else query.Append("),(null");
        }
        Debug.Log(query);
        doQuery(query.ToString()).Close();
        Debug.Log("載入資料成功");
    }

    public void update(string table,string[] column, string[] data,string where_s)
    {
        StringBuilder query = new StringBuilder();
        query.Append(string.Format("UPDATE {0} SET {1}={2}",table,column[0],data[0]));
        for (int i = 1; i < data.Length; i++)
        {
            query.Append(string.Format(", {0}={1}",column[i],data[i]));
        }
        query.Append(" "+where_s+";");
        doQuery(query.ToString());
        Debug.Log(query);
        // doQuery(query.ToString()).Close();
        // Debug.Log("載入資料成功");
    }

    public void createTable(string tableName, string[] column, string[] columnType)
	{
		if (column.Length != columnType.Length)
		{
			throw new Exception("column.Length != columnType.Length");
		}

		StringBuilder query = new StringBuilder();
		query.Append("CREATE TABLE " + tableName + " ( id int(10) not null AUTO_INCREMENT PRIMARY KEY," + column[0] + " " + columnType[0]);

		for (int i = 1; i < column.Length; i++)
		{
			query.Append(", " + column[i] + " " + columnType[i]);
		}
		query.Append(")");

		doQuery(query.ToString()).Close();
	}
    public void deleteTable(string tableName)
	{
		doQuery("DROP TABLE "+db+"."+tableName+";").Close();
	}

    public bool isTableExists(string tableName)
    {
        IDataReader dr = doQuery("SELECT COUNT(*) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"+db+"' and TABLE_NAME='"+tableName+"';");
        if (dr.Read()&&dr.GetInt32(0) == 1)
        {
            dr.Close();
            return true;
        }
        else
        {
            dr.Close();
            return false;
        }
    }


}
