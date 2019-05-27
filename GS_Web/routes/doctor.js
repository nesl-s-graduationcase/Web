var express = require('express');
var router = express.Router();
const query = require('./require/asyncDB');

var monster = async function(pageNo){
  const linePerPage = 12;    //設定每頁資料筆數
  const navSegments = 5;   //設定導覽列顯示分頁數
  const startPage = Math.floor((pageNo-1) / navSegments) * navSegments + 1;  //計算導覽列的起始頁數

  var totalLine, totalPage;
  var result = {};
var s=" from monster join `level` on `level`.level_id join combination on `level`.combination_id ORDER BY `level`.combination_id,`level`.level_id ";
  await query("select count(*) as cnt"+s)
      .then((data) => {
          totalLine = data[0].cnt;
          totalPage = Math.ceil(totalLine/linePerPage);   
      }, (error) => {
          totalLine = 0;
          totalPage = 0;  
      });

  await query('select `level`.*,combination.combination_title,monster.monster_id,monster_name'+s+'LIMIT ?, ?', [(pageNo-1)*linePerPage, linePerPage])
      .then((data) => {
          result = {code:0, data:data, pageNo:pageNo, totalLine:totalLine, totalPage:totalPage, startPage:startPage, linePerPage:linePerPage, navSegments:navSegments};  
      }, (error) => {
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
 
  monster(pageNo).then(d => {
    res.render('doctor', {data:d});
  })
});

module.exports = router;
