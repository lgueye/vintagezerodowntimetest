/* inject:env */

var apiUrl = 'http://localhost:9000/ws-api';

/* endinject */

var stompClient = null;
var myNewChart = null;

$(function () {
    var ctx = $("#hrf-chart").get(0).getContext("2d");
    myNewChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [
                {
                    label: "heart rate facts",
                    backgroundColor: [],
                    borderColor: [],
                    borderWidth: 1,
                    data: []
                }
            ]
        },
        options: {
            responsive: true,
            legend: {
                position: 'top'
            }
        }
    });
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
});

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function connect() {
    var socket = new SockJS(apiUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/D-8563461', function (fact) {
            var value = JSON.parse(fact.body).value;
            var ts = JSON.parse(fact.body).timestamp;
            var chartModel = myNewChart.data;
            var chartLabels = chartModel.labels;
            var chartFirstDataSet = chartModel.datasets[0];
            if (chartFirstDataSet.data.length > 50) {
                chartLabels.shift();
                chartFirstDataSet.data.shift();
                chartFirstDataSet.backgroundColor.shift();
                chartFirstDataSet.borderColor.shift();
            }
            chartLabels.push(moment(ts).format("DD MMM HH:mm:ss"));
            chartFirstDataSet.data.push(value);
            chartFirstDataSet.backgroundColor.push('rgba(75, 192, 192, 0.2)');
            chartFirstDataSet.borderColor.push('rgba(75, 192, 192, 1)');
            myNewChart.update();
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}
