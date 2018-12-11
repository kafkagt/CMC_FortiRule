package library.dataEstructure.Data;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.omg.PortableServer.ThreadPolicyOperations;

public class GrupoIPsLight {

	String name = "";
	Set<String> ips;
	Set<String> services;
	int hashCode ;

	public GrupoIPsLight(CSVRecord row) {

		this.name = row.get(0);
		this.ips = new HashSet<String>();
		this.services = new HashSet<String>();
		this.addIP(row.get(1));
		this.hashCode = this.name.hashCode();

	}
	public GrupoIPsLight(String ip) {
		
		//System.out.print("Grupo creado: " + ip);

		this.name = ip;
		this.ips = new HashSet<String>();
		this.services = new HashSet<String>();
		
		if(ip.contains(".") ) {
			if(!ip.equals("0.0.0.0"))			
				this.addIP(ip);
		}else {
			
			throw new IllegalArgumentException("Error, esto no es una IP: " + ip);
		}
		this.hashCode = this.name.hashCode();
		

	}
	
	public boolean isContained(String ip) {
		
		return this.ips.contains(ip);
	}

	public void addIP(String ip) {

		this.ips.add(ip);
		//this.name = ip;

	}

	public void addService(String service) {

		this.services.add(service);

	}

	public String getName() {
		return name;
	}

	public Set<String> getIps() {
		return ips;
	}

	public Set<String> getServices() {
		return services;
	}

	@Override
	public String toString() {

		return "Name: " + this.getName() + " IPs: " + this.getIps();
	}
	
	@Override
	public int hashCode() {
		
		return this.hashCode;
	}
	
//	@Override
//    public boolean equals(Object obj) {
//		
//		boolean res = false;
//		
//		if(obj instanceof GrupoIPsLight) {
//			
//			GrupoIPsLight g = (GrupoIPsLight) obj;
//			
//			
//			res = this.name.equals(g.getName());
//			
//			//if(res) {
//				
//				//System.out.println("Grupo agrupado: " + this.name + " este: " + g.getName());
//			//}
//			
//			return res;
//			
//		}
//		
//		return false;
//		
//		
//		
//	}
	
	@Override
    public boolean equals(Object obj) {
		return obj.hashCode() == this.hashCode();
		
		
		
	}

}
