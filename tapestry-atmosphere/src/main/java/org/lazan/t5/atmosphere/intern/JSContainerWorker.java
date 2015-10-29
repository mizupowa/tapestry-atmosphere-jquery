package org.lazan.t5.atmosphere.intern;

import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.lazan.t5.atmosphere.AtmosphereConstants;

public class JSContainerWorker implements ComponentClassTransformWorker2 {

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    @Symbol(AtmosphereConstants.USE_FULL_JS_CONTAINER)
    private boolean fullJS;

    public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model) {
        final PlasticMethod setupRender = plasticClass.introduceMethod(TransformConstants.SETUP_RENDER_DESCRIPTION);

        setupRender.addAdvice(new MethodAdvice() {

            public void advise(MethodInvocation invocation) {
                if(fullJS) {
                    javaScriptSupport.importStack(AtmosphereConstants.FULL_JS_CONTAINER_STACK);
                }
                invocation.proceed();
            }
        });
        model.addRenderPhase(SetupRender.class);
    }
}
