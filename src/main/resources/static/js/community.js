/**
 * 提交回复
 */
function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    let commentator = $("#this_commentator").val();
    comment2target(questionId, 1, content, commentator);
}

function comment2target(targetId, type, content, commentator) {
    if(content === ""){ // content不能是空串
        alert("请输入评论");
        return;
    }
    if(commentator === ""){
        alert("请先登录");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({ // 将js对象转换为json
            "parentId": targetId,
            "content": content,
            "type": type,
            "creator": commentator
        }),
        contentType: "application/json",
        success: function (response) {
            if(response.code === 200 && content !== "") {
                window.location.reload();
            }else if(response.code === 2003){
                let isAccepted = confirm(response.message);
                if(isAccepted){
                    window.open("https://gitee.com/oauth/authorize?client_id=f7425bc7ee111e40a70a5cf425be9d32eb47dcbe25b5b9617ef3229fa32984e1&redirect_uri=http://localhost:8080/callback&response_type=code&state=1");
                    window.localStorage.setItem("closable", "true"); // 如果点击确定，用localStorage存储一条信息
                }
            }else {
                alert(response.message)
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    let commentId = e.getAttribute("data-id"); // 页面将commentId存储在data-id里面了，在这里取出
    let commentator = $("#comment_creator").val();
    let content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content, commentator);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    let id = e.getAttribute("data-id"); // 页面将commentId存储在data-id里面了，在这里取出
    let comments = $("#comment-" + id); // 取出id为comment-id的对象

    // 获取二级评论展开状态
    let attribute = e.getAttribute("data-collapse");
    if(attribute){ // 获取到了"data-collapse"属性，说明是展开状态，删除该属性，闭合二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active"); // 移除高亮
    }else {
        let subCommentContainer = $("#comment-" + id);
        comments.addClass("in"); // 在class中加入in，展开二级评论
        e.setAttribute("data-collapse", "in"); // 标记二级评论展开状态
        e.classList.add("active"); // 添加高亮

        // 如果里面的元素个数为1，说明只有一个回复框，就获取数据追加显示在页面上（没有这个判断，每次点击二级回复按钮都会重复追加显示回复内容）
        if(subCommentContainer.children().length === 1){
            $.getJSON( "/comment/" + id, function(data) {

                // 追加显示二级回复的标签和内容（用js手写页面 强）
                $.each( data.data, function(index, comment) {

                    let mediaLeftElement = $("<div/>", {
                       "class":"media-left"
                    }).append($("<img>", {
                        "class":"media-object img-rounded comments",
                        "src":comment.user.avatarUrl
                    }));

                    let mediaBodyElement = $("<div/>", {
                        "class":"media-body"
                    }).append($("<h5>", {
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<h5>", {
                        "class":"commentContent",
                        "html":comment.content
                    })).append($("<div/>", {
                        "class":"commentMenu",
                    })).append($("<div/>", {
                        "class":"pull-right commentMenu",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD')
                    }));

                    let mediaElement = $("<div/>", {
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    const commentElement = $("<div/>", {
                        "class": "col-xs-12 col-sm-12 col-md-12 col-lg-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });

            });
        }
    }

}

function delComment(){
    let message = confirm("是否确认删除?");
    if(message === false){
        return;
    }
    let creator = $("#creator").val();
    let commentId = $("#commentId").val();
    let parentId = $("#parentId").val();
    $.ajax({
        type: "POST",
        url: "/delComment",
        data: JSON.stringify({ // 将js对象转换为json
            "creator": creator,
            "commentId": commentId,
            "parentId": parentId
        }),
        contentType: "application/json",
        success: function (response) {
            if(response.code === 200) {
                window.location.reload();
            }else {
                alert(response.message)
            }
        },
        dataType: "json"
    });
}

function delQuestion() {
    let message = confirm("删除后将无法恢复，是否确认删除?");
    if(message === false){
        return;
    }
    let questionId = $("#questionId").val();
    let creator = $("#question_creator").val();
    $.ajax({
        type: "POST",
        url: "/delQuestion",
        data: JSON.stringify({ // 将js对象转换为json
            "id": questionId,
            "creator": creator,
        }),contentType: "application/json",
        success: function (response) {
            if(response.code === 200) {
                window.open("/","_self");
            }else {
                alert(response.message)
            }
        },
        dataType: "json"
    });
}
