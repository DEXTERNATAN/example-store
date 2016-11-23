package org.demoiselle.jee7.example.store.sale.service;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.demoiselle.jee.core.message.DemoiselleMessage;
import org.demoiselle.jee7.example.store.sale.configuration.AppConfiguration;

import io.swagger.annotations.Api;

@Path("info")
@Api("System Information")
public class InfoREST {

	@Inject
	private DemoiselleMessage messages;

	@Inject
	private AppConfiguration configuration;

	@GET
	@Path("version")
	@Produces("text/plain")
	public String getMessage() throws Exception {
		return messages.version();
	}

	@GET
	@Path("search/url")
	@Produces("text/plain")
	public String getSearchUrl() throws Exception {
		return configuration.getAppSearchUrl();
	}

}