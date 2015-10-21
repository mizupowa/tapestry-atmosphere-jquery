package org.lazan.t5.atmosphere.demo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.lazan.t5.atmosphere.components.PushTarget;
import org.lazan.t5.atmosphere.services.AtmosphereBroadcaster;

public class AjaxUpdate {

    @Inject
    private Block updateBlock;

    @Property
    private boolean updated;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private AtmosphereBroadcaster atmosphereBroadcaster;

    @Component
    private Zone updateZone, zonePlus;

    @Component
    @Property
    private ActionLink updatePush;

    @Component
    @Property
    private PushTarget pushTarget, pushTarget1;

    @Persist
    private int counter;

    @Property
    private String tmpCount;

    @Property
    boolean plus;

    public Block onUpdate(String count){
        tmpCount = count;
        return updateBlock;
    }

    public void onActionFromPlusUpdate(){
        plus = true;
        ajaxResponseRenderer.addRender("zonePlus",zonePlus.getBody());
    }

    public void onActionFromUpdateLink(){
        updated = true;
        ajaxResponseRenderer.addRender(updateZone);
    }

    public void onActionFromUpdatePush(){
        atmosphereBroadcaster.broadcast("testAjaxUpdate",""+counter++);
    }
}
