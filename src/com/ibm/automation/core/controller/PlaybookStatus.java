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

@ServerEndpoint("/updatePlaybookStatus")
public class PlaybookStatus {
	public static Logger logger = Logger.getLogger(PlaybookStatus.class);
	private Session session;
	private static final Set<PlaybookStatus> playbookStatus = new CopyOnWriteArraySet<>();

	public PlaybookStatus() {
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		playbookStatus.add(this);
		logger.info("获取playbook运行状态的ws连接" + session.getId() + "建立");
	}
	@OnClose
	public void onClose() {
		playbookStatus.remove(this);
		logger.info("获取playbook运行状态的ws连接" + session.getId() + "关闭");

	}
	@OnError
	public void onError(Throwable t) throws Throwable {
		logger.error("获取playbook运行状态的ws连接  Error: " +session.getId()+" " + t.toString(), t);
	}
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("获取playbook运行状态的ws连接" + session.getId() + "的消息" + message);
		for (PlaybookStatus ws : playbookStatus) {
			try {
				synchronized (ws) {
					//ws.sendMessage(message);
					ws.session.getBasicRemote().sendText(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("获取playbook运行状态的ws连接" + session.getId() + "的报错消息" + e.getMessage());
				playbookStatus.remove(ws);
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
