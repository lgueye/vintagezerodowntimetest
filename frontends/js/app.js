/* inject:env */

let apiUrl = 'http://localhost:9000/ws-api';

/* endinject */

let stompClient = null;
let heartRateChart = null;
let respirationRateChart = null;

$(function () {
    heartRateChart = new Chart($("#heart-rates-chart").get(0).getContext("2d"), {
        type: 'bar',
        data: {
            labels: [],
            datasets: [
                {
                    label: "D-8563461 (Heart rate facts)",
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
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }

        }
    });
    respirationRateChart = new Chart($("#respiration-rates-chart").get(0).getContext("2d"), {
        type: 'line',
        data: {
            labels: [],
            datasets: [
                {
                    label: "D-8563461 (Respiration rate facts)",
                    backgroundColor: [],
                    borderColor: [],
                    borderWidth: 1,
                    data: [],
                    fill: false,
                    lineTension: 0.1,
                    borderCapStyle: 'butt',
                    borderDash: [],
                    borderDashOffset: 0.0,
                    borderJoinStyle: 'miter',
                    pointBorderColor: [],
                    pointBackgroundColor: "#fff",
                    pointBorderWidth: 1,
                    pointHoverRadius: 5,
                    pointHoverBackgroundColor: [],
                    pointHoverBorderColor: [],
                    pointHoverBorderWidth: 2,
                    pointRadius: 1,
                    pointHitRadius: 10,
                    spanGaps: false
                }
            ]
        },
        options: {
            responsive: true,
            legend: {
                position: 'top'
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
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
    let socket = new SockJS(apiUrl);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/D-8563461/heart_rate', function (fact) {
            let value = JSON.parse(fact.body).value;
            let ts = JSON.parse(fact.body).timestamp;
            let chartModel = heartRateChart.data;
            let chartLabels = chartModel.labels;
            let chartFirstDataSet = chartModel.datasets[0];
            if (chartFirstDataSet.data.length > 50) {
                chartLabels.shift();
                chartFirstDataSet.data.shift();
                chartFirstDataSet.backgroundColor.shift();
                chartFirstDataSet.borderColor.shift();
            }
            chartLabels.push(moment(ts).format("DD MMM HH:mm:ss"));
            chartFirstDataSet.data.push(value);
            chartFirstDataSet.backgroundColor.push('rgba(75,192,192,0.4)');
            chartFirstDataSet.borderColor.push('rgba(75,192,192,1)');
            heartRateChart.update();
        });
        stompClient.subscribe('/topic/D-8563461/respiration_rate', function (fact) {
            let value = JSON.parse(fact.body).value;
            let ts = JSON.parse(fact.body).timestamp;
            let chartModel = respirationRateChart.data;
            let chartLabels = chartModel.labels;
            let chartFirstDataSet = chartModel.datasets[0];
            if (chartFirstDataSet.data.length > 50) {
                chartLabels.shift();
                chartFirstDataSet.data.shift();
                chartFirstDataSet.backgroundColor.shift();
                chartFirstDataSet.borderColor.shift();
                chartFirstDataSet.pointBorderColor.shift();
                chartFirstDataSet.pointHoverBackgroundColor.shift();
                chartFirstDataSet.pointHoverBorderColor.shift();
            }
            chartLabels.push(moment(ts).format("DD MMM HH:mm:ss"));
            chartFirstDataSet.data.push(value);
            chartFirstDataSet.backgroundColor.push('rgba(75, 192, 192, 0.2)');
            chartFirstDataSet.borderColor.push('rgba(75, 192, 192, 1)');
            chartFirstDataSet.pointBorderColor.push('rgba(75,192,192,1)');
            chartFirstDataSet.pointHoverBackgroundColor.push('rgba(75,192,192,1)');
            chartFirstDataSet.pointHoverBorderColor.push('rgba(220,220,220,1)');
            respirationRateChart.update();
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
