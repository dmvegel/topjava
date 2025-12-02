const mealAjaxUrl = "ui/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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

function clearFilter() {
    $("#startDate").val("");
    $("#endDate").val("");
    $("#startTime").val("");
    $("#endTime").val("");
}

function updateTable() {
    $.get(ctx.ajaxUrl + "filter", {
        startDate: $("#startDate").val(),
        endDate: $("#endDate").val(),
        startTime: $("#startTime").val(),
        endTime: $("#endTime").val()
    }, function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
    });
}