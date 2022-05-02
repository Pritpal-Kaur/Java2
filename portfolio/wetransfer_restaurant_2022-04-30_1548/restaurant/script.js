// Get the modal
var modal= document.getElementById("myModal");

// Get the button that opens the modal
var btn= document.getElementById("myBtn");

//Get the <span> element that closes the modal
var span = document.getElementByClassName("close")[0];

//when the user clicks on the button, open the modal
btn.onclick = function() {
	modal.style.display = "block";
}

//When the user clicks on <span> (x), close the modal
span.onclick = function() {
	modal.style.display = "none";
}

//when the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
	if(event.target == modal) {
		modal.style.display = "none";
	}
}

$("#myModal .btn-primary").onclick {
   $("button").hide();
   $("#conf-alert").show();
});

function myFunction() {
	let text;
	if(document.getElementById("validationCustom03").validity.rangeOverflow) {
		text="Invalid phone number";
	} else if(document.getElementById("validationCustom03").validity.rangeUnderflow) {
		text="Invalid phone number";
	} else {
		text="correct input";
	}