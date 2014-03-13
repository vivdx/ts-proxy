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
 *
 */
public class Entry {
	
	private static final Logger log = LoggerFactory.getLogger(Entry.class);

	private URL sourceUrl;
	private String license;
	private String dataFormat;
	private URL dataType;
	private String phen;
	
	private String wktPolygon;
	private String isoBegin;
	private String isoEnd;

	
	public Entry(URL sourceUrl, String dataFormat, URL type, String license, 
			String phenomenonUri, String wktPolygon, String isoBegin, String isoEnd) {
		this.sourceUrl = sourceUrl;
		this.dataFormat = dataFormat;
		this.dataType = type;
		this.license = license;
		this.phen = phenomenonUri;
		this.wktPolygon = wktPolygon;
		this.isoBegin = isoBegin;
		this.isoEnd = isoEnd;
	}

	public static Entry createFromJSON(JSONObject input){
		Entry entry = null;
		try {
			URL sourceUrl = new URL(input.getString(JsonNames.SOURCE_URL));
			String dataFormat = input.getString(JsonNames.FORMAT);
			String dataTypeString = input.getString(JsonNames.DATA_TYPE);
			URL dataType = new URL(dataTypeString);
			String license = input.getString(JsonNames.LICENSE);
			String phen = input.getString(JsonNames.PHENOMENON);
			String wktPolygon = input.getString(JsonNames.OBS_WIN_WKT);
			String isoBegin = input.getString(JsonNames.OBS_WIN_TMP_START);
			String isoEnd = input.getString(JsonNames.OBS_WIN_TMP_END);
			entry = new Entry(sourceUrl,dataFormat,dataType,license,phen,wktPolygon,isoBegin,isoEnd);
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
					"SHPX",new URL("http://www.meaningfulspatialstatistics.org/theories/MeaningfulSpatialStatistics.owl#MarkedSpatioTemporalPointPattern"),"GPLv2","http://sweet.jpl.nasa.gov/2.2/matrAerosol.owl#PM10","POLYGON(10.1 10.2, 10.3 10.4)","2010-01-01T00:00:00Z","2011-01-01T00:00:00Z"
			);
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
			entryJ.put(JsonNames.DATA_TYPE, this.dataType.toExternalForm());
			entryJ.put(JsonNames.LICENSE, this.license);
			entryJ.put(JsonNames.PHENOMENON, this.phen);
			entryJ.put(JsonNames.OBS_WIN_WKT, this.wktPolygon);
			entryJ.put(JsonNames.OBS_WIN_TMP_START, this.isoBegin);
			entryJ.put(JsonNames.OBS_WIN_TMP_END, this.isoEnd);			
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
	public URL getDataType() {
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
	
	
}
