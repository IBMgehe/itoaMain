package com.ibm.automation.core.controller;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

/*
 * 这个类主要用于添加主机的时候更新table 中这行的状态
 */
@ServerEndpoint("/updateServerStatus")
public class ServerStatus {
	private Session session;
	private static final CopyOnWriteArraySet<ServerStatus> serverStatus = new CopyOnWriteArraySet<ServerStatus>();
	public static Logger logger = Logger.getLogger(ServerStatus.class);
	public ServerStatus() {
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		serverStatus.add(this);
		try {
			Thread.sleep(500); // 睡眠2秒是因为网页跳转到main_log需要连接websocket 太快，获取不了数据
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("来自客户端" + session.getId() + "的消息" + message);
		logger.info("来自客户端" + session.getId() + "的消息" + message);
		for (ServerStatus ws : serverStatus) {
			try {
				synchronized (ws) {
					ws.sendMessage(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("来自客户端" + session.getId() + "的报错消息" + e.getMessage());
				serverStatus.remove(ws);
				/*try {
					ws.session.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				continue;
			}
		}
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	@OnClose
	public void onClose() {
		serverStatus.remove(this);
		System.out.println("连接" + session.getId() + "关闭");
		logger.info("来自客户端" + session.getId() + "关闭");

	}
}
