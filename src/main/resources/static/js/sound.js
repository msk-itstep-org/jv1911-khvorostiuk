var audio = $("#sound")[0];
$(".play").click(function (event) {
    event.preventDefault();
    audio.pause();
    $(audio).attr("src", $(event.target).attr("href"));
    audio.play();
});
$(".pause").click(function (event) {
        event.preventDefault();
        audio.pause();
});
