function getGeneralOptions(title) {
	return {
		chartArea: {
			width: '100%,',
			height: '90%',
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
}

function getDataForPieChart(url, title, componentId) {
	getDataForChart(url, title, new google.visualization.PieChart(document.getElementById(componentId)));
}

function getDataForBarChart(url, title, componentId) {
	getDataForChart(url, title, new google.visualization.BarChart(document.getElementById(componentId)));
}

function getDataForColumnChart(url, title, componentId) {
	getDataForChart(url, title, new google.visualization.ColumnChart(document.getElementById(componentId)));
}

function getDataForChart(url, title, chart) {
	$.ajax({
		url: url,
		type: 'GET',
		dataType: 'json',
		success: function (data) {
			var graphData = google.visualization.arrayToDataTable(data);
			var options = getGeneralOptions(title);
			chart.draw(graphData, options);
		}
	});
}