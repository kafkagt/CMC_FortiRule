package library.dataEstructure.Logs;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;

import library.dataEstructure.Matrix.VlanLight;
import library.dataEstructure.Matrix.VlansLight;
import library.dataEstructure.tools.GestionInformacion;
import library.dataEstructure.tools.PositionLocator;
import reglas.Rule;

public class LineaLogLight implements Comparable<LineaLogLight> {

	private String srcIP = ""; // IP de origen
	private String dstIP = ""; // IP de destino
	private String dstPort = ""; // Puerto de destino
	private String service = ""; // Puerto de destino
	private String direction = "";
	private String allServices = "";

	private String error = "";
	private VlanLight srcVlan;
	private VlanLight dstVlan;
	private VlanLight srcVlanForce;
	private VlanLight dstVlanForce;

	public LineaLogLight(CSVRecord row, PositionLocator position, VlansLight vlans) {

		try {

			this.setDstIP(row.get(position.getDstIP()));
			this.setSrcIP(row.get(position.getSrcIP()));
			this.setService(row.get(position.getService()));
			
			if(this.getSrcIP().equals("10.143.6.20")) {
				int a = 1;
			}
			

			if (position.getDirection() != -1) {
				this.setDirection(row.get(position.getDirection()));
			}
			
			if (position.getAllServices() != -1) {
				
				String s = row.get(position.getAllServices());
				
				if(s.length() > 1)				
					this.setAllServices(s);
			}

		} catch (ArrayIndexOutOfBoundsException e) {

			this.setDstIP(row.get(2));
			this.setSrcIP(row.get(3));
			this.setService(row.get(5));
			this.setDirection(row.get(6));

		}

		this.setVlan(vlans);

	}

	public void setVlan(VlansLight tablaVlan) {

		this.setSrcVlan(tablaVlan.getVlanAssigned(this.getSrcIP()));
		this.setDstVlan(tablaVlan.getVlanAssigned(this.getDstIP()));
		this.srcVlanForce = tablaVlan.getVlanLigh(this.getSrcIP());
		this.dstVlanForce = tablaVlan.getVlanLigh(this.getDstIP());
		this.direction = srcVlanForce.getDirectionPartial() + dstVlanForce.getDirectionPartial();

		// System.out.println(this);

	}
	

	public VlanLight getSrcVlanForce() {
		return srcVlanForce;
	}

	public void setSrcVlanForce(VlanLight srcVlanForce) {
		this.srcVlanForce = srcVlanForce;
	}

	public VlanLight getDstVlanForce() {
		return dstVlanForce;
	}

	public void setDstVlanForce(VlanLight dstVlanForce) {
		this.dstVlanForce = dstVlanForce;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getAllServices() {
		return allServices;
	}

	public void setAllServices(String allServices) {
		this.allServices = allServices;
	}

	public String getSrcIP() {
		return srcIP;
	}

	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP.replace(GestionInformacion.srcIP, "");
	}

	public String getDstIP() {
		return dstIP;
	}

	public void setDstIP(String dstIP) {
		this.dstIP = dstIP.replace(GestionInformacion.dstIP, "");
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort.replace(GestionInformacion.dstPort, "");
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service.replace(GestionInformacion.service, "");

		String[] s = this.service.split("/");

		if (s.length > 1 && !s[1].equals("*") && s[0].equals("udp")) {

			Integer i = Integer.parseInt(s[1]);

			if (i > 500) {

				this.service = "udp/*";
			}

		}

	}

	@Override
	public String toString() {
		String string = "srcIP: " + this.getSrcIP() + " dstIP: " + this.getDstIP() + " dstport: " + this.getDstPort()
				+ " service: " + this.getService();
		string += " SrcVlan: " + this.getSrcVlan().getVlanName();
		string += " DstVlan: " + this.getDstVlan().getVlanName();
		string += " Direction: " + this.getDirection();
		return string;
	}

	public VlanLight getSrcVlan() {
		return srcVlan;
	}

	public void setSrcVlan(VlanLight srcVlan) {
		this.srcVlan = srcVlan;
	}

	public VlanLight getDstVlan() {
		return dstVlan;
	}

	public void setDstVlan(VlanLight dstVlan) {
		this.dstVlan = dstVlan;
	}

	@Override
	public boolean equals(Object obj) {

		LineaLogLight log = (LineaLogLight) obj;
		// System.out.println("Compara");
		return log.getSrcIP().equals(this.getSrcIP()) && log.getDstIP().equals(this.getDstIP())
				&& log.getService().equals(this.getService());
	}

	@Override
	public int compareTo(LineaLogLight o) {

		int i = this.getSrcIP().compareTo(o.getSrcIP()) + this.getDstIP().compareTo(o.getDstIP())
				+ this.getDstPort().compareTo(o.getDstPort());

		// System.out.println("1-" +this);
		// System.out.println("2-" +o);

		return i;
	}

	@Override
	public int hashCode() {

//		Long res = 0L;
//
//		Long src = Long.parseLong(this.dstIP.replaceAll("\\.", ""));
//		Long dst = Long.parseLong(this.srcIP.replaceAll("\\.", ""));
//		int port = this.getService().hashCode();
//
//		res = src * dst * port;
		// System.out.println(this.srcIP+" " +this.dstIP+" " +this.service + " " +
		// (this.srcIP+this.dstIP+this.service).hashCode() );
		return (this.srcIP + this.dstIP + this.service + this.allServices).hashCode();

//		return res.intValue();

	}

}
