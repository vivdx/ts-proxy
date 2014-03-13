package org.mss.tsproxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.Query;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbn.parliament.jena.joseki.client.RemoteModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mss.tsproxy.Constants;
import org.mss.tsproxy.Constants.RDFNamespacePrefixes;
import org.mss.tsproxy.Constants.RDFNamespaces;
import org.mss.tsproxy.Constants.VariableTypes;

/**
 * Proxy for converting JSON to RDF and for inserting the Entries to the triple
 * store
 * 
 * @author staschc
 * 
 */
public class TSProxy {

	private static final Logger log = LoggerFactory.getLogger(TSProxy.class);
	
	
	
	private String parliamentURL;

	public TSProxy(String parliamentURL) {
		this.parliamentURL = parliamentURL;
	}

	public Response query(String type) throws JSONException, MalformedURLException {
		Collection<Entry> entries = null;
		JSONObject result = new JSONObject();
		JSONArray entriesArray = new JSONArray();
		String typeURL = Constants.QUERY_VARTYPE_MAP.get(type);
		String query = SPARQLQueries.COMMON_QUERY.replace("MSS_TYPE", "<"+typeURL+">");		
		entries = issueSelectQuery(query,typeURL);
		for (Entry entry:entries){
			entriesArray.put(entry.toJSON());
		}
		result.put("Entries", entriesArray);
		System.out.println(result.toString());
		return Response.ok().type(Constants.JSON_TYPE).entity(result.toString()).build();
	}

	public void insert(Entry entry) throws IOException {
		RemoteModel rmParliament = new RemoteModel(parliamentURL + "/sparql",
				parliamentURL + "/bulk");
		try {
			rmParliament.insertStatements(createModel4Entry(entry));
		} catch (IOException e) {
			log.error("Error while inserting entry into parliament", e);
			throw e;
		}
	}

	// TODO change to private method
	private Model createModel4Entry(Entry entry) {
		Model model = null;
		try {
			model = ModelFactory.createDefaultModel();
			// set up namespaces
			model.setNsPrefix(RDFNamespacePrefixes.MSS, RDFNamespaces.MSS);
			model.setNsPrefix(RDFNamespacePrefixes.DCTERMS,
					RDFNamespaces.DCTERMS);
			model.setNsPrefix(RDFNamespacePrefixes.GEOSPARQL,
					RDFNamespaces.GEOSPARQL);
			model.setNsPrefix(RDFNamespacePrefixes.TIME, RDFNamespaces.TIME);

			// create observation data resource
			Resource resource = model.createResource(entry.getSourceUrl()
					.toString());
			Property rdfTypeProp = model.createProperty(RDFNamespaces.RDF,
					"type");
			resource.addProperty(rdfTypeProp, RDFNamespaces.MSS
					+ "ObservationData");
			if (!entry.getLicense().equals(""))
				resource.addProperty(
						model.createProperty(RDFNamespaces.DCTERMS, "license"),
						entry.getLicense());
			if (!entry.getDataFormat().equals(""))
				resource.addProperty(
						model.createProperty(RDFNamespaces.DCTERMS, "format"),
						entry.getDataFormat());
			if (!entry.getPhen().equals(""))
				resource.addProperty(
						model.createProperty(RDFNamespaces.MSS, "represents"),
						entry.getPhen());

			Property varTypeProp = model.createProperty(RDFNamespaces.MSS,
					"ofVariableType");
			Property genByProp = model.createProperty(RDFNamespaces.MSS,
					"generatedBy");
			// set data type and depending on that optional spatial and temporal
			// windows!
			String varType = entry.getDataType().toExternalForm();
			if (varType.contains("PointPattern")) {
				Resource ppObsFunction = model.createResource(Util
						.generateURL().toExternalForm());
				ppObsFunction.addProperty(varTypeProp,
						model.createResource(varType));
				ppObsFunction.addProperty(rdfTypeProp, RDFNamespaces.MSS
						+ "ObservationFunction");
				resource.addProperty(genByProp, ppObsFunction);
				Resource obsWin = null;
				// add ObservedWindow only, if datatype is point pattern!
				if ((varType.contains("Spatial") || varType.contains("Spatio"))
						&& entry.getWktPolygon() != null
						&& !entry.getWktPolygon().equals("")) {
					obsWin = model.createResource(Util.generateURL()
							.toExternalForm());
					Resource geometry = model.createResource(Util.generateURL()
							.toExternalForm());
					geometry.addProperty(rdfTypeProp,
							Constants.RDFNamespaces.GEOSPARQL + "Geometry");
					geometry.addProperty(model.createProperty(
							RDFNamespaces.GEOSPARQL, "asWKT"), entry
							.getWktPolygon());
					obsWin.addProperty(model.createProperty(RDFNamespaces.MSS,
							"observedWhere"), geometry);
				}
				if ((varType.contains("Temporal") || varType
						.contains("temporal"))
						&& (entry.getIsoBegin() != null && !entry.getIsoBegin()
								.equals(""))
						&& (entry.getIsoEnd() != null && !entry.getIsoEnd()
								.equals(""))) {
					if (obsWin == null)
						obsWin = model.createResource(Util.generateURL()
								.toExternalForm());

					Resource begin = model.createResource(Util.generateURL()
							.toExternalForm());
					begin.addProperty(rdfTypeProp, Constants.RDFNamespaces.TIME
							+ "Instant");
					begin.addProperty(model.createProperty(RDFNamespaces.TIME,
							"inXSDDateTime"), entry.getIsoBegin());

					Resource end = model.createResource(Util.generateURL()
							.toExternalForm());
					end.addProperty(rdfTypeProp, Constants.RDFNamespaces.TIME
							+ "Instant");
					end.addProperty(model.createProperty(RDFNamespaces.TIME,
							"inXSDDateTime"), entry.getIsoEnd());

					Resource time = model.createResource(Util.generateURL()
							.toExternalForm());
					time.addProperty(rdfTypeProp, Constants.RDFNamespaces.TIME
							+ "Interval");
					time.addProperty(model.createProperty(RDFNamespaces.TIME,
							"hasBeginning"), begin);
					time.addProperty(
							model.createProperty(RDFNamespaces.TIME, "hasEnd"),
							end);
					obsWin.addProperty(model.createProperty(RDFNamespaces.MSS,
							"observedWhen"), time);
				}
				if (obsWin != null)
					resource.addProperty(model.createProperty(
							RDFNamespaces.MSS, "observedWindow"), obsWin);
			} else if (varType.contains("Geostatistical")) {
				Resource geostatObsFunction = model.createResource(Util
						.generateURL().toExternalForm());
				geostatObsFunction.addProperty(
						varTypeProp,
						model.createResource(RDFNamespaces.MSS
								+ "GeostatisticalVariable"));
				geostatObsFunction.addProperty(rdfTypeProp, RDFNamespaces.MSS
						+ "ObservationFunction");
				resource.addProperty(genByProp, geostatObsFunction);

			} else if (varType.contains("Lattice")) {
				Resource latticeObsFunction = model.createResource(Util
						.generateURL().toExternalForm());
				latticeObsFunction.addProperty(
						varTypeProp,
						model.createResource(RDFNamespaces.MSS
								+ "LatticeVariable"));
				latticeObsFunction.addProperty(rdfTypeProp, RDFNamespaces.MSS
						+ "ObservationFunction");
				resource.addProperty(genByProp, latticeObsFunction);
			} else if (varType.contains("Trajectory")) {
				Resource trajObsFunction = model.createResource(Util
						.generateURL().toExternalForm());
				trajObsFunction.addProperty(varTypeProp,
						model.createResource(RDFNamespaces.MSS + "Trajectory"));
				trajObsFunction.addProperty(rdfTypeProp, RDFNamespaces.MSS
						+ "ObservationFunction");
				resource.addProperty(genByProp, trajObsFunction);

			}
		} catch (MalformedURLException e) {
			log.error("Error while generating RDF from entry", e);
		}
		return model;
	}

	private Collection<Entry> issueSelectQuery(String sparqlSelectQuery, String variableType)
			throws MalformedURLException {
		Collection<Entry> entries = null;
		QueryExecution exec = QueryExecutionFactory.sparqlService(
				this.parliamentURL+"/sparql",
				sparqlSelectQuery);
		ResultSet rs = exec.execSelect();
		entries = new ArrayList<Entry>(rs.getRowNumber());
		while (rs.hasNext()) {
			QuerySolution result = rs.next();
			Resource sourceURL = result.getResource("url");
			Literal phen = result.getLiteral("phen");
			Literal license = result.getLiteral("license");
			Literal format = result.getLiteral("format");
			Entry entry = new Entry(new URL(sourceURL.getURI()),
					format.getString(), new URL(variableType),
					license.getString(), phen.getString(), "", "", "");
			entries.add(entry);
		}
		return entries;

	}

	public static void main(String[] args) {
		Entry entry = Entry.createDefault();
		try {
			TSProxy proxy = new TSProxy("http://giv-mss.uni-muenster.de:8081/parliament");
			proxy.query("GEOST");
			proxy.query("MSTPP");
			proxy.query("SPP");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TSProxy proxy = new TSProxy("http://localhost:8081/parliament");
		// try {
		// proxy.insert(entry);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
