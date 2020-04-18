function onActionClick(event) {
    event.preventDefault();
    const link = $(event.currentTarget);
    $.ajax({
        url: link.attr("href"),
        method: "get",
        success: result => {
            $("#js-friend-status").text(result.status);

            const newLink = $("<a></a>")
                .attr("id", result.id)
                .attr("href", result.href)
                .text(result.action)
                .click(onActionClick);
            link.parent().append(newLink);
            link.remove();
        }
    });
}

$("#js-add-friend, #js-delete-friend").click(onActionClick);