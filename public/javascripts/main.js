
$("#submitColumns").click(function () {
    var columns = [];
    $("input:checkbox:checked").each(function () {
        columns.push($(this).val());
    });
    var joined = columns.join(",");
    window.location = window.location.pathname.replace("/columns", "/results") + "?addressColumns=" + joined;
});
