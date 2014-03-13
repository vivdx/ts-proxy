package org.mss.tsproxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Util {
	
	
	public static URL generateURL() throws MalformedURLException{
		return	new URL(Constants.RDFNamespaces.MSS.replace("#","/resources/")+UUID.randomUUID().toString());
	}
	
	
}
