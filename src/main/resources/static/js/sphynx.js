/**
 * Script that handles data collection for each sphynx-powered website;
 */
function myScript(userId, siteId) {
	const COOKIE_EXPIRATION_LENGTH = 86400;
	const domain = "https://sphynx.dev/";
	
	let date = new Date();
	let path;
	let isScrolling = false;
	let scrollTopStart = 0;
	let scrollTopFinish = 0;
	let scrollTopLast = 0;
	let percentageView = "";
	let isReachedEndOfPage = false;
	
	let sessionId = getSessionId();
		
	// Retrieves the current sessionId if existing or generates a new one.
	function getSessionId() {
		// Checks if there is a sphynxSessionId cookie already set. 
		const cookieArray = document.cookie.split(";");
		for (let i = 0; i < cookieArray.length; i++) {
			let pairValueArray = cookieArray[i].split("=");
			if (pairValueArray[0].trim() === "sphynxSessionId") {
				return pairValueArray[1].trim();
			}
		}
		
		// Generates a new sphynxSessionId. Sets the cookie with its expiration date.
		let newSessionId = uuidv4();
		document.cookie = "sphynxSessionId=" + newSessionId + ";max-age=" + COOKIE_EXPIRATION_LENGTH;
		
		// Saves the new session and the 'start' event in database.
		let startTime = date = new Date();
		path = window.location.href;
		host = document.location.host;
		sendSession({
			sessionId: newSessionId,
			siteId,
			userId,
			date,
			host
		});
		sendEvent({
			type: "START",
			date,
			sessionId: newSessionId,
			siteId,
			userId,
			target: "",
			path
		});
		return newSessionId;
	}
	
	// Generates a UUID string.
	function uuidv4() {
	    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	    	var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
	}
	
	// Send a POST request to save data in the database.
	function sendData(json, urlExtension) {
		var xhr = new XMLHttpRequest();
		var url = domain + urlExtension;
		xhr.open("POST", url, true);
		xhr.withCredentials = true;
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(json));
	}
	
	function sendSession(json) {
		sendData(json, "save-session");
	}
	
	function sendEvent(json) {
		sendData(json, "save-event");
	}
	
	// Listens for 'scroll' event.
	// Updates the date to keep the status active.
	// Checks if the path has changed.
	// Checks if the end of document has been reached.
	document.addEventListener("scroll", function() {
		date = new Date();
		scrollTopLast = document.documentElement.scrollTop;
		if (pathHasChanged()) {
			path = window.location.href;
			sendEvent( {
				type: "CHANGE_URL",
				date,
				sessionId,
				siteId,
				userId,
				target: "",
				path
			});
		}
		path = window.location.href;
		if (isScrolling === false) {
			isScrolling = true;
			scrollTopFinish = scrollTopStart = document.documentElement.scrollTop;
		} else {
			if (scrollTopLast > scrollTopFinish) {
				scrollTopFinish = scrollTopLast;
			} else if (scrollTopLast < scrollTopStart) {
				scrollTopStart = scrollTopLast;
			}
			if (hasReachedEndOfPage()) {
				isReachedEndOfPage = true;
				sendEvent( {
					type: "VIEW_ONE_HUNDRED",
					date,
					target: "",
					sessionId,
					siteId,
					userId,
					path
				});
			}
	    }
	});
	
	// Adds mousemove event listener.
	// Updates date to keep status actif
	document.addEventListener("mousemove", function() {
		date = new Date();
	});
	  
	
	// Returns true if the end of page hasn't been reach before and 
	// the top of current view is the height of the document minus the height of the screen
	function hasReachedEndOfPage() {
		return (!isReachedEndOfPage && scrollTopLast === (document.documentElement.scrollHeight - window.innerHeight));
	}
	 
	// Listens and saves 'click' events.
	document.addEventListener("click", (e) => {
		path = window.location.href;
		date = new Date();
		let target = getTarget(e);
		sendEvent( {
			type: "CLICK",
			date,
			sessionId,
			siteId,
			userId,
			target,
			path
		});
    });
	
	// Gets the target of a given event.
	function getTarget(e) {
		let target = "";
		const nodeName = e.target.nodeName;
		const alt = e.target.alt;
		const placeholder = e.target.placeholder;
		const id = e.target.id;
		if (nodeName != undefined) {
			target = target.concat(nodeName);
		}
		
		if (nodeName === 'IMG') {
			if (alt != null) {
				return target.concat(" - ", alt);
			}
		} else if (nodeName === 'INPUT' || nodeName === 'TEXTAREA') {
			if (placeholder != null) {
				return target.concat(" - ", placeholder);
			}
		} else if (nodeName === 'INPUT' || nodeName === 'TEXTAREA') {
			if (id != null) {
				return target.concat(" - ", id);
			}
		} else if (e.target.innerHTML != undefined || e.target.innerHTML != null) {
			let html = e.target.innerHTML.trim().split(" ");
			target = target.concat(" - ", html[0]);
			for (let i = 1; i < 3; i ++) {
				if (html[i] != undefined && html[i] != null) {
					return target.concat(" ", html[i]);
				}
			}
		}
		return target;
	}

	// Add keydown listener when on focus.
	document.addEventListener("focusin", (e) => {
		document.addEventListener("keydown", sendTypingEvent);
	});
	
	// Sends 'keydown' event
	function sendTypingEvent(e) {
		date = new Date();
		path = window.location.href;
		let target = getTarget(e);
		sendEvent({
			type: "KEYDOWN",
			date,
			sessionId,
			siteId,
			userId,
			target,
			path
		});
		document.removeEventListener("keydown", sendTypingEvent);
	}
	  
	// Adds event listener to catch when user leave and return to tab.
	document.addEventListener("visibilitychange", function() {
		path = window.location.href;
		if (document.hidden) {
			isScrolling = false;
			date = new Date();
			sendEvent({
				type: "LEAVE_TAB",
				date,
				sessionId,
				siteId,
				userId,
				target: "",
				path
			});
		} else {
			date = new Date();
			sendEvent({
				type: "RETURN_TAB",
				date,
				sessionId,
				siteId,
				userId,
				target: "",
				path
			});
		}
	});	
	
	// Send inactive event
	let activeTimer = setTimeout(checkActivity, 30000);
	let isInactive = false;
	function checkActivity() {
		let now = new Date();
		path = window.location.href;
	    if ((now - date) > 60000) {
	    	if (isInactive == false) {
	    		sendEvent( {
	    			type: "INACTIVE",
	    			date,
	    			sessionId,
	    			siteId,
	    			userId,
	    			target: "",
	    			path
	    		});
	    		isInactive = true;
	    	}
	    	if (now - date > 360000 && isInactive == true) {
	    		isInactive = false;
	    		document.cookie = "sessionIdSphynx=" + sessionId + ";max-age=1";
	    		setTimeout(function() {
	    			sessionId = getSessionId();
	    		}, 2000);
	    	}
    		isScrolling = false;
	    	activeTimer = setTimeout(checkActivity, 60000);
	    } else if (isInactive == false) {
	    	activeTimer = setTimeout(checkActivity, 30000);
	    }
	}
	
	// Checks if path has changed.
	function pathHasChanged() {
		return  path != window.location.href;
	}
	
	// Changes the cookie expiration to 1 second when the user is about to leave the page.
	window.addEventListener('beforeunload', function() {
		document.cookie = "sessionIdSphynx=" + sessionId + ";max-age=1";
	});
}