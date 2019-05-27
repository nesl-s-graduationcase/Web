'use strict';

//引用mysql模組
var mysql = require('mysql');

//建立資料庫連接池
var pool  = mysql.createPool({
    user: 'remote',
    password: 'mysql123',
    host: '140.131.114.143',
    database: 'unity3.0'     
});

//產生可同步執行query物件的函式
function query(sql, value) {
    return new Promise((resolve, reject) => {
        pool.query(sql, value, function (error, results, fields) {
            if (error){
                reject(error);
            }else{
                resolve(results);
            }
        });
    });
}

//匯出
module.exports = query;