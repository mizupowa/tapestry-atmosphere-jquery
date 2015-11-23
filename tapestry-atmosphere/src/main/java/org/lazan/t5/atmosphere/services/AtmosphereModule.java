package org.lazan.t5.atmosphere.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.atmosphere.cpr.*;
import org.lazan.t5.atmosphere.AtmosphereConstants;
import org.lazan.t5.atmosphere.intern.AtmosphereAssetFactory;
import org.lazan.t5.atmosphere.intern.ContainerPageRenderFilter;
import org.lazan.t5.atmosphere.intern.FullJSContainerStack;
import org.lazan.t5.atmosphere.intern.JSContainerWorker;
import org.lazan.t5.atmosphere.services.internal.*;
import org.slf4j.Logger;

public class AtmosphereModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(AtmosphereManager.class, AtmosphereManagerImpl.class);
        binder.bind(PageGlobals.class, PageGlobalsImpl.class);
        binder.bind(AtmosphereHandler.class, AtmosphereHandlerImpl.class);
        binder.bind(AtmosphereHttpServletRequestFilter.class, AtmosphereHttpServletRequestFilter.class);
        binder.bind(AtmosphereResourceEventListener.class, AtmosphereResourceEventListenerImpl.class);
        binder.bind(AtmosphereSessionManager.class, AtmosphereSessionManagerImpl.class);
        binder.bind(AtmosphereBroadcaster.class, AtmosphereBroadcasterImpl.class);
        binder.bind(TopicAuthorizer.class, TopicAuthorizerImpl.class);
        binder.bind(TopicListener.class, TopicListenerImpl.class);

        // note that the concrete class is required here
        binder.bind(PerRequestBroadcastFilterImpl.class, PerRequestBroadcastFilterImpl.class);
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> config) {
        config.add("atmosphere.logLevel", "debug");
        config.add("atmosphere.transport", "websocket");
        config.add("atmosphere.fallbackTransport", "long-polling");
        config.add("atmosphere.uri", "atmosphere");
        config.add("atmosphere.secure", "false");
        config.add(AtmosphereConstants.USE_FULL_JS_CONTAINER,false);
        config.add(AtmosphereConstants.FULL_JS_TRANSPORT,"websocket");
        config.add(AtmosphereConstants.FULL_JS_FALLBACK_TRANSPORT,"long-polling");
        config.add(AtmosphereConstants.FULL_JS_LOG_LEVEL,"debug");
        config.add(AtmosphereConstants.FULL_JS_OPTIONS,new JSONObject());
    }

    public static AtmosphereFramework buildAtmosphereFramework(AtmosphereHttpServletRequestFilter atmosphereFilter) {
        return atmosphereFilter.getAtmosphereFramework();
    }

    public static BroadcasterFactory buildBroadcasterFactory() {
        return new DefaultBroadcasterFactory();
    }

    public static AtmosphereResourceFactory buildAtmosphereResourceFactory() {
        return new DefaultAtmosphereResourceFactory();
    }

    public static void contributeHttpServletRequestHandler(
            OrderedConfiguration<HttpServletRequestFilter> configuration,
            AtmosphereHttpServletRequestFilter atmosphereFilter) {
        configuration.add("atmosphere", atmosphereFilter, "before:GZIP");
    }

    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration){
        configuration.addInstance("fullJS", ContainerPageRenderFilter.class,"after:*");
    }


    public static void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration){

    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration, Logger log) {
        log.info("Registering atmosphere component library");
        configuration.add(new LibraryMapping("atmos", "org.lazan.t5.atmosphere"));
    }

    public static void contributeAssetSource(MappedConfiguration<String, AssetFactory> configuration, AssetPathConverter assetPathConverter, ClasspathAssetAliasManager aliasManager) {
        configuration.add("atmos", new AtmosphereAssetFactory(assetPathConverter, aliasManager));
    }

    /**
     * Create a worker for include automatically in each page some stack
     *
     * @param configuration
     */
    @Contribute(ComponentClassTransformWorker2.class)
    @Primary
    public static void addWorker(OrderedConfiguration<ComponentClassTransformWorker2> configuration) {
        configuration.addInstance("FullJSWorker", JSContainerWorker.class, "before:Import", "after:RenderPhase");
    }

    public static void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> config) {
        config.addInstance("PageGlobals", PageGlobalsComponentRequestFilter.class, "before:*");
    }

    public static void contributeAtmosphereHttpServletRequestFilter(MappedConfiguration<String, String> config) {
        config.add(ApplicationConfig.OBJECT_FACTORY, TapestryAtmosphereObjectFactory.class.getName());
        config.add(ApplicationConfig.BROADCAST_FILTER_CLASSES, PerRequestBroadcastFilterImpl.class.getName());
        config.add(ApplicationConfig.PROPERTY_SESSION_SUPPORT, "true");
        config.add(ApplicationConfig.JSR356_MAPPING_PATH, "/atmosphere");
    }

    /**
     * Create JS ans CSS stack
     *
     * @param configuration
     */
    public static void contributeJavaScriptStackSource(MappedConfiguration<String, JavaScriptStack> configuration) {
        configuration.addInstance(AtmosphereConstants.FULL_JS_CONTAINER_STACK, FullJSContainerStack.class);
    }

    @Startup
    public static void configureAtmosphereFramework(AtmosphereFramework framework, AtmosphereHandler handler) {
        framework.addAtmosphereHandler("/", handler);
    }
}
