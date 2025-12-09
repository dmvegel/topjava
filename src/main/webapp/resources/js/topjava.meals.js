const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});

$.datetimepicker.setLocale(navigator.language.substring(0, 2));

$('.datetimepicker').datetimepicker({
    format: 'Y-m-d H:i'
});

$('.timepicker').datetimepicker({
    datepicker: false,
    format: 'H:i'
});

$('.datepicker').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});

$.ajaxSetup({
    converters: {
        "text json": function (text) {
            return JSON.parse(text,
                function (key, value) {
                    return (key === 'dateTime') ? value.substring(0, 16).replace('T', ' ') : value;
                }
            );
        }
    }
});