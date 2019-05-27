var express = require('express');
var router = express.Router();
const query = require('./require/asyncDB');

var blog = async function (id) {
  var result = {};
  await query("select * from combination where combination_id=" + id + ";")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      result = { code: -1 };
    });
  return result;
}

var tag = async function (combination_id) {
  var result = {};
  await query("SELECT combination_tag.*,tag.tag_title,combination_title FROM `combination_tag` join tag on tag.tag_id join combination on combination_tag.combination_id WHERE combination.combination_id=" + combination_id + ";")
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      result = { code: -1 };
    });
  return result;
}

// todo 推薦學習
var all_tag = async function () {
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

var comments = async function (id) {
  var result = {};
  await query('select * from combination;')
      .then((data) => {
        var data2=data;
        for(var i=0;i<data.length;i++){
          data2[i].reply =JSON.parse(data[i].reply);
        }
        data=data2;
          result = {code:0, data:data};  
      }, (error) => {
        console.log(error)
          result = {code:-1};
      });

  return result;
}

router.get('/', function (req, res, next) {
  var id = Number(req.param('id'));  //頁碼, 轉數字
  if (isNaN(id) || id < 1) {
    pageNo = 1;
  }
  blog(id).then(blog_data => {
    tag(id).then(tag_data => {
      all_tag().then(all_tag => {
        user(blog_data.data[0].user_id).then(user_set => {
          comments(id).then(comments => {
            res.render('blog-single', { blog_data: blog_data, tag_data: tag_data,all_tag:all_tag,user_set:user_set ,comments:comments});
          })
        })
      })
    })
  })
});

module.exports = router;
