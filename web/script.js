$(document).ready(function () {

    var gamepath;
    getHighscore();

    // Login form
    $("#login_form").submit(function (event) {

        event.preventDefault();
        var user = $("#username").val();
        var pass = $("#password").val();

        var sendInfo = {
            username: user,
            password: pass
        };

        $.ajax({
            type: "POST",
            url: "api/login",
            data: JSON.stringify(sendInfo),
            contentType: "application/json; charset=utf-8",
            statusCode: {
                200: function (response) {
                    gamepath = response.gamepath;
                    $("#login").hide();
                    $("#game").show();

                    // Init game in progress
                    $.get(gamepath, function (result) {
                        updateGameInfo(result);
                        $("#name").html("Logget ind som:<br>" + result.name);
                    });
                },
                401: function () {
                    $("#password").val(""); // empty box
                    $("#feedback").html('<div class="alert alert-warning alert-dismissible fade show" role="alert">Forkert brugernavn eller kode.<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                },
                503: function () {
                    $("#feedback").html('<div class="alert alert-warning alert-dismissible fade show" role="alert">Servicen er i øjeblikket utilgængeligt.<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button></div>');
                }
            }
        });
    });

    // Update gameplay data
    $("#guess_form").submit(function (event) {
        event.preventDefault();
        // Post the guessed letter to api
        $.post(gamepath, $("#letter").serialize(), function (result) {
            $("#result").html(result);
            $("#result2").html(result);
        });

        $.get(gamepath, function (result) {
            updateGameInfo(result);
            $("#letter").val(""); // empty box

            if (result.gameover === "true") {
                $("#guess_form").hide();
                $("#reset_form").show();
            }
        });
    });

    // Reset game
    $("#button").click(function (event) {
        event.preventDefault();
        $.post(gamepath, $("#reset").serialize(), function (result) {
            $("#result").html(result);
        });
        getHighscore();
        $.get(gamepath, function (result) {
            updateGameInfo(result);
        });
        $("#reset_form").hide();
        $("#guess_form").show();
    });

    // Update game info
    function updateGameInfo(info) {
        $("#image").attr("src", "grafik/forkert" + info.wrongletters + ".png");
        $("#usedletters").html(info.usedletters);
        $("#invisibleword").html(info.invisibleword);
    }

    // Get highscore
    function getHighscore() {
        $.getJSON("api/highscore", function (data) {
            var highscoreList = "<div class=\"row\"><div class=\"col-12 pt-0\"><h5>Highscore</h5></div></div>";
            $(data.highscores).each(function (i, scores) {
                highscoreList += "<div class=\"row border-top\">";
                highscoreList += "<div class=\"col-1 p-1 text-muted\">" + (i + 1) + ".</div>"
                highscoreList += "<div class=\"col-9 p-1\">" + scores.username + "</div>"
                highscoreList += "<div class=\"col-2 text-right p-1\">" + scores.score + "</div>";
                highscoreList += "</div>";
            });
            $("#highscore").html(highscoreList);
        });
    }
});