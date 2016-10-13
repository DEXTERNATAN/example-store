package org.demoiselle.store.usuario.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.demoiselle.jee.core.api.security.DemoisellePrincipal;
import org.demoiselle.jee.core.api.security.SecurityContext;
import org.demoiselle.jee.core.api.security.Token;
import org.demoiselle.jee.security.annotation.LoggedIn;
import org.demoiselle.jee.security.annotation.RequiredPermission;
import org.demoiselle.jee.security.annotation.RequiredRole;
import org.demoiselle.jee.security.exception.DemoiselleSecurityException;
import org.demoiselle.jee.security.message.DemoiselleSecurityMessages;
import org.demoiselle.store.usuario.security.Credentials;
import org.jose4j.json.internal.json_simple.JSONObject;

import io.swagger.annotations.Api;

@Path("security")
@Api("Segurança")
@Produces({ MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON })
@Consumes(MediaType.APPLICATION_JSON)
public class SecurityREST {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private DemoisellePrincipal loggedUser;

	@Inject
	private Token token;

	@Inject
	private DemoiselleSecurityMessages bundle;

	@SuppressWarnings("unchecked")
	public JSONObject loggedUserObject() {
		JSONObject json = new JSONObject();
		json.put("identity", loggedUser.getIdentity());
		json.put("name", loggedUser.getName());
		json.put("roles", loggedUser.getRoles());
		json.put("permissions", loggedUser.getPermissions());
		return json;
	}
	
	@GET
	@Path("sem")
	@SuppressWarnings("unchecked")
	public Response testeSem() {
		JSONObject json = new JSONObject();
		json.put("message", "Foi sem");		
		return Response.ok(json).build();
	}

	@GET
	@Path("com")
	@LoggedIn
	public Response testeCom() {
		return Response.ok(loggedUserObject()).build();
	}

	@GET
	@Path("role/ok")
	@RequiredRole("ADMINISTRATOR")
	public Response testeRoleOK() {
		return Response.ok(loggedUserObject()).build();
	}

	@GET
	@Path("role/error")
	@RequiredRole("USUARIO")
	public Response testeRoleErro() {
		return Response.ok(loggedUserObject()).build();
	}

	@GET
	@Path("permission/ok")
	@RequiredPermission(resource = "Categoria", operation = "Consultar")
	public Response testePermissionOK() {
		return Response.ok(loggedUserObject()).build();
	}

	@GET
	@Path("permission/error")
	@RequiredPermission(resource = "Produto", operation = "Incluir")
	public Response testePermissionErro() {
		return Response.ok(loggedUserObject()).build();
	}

	@POST
	@Path("login")
	public String testeLogin(Credentials credentials) {
		if (credentials.getUsername().equalsIgnoreCase("Gladson")
				&& credentials.getPassword().equalsIgnoreCase("123456")) {
			loggedUser.setName(credentials.getUsername());
			loggedUser.setIdentity("" + System.currentTimeMillis());
			ArrayList<String> roles = new ArrayList<>();
			roles.add("ADMINISTRATOR");
			roles.add("MANAGER");
			Map<String, String> permissions = new HashMap<>();
			permissions.put("Produto", "Alterar");
			permissions.put("Categoria", "Consultar");
			loggedUser.setRoles(roles);
			loggedUser.setPermissions(permissions);
			securityContext.setUser(loggedUser);
		} else {
			throw new DemoiselleSecurityException(bundle.invalidCredentials(), 401);
		}
		return token.getKey();
	}
}
