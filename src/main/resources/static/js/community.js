function post() {
    let questionId = $("#question_id").val();
    let content = $("#comment_content").val();
    if(content === ""){ // content不能是空串
        alert("请输入评论");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({ // 将js对象转换为json
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        contentType: "application/json",
        success: function (response) {
            if(response.code === 200 && content !== "") { // 如果响应码是200，将回复框隐藏
                window.open("/question/" + questionId);
                $("#comment_section").hide();
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
