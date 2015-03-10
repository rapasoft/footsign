/**
 * Created by cepe on 09.03.2015.
 */

function getDataForPieChart(url, title, componentId) {

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

//todo @rap: merge with the method above
function getDataForBarChart(url, title, componentId) {

	$.ajax({
		url: url,
		type: 'GET',
		dataType: 'json',
		success: function (data) {
			var data = google.visualization.arrayToDataTable(data);
			var options = {
				backgroundColor: 'transparent',
				title: title
			};
			var chart = new google.visualization.BarChart(document.getElementById(componentId));

			chart.draw(data, options);
		}

	});
}