var removedAttributes = [];

function onLoad(showDetails) {
	if (showDetails) {
		var button = document.createElement("button");
		button.setAttribute("data-bs-target", "#editCategoryModal");
		button.setAttribute("data-bs-toggle", "modal");
		button.style = "display: none";
		var parent = document.getElementById("addCategoryModal");
		parent.appendChild(button); button.click();

		var list = document.getElementById("attributesListOnEdit");
		if (list) {
			for (var item of list.children) {
				item.children[1].addEventListener("click", (e) => {
					list.removeChild(e.target.parentElement);
					if (e.target.parentElement.children[2])
						removedAttributes.push({ attributeId: e.target.parentElement.children[2].innerHTML });
				});
			}
		}
	}

	var searchQuery = document.getElementById("search-query");
	if (searchQuery) {
		searchQuery.addEventListener("input", (event) => {
			if (event.target.value === "") {
				searchData(event.target.parentElement);
			}
		});
	}

	var deleteActionElements = document.getElementsByClassName("delete-id");
	for (var element of deleteActionElements) {
		element.addEventListener('click', function(e) {
			setSelectedCategoryId(e.target.id.substring(5));
		});
	}

	const draggbles = document.querySelectorAll(".draggable-item");
	const containers = document.querySelectorAll(".draggable-container");

	draggbles.forEach((draggble) => {
		draggble.addEventListener("dragstart", () => {
			draggble.classList.add("dragging");
		})

		draggble.addEventListener("dragend", () => {
			draggble.classList.remove("dragging");
		})
	})

	containers.forEach((container) => {
		container.addEventListener("dragover", function(e) {
			e.preventDefault();
			const afterElement = dragAfterElement(container, e.clientY);
			const dragging = document.querySelector(".dragging")
			if (afterElement == null) {
				container.appendChild(dragging);
			} else {
				container.insertBefore(dragging, afterElement);
			}
		})
	})

}


function dragAfterElement(container, y) {
	const draggbleElements = [...container.querySelectorAll(".draggable-item:not(.dragging)")]

	return draggbleElements.reduce(
		(closest, child) => {
			const box = child.getBoundingClientRect()
			const offset = y - box.top - box.height / 2
			if (offset < 0 && offset > closest.offset) {
				return { offset: offset, element: child }
			} else {
				return closest
			}
		},
		{ offset: Number.NEGATIVE_INFINITY }
	).element;
}

var categoryId;
function setSelectedCategoryId(rowNum) {
	var tr = document.getElementById("tr-" + rowNum);
	if (!tr) categoryId = 0;
	else categoryId = tr.children[0].innerHTML;
}

function submitDelete() {
	form = document.getElementById("delete-form");
	if (!form)
		return;
	var parameters = [{ name: "categoryId", value: categoryId }];
	submitFormWithParameters(form, parameters);
}

function addCategory() {
	let form;

	form = document.getElementById("insertCategoryForm");
	if (!form)
		return;

	if (form.name.checkValidity()) {
		var attributes = [];
		var list = document.getElementById("attributesListOnInsert");

		for (var item of list.children) {
			attributes.push({ name: item.children[0].innerHTML });
		}
		var parameters = [{ name: "attributes", value: JSON.stringify(attributes) }];
		submitFormWithParameters(form, parameters);
	}
	else
		form.reportValidity();
}

function updateCategory() {
	let form;
	form = document.getElementById("editCategoryForm");
	if (!form)
		return;

	if (form.name.checkValidity()) {
		var attributes = [];
		var list = document.getElementById("attributesListOnEdit");

		for (var item of list.children) {
			if (item.children[2])
				attributes.push({ attributeId: item.children[2].innerHTML, name: item.children[0].innerHTML });
			else
				attributes.push({ name: item.children[0].innerHTML });
		}
		var parameters = [{ name: "attributes", value: JSON.stringify(attributes) }, { name: "deleted", value: JSON.stringify(removedAttributes) }];
		submitFormWithParameters(form, parameters);
	}
	else
		form.reportValidity();
}

function addAttribute(isEdit) {
	let form;
	if (!isEdit)
		form = document.getElementById("attributeFormOnInsert");
	else
		form = document.getElementById("attributeFormOnEdit");

	if (!form)
		return;

	if (form.attributeName.checkValidity()) {
		var attrName = form.attributeName.value;
		let list;
		if (!isEdit)
			list = document.getElementById("attributesListOnInsert");
		else
			list = document.getElementById("attributesListOnEdit");
		var item = document.createElement("div");
		item.className = "list-group-item list-group-item-action draggable-item d-flex justify-content-start gap-1 align-items-center";
		item.draggable = "true";
		var p = document.createElement("p");
		p.className = "m-0 flex-grow-1";
		p.innerHTML = attrName;
		var i = document.createElement("i");
		i.className = "text-secondary cursor-pointer material-symbols-outlined";
		i.innerHTML = "close_small";
		i.addEventListener("click", () => {
			list.removeChild(item);
		});
		item.addEventListener("dragstart", () => {
			item.classList.add("dragging");
		});
		item.addEventListener("dragend", () => {
			item.classList.remove("dragging");
		});
		item.appendChild(p);
		item.appendChild(i);
		list.appendChild(item);
		form.reset();
	}
	else
		form.reportValidity();
}


function showCategoryDetails(id) {
	var form = document.createElement("form");
	form.method = "post";
	form.action = "?action=getCategory";
	form.style = "display: none";
	var modal = document.getElementById("editCategoryModal");
	modal.appendChild(form);

	var parameters = [{ name: "categoryId", value: id }];
	submitFormWithParameters(form, parameters);
	modal.removeChild(form);
}
