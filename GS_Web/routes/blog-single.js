var express = require('express');
var router = express.Router();
const query = require('./require/asyncDB');

var get_blog = async function (id) {
  var result = {};
  await query("select * from combination where combination_id=" + id + ";")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

var get_blog_tag = async function (id) {
  var result = {};
  await query("select * from (select * from combination_tag where combination_id="+id+") as f join tag on tag.tag_id=f.tag_id;")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

// -- 較糟:先將id與中文字抓出來，再計算
// -- SELECT *,count(*) as cnt FROM `tag` join combination_tag on combination_tag.tag_id=tag.tag_id where is_important=1 GROUP BY tag.tag_id

// -- 較好:先算id數量，在連起中文字
// select * from (select count(*) as cnt,combination_tag.tag_id from (SELECT tag_id FROM `tag` where is_important=1) as f join combination_tag on combination_tag.tag_id=f.tag_id GROUP BY f.tag_id ) as f JOIN tag on tag.tag_id=f.tag_id;
var get_impt_tag = async function (combination_id) {
  var result = {};
  await query("select * from (select count(*) as cnt,combination_tag.tag_id from (SELECT tag_id FROM `tag` where is_important=1) as f join combination_tag on combination_tag.tag_id=f.tag_id GROUP BY f.tag_id ) as f JOIN tag on tag.tag_id=f.tag_id;")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

// todo 推薦學習
var get_recommend_tag = async function (user_id) {
  var result = {};
  await query("SELECT * FROM `tag` ORDER BY RAND() LIMIT 20;")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

var user = async function (id) {
  var result = {};
  await query("SELECT * FROM `user` where user_id='"+id+"';")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

//用關鍵字查詢
var get_last_update_blogs = async function (s) {
  var result = {};
  await query("select * from combination where combination_title like '%"+s+"%' ORDER BY last_update DESC limit 3;")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

router.get('/', function (req, res, next) {
  var id = Number(req.param('id'));  //頁碼, 轉數字
  if (isNaN(id) || id < 1) {
    pageNo = 1;
  }
  get_blog(id).then(blog_data => {
    var b= blog_data.data[0]
    b.reply=JSON.parse(b.reply)
    blog_data.data[0]=b
    get_impt_tag(id).then(impt_tag_table => {
      get_recommend_tag().then(recommend_tag_table => {
        user(blog_data.data[0].user_id).then(user_set => {
          get_blog_tag(id).then(blog_tag => {
            get_last_update_blogs(blog_data.data[0].combination_title).then(last_update_blogs => {
              res.render('blog-single', { 
                blog_data: blog_data,
                 impt_tag_table: impt_tag_table,
                 recommend_tag_table:recommend_tag_table,
                 user_set:user_set,
                 blog_tag:blog_tag,
                 last_update_blogs:last_update_blogs
                });
            })
          })
        })
      })
    })
  })
});

module.exports = router;
