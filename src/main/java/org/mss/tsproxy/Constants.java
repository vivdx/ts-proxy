
package org.mss.tsproxy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

/**
 * constants used for JSON and RDF
 * 
 * @author staschc
 * @edit vivek
 */
public class Constants {
	
	public static final String JSON = "application/json";
	public static final MediaType JSON_TYPE = MediaType.valueOf(JSON);
	
	abstract class JsonNames {
		public static final String SOURCE_URL = "sourceURL";
		public static final String FORMAT = "format";
		public static final String DATA_TYPE = "dataType";
		public static final String LICENSE = "license";
		public static final String PHENOMENON = "phenomenon";
		public static final String OBS_WIN_WKT = "obsWindowWKT";
		public static final String OBS_WIN_TMP_START = "obsWindowTempStart";
		public static final String OBS_WIN_TMP_END = "obsWindowTempEnd";
		
		public static final String TITLE = "idTitle";
		public static final String PROJECT= "idProject";
		public static final String INSTITUTE_URL= "idInstituteURL";
		public static final String AUTHOR= "idAuthor";
		public static final String ABSTRACT= "idAbstract";
		public static final String KEYWORD= "idKeyword";
		public static final String CITATION= "idCitation";
		public static final String PARAMETER= "idParameter";
		public static final String UNIT= "idUnit";
		
	}
	
	abstract class RDFProperties {
		public static final String FORMAT = "format";
		public static final String DATA_TYPE = "dataType";
		public static final String LICENSE = "license";
		public static final String PHENOMENON = "phenomenon";
		public static final String OBS_WIN_WKT = "obsWindowWKT";
		public static final String OBS_WIN_TMP_START = "obsWindowTempStart";
		public static final String OBS_WIN_TMP_END = "obsWindowTempEnd";	
		
		public static final String TITLE = "idTitle";
		public static final String PROJECT= "idProject";
		public static final String INSTITUTE_URL= "idInstituteURL";
		public static final String AUTHOR= "idAuthor";
		public static final String ABSTRACT= "idAbstract";
		public static final String KEYWORD= "idKeyword";
		public static final String CITATION= "idCitation";
		public static final String PARAMETER= "idParameter";
		public static final String UNIT= "idUnit";
	}
	
	abstract class RDFNamespacePrefixes{
		public static final String MSS = "mss";
		public static final String DCTERMS = "dcterms";
		public static final String RDF = "rdf";
		public static final String RDFS = "rdfs";
		public static final String GEOSPARQL = "geo";
		public static final String OWL = "owl";
		public static final String TIME = "time";
		public static final String STF = "stf";
		public static final String UCUM = "ucum";
		
	}
	
	abstract class RDFNamespaces{
		public static final String MSS = "http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#";
		public static final String DCTERMS = "http://purl.org/dc/terms/";
		public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		public static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
		public static final String GEOSPARQL = "http://www.opengis.net/ont/geosparql#";
		public static final String OWL = "http://www.w3.org/2002/07/owl#";
		public static final String TIME = "http://www.w3.org/2006/time#";
		public static final String STF = "http://www.geographicknowledge.de/vocab/SpatioTemporalFeature#";
		public static final String UCUM = "http://ontology-of-clinical-research.googlecode.com/svn/trunk/Archive/ucum.owl";
	}
	
	abstract class VariableTypes{
		public static final String PP = RDFNamespaces.MSS+"PointPattern";
		public static final String SPP = RDFNamespaces.MSS+"SpatialPointPattern";
		public static final String MSPP = RDFNamespaces.MSS+"MarkedSpatialPointPattern";
		public static final String TPP = RDFNamespaces.MSS+"TemporalPointPattern";
		public static final String MTPP = RDFNamespaces.MSS+"MarkedTemporalPointPattern";
		public static final String STPP = RDFNamespaces.MSS+"SpatioTemporalPointPattern";
		public static final String MSTPP = RDFNamespaces.MSS+"MarkedSpatioTemporalPointPattern";
		public static final String GEOST = RDFNamespaces.MSS+"GeostatisticalVariable";
		public static final String LAT = RDFNamespaces.MSS+"LatticeVariable";
		public static final String TRAJ = RDFNamespaces.MSS+"Trajectory";
		public static final String MTRAJ = RDFNamespaces.MSS+"MarkedTrajectory";
			
	}
	
	public static final Map<String, String> QUERY_VARTYPE_MAP = createMap();

    private static Map<String, String> createMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("GEOST", VariableTypes.GEOST);
        result.put("PP", VariableTypes.PP);
        result.put("SPP", VariableTypes.SPP);
        result.put("MSPP", VariableTypes.MSPP);
        result.put("TPP", VariableTypes.TPP);
        result.put("MTPP", VariableTypes.MTPP);
        result.put("STPP", VariableTypes.STPP);
        result.put("MSTPP", VariableTypes.MSTPP);
        result.put("LAT", VariableTypes.LAT);
        result.put("TRAJ", VariableTypes.TRAJ);
        result.put("MTRAJ", VariableTypes.MTRAJ);
        return Collections.unmodifiableMap(result);
    }
	
}
