const REMEMBER_ME_COOKIE = "IP_FITNESS_REMEMBER";

function getCookie() {
	let name = REMEMBER_ME_COOKIE + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for (let i = 0; i < ca.length; i++) {
		let c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

function saveCookie(value) {
	document.cookie = REMEMBER_ME_COOKIE + "=" + value;
}

function onRememberMeChanged() {
	if (!document.getElementById("remember").checked)
		document.cookie = REMEMBER_ME_COOKIE + "=";
	else
		saveCookie("");
}

function onLoginPageLoad() {
	var username = getCookie();
	if (username == "")
		return;

	document.getElementById("remember").checked = true;
	document.getElementById("username").value = username;
}

function onFormSubmit() {
	if (!document.getElementById("remember").checked)
		return;

	document.cookie = REMEMBER_ME_COOKIE + "=" + document.getElementById("username").value;
}