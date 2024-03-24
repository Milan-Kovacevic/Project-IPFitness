function onLoad() {
	var detailsActionElements = document.getElementsByClassName("details-id");
	for (var element of detailsActionElements) {
		element.addEventListener('click', function(e) {
			fillDetailsForm(e.target.id.substring(5));
		});
	}
}

function fillDetailsForm(rowNum) {
	let time = document.getElementById("time");
	let message = document.getElementById("message");
	if (!time || !message)
		return;

	var tr = document.getElementById("tr-" + rowNum);
	time.innerHTML = tr.children[4].innerHTML;
	message.innerHTML = tr.children[6].innerHTML;
}
