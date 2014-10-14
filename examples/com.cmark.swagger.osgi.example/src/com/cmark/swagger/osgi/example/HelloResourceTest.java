/*******************************************************************************
 * Copyright (c) 2014 B2i Healthcare. All rights reserved.
 *******************************************************************************/
package com.cmark.swagger.osgi.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Assert;
import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.RequestContext;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

/**
 * @author mczotter
 * @since 1.0
 */
@RunWith(HttpJUnitRunner.class)
public class HelloResourceTest {

	private static final String MARK = "Mark";

	@Rule
	public Destination destination = getDestination();

	@Context
	private Response response;

	@HttpTest(method = Method.GET, path = "/services/hello/{name}")
	public void helloResourceShouldBeDeployedProperly() throws Exception {
		Assert.assertOk(response);
		printBody();
		assertEquals("Welcome " + MARK + " to Swagger OSGi Connector!", response.getBody());
	}

	@HttpTest(method = Method.GET, path = "/services/api-docs")
	public void environmentShouldProvideGlobalDocumentation() throws Exception {
		Assert.assertOk(response);
		printBody();
		assertTrue(response.getBody().contains("apiVersion"));
	}
	
	@HttpTest(method = Method.GET, path = "/services/api-docs/hello")
	public void helloResourceShouldProvideSwaggerDocumentation() throws Exception {
		Assert.assertOk(response);
		printBody();
		assertTrue(response.getBody().contains("resourcePath"));
	}
	
	private void printBody() {
		System.out.println(response.getBody());
	}

	private Destination getDestination() {
		final Destination destination = new Destination(this, "http://localhost:8280");
		final RequestContext context = destination.getRequestContext();
		context.addPathSegment("name", MARK);
		return destination;
	}

}
