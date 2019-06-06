'use strict';
const query = require('./asyncDB').query;

var do_query=async function (s) {
  var result = {};
  await query(s)
    .then((data) => {
      result = { code: 0, data: data };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });
  return result;
}

var get_blog = async function (id) {
  return do_query("select * from blog where blog_id=" + id + ";");
}

var get_blog_comment = async function (id) {
  return do_query("select * from reply where blog_id=" + id + ";");
}

var get_blog_tag = async function (id) {
  return do_query("select * from (select * from blog_tag where blog_id="+id+") as f join tag on tag.tag_id=f.tag_id;");
}

// -- 較糟:先將id與中文字抓出來，再計算
// -- SELECT *,count(*) as cnt FROM `tag` join blog_tag on blog_tag.tag_id=tag.tag_id where is_important=1 GROUP BY tag.tag_id

// -- 較好:先算id數量，在連起中文字
// select * from (select count(*) as cnt,blog_tag.tag_id from (SELECT tag_id FROM `tag` where is_important=1) as f join blog_tag on blog_tag.tag_id=f.tag_id GROUP BY f.tag_id ) as f JOIN tag on tag.tag_id=f.tag_id;
var get_impt_tag = async function () {
  return do_query("select * from (select count(*) as cnt,blog_tag.tag_id from (SELECT tag_id FROM `tag` where is_important=1) as f join blog_tag on blog_tag.tag_id=f.tag_id GROUP BY f.tag_id ) as f JOIN tag on tag.tag_id=f.tag_id;");
}

// todo 推薦標籤
var sql="FROM `tag` ORDER BY RAND() LIMIT 20";

var get_recommend_tag = async function (user_id) {
  return do_query("SELECT * "+sql+";");
}

// todo 推薦部落格
var get_recommend_blog = async function () {
return do_query("select * from (SELECT blog_tag.* from (select tag_id "+sql+") as f join blog_tag on f.tag_id=blog_tag.tag_id GROUP BY blog_id) as f join blog on blog.blog_id=f.blog_id;");
}

var user = async function (id) {
  return do_query("SELECT * FROM `user` where user_id='"+id+"';");
}

// 用關鍵字查詢最近更新
var get_latest_blogs = async function (s) {
  return do_query("select * from blog where blog_title like '%"+s+"%' ORDER BY last_update DESC limit 3;");
}

// 最熱門
var get_hot_blogs = async function () {
  return do_query("SELECT count(*) as cnt,blog.* from reply join blog on blog.blog_id=reply.blog_id GROUP BY reply.blog_id ORDER BY cnt desc;");
}

// TODO 評價最高
var get_good_blogs = async function () {
  return do_query("SELECT count(*) as cnt,blog.* from reply join blog on blog.blog_id=reply.blog_id where is_good=1 GROUP BY reply.blog_id ORDER BY cnt desc;");
}

// TODO 優惠
var get_offer_blogs = async function () {
  return do_query("select * from blog where blog_title like '%"+s+"%' ORDER BY last_update DESC limit 3;");
}

// TODO 最有價值留言
var get_recommend_comment = async function (blog_id) {
  return do_query("select * from reply where blog_id=" + blog_id + ";");
}

var search_blog_by_tag= async function (s,pageNo) {
  var result = {};
  const linePerPage = 12;    //設定每頁資料筆數
  const navSegments = 5;   //設定導覽列顯示分頁數
  const startPage = Math.floor((pageNo - 1) / navSegments) * navSegments + 1;  //計算導覽列的起始頁數
  var totalPage=0;
  var totalLine=0;

  var s2="from (SELECT blog_tag.blog_id from tag right join blog_tag on tag.tag_id=blog_tag.tag_id where tag.tag_title like '%"+s+"%') as f JOIN blog on f.blog_id=blog.blog_id"
  await query("SELECT count(*) as cnt "+s2+";")
    .then((data) => {
      totalLine = data[0].cnt;
      totalPage = Math.ceil(totalLine / linePerPage);
    }, (error) => {
      console.log(error)
      totalLine = 0;
      totalPage = 0;
    });

  await query("SELECT * "+s2+" LIMIT ?,?", [(pageNo-1)*linePerPage, linePerPage])
    .then((data) => {
      result = { code: 0, data: data, pageNo: pageNo, totalLine: totalLine, totalPage: totalPage, startPage: startPage, linePerPage: linePerPage, navSegments: navSegments };
    }, (error) => {
      console.log(error)
      result = { code: -1 };
    });

    return result;
}

module.exports = {
  get_blog:get_blog,
  get_blog_tag:get_blog_tag,
  get_impt_tag:get_impt_tag,
  get_recommend_tag:get_recommend_tag,
  get_recommend_blog:get_recommend_blog,
  user:user,
  get_latest_blogs:get_latest_blogs,
  search_blog_by_tag:search_blog_by_tag,
  get_hot_blogs:get_hot_blogs,
  get_good_blogs:get_good_blogs,
  get_offer_blogs:get_offer_blogs,
  get_blog_comment:get_blog_comment
};
