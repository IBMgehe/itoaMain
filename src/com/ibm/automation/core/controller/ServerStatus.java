package com.ibm.automation.core.controller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
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
	public static Logger logger = Logger.getLogger(ServerStatus.class);
	private Session session;
	private static final Set<ServerStatus> connections = new CopyOnWriteArraySet<>();

	public ServerStatus() {
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		connections.add(this);
		logger.info("获取添加主机的状态连接" + session.getId() + "建立");

	}

	@OnClose
	public void onClose() {
		connections.remove(this);
		logger.info("获取添加主机的状态连接" + session.getId() + "关闭");
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		logger.error("获取添加主机的状态连接  Error: "+session.getId()+" " + t.toString(), t);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("获取添加主机的状态连接" + session.getId() + "的消息" + message);
		for (ServerStatus ws : connections) {
			try {
				synchronized (ws) {
					//ws.sendMessage(message);
					ws.session.getBasicRemote().sendText(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("获取添加主机的状态连接" + session.getId() + "的报错消息" + e.getMessage());
				connections.remove(ws);
				 try {
					ws.session.close();
				} catch (Exception e2) {
				}
				 
			}
		}
	}

	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

}
