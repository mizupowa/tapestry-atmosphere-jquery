package org.lazan.t5.atmosphere.demo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.atmosphere.components.Container;
import org.lazan.t5.atmosphere.demo.model.ChatMessage;
import org.lazan.t5.atmosphere.demo.services.ChatManager;

public class UpdateTapestryDemo {


    public static final String ROOM_NAME = "tapestryUpdateRoom";
    private static final String USER_NAME = "testUser";
    @Inject
    private ChatManager chatManager;

    private static int i=0;

    @Component
    private Zone zoneUpdateWithTarget;

    @Inject
    private Block messageBlock;

    @Property
    private String message;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @BeforeRenderTemplate
    public void init(){
        chatManager.joinRoom(ROOM_NAME,USER_NAME);
    }

    public void onActionFromClickForUpdate(){
        chatManager.sendRoomMessage(ROOM_NAME, USER_NAME,"Click on link "+(++i));
    }

    public String getTopic(){
        return ROOM_NAME;
    }

    public Block onUpdate(ChatMessage message){
        this.message = message.getMessage();
        return messageBlock;
    }

    public void onActionFromATestLink(String message){
        chatManager.sendRoomMessage(ROOM_NAME, USER_NAME,"Click on generated link "+(++i));
    }

    public void onActionFromClickForUpdateSecondZone(){
        ajaxResponseRenderer.addRender("zoneUpdateWithTarget",zoneUpdateWithTarget.getBody());
    }

}
