const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        refreshTable(data);
    });
}

$(document).on("click", ".enabled-cbx", function () {
    let checkbox = $(this);
    let tr = checkbox.closest("tr");
    let userId = tr.attr("id");
    let enable = checkbox.is(":checked");

    $.ajax({
        url: ctx.ajaxUrl + userId + "/enable",
        type: "POST",
        data: {enabled: enable},
    }).done(function () {
        tr.attr("data-user-enabled", enable);
    }).fail(function () {
        checkbox.prop("checked", !enable);
    });
});