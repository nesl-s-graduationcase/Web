var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('blog-single', { title: 'Express' });
});

module.exports = router;
