/**
 * 
 */

sphynx();
function sphynx() {
	
	let dateLastEvent = new Date();
	let COOKIE_EXPIRATION_LENGTH = 86400;
	
	let userId = "3001";
	let siteId = "1301";
	let path = window.location.href;
	let sessionId = setSessionId();
	
	function setSessionId() {
		let cookieArray = document.cookie.split(";");
		for (let i = 0; i < cookieArray.length; i++) {
			let pairValueArray = cookieArray[i].split("=");
			if (pairValueArray[0].trim() === "sessionIdSphynx") {
				console.log("already exist - sessionId = " + pairValueArray[1].trim());
				return pairValueArray[1].trim();
			}
		}
		let sessionId = Math.floor(Math.random() * 1000000000);
		document.cookie = "sessionIdSphynx=" + sessionId + ";max-age=" + COOKIE_EXPIRATION_LENGTH;
		console.log("new session - sessionId = " + sessionId);
		sendSession({
			"sessionId": sessionId,
			"siteId": siteId,
			"userId": userId
		});
		return sessionId;
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
   
	let isScrolling = false;
	let scrollTopStart = 0;
	let scrollTopFinish = 0;
	let scrollTopLast = 0;
	let percentageViewStart = 0;
	let percentageViewFinish = 0;
	let isReachedEndOfPage = false;
	  
	document.addEventListener("scroll", function() {
		dateLastEvent = new Date();
		scrollTopLast = document.documentElement.scrollTop;
		if (isScrolling === false) {
			sendEvent( {
				"type":"READING",
				"groupType": "2",
				"timestamp":dateLastEvent,
				"sessionId":sessionId,
				"siteId": siteId,
				"userId": userId,
				"path": path
			});
			isScrolling = true;
			scrollTopFinish = scrollTopStart = document.documentElement.scrollTop;
		} else {
			setTimeout(function() {
				sendEvent( {
					"type":"READING",
					"groupType": "2",
					"timestamp":dateLastEvent,
					"sessionId":sessionId,
					"siteId": siteId,
					"userId": userId,
					"path": path
				});
			}, 60000);
			if (scrollTopLast > scrollTopFinish) {
				scrollTopFinish = scrollTopLast;
			} else if (scrollTopLast < scrollTopStart) {
				scrollTopStart = scrollTopLast;
			}
			percentageViewStart = (100 * scrollTopStart / document.documentElement.scrollHeight).toFixed(0);
			percentageViewFinish = (100 * (scrollTopFinish + window.innerHeight) / document.documentElement.scrollHeight).toFixed(0);
			if (!isReachedEndOfPage && scrollTopLast === (document.documentElement.scrollHeight - window.innerHeight)) {
				isReachedEndOfPage = true;
				let percentageView = "percentage view: " + percentageViewStart + " - " + percentageViewFinish;
				sendEvent( {
					"type":"VIEW_ONE_HUNDRED",
					"groupType": "1",
					"timestamp":dateLastEvent,
					"sessionId":sessionId,
					"siteId": siteId,
					"userId": userId,
					"path": path,
					"target": percentageView
				});
			}
	    }
	});
	  
	document.addEventListener("mousemove", function() {
		dateLastEvent = new Date();
	});
	  
	let activeTimer = setTimeout(checkActivity, 30000);
	  
	function checkActivity() {
		let now = new Date();
	    if ((now - dateLastEvent) > 60000) {
	    	printInactiveEvent();
	    	isScrolling = false;
	    	activeTimer = setTimeout(checkActivity, 60000);
	    } else {
	    	activeTimer = setTimeout(checkActivity, 30000);
	    }
	}
	  
	function printInactiveEvent() {
		let percentageView = "percentage view: " + percentageViewStart + " - " + percentageViewFinish;
		sendEvent( {
			"type":"INACTIVITY",
			"groupType": "2",
			"timestamp":dateLastEvent,
			"sessionId":sessionId,
			"siteId": siteId,
			"userId": userId,
			"path": path,
			"target": percentageView
		});
	}

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
			"path": path,
			"target": target
		});
    })

	
// only when change focus
	document.addEventListener("keydown", function() {
		dateLastEvent = new Date();
		sendEvent({
			"type":"KEYDOWN",
			"groupType": "2",
			"timestamp":dateLastEvent,
			"sessionId":sessionId,
			"siteId": siteId,
			"userId": userId,
			"path": path
		});
	});
	  
	document.addEventListener("visibilitychange", function() {
		if (document.hidden) {
			isScrolling = false;
			let dateLeaving = new Date();
			let percentageView = "percentage view: " + percentageViewStart + " - " + percentageViewFinish;
			sendEvent({
				"type":"LEAVE_TAB",
				"groupType": "1",
				"timestamp":dateLastEvent,
				"sessionId":sessionId,
				"siteId": siteId,
				"userId": userId,
				"path": path,
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
				"userId": userId,
				"path": path
			});
		}
	});	
}

/*
 * 
 * 	function getMonthAndDay(date) {
		return (date.getMonth() + 1) + "/" + date.getDate();
	}
	  
	function getTime(date) {
		return (date.getHours() < 10 ? '0' : '') + date.getHours() + ":" + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes() + ":" + (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
	}
 * 
 * 
 * 
 * 	function getContent(element) {
		let content = null;
		if (element.tagName != null) {
		    if (element.tagName.toLowerCase() === "img") {
		    	content = element.alt;
		    } else if (element.tagName.toLowerCase() === "a") {
		    	content = element.innerHTML.trim().slice(0, 10);
		    	if (content === null) {
		    		content = element.href;
		    	}
		    } else if (element.tagName.toLowerCase() === "button") {
		    	content = element.value;
		    }
		}
	    if ((content === null || content === "") && element.innerHTML != null) {
	    	content = element.innerHTML.trim().slice(0, 10);
	    }
	    if (content === null || content === "") {
			content = element.name;
		}
		if (content === null || content === "") {
			content = element.id;
	    }
	    return content;
	}
	*/
