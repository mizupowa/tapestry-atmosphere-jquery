package org.lazan.t5.atmosphere.intern;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.MarkupWriterListener;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.lazan.t5.atmosphere.AtmosphereConstants;
import org.lazan.t5.atmosphere.components.Container;
import org.lazan.t5.atmosphere.model.ContainerModel;
import org.lazan.t5.atmosphere.model.PushTargetModel;
import org.lazan.t5.atmosphere.services.PageGlobals;

public class ContainerPageRenderFilter implements MarkupRendererFilter {

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private BaseURLSource baseUrlSource;

    @Inject
    @Symbol("atmosphere.secure")
    private boolean secure;

    @Inject
    private ApplicationGlobals applicationGlobals;

    @Inject
    @Symbol("atmosphere.uri")
    private String uri;

    @Inject
    @Symbol(AtmosphereConstants.FULL_JS_OPTIONS)
    private JSONObject options;

    @Inject
    @Symbol(AtmosphereConstants.USE_FULL_JS_CONTAINER)
    private boolean useFullJS;

    @Inject
    @Symbol(AtmosphereConstants.FULL_JS_TRANSPORT)
    private String transport;

    @Inject
    @Symbol(AtmosphereConstants.FULL_JS_FALLBACK_TRANSPORT)
    private String fallbackTransport;

    @Inject
    @Symbol(AtmosphereConstants.FULL_JS_LOG_LEVEL)
    private String logLevel;

    @Inject
    private PageGlobals pageGlobals;

    @Override
    public void renderMarkup(final MarkupWriter writer, MarkupRenderer renderer) {
        if(useFullJS) {
            final List<PushTargetModel> pushTargets = CollectionFactory.newList();
            final LinkedHashSet<String> topics = new LinkedHashSet<String>();
            environment.push(ContainerModel.class, new ContainerModel() {
                public void addPushTarget(PushTargetModel pushTarget) {
                    pushTargets.add(pushTarget);
                    topics.addAll(pushTarget.getTopics());
                }
            });

            final String clientId = javaScriptSupport.allocateClientId("at-container");
            writer.addListener(new MarkupWriterListener() {
                @Override
                public void elementDidStart(Element element) {
                    if("body".equalsIgnoreCase(element.getName())){
                        writer.element("div","id",clientId,"class","at-container");
                        writer.end();
                    }
                }

                @Override
                public void elementDidEnd(Element element) {

                }
            });
            renderer.renderMarkup(writer);

            JSONObject config = Container.createConfig(baseUrlSource, secure, applicationGlobals, uri, options, transport, fallbackTransport, logLevel, clientId, pageGlobals, pushTargets);
            javaScriptSupport.addInitializerCall("atmosphereContainer", config);
            environment.pop(ContainerModel.class);
        }else{
            renderer.renderMarkup(writer);
        }
    }
}
