$(document).ready(function() {
	
	var gamepath;
	
// Login form
	$("#login_form").submit(function(event){

		event.preventDefault();
		var user = $("#username").val(); 
		var pass = $("#password").val();
		
		var sendInfo = {
			username: user,
			password: pass
		};
		
		$.ajax({
			type: "POST",
			url: "http://ubuntu4.saluton.dk:38055/web/api/login",
			data: JSON.stringify(sendInfo),
			contentType: "application/json; charset=utf-8",
			statusCode: {
				200: function (response) {
					gamepath = response.gamepath;
					$("body").removeClass("not-logged-in").addClass("logged-in");
					$("#login_form").hide();
					
					// Init game in progress
					$.get(gamepath, function(result){
					$("#image").attr("src","grafik/forkert" + result.wrongletters + ".png");
					document.getElementById("usedletters").innerHTML = result.usedletters;
					document.getElementById("invisibleword").innerHTML = result.invisibleword;
					document.getElementById("name").innerHTML = "Velkommen " + result.name;
					});
				},
				401: function (response) {
					$("#password").val(""); // empty box
					document.getElementById("feedback").innerHTML =
					'<div class="alert alert-warning alert-dismissible">'
					+ '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'
					+ 'Forkert brugernavn eller kodeord.</div>';
				},
				503: function (response) {
					document.getElementById("feedback").innerHTML = 
					'<div class="alert alert-warning alert-dismissible">'
					+ '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'
					+ 'Servicen er midlertidig utilgængelig.</div>';
				}
			}
		});
    });

// Update gameplay data
	$("#guess_form").submit(function(event){
		event.preventDefault();
		// Post the guessed letter to api
		 $.post(gamepath, $("#letter").serialize(), function(result) {
			document.getElementById("result").innerHTML = result;
			document.getElementById("result2").innerHTML = result;
		});
		
		$.get(gamepath, function(result){
			$("#image").attr("src","grafik/forkert" + result.wrongletters + ".png");
			document.getElementById("usedletters").innerHTML = result.usedletters;
			document.getElementById("invisibleword").innerHTML = result.invisibleword;
			$("#letter").val(""); // empty box
			
			if(result.gameover === "true") {
				$("#guess_box").hide();
				$("#retry_button").show();
			} 
		});
	});
	
	$( "#button" ).click(function(event) {
		event.preventDefault();
		$.post(gamepath, $("#reset").serialize(), function(result) {
			document.getElementById("result").innerHTML = result;
		});
			$.get(gamepath, function(result){
			document.getElementById("usedletters").innerHTML = result.usedletters;
			document.getElementById("invisibleword").innerHTML = result.invisibleword;
			$("#image").attr("src","grafik/forkert" + result.wrongletters + ".png");
		});
		$("#retry_button").hide();
		$("#guess_box").show();
	});
});