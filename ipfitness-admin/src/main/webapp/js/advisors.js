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
			setSelectedAdvisorId(e.target.id.substring(5));
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

var advisorId;
function setSelectedAdvisorId(rowNum) {
	var tr = document.getElementById("tr-" + rowNum);
	if (!tr) advisorId = 0;
	else advisorId = tr.children[0].innerHTML;
}

function fillEditForm(rowNum) {
	form = document.getElementById("edit-form");
	if (!form)
		return;

	var tr = document.getElementById("tr-" + rowNum);

	form["advisorId"].value = tr.children[0].innerHTML;
	form["displayName"].value = tr.children[1].innerHTML;
	form["username"].value = tr.children[2].innerHTML;
	form["email"].value = tr.children[3].innerHTML;
}

function submitDelete() {
	form = document.getElementById("delete-form");
	if (!form)
		return;
	var parameters = [{ name: "advisorId", value: advisorId }];
	submitFormWithParameters(form, parameters);
}

