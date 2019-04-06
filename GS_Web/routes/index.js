var express = require('express');
const request = require('request');
var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
  //https://www.cnblogs.com/woodk/p/6147436.html
  var apiURL = 'https://api.github.com/repos/neslxzhen/GraduationCase/commits?per_page=5&sha='
  request(apiURL, { headers: { 'User-Agent': 'ua' } }, function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var arr = JSON.parse(body)
      var sha_arr=[]
      
      for(var i=0;i<arr.length;i++){
        var arr2=arr[i].sha.split('')
        var sha=""
        for(var j=0;j<5;j++){
          sha+=arr2[j]
        }
        sha_arr.push(sha)
      }
      res.render('index', { arr:arr,sha_arr:sha_arr});
    }else{
		console.log("apiURL error:"+error)
		res.render('index', { arr:[],sha_arr:[]});
	}
  });
});

module.exports = router;