package org.mss.tsproxy;

/**
 * TODO quick hack; read from txt file or directly use JENA later
 * 
 * @author staschc
 *
 */
public class SPARQLQueries {
	
	public static final String COMMON_QUERY =  "PREFIX mss: <http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#>"
			+ "PREFIX dcterms: <http://purl.org/dc/terms/>"
			+ "SELECT DISTINCT"
			+ "?url ?phen ?license ?format "
			+ "WHERE {"
			+ "?url mss:generatedBy ?obsFun; mss:represents ?phen; dcterms:license ?license; dcterms:format ?format."
			+ "?obsFun  mss:ofVariableType MSS_TYPE."
			+ "}";

}
