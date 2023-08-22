<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Home</title>
<style>
#chatArea {
	border: 2px solid black;
	padding: 10px;
	width: 500px;
	height: 500px;
	background-color: #6884B3;
	border-radius:10px;
	box-sizing: border-box;
	overflow: overlay;
}
#chatArea::overlay{
	
}

.receiveMsg {
	margin-bottom: 5px;
}

.sendMsg {
	margin-bottom: 5px;
	text-align: right;
}
.commMsg {
	margin-bottom: 5px;
	text-align: center;
}
.sendMsgBox{
	background-color: #F7E600;
	border-radius: 7px;
	max-width: 220px;
	padding:6px;
	
}
.receiveMsgBox{
	background-color: white;
	border-radius: 7px;
	padding:6px;
	margin-top:0px;
	max-width: 220px;
}
div>span{
	margin:5px;
	display:inline-block;
}
.commMsgBox{
	background-color:9FABCA;
	border-radius: 7px;
	padding:6px;
	margin:0;
	min-width: 300px;
}
#inputArea{
	border: 2px solid black;
	padding-left: 10px;
	width: 500px;
	background-color: white;
	border-radius:10px;
	box-sizing: border-box;
}
#inputArea>input{
	width: 390px;
	padding: 10px;
}
#inputArea>button{
	padding: 5px;
}
</style>
</head>
<body>
	<h1>ChatPage.jsp - ${sessionScope.loginId }</h1>
	<button onclick="location.href='${pageContext.request.contextPath}/'">메인페이지</button>
	<hr>
	<div id="chatArea"></div>
	<div id="inputArea">
	<input type="text" id="sendMsg"><button onclick="sendMsg()">전송</button>
	</div>
</body>
<script type="text/javascript">
	let chatAreaDiv = document.querySelector("#chatArea");
	function sendMsg(){
		let msgInput = document.querySelector("#sendMsg");
		sock.send(msgInput.value);
		let sendMsgDiv = document.createElement("div");
		sendMsgDiv.classList.add("sendMsg");
		
		let sendMsgBox = document.createElement("span");
		sendMsgBox.classList.add("sendMsgBox");
		sendMsgBox.innerText = msgInput.value;
		sendMsgDiv.appendChild(sendMsgBox);
		
		chatAreaDiv.appendChild(sendMsgDiv);
		
		msgInput.value = "";
	}
</script>

<script	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script type="text/javascript">
	var sock = new SockJS('chatPage');
	sock.onopen = function() {
		console.log('open');
		//sock.send('test');
	};

	sock.onmessage = function(e) {
		console.log('message', e.data);
		let msgObj = JSON.parse(e.data);
		if(msgObj.msgtype == "m"){
			printMessage(msgObj);
		} else if(msgObj.msgtype == "c" || msgObj.msgtype == "d"){
			printConnect(msgObj);
		}
		
	};

	sock.onclose = function() {
		console.log('close');
	};
</script>
<script type="text/javascript">
	function printMessage(msgObj){
		let receiveMsgDiv = document.createElement("div");
		receiveMsgDiv.classList.add("receiveMsg");
		
		let receiveMsgBox =  document.createElement("span");
		receiveMsgBox.classList.add("receiveMsgBox");
		receiveMsgBox.innerText = msgObj.msgcomm;
		
		let brTag = document.createElement("br");
		
		let nicknameBox = document.createElement("span");
		nicknameBox.classList.add("nickname");
		nicknameBox.innerText = msgObj.msgid;
		
		receiveMsgDiv.appendChild(nicknameBox);
		receiveMsgDiv.appendChild(brTag);
		receiveMsgDiv.appendChild(receiveMsgBox);
		chatAreaDiv.appendChild(receiveMsgDiv);
	}
	function printConnect(msgObj){
		let commMsgDiv = document.createElement("div");
		commMsgDiv.classList.add("commMsg");
		
		let commMsgBox = document.createElement("span");
		commMsgBox.classList.add("commMsgBox");
		commMsgBox.innerText = msgObj.msgid + msgObj.msgcomm;
		
		commMsgDiv.appendChild(commMsgBox);
		chatAreaDiv.appendChild(commMsgDiv);
	}
</script>
</html>