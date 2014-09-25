package com.cmark.swagger.publisher;

import java.io.IOException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jersey.JerseyApiReader;
import com.wordnik.swagger.reader.ClassReaders;

/**
 * @author mczotter
 */
public class Activator implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(Activator.class); 
	
	// JAX-RS related properties
	private static final String JAXRS_CONFIG_SERVICE_PID = "com.eclipsesource.jaxrs.connector";
	private static final String JAXRS_SERVICE_ROOT_PROPERTY = "root";
	
	private SwaggerResourceTracker tracker;
	private ServiceRegistration<SwaggerResourceTracker> serviceRegistration;

	@Override
	public void start(BundleContext context) throws Exception {
		openSwaggerServiceTracker(context);
		configureSwagger(context);
		ScannerFactory.setScanner(new DefaultJaxrsScanner());
		ClassReaders.setReader(new JerseyApiReader());
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		tracker.close();
		serviceRegistration.unregister();
	}

	private void openSwaggerServiceTracker(BundleContext context)
			throws InvalidSyntaxException {
		Filter filter = context
				.createFilter(SwaggerResourceTracker.ANY_SERVICE_FILTER);

		tracker = new SwaggerResourceTracker(context, filter);
		serviceRegistration = context.registerService(
				SwaggerResourceTracker.class, tracker, null);
		tracker.open();
	}
	
	private void configureSwagger(BundleContext bundleContext) throws IOException {
		final String basePath = getBasePath(bundleContext);
		final SwaggerConfig config = ConfigFactory.config();
		if (basePath != null && !basePath.isEmpty()) {
			LOG.info("Setting Swagger base path to {}", basePath);
			config.setBasePath(basePath);
		}
		config.setApiVersion("v1.0");
	}

	private String getBasePath(BundleContext context) throws IOException {
		final ServiceReference<ConfigurationAdmin> caRef = context.getServiceReference(ConfigurationAdmin.class);
		String basePath = null;
		if (caRef != null) {
			final ConfigurationAdmin ca = context.getService(caRef);
			if (ca != null) {
				final Configuration config = ca.getConfiguration(JAXRS_CONFIG_SERVICE_PID);
				if (config != null && config.getProperties() != null) {
					basePath = (String) config.getProperties().get(JAXRS_SERVICE_ROOT_PROPERTY);
				}
			}
		}
		return basePath == null ? "http://localhost:8280/services" : basePath;
	}
	
}