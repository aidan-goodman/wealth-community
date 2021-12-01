// 用户输入完毕后点击发布，调用 Ajax 异步请求
$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    // 隐藏输入框
    $("#publishModal").modal("hide");

    // 获取已输入的标题和内容
    let title = $("#recipient-name").val();
    let content = $("#message-text").val();

    // 执行 Ajax 方法
    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title": title, "content": content},
        function (data) {
            // 解析 JSON 对象
            data = $.parseJSON(data);

            // 显示提示
            $("#hintBody").text(data.msg);
            $("#hintModal").modal("show");

            // 2000 毫秒后隐藏提示框并刷新页面
            setTimeout(function () {
                $("#hintModal").modal("hide");
                // 发布成功则重新加载主页
                if (data.code === 200) {
                    window.location.reload();
                }
                // 延时执行
            }, 2000);
        }
    )


}