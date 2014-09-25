package com.cmark.swagger.osgi.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author mczotter
 */
@Path("/hello")
@Api(value = "hello", description = "Simple example how to use Swagger in OSGi context")
public class HelloResource {

	@GET
	@Path("/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation("Say Hello to JAX-RS Swagger")
	@ApiResponses({
		@ApiResponse(code = 200, message = "HTTP OK")
	})
	public String hello(
			@PathParam("name") String name) {
		return "Welcome " + name + " to Swagger OSGi Connector!";
	}
	
}
