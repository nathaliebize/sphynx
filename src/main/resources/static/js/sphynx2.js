(function() {
	const userId = "3001";
	const siteId = "1301";
	const COOKIE_EXPIRATION_LENGTH = 86400;
	
	let dateLastEvent = new Date();
	let path;
	let isScrolling = false;
	let scrollTopStart = 0;
	let scrollTopFinish = 0;
	let scrollTopLast = 0;
	let percentageView = "";
	let isReachedEndOfPage = false;
	
	let sessionId = setSessionId();
	
	checkPathChange();
	
	function setSessionId() {
		let cookieArray = document.cookie.split(";");
		for (let i = 0; i < cookieArray.length; i++) {
			let pairValueArray = cookieArray[i].split("=");
			if (pairValueArray[0].trim() === "sessionIdSphynx") {
				console.log("already exist - sessionId = " + pairValueArray[1].trim());
				return pairValueArray[1].trim();
			}
		}
		path = window.location.href;
		let sessionIdTemp = Math.floor(Math.random() * 1000000000);
		document.cookie = "sessionIdSphynx=" + sessionIdTemp + ";max-age=" + COOKIE_EXPIRATION_LENGTH;
		console.log("new session - sessionId = " + sessionIdTemp);
		let startTime = new Date();
		sendSession({
			"sessionId": sessionIdTemp,
			"siteId": siteId,
			"userId": userId,
			"startTime": startTime
		});
		dateLastEvent = new Date();
		sendEvent({
			"type":"START",
			"groupType": "0",
			"timestamp":dateLastEvent,
			"sessionId":sessionIdTemp,
			"siteId": siteId,
			"userId": userId,
			"path": path
		});
		return sessionIdTemp;
	}
	  
	function sendEvent(json) {
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://localhost:8080/save-event", true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(json));
	}
	
	function sendSession(json) {
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://localhost:8080/save-session", true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(json));
	}
	
	function sendQuitEvent(json) {
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://localhost:8080/save-quit", true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(json));
	}
	  
	document.addEventListener("scroll", function() {
		dateLastEvent = new Date();
		scrollTopLast = document.documentElement.scrollTop;
		checkPathChange();
		if (isScrolling === false) {
			sendEvent( {
				"type":"READING",
				"groupType": "2",
				"timestamp":dateLastEvent,
				"sessionId":sessionId,
				"siteId": siteId,
				"userId": userId
			});
			isScrolling = true;
			scrollTopFinish = scrollTopStart = document.documentElement.scrollTop;
		} else {
			if (scrollTopLast > scrollTopFinish) {
				scrollTopFinish = scrollTopLast;
			} else if (scrollTopLast < scrollTopStart) {
				scrollTopStart = scrollTopLast;
			}
			let percentageViewStart = (100 * scrollTopStart / document.documentElement.scrollHeight).toFixed(0);
			let percentageViewFinish = (100 * (scrollTopFinish + window.innerHeight) / document.documentElement.scrollHeight).toFixed(0);
			percentageView = "percentage view: " + percentageViewStart + " - " + percentageViewFinish;
			if (!isReachedEndOfPage && scrollTopLast === (document.documentElement.scrollHeight - window.innerHeight)) {
				isReachedEndOfPage = true;
				sendEvent( {
					"type":"VIEW_ONE_HUNDRED",
					"groupType": "1",
					"timestamp":dateLastEvent,
					"sessionId":sessionId,
					"siteId": siteId,
					"userId": userId
				});
			}
	    }
	});
	  
	document.addEventListener("click", (e) => {
		dateLastEvent = new Date();
		let target = e.target.nodeName + " - " + e.target.innerHTML.trim().slice(0, 10);
		sendEvent( {
			"type":"CLICK",
			"groupType": "1",
			"timestamp":dateLastEvent,
			"sessionId":sessionId,
			"siteId": siteId,
			"userId": userId,
			"target": target
		});
    });

	document.addEventListener("focus", function() {
		document.addEventListener("keydown", sendTypingEvent);
	});
	
	function sendTypingEvent() {
		dateLastEvent = new Date();
		let target = e.target.nodeName + " - " + e.target.innerHTML.trim().slice(0, 10);
		sendEvent({
			"type":"KEYDOWN",
			"groupType": "2",
			"timestamp":dateLastEvent,
			"sessionId":sessionId,
			"siteId": siteId,
			"userId": userId,
			"target": target
		});
		document.removeEventListener("keydown", sendTypingEvent);
	}
	  
	document.addEventListener("visibilitychange", function() {
		if (document.hidden) {
			isScrolling = false;
			dateLastEvent = new Date();
			sendEvent({
				"type":"LEAVE_TAB",
				"groupType": "1",
				"timestamp":dateLastEvent,
				"sessionId":sessionId,
				"siteId": siteId,
				"userId": userId,
				"target": percentageView
			});
		} else {
			dateLastEvent = new Date();
			sendEvent({
				"type":"RETURN_TAB",
				"groupType": "1",
				"timestamp":dateLastEvent,
				"sessionId":sessionId,
				"siteId": siteId,
				"userId": userId
			});
		}
	});	
	
	document.addEventListener("mousemove", function() {
		dateLastEvent = new Date();
	});
	  
	let activeTimer = setTimeout(checkActivity, 30000);
	
	let isInactive = false;
	function checkActivity() {
		let now = new Date();
	    if ((now - dateLastEvent) > 60000) {
	    	if (isInactive == false) {
	    		sendEvent( {
	    			"type":"INACTIVITY",
	    			"groupType": "2",
	    			"timestamp":dateLastEvent,
	    			"sessionId":sessionId,
	    			"siteId": siteId,
	    			"userId": userId
	    		});
	    		isInactive = true;
	    	}
	    	if (now - dateLastEvent > 360000 && isInactive == true) {
	    		isInactive = false;
	    		document.cookie = "sessionIdSphynx=" + sessionId + ";max-age=1";
	    		setTimeout(function() {
	    			sessionId = setSessionId();
	    		}, 2000);
	    		
	    		
	    	}
    		isScrolling = false;
	    	activeTimer = setTimeout(checkActivity, 60000);
	    } else if (isInactive == false) {
	    	activeTimer = setTimeout(checkActivity, 30000);
	    }
	}
	
	function checkPathChange() {
		if (path != window.location.href) {
			dateLastEvent = new Date();
			path = window.location.href;
			isScrolling = false;
			scrollTopStart = 0;
			scrollTopFinish = 0;
			scrollTopLast = 0;
			isReachedEndOfPage = false;
			let percentageViewStart = (100 * scrollTopStart / document.documentElement.scrollHeight).toFixed(0);
			let percentageViewFinish = (100 * (scrollTopFinish + window.innerHeight) / document.documentElement.scrollHeight).toFixed(0);
			percentageView = "percentage view: " + percentageViewStart + " - " + percentageViewFinish;
			sendEvent( {
				"type":"CHANGE_URL",
				"groupType": "1",
				"timestamp": dateLastEvent,
				"sessionId": sessionId,
				"siteId": siteId,
				"userId": userId,
				"target": percentageView,
				"path": path
			});
		}
	}
	
	
	window.addEventListener('beforeunload', function() {
		sendQuitEvent( {
			"type":"QUIT",
			"groupType": "1",
			"timestamp": dateLastEvent,
			"sessionId": sessionId,
			"siteId": siteId,
			"userId": userId,
			"path": path
		});
		document.cookie = "sessionIdSphynx=" + sessionId + ";max-age=1";
	});
})();


