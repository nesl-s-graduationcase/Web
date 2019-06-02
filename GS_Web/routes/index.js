var express = require('express');
var router = express.Router();

var funcLib = require('./require/funcLib');

router.get('/', function (req, res, next) {
  funcLib.get_impt_tag().then(impt_tag_table => {
    funcLib.get_recommend_tag(null).then(recommend_tag_table => {
      funcLib.get_recommend_blog().then(recommend_blog_table => {
        res.render('index', { 
          impt_tag_table: impt_tag_table,
          recommend_tag_table:recommend_tag_table,
          recommend_blog_table:recommend_blog_table
         });
      })
    })
  })
});

module.exports = router;
