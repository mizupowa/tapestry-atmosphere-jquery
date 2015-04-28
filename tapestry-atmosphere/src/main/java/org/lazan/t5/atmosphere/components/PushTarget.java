package org.lazan.t5.atmosphere.components;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.atmosphere.intern.PushTargetUpdate;
import org.lazan.t5.atmosphere.model.ContainerModel;
import org.lazan.t5.atmosphere.model.PushTargetModel;
import org.lazan.t5.atmosphere.model.UpdateStrategy;

public class PushTarget extends Any {
    @Inject
    private ComponentResources resources;

	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	private List<String> topics;
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL, value="replace", required=true)
	private UpdateStrategy update;
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL, required=true)
	private String event;
	
	@Environmental
	private ContainerModel containerModel;

    @Environmental
    private Heartbeat heartbeat;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

	void afterRender() {
        PushTargetModel targetModel = new PushTargetModel(topics, event, update, getClientId(), resources.getNestedId());
        if(environment.peek(ContainerModel.class) == null){
            PushTargetUpdate update = new PushTargetUpdate(javaScriptSupport);
            environment.push(ContainerModel.class,update);
            heartbeat.defer(update);
        }
		containerModel.addPushTarget(targetModel);
	}
}
