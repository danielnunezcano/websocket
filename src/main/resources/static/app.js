var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        init();
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendHello() {
    stompClient.send("/app/hello", {}, JSON.stringify({'userId': $("#userId").val(),'vacant': $("#vacant").val()}));
}

function init() {
    stompClient.send("/app/init", {}, null);
}

function sendBye() {
    stompClient.send("/app/bye", {}, JSON.stringify({'name': $("#nameBye").val()}));
}

function showGreeting(message) {
    const result = message.map(obj => `${obj.userId}:${obj.vacant}`)
        .join(';');
    $("#greetings").html("<tr><td>" + result + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#sendHello" ).click(function() { sendHello(); });
    $( "#sendBye" ).click(function() { sendBye(); });
});