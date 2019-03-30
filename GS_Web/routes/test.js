'use strict'
//https://www.cnblogs.com/woodk/p/6147436.html
//https://segmentfault.com/a/1190000015144126
const request = require('request');
request('https://api.github.com/repos/neslxzhen/GraduationCase/commits', {headers: { 'User-Agent': 'ua' }}, function(error, response, body) {
   if (!error && response.statusCode == 200) {
      var o= JSON.parse(body)
      console.log(o)
    }
});