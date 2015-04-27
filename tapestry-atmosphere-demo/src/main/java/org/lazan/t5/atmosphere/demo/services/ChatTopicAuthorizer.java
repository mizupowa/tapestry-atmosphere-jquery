package org.lazan.t5.atmosphere.demo.services;

import javax.servlet.http.HttpSession;

import org.atmosphere.cpr.AtmosphereResource;
import org.lazan.t5.atmosphere.demo.pages.UpdateTapestryDemo;
import org.lazan.t5.atmosphere.services.TopicAuthorizer;

public class ChatTopicAuthorizer implements TopicAuthorizer {
	public boolean isAuthorized(AtmosphereResource resource, String topic) {
		HttpSession session = resource.getRequest().getSession(false);
		if (session != null) {
			String chatUser = (String) session.getAttribute(ChatConstants.CHAT_USER_SESSION_KEY);
			return chatUser != null || topic.equals("rooms/"+UpdateTapestryDemo.ROOM_NAME+"/messages");
		}
		return topic.equals("rooms/"+UpdateTapestryDemo.ROOM_NAME+"/messages");
	}
}
