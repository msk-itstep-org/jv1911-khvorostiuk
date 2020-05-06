$(document).ready(function () {
    $(".fa-heart.fas").hide();
    $(".js-like").click(function (event) {
        event.preventDefault();
        const like = $(event.currentTarget);
        $.ajax({
            url: like.attr("href"),
            method: "POST",
            success: function (response) {
                console.log(response);
                if (response) {
                    like.find(".fa-heart").attr("data-prefix", "fas");
                } else {
                    like.find(".fa-heart").attr("data-prefix", "far");
                }
            }
        });
    });
});