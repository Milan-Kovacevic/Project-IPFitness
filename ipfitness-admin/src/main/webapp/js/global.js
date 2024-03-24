setTimeout(() => {
	var alerts = document.getElementsByClassName("alert");
	for (var alert of alerts) {
		alert.style.display = "none";
	}
}, 2500);

function submitFormWithParameters(form, parameters) {
	if (!form || !parameters)
		return;
	for (var pair of parameters) {
		var input = document.createElement('input');
		input.type = 'hidden';
		input.style = 'display: none';
		input.name = pair.name;
		input.value = pair.value;
		form.appendChild(input);
	}
	form.submit();
}

function setPage(page) {
	var form = document.getElementById("page-form");
	if (!form)
		return;
	var parameters = [{ name: "currentPage", value: page }];
	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		parameters.push({ name: "searchQuery", value: `${searchQuery.value}` });
	}
	submitFormWithParameters(form, parameters);
}

function goToNextPage() {
	var form = document.getElementById("page-form");
	if (!form)
		return;

	var parameters = [{ name: "nextPage", value: true }];
	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		parameters.push({ name: "searchQuery", value: `${searchQuery.value}` });
	}
	submitFormWithParameters(form, parameters);
}

function goToPreviousPage() {
	var form = document.getElementById("page-form");
	if (!form)
		return;
	var parameters = [{ name: "previousPage", value: true }];
	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		parameters.push({ name: "searchQuery", value: `${searchQuery.value}` });
	}
	submitFormWithParameters(form, parameters);
}

function searchData() {
	var form = document.getElementById("page-form");
	if (!form)
		return;
	var currentPage = document.getElementsByClassName("page-item active")[0];
	if (!currentPage)
		return;
	var parameters = [{ name: "currentPage", value: currentPage.children[0].innerHTML }];

	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		parameters.push({ name: "searchQuery", value: `${searchQuery.value}` });
	}
	submitFormWithParameters(form, parameters);
}