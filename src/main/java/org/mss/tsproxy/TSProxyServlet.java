/*
 * Copyright (C) 2012 52° North Initiative for Geospatial Open Source Software
 *                   GmbH, Contact: Andreas Wytzisk, Martin-Luther-King-Weg 24,
 *                   48155 Muenster, Germany                  info@52north.org
 *
 * Author: Christian Autermann
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc.,51 Franklin
 * Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.mss.tsproxy;

import static org.mss.tsproxy.Constants.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class TSProxyServlet {
	
	private static final Logger log = LoggerFactory.getLogger(TSProxyServlet.class);
	
	//private static final String ENDPOINT = "http://giv-mss.uni-muenster.de:8081/parliament";
	private static final String ENDPOINT = "http://localhost:8081/parliament";

	private WebApplicationException missingQueryParameter(String name) {
		return new WebApplicationException(Response.status(Status.BAD_REQUEST)
				.type("plain").entity(name + " query parameter is missing")
				.build());
	}

	@GET
	@Produces({JSON})
	public Response get(@QueryParam("datatype") String datatype) {
		Response response = null;
		log.info("Received get Request with parameter datatype = "+ datatype + "!");
		try {
			response = new TSProxy(ENDPOINT).query(datatype);
		} catch (MalformedURLException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@POST
	@Consumes({JSON})
	public Response post(InputStream inputStream) {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer,"UTF-8");
			JSONObject jsonInput = new JSONObject(writer.toString());
			new TSProxy(ENDPOINT).insert(Entry.createFromJSON(jsonInput));
			return Response.ok().type(JSON).entity(jsonInput.toString()).build();
		} catch (IOException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		} catch (JSONException e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}
		
	}
}
