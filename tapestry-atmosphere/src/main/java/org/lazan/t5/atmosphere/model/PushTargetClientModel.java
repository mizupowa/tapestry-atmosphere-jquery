package org.lazan.t5.atmosphere.model;

import java.util.Set;

public class PushTargetClientModel {
	private final String nestedComponentId;
	private final String event;
	private final String clientId;
	private final Set<String> topics;
	private final String activePageName;
	private final String containingPageName;

	public PushTargetClientModel(String nestedComponentId, String clientId, String event, Set<String> topics, String activePageName, String containingPageName) {
		super();
		this.activePageName = activePageName;
		this.containingPageName = containingPageName;
		this.nestedComponentId = nestedComponentId;
		this.clientId = clientId;
		this.event = event;
		this.topics = topics;
	}

	public String getNestedComponentId() {
		return nestedComponentId;
	}
	public String getEvent() {
		return event;
	}
	public String getClientId() {
		return clientId;
	}
	public Set<String> getTopics() {
		return topics;
	}

	public String getActivePageName() {
		return activePageName;
	}

	public String getContainingPageName() {
		return containingPageName;
	}
}