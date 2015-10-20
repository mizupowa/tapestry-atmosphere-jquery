package org.lazan.t5.atmosphere.intern;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.atmosphere.model.ContainerModel;
import org.lazan.t5.atmosphere.model.PushTargetModel;

public class PushTargetUpdate implements Runnable, ContainerModel {

    private JavaScriptSupport javaScriptSupport;

    private List<PushTargetModel> pushTargetModels;

    public PushTargetUpdate(JavaScriptSupport javaScriptSupport) {
        this.javaScriptSupport = javaScriptSupport;
        this.pushTargetModels = new LinkedList<PushTargetModel>();
    }

    @Override
    public void addPushTarget(PushTargetModel model) {
        pushTargetModels.add(model);
    }

    @Override
    public void run() {
        JSONArray config = new JSONArray();
        for (PushTargetModel pushTarget : pushTargetModels) {
            JSONObject targetConfig = new JSONObject();
            targetConfig.put("topics", new JSONArray(pushTarget.getTopics().toArray()));
            targetConfig.put("id", pushTarget.getClientId());
            targetConfig.put("nestedComponentId", pushTarget.getNestedId());
            targetConfig.put("event", pushTarget.getEvent());
            targetConfig.put("update", pushTarget.getUpdateStrategy().name());
            config.put(targetConfig);
        }
        javaScriptSupport.addScript("$('.at-container')[0].atmContainer.updatePushTarget(JSON.parse('%s'))",config.toCompactString());
    }
}
