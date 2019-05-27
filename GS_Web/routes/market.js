var express = require('express');
var router = express.Router();
const query = require('./require/asyncDB');

var market = async function(pageNo){
  const linePerPage = 12;    //設定每頁資料筆數
  const navSegments = 5;   //設定導覽列顯示分頁數
  const startPage = Math.floor((pageNo-1) / navSegments) * navSegments + 1;  //計算導覽列的起始頁數

  var totalLine, totalPage;
  var result = {};
await query("select count(*) as cnt from combination;")
      .then((data) => {
          totalLine = data[0].cnt;
          totalPage = Math.ceil(totalLine/linePerPage);   
      }, (error) => {
        console.log(error)
          totalLine = 0;
          totalPage = 0;  
      });

  await query('select * from combination;')
      .then((data) => {
          result = {code:0, data:data, pageNo:pageNo, totalLine:totalLine, totalPage:totalPage, startPage:startPage, linePerPage:linePerPage, navSegments:navSegments};  
      }, (error) => {
        console.log(error)
          result = {code:-1};
      });

  return result;
}


router.get('/', function(req, res, next) {
  var pageNo = Number(req.param('pageNo'));  //頁碼, 轉數字
  // 如果輸入頁碼有誤
  if(isNaN(pageNo) || pageNo < 1){
      pageNo=1;
  }
 
  market(pageNo).then(d => {
    res.render('market', {data:d});
  })
});

module.exports = router;
