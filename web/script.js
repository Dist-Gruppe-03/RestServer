$(document).ready(function() {
	
	$("#login_form").submit(function(event){
		event.preventDefault();
		console.log($(this).serializeArray());
		//<!--$.post("api", $(this).serializeArray(), function(result){ -->
		$.get("https://dawa.aws.dk/autocomplete?kommunekode=101&q=" + $("#letter").val() + "&type=adresse&caretpos=2&supplerendebynavn=true&stormodtagerpostnumre=true&multilinje=true&fuzzy=", function(result){
		
			console.log(result);
			$("body").removeClass("not-logged-in").addClass("logged-in");
		});
		$("#login_form").hide();
	});
	
		$.get("http://ubuntu4.saluton.dk:38055/RestServer/hangman/play/json/s114992?reset=true", function(result){
		document.getElementById("usedletters").innerHTML = result.usedletters;
		document.getElementById("invisibleword").innerHTML = result.invisibleword;
	});
	
	$("#guess_form").submit(function(event){
		event.preventDefault();
		$.get("http://ubuntu4.saluton.dk:38055/RestServer/hangman/play/json/s114992?letter=" + $("#letter").val(), function(result){
		
			$("#image").attr("src","grafik/forkert" + result.wrongletters + ".png");
			document.getElementById("result").innerHTML = result.response;
			document.getElementById("usedletters").innerHTML = result.usedletters;
			document.getElementById("invisibleword").innerHTML = result.invisibleword;
			$("#letter").val(""); // empty box
			
			if(result.gameover === "true") {
				$("#guess_box").hide();
				$("#retry_button").show();
				document.getElementById("result2").innerHTML = result.response;
			} 
			});
	});
	
	$( "#button" ).click(function() {
		$.get("http://ubuntu4.saluton.dk:38055/RestServer/hangman/play/json/s114992?reset=true", function(result){
			$("#retry_button").hide();
			$("#guess_box").show();
			document.getElementById("result").innerHTML = result.response;
			document.getElementById("usedletters").innerHTML = result.usedletters;
			document.getElementById("invisibleword").innerHTML = result.invisibleword;
			$("#image").attr("src","grafik/forkert" + result.wrongletters + ".png");
		});
	});
	
});
