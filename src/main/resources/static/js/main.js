$(function () {
    'use strict';
    // HEADER: Adjust Slider Height
    var windowHeigh = $(window).height();
    var upperH = $('.upper-bar').innerHeight();
    var navH = $('.navbar').innerHeight();
    $('.slider, .carousel-item').height(windowHeigh - (upperH + navH));
});
// // ++++++++++++++++++++++++++ End Change the Color if the link is clicked ++++++++++++++++++++++++++++++++++++++++++//

// ++++++++++++++++++++++++++++++ Start scroll from navbar to the determined element +++++++++++++++++++++++++++++++//
$('.nav-item .einfuer').click(function () {

    $('html, body').animate({

        scrollTop: $('.einf').offset().top

    }, 1000);
});

$('.nav-item .anwendun').click(function () {

    $('html, body').animate({

        scrollTop: $('.anwend').offset().top

    }, 1000);
});

$('.nav-item .exakte').click(function () {

    $('html, body').animate({

        scrollTop: $('.exakt').offset().top

    }, 1000);
});

$('.nav-item .heuristische').click(function () {

    $('html, body').animate({

        scrollTop: $('.heuristi').offset().top

    }, 1000);
});

$('.nav-item .observat').click(function () {

    $('html, body').animate({

        scrollTop: $('.observ').offset().top

    }, 1000);
});

// ++++++++++++++++++++++++++++++ Start Select Algorithmen +++++++++++++++++++++++++++++++++
var expanded = false;

function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}

/*
* In this methods send a list of Algorithms to Backend in order to implement this selected Algorithms and send it
* back using AJAX.
*/

function submitSelectedAlgorithms() {
    /* declare an checkbox array */
    var chkArray = [];
    let selected;
    /* look for all checkboxes that have a class 'chk' attached to it and check if it was checked */
    $(".chk:checked").each(function () {
        chkArray.push($(this).val());
    });

    if (chkArray.length === 0) {
        Swal.fire({
            type: "warning",
            title: "Ankreuzen",
            text: "Bitte mindestens eines der Checkboxen anzreuzen",
        })
    } else {
        selected = JSON.stringify(chkArray);
        console.log("my Selected: " + selected);
        var a = sendAlgorithms(selected);
    }
}

function sendAlgorithms(selected) {
    let x = "";

    $.ajax({
        contentType: "application/json",
        type: "POST",
        data: selected,
        url: "/check",
        success: function (data) {

            if (isJson(data)) {
                let myObj = JSON.parse(data);

                console.log("ich bin drin: " + myObj);
                // fillGraph(myObj.graph);
                let y = "";
                x = "<div>" + "<hr>";
                x += "<h4>" + "Das Ergebnis der Implementierung der Algorithmen:" + "</h4>";
                x += "<p>" + y + "</p>";
                x += "<b>" + "<a " + " id = " + "showJsonText " + " onclick=" + "showAndHide()" + " >" + "Das ganze Ergebnis in JSON Format anzeigen"
                    + "</a>" + "</b>";
                x += "<pre>" + JSON.stringify(myObj, null, '\t') + "</pre>";
                x += "<b>" + "<a " + " id = " + "showJsonTh " + " onclick=" + "hide()" + " >" + "</a>" + "</b>";
                x += "<hr>" + "</div>";
                document.getElementById("showmyjson").innerHTML = x;
                // createGraph(myObj);

            } else
                document.getElementById("showmyjson").innerHTML = "<h3>" + data + "!" + "</h3>";
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('error while post to java');
        }
    });
    // return myObjectt;
}

function showAndHide() {

    document.getElementsByTagName('pre')[0].style.display =
        (document.getElementsByTagName('pre')[0].style.display !== "block") ?
            "block" : "none";
    if (document.getElementsByTagName('pre')[0].style.display === "block") {
        document.getElementById("showJsonText").innerText = "Das Ergebnis ausblenden"
        document.getElementById("showJsonTh").innerText = "Das Ergebnis ausblenden"
    } else {
        document.getElementById("showJsonText").innerText = "Das ganze Ergebnis in JSON Format anzeigen"
        document.getElementById("showJsonTh").innerText = ""
    }
}

function hide() {
    document.getElementsByTagName('pre')[0].style.display =
        (document.getElementsByTagName('pre')[0].style.display !== "block") ?
            "none" : "";
    document.getElementById("showJsonTh").innerText = ""
    document.getElementById("showJsonText").innerText = "Das ganze Ergebnis in JSON Format anzeigen"
}

function isJson(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

// ++++++++++++++++++++++++++++++ End Select Algorithmen ++++++++++++++++++++++++++++++++++++++++++++++++++++//

function isNatural(s) {
    if (s === "") {
        return true;
    } else {
        let n = parseInt(s);
        return n > 0 && n < 10 && n.toString() === s;
    }
}