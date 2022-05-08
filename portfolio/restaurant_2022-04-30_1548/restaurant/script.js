$("#myModal .btn-primary").click(function() {
	$("button").hide();
});

$("#myModal .btn-default").click(function() {
	$("button").hide();
});

function validateForm() {
  var x = document.forms["myForm"]["name"].value;
  if (x == "" || x == null) {
    alert("Name must be filled out");
    return false;
  }
}


