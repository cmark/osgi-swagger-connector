package com.cmark.swagger.publisher;

import javax.ws.rs.Path;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.core.SwaggerContext;
import com.wordnik.swagger.jersey.listing.ApiListingCache;

/**
 * @author mczotter
 */
public class SwaggerResourceTracker extends ServiceTracker<Object, Object> {
    static final String ANY_SERVICE_FILTER = "(objectClass=*)";

    private final BundleContext context;

    public SwaggerResourceTracker(BundleContext context, Filter filter) {
        super(context, filter, null);
        this.context = context;
    }

    @Override
    public Object addingService(ServiceReference<Object> reference) {
        final Object service = context.getService(reference);
        return delegateAddService(reference, service);
    }

    @Override
    public void removedService(ServiceReference<Object> reference, Object service) {
        delegateRemoveService(reference, service);
    }

    private Object delegateAddService(ServiceReference<Object> reference, Object service) {
        Object result = super.addingService(reference);
        if (isJaxRsSwaggerResource(service)) {
        	ApiListingCache.invalidateCache();
            SwaggerContext.registerClassLoader(service.getClass().getClassLoader());
        }
        return result;
    }
    
    private void delegateRemoveService(ServiceReference<Object> reference, Object service) {
    	super.removedService(reference, service);
        if (isJaxRsSwaggerResource(service)) {
        	// TODO unregister from swagger classloader???
        	ApiListingCache.invalidateCache();
        }
    }

    private boolean isJaxRsSwaggerResource(Object service) {
        return service != null && hasRegisterableAnnotation(service);
    }

    private boolean hasRegisterableAnnotation(Object service) {
        boolean result = isRegisterableAnnotationPresent(service.getClass());
        if (!result) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            for (Class<?> type : interfaces) {
                result = result || isRegisterableAnnotationPresent(type);
            }
        }
        return result;
    }

    private boolean isRegisterableAnnotationPresent(Class<?> type) {
        if (type != null) {
            return type.isAnnotationPresent(Path.class) && type.isAnnotationPresent(Api.class);
        }
        return false;
    }

}