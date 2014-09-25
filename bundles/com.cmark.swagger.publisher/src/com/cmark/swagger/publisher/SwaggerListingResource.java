package com.cmark.swagger.publisher;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.servlet.WebConfig;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;

/**
 * For debugging purposes only
 */
@Path("/api-docs")
@Api("/api-docs")
@Produces(MediaType.APPLICATION_JSON)
public class SwaggerListingResource extends ApiListingResourceJSON {

	@GET
	@Override
	public Response resourceListing(@Context Application app, @Context WebConfig wc,
			@Context HttpHeaders headers, @Context UriInfo uriInfo) {
		System.out.println("High level resources");
		return super.resourceListing(app, wc, headers, uriInfo);
	}
	
	@GET
	@Path("/{route: .+}")
	@Override
	public Response apiDeclaration(@PathParam("route") String route, @Context Application app, @Context  WebConfig wc,
			@Context HttpHeaders headers, @Context UriInfo uriInfo) {
		System.out.println("API listing");
		return super.apiDeclaration(route, app, wc, headers, uriInfo);
	}
	
}
