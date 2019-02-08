package kr.sys4u.httpserver;

public class StatusLineParser {

	private final String statusLine;
	private String url;
	private boolean hasExtension;
	private String extension;
	private String fileName;

	public StatusLineParser(String statusLine) {
		
		this.statusLine = statusLine;
		
		
	}
	public void parse() {
		
		url = statusLine.split(" ")[1];
		if (url==null) {
			return;
		}
		
		fileName = url.substring(this.url.lastIndexOf("/"));
		String[] parsed = fileName.split("\\.");
		hasExtension = (parsed.length >= 2);
		extension = (hasExtension)? 
				parsed[parsed.length-1] : "text";
	}

	public String getFileName() {
		return fileName;
	}

	public String getExtension() {
		return extension;
	}

	public boolean getHasExtension() {
		return hasExtension;
	}

	public String getUrl() {
		return url;
	}
}
