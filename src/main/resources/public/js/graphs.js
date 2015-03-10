/**
 * Created by cepe on 09.03.2015.
 */

function getGeneralOptions(title) {
    var options = {
        chartArea: {
            width: '100%,',
            height: '89%',
            top: 15
        },
        legend: {
            alignment: 'center'
        },
        tooltip: {
            trigger: 'hover'
        },
        backgroundColor: 'transparent',
        title: title
    };
    return options;
}

function getDataForPieChart(url, title, componentId) {

    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success:function(data) {
            var graphData = google.visualization.arrayToDataTable(data);
            var options = getGeneralOptions(title);
            var chart = new google.visualization.PieChart(document.getElementById(componentId));

            chart.draw(graphData, options);
        }

    });
}

//todo @rap: merge with the method above
function getDataForBarChart(url, title, componentId) {

	$.ajax({
		url: url,
		type: 'GET',
		dataType: 'json',
		success: function (data) {
			var data = google.visualization.arrayToDataTable(data);
			var options = getGeneralOptions(title);
			var chart = new google.visualization.BarChart(document.getElementById(componentId));

			chart.draw(data, options);
		}

	});
}