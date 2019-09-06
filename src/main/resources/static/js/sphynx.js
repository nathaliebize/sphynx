/**
 * 
 */



let elements = document.body.getElementsByTagName("*");
let dateLastEvent = new Date();

for (let element of elements) {
	let childNodes = element.childNodes;
    let isLastNode = true;
    for (let node of childNodes) {
    	if (node.nodeType != Node.TEXT_NODE) {
    		isLastNode = false;
        }
    }
    if (isLastNode) {
        element.addEventListener("click", function() {
        	dateLastEvent = new Date();
        	let content = getContent(this);
        	console.log("clicks on " + this.tagName + " - " + content + " the " + getMonthAndDay(dateLastEvent)
        			+ " at " + getTime(dateLastEvent));
        });
    }       
};
  
function getContent(element) {
	let content = null;
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
    if (content === null || content === "") {
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
		console.log("scrolls page starts on the " + getMonthAndDay(dateLastEvent)
				+ " at " + getTime(dateLastEvent));
		isScrolling = true;
		scrollTopFinish = scrollTopStart = document.documentElement.scrollTop;
	} else {
		if (scrollTopLast > scrollTopFinish) {
			scrollTopFinish = scrollTopLast;
		} else if (scrollTopLast < scrollTopStart) {
			scrollTopStart = scrollTopLast;
		}
		percentageViewStart = (100 * scrollTopStart / document.documentElement.scrollHeight).toFixed(0);
		percentageViewFinish = (100 * (scrollTopFinish + window.innerHeight) / document.documentElement.scrollHeight).toFixed(0);
		if (!isReachedEndOfPage && scrollTopLast === (document.documentElement.scrollHeight - window.innerHeight)) {
			isReachedEndOfPage = true;
			console.log("reaches end of page at " + getTime(dateLastEvent));
		}
    }
});
  
document.addEventListener("mousemove", function() {
	dateLastEvent = new Date();
	document.cookie="mouse=" + dateLastEvent;
});
  
let activeTimer = setTimeout(checkActivity, 30000);
  
function checkActivity() {
	let now = new Date();
    if ((now - dateLastEvent) > 60000) {
    	printInactiveEvent();
    	isScrolling = false;
    }
    activeTimer = setTimeout(checkActivity, 30000);
}
  
function printInactiveEvent() {
	console.log("is inactive since " + getTime(dateLastEvent)
			+ " - percentage view: " 
			+ percentageViewStart + " - " + percentageViewFinish);   
}

document.addEventListener("keydown", function() {
	dateLastEvent = new Date();
	console.log("presses keyboard on the " + getMonthAndDay(dateLastEvent)
			+ " at " + getTime(dateLastEvent));
});
  
document.addEventListener("visibilitychange", function() {
	if (document.hidden) {
		let dateLeaving = new Date();
		isScrolling = false;
		console.log("switches to another tab the " + getMonthAndDay(dateLeaving)
				+ " at " + getTime(dateLeaving) + " - percentage view: " 
      + percentageViewStart + " - " + percentageViewFinish);
     
	} else {
		dateLastEvent = new Date();
		console.log("comes back to sites tab the " + getMonthAndDay(dateLastEvent)
			  + " at " + getTime(dateLastEvent));
	}
});
  
function getMonthAndDay(date) {
	return (date.getMonth() + 1) + "/" + date.getDate();
}
  
function getTime(date) {
	return (date.getHours() < 10 ? '0' : '') + date.getHours() + ":" + (date.getMinutes() < 10 ? '0' : '') + date.getMinutes() + ":" + (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();
}

