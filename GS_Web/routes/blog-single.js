var express = require('express');
var router = express.Router();
var funcLib = require('./require/funcLib');

router.get('/', function (req, res, next) {
	var id = Number(req.param('id'));  //頁碼, 轉數字
	if (isNaN(id) || id < 1) {
		pageNo = 1;
	}
	funcLib.get_blog(id).then(blog_data => {
		funcLib.get_blog_comment(id).then(comment => {
			for (var j = 0; j < comment.data.length; j++) {
				comment.data[j].reply = JSON.parse(comment.data[j].reply)
			}

			funcLib.get_impt_tag().then(impt_tag_table => {
				funcLib.get_recommend_tag().then(recommend_tag_table => {
					funcLib.user(blog_data.data[0].user_id).then(user_set => {
						funcLib.get_blog_tag(id).then(blog_tag => {
							funcLib.get_latest_blogs(blog_data.data[0].blog_title).then(latest_blogs => {
								funcLib.get_recommend_blog().then(recommend_blog => {
									funcLib.get_recommend_comment(id).then(recommend_blog => {
										var ds = recommend_blog.data;
										var arr2d = []
										var arr = []
										if (ds.length > 3) {
											for (var i = 0; i < ds.length; i++) {
												arr.push(ds[i])
												if (i % 3 == 0) {
													arr2d.push(arr)
													arr = []
												}
											}
										}else{
											arr2d.push(ds)
										}
										recommend_blog.data = arr2d
	
										res.render('blog-single', {
											blog_data: blog_data,
											impt_tag_table: impt_tag_table,
											recommend_tag_table: recommend_tag_table,
											user_set: user_set,
											blog_tag: blog_tag,
											latest_blogs: latest_blogs,
											comment: comment,
											recommend_blog: recommend_blog
										});
									})
								})
							})
						})
					})
				})
			})
		})
	})
});

module.exports = router;
