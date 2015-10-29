package org.lazan.t5.atmosphere.intern;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.AbstractAsset;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetPathConverter;
import org.apache.tapestry5.services.ClasspathAssetAliasManager;
import org.lazan.t5.atmosphere.AtmosphereConstants;

public class AtmosphereAssetFactory implements AssetFactory {

    private final Resource rootResource;
    private final AssetPathConverter converter;
    private final ClasspathAssetAliasManager aliasManager;

    public AtmosphereAssetFactory(AssetPathConverter converter, ClasspathAssetAliasManager aliasManager) {
        this.converter = converter;
        this.aliasManager = aliasManager;
        rootResource = new ClasspathResource(AtmosphereConstants.RESOURCES);
    }

    @Override
    public Resource getRootResource() {
        return rootResource;
    }

    @Override
    public Asset createAsset(final Resource resource) {
        return new AbstractAsset(converter.isInvariant()) {
            public Resource getResource() {
                return resource;
            }

            public String toClientURL() {
                return converter.convertAssetPath(aliasManager.toClientURL(resource.getPath()));
            }
        };
    }
}
