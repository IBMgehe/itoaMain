package com.ibm.automation.core.controller;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/updatePlaybookStatus")
public class PlaybookStatus {
		private Session session;
		private static CopyOnWriteArraySet<PlaybookStatus> playbookStatus =  new CopyOnWriteArraySet<PlaybookStatus>();

		public PlaybookStatus() {
		}

		@OnOpen
		public void onOpen(Session session) {
			this.session = session;
			PlaybookStatus.playbookStatus.add(this);
			try {
				Thread.sleep(2000); //睡眠2秒是因为网页跳转到main_log需要连接websocket 太快，获取不了数据
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		@OnMessage
		public void onMessage(String message, Session session) {
			System.out.println("来自客户端"+session.getId()+"的消息" + message);
			
			for (PlaybookStatus ws : playbookStatus) {
				try {
					ws.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		}

		public void sendMessage(String message) throws IOException {
			this.session.getBasicRemote().sendText(message);
		}

		@OnClose
		public void onClose() {
			PlaybookStatus.playbookStatus.remove(this);
			System.out.println("连接"+session.getId()+"关闭");

		}

	}
