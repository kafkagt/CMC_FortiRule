package library.dataEstructure.Data;

import java.util.Comparator;

public class ServiceLight implements Comparable<ServiceLight>, Comparator<ServiceLight> {

	private String name;
	private int startPort = 0;
	private int stopPort = 0;
	private String protocol;

	public ServiceLight(String servicio) {

		String input = servicio.toLowerCase();
		

		this.setName(servicio);

		if (input.startsWith("icmp")) {

			this.setProtocol("ICMP");

		} else if (input.startsWith("udp")) {

			this.setProtocol("UDP");

		} else if (input.startsWith("tcp")){

			this.setProtocol("tcp");
			
			
		}
		
		this.setPortName(input);
		
		

	}
	
	private void setPortName(String input) {
		
		
		String[] port = input.split("[/|_-]+");
		
		if (port.length == 2) {

			this.setPortRange(port[1], port[1]);

		}else if (port.length == 3) {
			
			this.setPortRange(port[1], port[2]);
			
		}
		
	}

	public String getNameService() {

		if (name != null)
			return name;

		return protocol + "_" + getPorts();

	}

	public int portRange() {

		if (startPort == 0)
			throw new IllegalArgumentException("Objeto servicio con puertos no configurados");

		return stopPort - startPort;

	}

	public String getPorts() {

		String res = Integer.toString(startPort);

		if (stopPort != startPort) {

			res += "-" + Integer.toString(stopPort);
 
		}

		return res;

	}

	public void setPortRange(String start, String stop) {
		
		
		try {
			this.setStartPort(Integer.parseInt(start));
			this.setStopPort(Integer.parseInt(stop));		
		}catch (NumberFormatException e) {
			
			
		}


	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartPort() {
		return startPort;
	}

	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}

	public int getStopPort() {
		return stopPort;
	}

	public void setStopPort(int stopPort) {
		this.stopPort = stopPort;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public int compareTo(ServiceLight o) {

		return this.getName().compareTo(o.getName());
	}

	@Override
	public int compare(ServiceLight o1, ServiceLight o2) {

		return o1.getName().compareTo(o2.getName());
	}

	@Override
	public int hashCode() {

		return name.hashCode();
	}
	
	@Override
	public String toString() {
		
		return this.getName();
	}
	
	@Override
    public boolean equals(Object obj) {
		
		return this.hashCode() == obj.hashCode();
	}

}
