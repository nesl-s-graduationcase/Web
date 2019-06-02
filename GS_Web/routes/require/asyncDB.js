'use strict';

//引用mysql模組
var mysql = require('mysql');
var imgur = require('imgur-upload');
var config = require('./db_pwd');

//建立資料庫連接池
var pool  = mysql.createPool(
    config
);

//產生可同步執行query物件的函式
var query =function (sql, value) {
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

var f = function (s) {
    imgur.setClientID(config.imgurID);
    imgur.upload(s, function (err, res) {
        console.log(res.data.link); //log the imgur url
        return res.data.link
    });
}

//匯出
module.exports = {
    query:query,
    upload_img: f
};