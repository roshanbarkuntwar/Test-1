// Load the Visualization API and the corechart package.
google.charts.load('current', {
	'packages' : [ 'corechart' ]
});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawChart);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChart() {

	var cp = $("#cp").val();
	$.ajax({
		type : 'GET',
		url : cp + 'getGoogleChartDetails?repCode=2&dashType=3',

		success : function(response) {
			if (response !== null && response !== "null" && response !== "") {
				var array = new Array();
				array.push([ "Element", "Data", {role : "style"} ]);
				if (response.length > 0) {
					for (var i = 0; i < response.length; i++) {
						array.push([ response[i].labels, response[i].series, response[i].color ]);
					}

				}

				var data = google.visualization.arrayToDataTable(array);

				var view = new google.visualization.DataView(data);
				view.setColumns([ 0, 1, {
					calc : "stringify",
					sourceColumn : 1,
					type : "string",
					role : "annotation"
				}, 2 ]);
				var options = {
					// title: "Density of Precious Metals, in g/cm^3",
					width : "100%",
					height : $(".dashboard2-graph").height() - 20,
					bar : {
						groupWidth : "95%"
					},

					legend : {
						position : "none"
					},
				};
				var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
				chart.draw(view, options);

			} else {
				alert("Some Error Occurred !");
			}

		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("errorThrown : " + errorThrown);
		}
	});

}