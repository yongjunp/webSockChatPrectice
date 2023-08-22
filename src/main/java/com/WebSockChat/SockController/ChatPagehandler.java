package com.WebSockChat.SockController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;

public class ChatPagehandler extends TextWebSocketHandler {

	private ArrayList<WebSocketSession> clientList = new ArrayList<WebSocketSession>();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("채팅 페이지 접속");
		Map<String, Object> sessionAttrs = session.getAttributes();
		String loginId = (String)sessionAttrs.get("loginId");
		
		Gson gson = new Gson();
		HashMap<String, String> msgInfo = new HashMap<String, String>();
		msgInfo.put("msgid", loginId);
		msgInfo.put("msgtype", "c"); //"c":접속, "d":접속해제, "m":채팅
		msgInfo.put("msgcomm", " 접속했습니다.");
		for(WebSocketSession client : clientList) {
			// 새 참여자 접속 안내 메세지 전송
			client.sendMessage(new TextMessage(gson.toJson(msgInfo)));
		}
		clientList.add(session); // 접속한 클라이언트를 목록에 저장
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("채팅 메세지 전송");
		Map<String, Object> sessionAttrs = session.getAttributes();
		String loginId = (String)sessionAttrs.get("loginId");
		Gson gson = new Gson();
		HashMap<String, String> msgInfo = new HashMap<String, String>();
		msgInfo.put("msgid", loginId);
		msgInfo.put("msgcomm", message.getPayload());
		msgInfo.put("msgtype", "m");
		
		
		for(WebSocketSession client : clientList) {
			if(!client.getId().equals(session.getId())) {
				client.sendMessage(new TextMessage(gson.toJson(msgInfo)));
				
			}
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("채팅 페이지 접속 해제");
		clientList.remove(session);
		Map<String, Object> sessionAttrs = session.getAttributes();
		String loginId = (String)sessionAttrs.get("loginId");
		
		Gson gson = new Gson();
		HashMap<String, String> msgInfo = new HashMap<String, String>();
		msgInfo.put("msgid", loginId);
		msgInfo.put("msgtype", "d"); //"c":접속, "d":접속해제, "m":채팅
		msgInfo.put("msgcomm", " 접속 해제 했습니다.");
		for(WebSocketSession client : clientList) {
			// 새 참여자 접속 안내 메세지 전송
			client.sendMessage(new TextMessage(gson.toJson(msgInfo)));
		}
		
	}

	
}
