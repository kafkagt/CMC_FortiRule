package reglas;

import org.apache.commons.csv.CSVRecord;

public class SDP{
	
	String source;
	String destiny;
	String service;
	String rcvBytes;
	
	public SDP(String s, String d, String p, String Bytes) {
	
		source = s.replace("srcip=", "");
		destiny = d.replace("dstip=", "");
		service = p.replace("\"", "").replace("service=", "").replace("TCP_", "TCP/");	
		rcvBytes = Bytes.replace("\"", "").replace("rcvdbyte=", "");	
		
		
	}
	public SDP(String s, String d, String p) {
		
		source = s.replace("srcip=", "");
		destiny = d.replace("dstip=", "");
		service = p.replace("\"", "").replace("service=", "").replace("TCP_", "TCP/");	
		rcvBytes = "";
		
		
	}
	
	
	public String getRcvBytes() {
		return rcvBytes;
	}
	public void setRcvBytes(String rcvBytes) {
		this.rcvBytes = rcvBytes;
	}
	public SDP(CSVRecord csv) {
		
		source = csv.get(0).replaceAll("[", "").replaceAll("]", "");
		destiny = csv.get(1).replaceAll("[", "").replaceAll("]", "");
		service = csv.get(2).replaceAll("[", "").replaceAll("]", "");		
		
		
	}
	
	public String[] toArray() {
		
		String[] tmp = new String[3];
		
		 
		tmp[0] = source;
		tmp[1] = destiny;
		tmp[2] = service;
		
		return tmp;
		
		
	}
	
	@Override
	public String toString() {
		
		//System.out.println("toString");

		return "Source: " + this.source + " Destiny: " + this.destiny + " service: " + this.service;
	}
	
	@Override
	public int hashCode() {
		
		//System.out.println("hash");
		
		return this.source.hashCode() + this.destiny.hashCode() + this.service.hashCode();
	}
	
	@Override
    public boolean equals(Object obj) {
		
		boolean res;
		
		//System.out.println("Equals");
		
		SDP sdp = (SDP) obj;
		
		res =  this.source.equals(sdp.source) && this.destiny.equals(sdp.destiny) && this.service.equals(sdp.service);
		
		//System.out.println("Equals:"  + res);
		
		return res;
		
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestiny() {
		return destiny;
	}

	public void setDestiny(String destiny) {
		this.destiny = destiny;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public boolean grupable(SDP s) {
		
		
		boolean sourceB = s.getSource().equals(this.getSource());
		boolean destinyB = s.getDestiny().equals(this.getDestiny());
		boolean servicesB = s.getService().equals(this.getService());
		
		if((sourceB && destinyB) || (destinyB && servicesB)) {
			
			return true;
		}
		
		return false;
		
		
	}
	
	

}
