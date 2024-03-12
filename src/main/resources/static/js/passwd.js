function passwordVisibility(){
    var x = document.getElementById("passwd");
    var y = document.getElementById("show");
    var z = document.getElementById("hide");

     if(x.type === 'password'){
       x.type="text";
       y.style.display="block";
       z.style.display="none";
     }
      else{
         x.type="password";
         y.style.display="none";
         z.style.display="block";
        }

}

function passwordVisibility1(){
    var x = document.getElementById("password");
    var y = document.getElementById("show1");
    var z = document.getElementById("hide1");

     if(x.type === 'password'){
       x.type="text";
       y.style.display="block";
       z.style.display="none";
     }
      else{
         x.type="password";
         y.style.display="none";
         z.style.display="block";
        }

}



function passwordVisibility2(){
    var x = document.getElementById("password2");
    var y = document.getElementById("show2");
    var z = document.getElementById("hide2");

     if(x.type === 'password'){
       x.type="text";
       y.style.display="block";
       z.style.display="none";
     }
      else{
         x.type="password";
         y.style.display="none";
         z.style.display="block";
        }

}

/*
var passwordregex6digits = new RegExp("(?=.*[0-9]{6,})");
var passwordregexLowercase = new RegExp("^(?=.*[a-z])");
var passwordregexUppercase = new RegExp("^(?=.*[A-Z])");
var passwordregexNumber = new RegExp("^(?=.*[0-9])");
var passwordRegexSpecial = new RegExp("^(?=.*[!@#$%^&*])");
var passwordRegexAll = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,})");

$(".pword").on('keyup',function () {
	var thepassword = $(".pword").val();	
	checkpassword(thepassword);
});

var checkpassword = function(thisPassword) {
	console.log(thisPassword);
	if (passwordregex6digits.test(thisPassword)) {
		$(".passfailSix").html('<span class="pass">Pass</span>');
	} else {
		$(".passfailSix").html('<span class="fail">Fail</span>');
	}

	if (passwordregexLowercase.test(thisPassword)) {
		$(".passfailLower").html('<span class="pass">Pass</span>');
	} else {
		$(".passfailLower").html('<span class="fail">Fail</span>');
	}
	
	if (passwordregexUppercase.test(thisPassword)) {
		$(".passfailUpper").html('<span class="pass">Pass</span>');
	} else {
		$(".passfailUpper").html('<span class="fail">Fail</span>');
	}

	if (passwordregexNumber.test(thisPassword)) {
		$(".passfailNum").html('<span class="pass">Pass</span>');
	} else {
		$(".passfailNum").html('<span class="fail">Fail</span>');
	}
	
	if (passwordRegexSpecial.test(thisPassword)) {
		$(".passfailSpecial").html('<span class="pass">Pass</span>');
	} else {
		$(".passfailSpecial").html('<span class="fail">Fail</span>');
	}
	if (passwordRegexAll.test(thisPassword)) {
		$(".passfailAll").html('<span class="pass">Pass</span>');
		$(".checkmark").show();
	} else {
		$(".passfailAll").html('<span class="fail">Fail</span>');
		$(".checkmark").hide();
	}
};

$(".pword").on("focus", function() {
	var thepassword = $(".pword").val();	
	checkpassword(thepassword);
	$(".tooltip").fadeIn('fast');
});

$(".pword").on("focusout", function() {
		$(".tooltip").fadeOut('fast', function () { 

		$(".passfail ").html('<span class="fail">Fail</span>');
		$(".checkmark").hide();
		});
});

*/

/*

// Definieren Sie die Passwort-Validierungsregeln
const passwordRules = {
    sixDigits: new RegExp("(?=.*[0-9]{6,})"),
    lowercase: new RegExp("^(?=.*[a-z])"),
    uppercase: new RegExp("^(?=.*[A-Z])"),
    number: new RegExp("^(?=.*[0-9])"),
    special: new RegExp("^(?=.*[!@#$%^&*])"),
    all: new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,})")
  };
  
  // Funktion zur Überprüfung des Passworts
  const checkPassword = (password) => {
    console.log(password);
    
    for (const rule in passwordRules) {
      if (passwordRules[rule].test(password)) {
        $(`.passfail${rule.charAt(0).toUpperCase() + rule.slice(1)}`).html('<span class="pass">Pass</span>');
      } else {
        $(`.passfail${rule.charAt(0).toUpperCase() + rule.slice(1)}`).html('<span class="fail">Fail</span>');
      }
    }
  
    if (passwordRules.all.test(password)) {
      $(".checkmark").show();
    } else {
      $(".checkmark").hide();
    }
  };
  
  // Event-Handler
  $(".pword").on('keyup focus', function () {
    const password = $(this).val();
    checkPassword(password);
    $(".tooltip").fadeIn('fast');
  });
  
  $(".pword").on("focusout", function() {
    $(".tooltip").fadeOut('fast', function () { 
      $(".passfail").html('<span class="fail">Fail</span>');
      $(".checkmark").hide();
    });
  });

*/

function checkPasswordSpecs() {
    var password = $('.pword').val();

    var speckOk1 = (password.length >= 8) ? 1 : 0;
    alterSpec($('.minimum-8'), speckOk1);

    var speckOk2 = (password.search(/\d/) == -1) ? 0 : 1;
    alterSpec($('.number'), speckOk2);

    var speckOk3 = (password.search(/[a-z]/) == -1) ? 0 : 1;
    alterSpec($('.lower-latin'), speckOk3);

    var speckOk4 = (password.search(/[A-Z]/) == -1) ? 0 : 1;
    alterSpec($('.upper-latin'), speckOk4);

    var speckOk5 = (password.search(/[_!@#\$\%\&]/) == -1) ? 0 : 1;
    alterSpec($('.symbols'), speckOk5);

    //Check for invalid characters
    if (password.length > 0 && false === /^[a-zA-Z0-9_!@#\$\%\&]+$/.test(password)) {
      $('.non-accepted').show();
      return false;
    } else {
      $('.non-accepted').hide();
    }

    if (speckOk1 && speckOk2 + speckOk3 + speckOk4 + speckOk5 >= 3) {
      return true;
    }

    return false;
}


function alterSpec($element, specOk) {
    if (specOk) {
      $element.addClass('text-success')
        .find('i').removeClass('fa-circle-o').addClass('fa-check')
    } else {
      $element.removeClass('text-success')
        .find('i').removeClass('fa-check').addClass('fa-circle-o')
    }
}

$(document).ready(function() {
    $(".pword").bind('keyup change', function() {
    checkPasswordSpecs();
    })
});