/**
 * Created by cepe on 09.03.2015.
 */

function getDataForPieGraph(url, title, componentId) {

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success:function(data) {
            var data = google.visualization.arrayToDataTable(data);
            var options = {
                backgroundColor: 'transparent',
                title: title
            };
            var chart = new google.visualization.PieChart(document.getElementById(componentId));

            chart.draw(data, options);
        }

    });
}