/**
 * 提交回复
 */
function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    let commentator = $("#question_creator").val();
    comment2target(questionId, 1, content, commentator);
}

function comment2target(targetId, type, content, commentator) {
    if(content === ""){ // content不能是空串
        alert("请输入评论");
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
            if(response.code === 200 && content !== "") { // 如果响应码是200，将回复框隐藏
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
    let comment = $("#comment-" + id); // 取出id为comment-id的对象

    // 获取二级评论展开状态
    let attribute = e.getAttribute("data-collapse");
    if(attribute){ // 获取到了"data-collapse"属性，说明是展开状态，删除该属性，闭合二级评论
        comment.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active"); // 移除高亮
    }else {
        comment.addClass("in"); // 在class中加入in，展开二级评论
        e.setAttribute("data-collapse", "in"); // 标记二级评论展开状态
        e.classList.add("active"); // 添加高亮
    }
}
