var express = require('express');
var router = express.Router();

var funcLib = require('./require/funcLib');


router.get('/', function (req, res, next) {
  var s = req.param('tag');
  
  var pageNo = Number(req.param('pageNo'));  //頁碼, 轉數字
  // 如果輸入頁碼有誤
  if (isNaN(pageNo) || pageNo < 1) {
    pageNo = 1;
  }
  funcLib.search_blog_by_tag(s,pageNo).then(d => {
    res.render('search', { search_result: d });
  })
});

module.exports = router;
