package org.lazan.t5.atmosphere.intern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

public class FullJSContainerStack implements JavaScriptStack {

    private final List<StylesheetLink> cssStack;
    private final List<Asset> jsAssets;

    public FullJSContainerStack(final AssetSource assetSource) {
        final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>() {
            public Asset map(String path) {
                return assetSource.getExpandedAsset(path);
            }
        };


        cssStack = new ArrayList<StylesheetLink>();
        jsAssets = F.flow("atmos:/components/atmosphere.js", "atmos:/components/tapestry-atmosphere.js", "atmos:/components/full-js-container.js").map(pathToAsset).toList();
    }


    @Override
    public List<String> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        return jsAssets;
    }

    @Override
    public List<StylesheetLink> getStylesheets() {
        return cssStack;
    }

    @Override
    public String getInitialization() {
        return null;
    }
}
