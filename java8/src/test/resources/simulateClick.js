/**
 * simulate mouse event on the element
 *
 * arguments[0] {Element} The event target.
 * arguments[1] {String} The name of the event (e.g. 'contextmenu').
 */

// http://stackoverflow.com/questions/6157929/how-to-simulate-a-mouse-click-using-javascript/6158050
// http://www.spookandpuff.com/examples/clickSimulation.html
// http://mouseevents.nerdyjs.com/search/MouseEvents
// https://raw.githubusercontent.com/sergeych/jsnippets/master/simulate_event.js
// http://stackoverflow.com/questions/35834671/similating-a-javascript-click-event-on-a-specific-page
simulate = function(element, eventName) {
	var options = extend(defaultOptions, arguments[2] || {});
	var oEvent, eventType = null;

	for (var name in eventMatchers) {
		if (eventMatchers[name].test(eventName)) {
			eventType = name;
			break;
		}
	}

	if (!eventType)
		throw new SyntaxError('Only HTMLEvents and MouseEvents interfaces are supported');

	if (document.createEvent) {
		oEvent = document.createEvent(eventType);
		if (eventType == 'HTMLEvents') {
			oEvent.initEvent(eventName, options.bubbles, options.cancelable);
		} else {
			oEvent.initMouseEvent(eventName, options.bubbles, options.cancelable, document.defaultView,
				options.button, options.pointerX, options.pointerY, options.pointerX, options.pointerY,
				options.ctrlKey, options.altKey, options.shiftKey, options.metaKey, options.button, element);
		}
		element.dispatchEvent(oEvent);
	} else {
		options.clientX = options.pointerX;
		options.clientY = options.pointerY;
		var evt = document.createEventObject();
		oEvent = extend(evt, options);
		element.fireEvent('on' + eventName, oEvent);
	}
	return element;
}

function extend(destination, source) {
	for (var property in source)
		destination[property] = source[property];
	return destination;
}
// contextmenu  is handled by simulateRightMouseButtonClick.js
var eventMatchers = {
	'HTMLEvents': /^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,
	'MouseEvents': /^(?:click|dblclick|mouse(?:down|up|over|move|out))$/
}
var defaultOptions = {
	pointerX: 0,
	pointerY: 0,
	button: 0,
	ctrlKey: false,
	altKey: false,
	shiftKey: false,
	metaKey: false,
	bubbles: true,
	cancelable: true
}

var element = arguments[0] || document.defaultView;
var eventName = arguments[1] || 'click';

simulate(element, eventName);