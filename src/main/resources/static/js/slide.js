$("img").click(function (event) {
    let img = $(event.target);
    let sub = img.parent().find("section");
    if (sub.is(":visible")) sub.slideUp();
    else sub.slideDown();
});
