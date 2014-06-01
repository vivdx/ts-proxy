package org.mss.tsproxy;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.mss.tsproxy.Constants.JsonNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * represents an entry object in the Meaningful Spatial Statistics portal
 * 
 * @author staschc
 * @edit vivek
 * 
 */
public class Entry {
	
	private static final Logger log = LoggerFactory.getLogger(Entry.class);

	private URL sourceUrl;
	private String license;
	private String dataFormat;
//	private URL dataType;
	private String dataType;
	private String phen;
	
	private String wktPolygon;
	private String isoBegin;
	private String isoEnd;

	private String idTitle;
    private String idProject;
    private URL idInstituteURL;
    private String idAuthor;
    private String idAbstract;
    private String idKeyword;
    private String idCitation;
    private String comment;
	private String idParameter;
    private String idUnit;

		  

	 
	public Entry(URL sourceUrl, String dataFormat, String type, String license, 
			String phenomenonUri, String wktPolygon, String isoBegin, String isoEnd,
			String idTitle, String idProject, URL idInstituteURL , String idAuthor, String idAbstract, String idKeyword, String idCitation,String comment, String idParameter, String idUnit) {
		this.sourceUrl = sourceUrl;
		this.dataFormat = dataFormat;
		this.dataType = type;
		this.license = license;
		this.phen = phenomenonUri;
		this.wktPolygon = wktPolygon;
		this.isoBegin = isoBegin;
		this.isoEnd = isoEnd;
		
		this.idTitle = idTitle;
	    this.idProject = idProject;
	    this.idInstituteURL = idInstituteURL;
	    this.idAuthor = idAuthor;
	    this.idAbstract = idAbstract;
	    this.idKeyword = idKeyword;
	    this.idCitation = idCitation;
	    this.comment = comment;
	    this.idParameter=idParameter;
	    this.idUnit=idUnit;
		
	}
	 public static Entry createFromJSON(JSONObject input){
		Entry entry = null;
		try {
			URL sourceUrl = new URL(input.getString(JsonNames.SOURCE_URL));
			String dataFormat = input.getString(JsonNames.FORMAT);
			String dataTypeString = input.getString(JsonNames.DATA_TYPE);
	//		URL dataType = new URL(dataTypeString);
			String license = input.getString(JsonNames.LICENSE);
			String phen = input.getString(JsonNames.PHENOMENON);
			String wktPolygon = input.getString(JsonNames.OBS_WIN_WKT);
			String isoBegin = input.getString(JsonNames.OBS_WIN_TMP_START);
			String isoEnd = input.getString(JsonNames.OBS_WIN_TMP_END);
			
			String idTitle = input.getString(JsonNames.TITLE);
		    String idProject = input.getString(JsonNames.PROJECT);
		    URL idInstituteURL = new URL(input.getString(JsonNames.INSTITUTE_URL));	    
		    String idAuthor = input.getString(JsonNames.AUTHOR);
		    String idAbstract = input.getString(JsonNames.ABSTRACT);
		    String idKeyword = input.getString(JsonNames.KEYWORD);
		    String idCitation= input.getString(JsonNames.CITATION);
		    String comment= input.getString(JsonNames.COMMENT);
			String idParameter = input.getString(JsonNames.PARAMETER);
		    String idUnit = input.getString(JsonNames.UNIT);
			
			entry = new Entry(sourceUrl,dataFormat,dataTypeString,license,phen,wktPolygon,isoBegin,isoEnd, idTitle, idProject, idInstituteURL, idAuthor, idAbstract, idKeyword, idCitation, comment, idParameter, idUnit);
		} catch (MalformedURLException e) {
			log.error("Error while creating entry from JSON input!",e);
		} catch (JSONException e) {
			log.error("Error while creating entry from JSON input!",e);
		}
		return entry;
	}
	
	public static Entry createFromRDF(){
		return null;	
	}
	
	public static Entry createDefault(){
		Entry entry = null;
		try {
			 entry = new Entry(new URL("http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#PointPattern123"),
					"SHPX","http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#MarkedSpatioTemporalPointPattern","GPLv2","http://sweet.jpl.nasa.gov/2.2/matrAerosol.owl#PM10","POLYGON(10.1 10.2, 10.3 10.4)","2010-01-01T00:00:00Z","2011-01-01T00:00:00Z",
			"Title Meaningful", "Project- Master Thesis", new URL( "http://www.uni-muenster.de/Geoinformatics"),"Vivek","Abstract Unknown","ontology","stasch","compliment", "CO2", "ppm");
		} catch (MalformedURLException e) {
			log.error("Error while creating default entry object!");
		}
		return entry;
	}
	 
	 public JSONObject toJSON(){
		JSONObject entryJ = new JSONObject();
		try {
			entryJ.put(JsonNames.SOURCE_URL,this.sourceUrl.toExternalForm());
			entryJ.put(JsonNames.FORMAT,this.dataFormat);
	//		entryJ.put(JsonNames.DATA_TYPE, this.dataType.toExternalForm());
			entryJ.put(JsonNames.DATA_TYPE, this.dataType);
			entryJ.put(JsonNames.LICENSE, this.license);
			entryJ.put(JsonNames.PHENOMENON, this.phen);
			entryJ.put(JsonNames.OBS_WIN_WKT, this.wktPolygon);
			entryJ.put(JsonNames.OBS_WIN_TMP_START, this.isoBegin);
			entryJ.put(JsonNames.OBS_WIN_TMP_END, this.isoEnd);
			
			entryJ.put(JsonNames.TITLE,this.idTitle);
			entryJ.put(JsonNames.PROJECT,this.idProject);
			entryJ.put(JsonNames.INSTITUTE_URL,this.idInstituteURL.toExternalForm());
			entryJ.put(JsonNames.AUTHOR,this.idAuthor);
			entryJ.put(JsonNames.ABSTRACT,this.idAbstract);
			entryJ.put(JsonNames.KEYWORD,this.idKeyword);
			entryJ.put(JsonNames.CITATION,this.idCitation);
			entryJ.put(JsonNames.COMMENT,this.comment);
			entryJ.put(JsonNames.PARAMETER,this.idParameter);
			entryJ.put(JsonNames.UNIT,this.idUnit);
						
		} catch (JSONException e) {
			
			log.error("Error while encoding entry object ",e);
		}
		return entryJ;
	}

	 	 
	/**
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}

	/**
	 * @return the sourceUrl
	 */
	public URL getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @return the dataFormat
	 */
	public String getDataFormat() {
		return dataFormat;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @return the phenUri
	 */
	public String getPhen() {
		return phen;
	}

	/**
	 * @return the wktPolygon
	 */
	public String getWktPolygon() {
		return wktPolygon;
	}

	/**
	 * @return the isoBegin
	 */
	public String getIsoBegin() {
		return isoBegin;
	}

	/**
	 * @return the isoEnd
	 */
	public String getIsoEnd() {
		return isoEnd;
	}
	
	
	 public String getidTitle() {
		return idTitle;
	 }
		
	public String getidProject() {
		return idProject;
	}
		
	public URL getidInstituteURL() {
		return idInstituteURL;
	}
	public String getidAuthor() {
		return idAuthor;
	}	
	public String getidAbstract() {
		return idAbstract;	
	}
	public String getidKeyword() {
		return idKeyword;					
	}				
	public String getidCitation() {
		return idCitation;	
	}	
	public String getcomment() {
		return comment;	
	}	
	public String getidParameter() {
		return idParameter;						
	}					
	public String getidUnit() {
		return idUnit;	
	}					
	 
	
}
