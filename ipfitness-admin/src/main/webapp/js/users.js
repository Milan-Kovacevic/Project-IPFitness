function onLoad() {
	var editActionElements = document.getElementsByClassName("edit-id");
	for (var element of editActionElements) {
		element.addEventListener('click', function(e) {
			fillEditForm(e.target.id.substring(5));
		});
	}

	var deleteActionElements = document.getElementsByClassName("delete-id");
	for (var element of deleteActionElements) {
		element.addEventListener('click', function(e) {
			setSelectedUserId(e.target.id.substring(5));
		});
	}

	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		searchQuery.addEventListener("input", (event) => {
			if (event.target.value === "") {
				searchData(event.target.parentElement);
			}
		});
	}
}

var userId;
function setSelectedUserId(rowNum) {
	var tr = document.getElementById("tr-" + rowNum);
	if (!tr) userId = 0;
	else userId = tr.children[0].innerHTML;
}

function fillEditForm(rowNum) {
	form = document.getElementById("edit-form");
	if (!form)
		return;

	var tr = document.getElementById("tr-" + rowNum);

	form["userId"].value = tr.children[0].innerHTML;
	form["firstName"].value = tr.children[1].innerHTML;
	form["lastName"].value = tr.children[2].innerHTML;
	form["username"].value = tr.children[3].innerHTML;
	form["mail"].value = tr.children[4].innerHTML;
	form["active"].checked = tr.children[5].firstChild.innerHTML == "Active";
	form["city"].value = tr.children[6].innerHTML;
}

function submitDelete() {
	form = document.getElementById("delete-form");
	if (!form)
		return;
	var parameters = [{ name: "userId", value: userId }];
	submitFormWithParameters(form, parameters);
}

