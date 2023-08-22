package com.WebSockChat.SockController;

import java.util.ArrayList;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class chatSockHandler extends TextWebSocketHandler{

	//접속 클라이언트 저장
	private ArrayList<WebSocketSession> connClientList = new ArrayList<WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 접속을 하는 경우에 실행
		System.out.println("afterConnectionEstablished() - 채팅서버 접속");
		
		
		for(WebSocketSession conn : connClientList) {
		
			conn.sendMessage( new TextMessage("새로운 사용자가 접속 했습니다."));
			
		}		
		connClientList.add(session); 	// 접속 클라이언트 목록에 저장
		
		System.out.println("접속자 : " + connClientList.size());
		
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 접속중인 클라이언트에서 데이터를 전송하면 실행
		System.out.println("handleTextMessage() - 메세지 전송");
		System.out.println("message.getPayload() : " + message.getPayload());
		
			for(WebSocketSession conn : connClientList) {
				if( !conn.getId().equals(session.getId())) {
					conn.sendMessage( new TextMessage(message.getPayload()));
					
				} 
		}
		
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 접속을 해제하는 경우에 실행
		System.out.println("afterConnectionClosed() - 채팅서버 접속해제");
		connClientList.remove(session);	// 접속 클라이언트 목록에서 삭제
		for(WebSocketSession conn : connClientList) {
			
			conn.sendMessage( new TextMessage("사용자가 접속을 해제 했습니다."));
			
		}
		System.out.println("접속자 : " + connClientList.size());
	}

}
