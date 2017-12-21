// polyfill
import 'babel-polyfill';

import Vue from 'vue';
import App from './App';
import store from './store';

Vue.config.devtools = true;

new Vue({
	el: 'body',
	components: {App},
	store: store
});


Vue.prototype.websocket = null;

Vue.prototype.initWebSocket = function (username) {
	var self = this;
	//判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		self.websocket = new WebSocket("ws://127.0.0.1:8089/websocket/" + username);
	} else {
		alert('请使用Chrome33+或FireFox最新版!');
	}

	//连接发生错误的回调方法
	self.websocket.onerror = function () {
		console.log("消息服务发送未知错误！");
	};

	//连接成功建立的回调方法
	self.websocket.onopen = function (event) {
		console.log("消息服务链接建立成功！");
	};

	//接收到消息的回调方法
	self.websocket.onmessage = function (event) {
		self.notifyMe(event.data);
	};

	//连接关闭的回调方法
	self.websocket.onclose = function () {
		console.log("消息服务链接关闭");
	};

	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function () {
		self.websocket.close();
	}
};

Vue.prototype.notifyMe = function (messageJson) {
	var message = JSON.parse(messageJson);
	var self = this;
	if (!("Notification" in window)) {
		alert("This browser does not support desktop notification");
	} else if (Notification.permission === "granted") {
		self.showNotification(message.title, message.content, message.headImg, message.clickUrl);
	} else if (Notification.permission !== "denied") {
		Notification.requestPermission(function (permission) {
			if (permission === "granted") {
				self.showNotification(message.title, message.content, message.headImg, message.clickUrl);
			}
		});
	}
};

Vue.prototype.showNotification = function (theTitle, theBody, theIcon, clickUrl) {
	var options = {
		body: theBody,
		icon: theIcon
	};
	var notification = new Notification(theTitle, options);
	if (clickUrl != null && "" !== clickUrl) {
		notification.onclick = function (event) {
			event.preventDefault();
			window.open(clickUrl, '_blank');
		}
	}
};