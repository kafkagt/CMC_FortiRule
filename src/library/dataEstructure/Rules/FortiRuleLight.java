package library.dataEstructure.Rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import library.dataEstructure.Data.ServiceLight;
import library.dataEstructure.Logs.LineaLogLight;
import library.dataEstructure.Matrix.CeldaLight;
import library.dataEstructure.Matrix.VlanLight;

public class FortiRuleLight implements Comparable<FortiRuleLight>, Comparator<FortiRuleLight> {

	private Set<String> ipSource;
	private Set<String> ipDestiny;
	private Set<ServiceLight> servicios;
	private CeldaLight celdaLocal;
	private int id;
	private String name;
	private String comment;
	private boolean permit = true;
	private String direction;
	static int idCount = 1;
	private Set<String> allServices;

	public FortiRuleLight() {

		this.inicializa();

	}

	public Set<String> getIpSource() {
		return ipSource;
	}

	public void setIpSource(Set<String> ipSource) {
		this.ipSource = ipSource;
	}

	public Set<String> getIpDestiny() {
		return ipDestiny;
	}

	public void setIpDestiny(Set<String> ipDestiny) {
		this.ipDestiny = ipDestiny;
	}

	public void inicializa() {

		servicios = new HashSet<ServiceLight>();
		ipSource = new HashSet<String>();
		ipDestiny = new HashSet<String>();
		allServices = new HashSet<String>();
		celdaLocal = new CeldaLight();
		id = idCount++;
	}

	public FortiRuleLight(CeldaLight celda) {

		this.celdaLocal = celda;

		this.inicializa();

		this.setDirection(celda.getLogs().iterator().next().getDirection());

		Set<LineaLogLight> logs = celda.getLogs();

		for (LineaLogLight l : logs) {

			this.addLinea(l);

		}

	}

	public void addLinea(LineaLogLight log) {

		this.ipSource.add(log.getSrcIP());
		this.ipDestiny.add(log.getDstIP());
		this.name = log.getService();

		if (this.direction == null) {
			
			this.setDirection(log.getDirection());
			
		} else {
			
			this.setDirection(this.setDirectionGrouped(log));
			
		}

		this.servicios.add(new ServiceLight(name));

		celdaLocal.add(log);

		if (log.getAllServices() != null && log.getAllServices().length() > 1) {
			this.allServices.add(log.getAllServices());
		}

	}

	private String setDirectionGrouped(LineaLogLight log) {

		String otro = log.getDirection();
		String este = this.getDirection();

		if (otro.equals(este)) {

			return este;
		}

		boolean grupo1 = este.endsWith("TOT") && otro.endsWith("TOT"); // X-OT
		boolean grupo2 = este.endsWith("WAN") && otro.endsWith("WAN"); // X-WAN
		boolean grupo4 = este.startsWith("WAN") && otro.startsWith("WAN"); // WAN-X

		if (grupo1) {

			return "XTOT";

		}
		if (grupo2) {

			return "XWAN";

		}
		if (grupo4) {

			return "WANX";

		}

		throw new IllegalArgumentException("Mezclado de logs incorrecto: "+ este + " - " + otro);

	}

	public void addLinea(Set<LineaLogLight> logs) {

		for (LineaLogLight log : logs) {
			this.addLinea(log);
		}

	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {


			this.direction = direction;


	}

	public Set<String> getVlanSource() {
		return ipSource;
	}

	public void setVlanSource(Set<String> vlanSource) {
		this.ipSource = vlanSource;
	}

	public Set<String> getVlanDestiny() {
		return ipDestiny;
	}

	public void setVlanDestiny(Set<String> vlanDestiny) {
		this.ipDestiny = vlanDestiny;
	}

	public Set<ServiceLight> getServicios() {
		return servicios;
	}

	public void setServicios(Set<ServiceLight> servicios) {
		this.servicios = servicios;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<String> getAllServices() {
		return allServices;
	}

	public void setAllServices(Set<String> allServices) {
		this.allServices = allServices;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int compareTo(FortiRuleLight o) {

		int iguales = 0;

		iguales += o.getVlanSource().hashCode() - this.getVlanSource().hashCode();
		iguales += o.getVlanDestiny().hashCode() - this.getVlanDestiny().hashCode();
		iguales += o.getServicios().hashCode() - this.getServicios().hashCode();

		return iguales;
	}

	@Override
	public int hashCode() {

		int res = this.getIpSource().hashCode() + this.getIpDestiny().hashCode() + this.getServicios().hashCode();
		// System.out.println(res);
		return res;

	}

	@Override
	public int compare(FortiRuleLight o1, FortiRuleLight o2) {
		return o1.hashCode() - o2.hashCode();
	}

	public boolean isPermit() {
		return permit;
	}

	public void setPermit(boolean permit) {
		this.permit = permit;
	}

	@Override
	public String toString() {

		String res = "";

		res += "'" + id + "';'" + this.celdaLocal.getSources() + "';'" + this.celdaLocal.getSourcesWOVlanName() + "';'"
				+ this.celdaLocal.getDestinysWOVlanName() + "';'" + this.celdaLocal.getDestinys() + "';'" + servicios
				+ "';'" + this.getDirection() + "','" + this.getAllServices() + "','" + this.getAllServices().size()
				+ "'";

		// System.out.println(res);

		return res;
	}

	public String[] toArray() {
		String[] res = new String[8];

		res[0] = "" + this.id;
		res[1] = this.celdaLocal.getSources().toString();
		res[2] = this.getIpSource().toString();
		res[3] = this.getIpDestiny().toString();
		res[4] = this.celdaLocal.getDestinys().toString();
		res[5] = this.servicios.toString();
		res[6] = this.limpiadireccion();
		res[7] = this.getAllServices().toString();

		return res;

	}

	private String limpiadireccion() {

		Set<String> ss = new HashSet<String>();
		String s = this.getDirection().replaceAll(" ", "");
		ss.addAll(Arrays.asList(s.split(",")));
		return ss.toString();

	}

	@Override
	public boolean equals(Object obj) {

		boolean res = this.hashCode() == obj.hashCode();

		return res;

	}

	public boolean equals(FortiRuleLight obj) {

		FortiRuleLight frc = obj;

		return frc.getVlanSource().equals(this.getVlanSource()) && frc.getVlanDestiny().equals(this.getVlanDestiny())
				&& frc.getServicios().equals(this.getServicios());

	}

	public boolean isGrupable(FortiRuleLight fr) {

		boolean res = false;

		String otro = fr.getDirection();
		String este = this.getDirection();

		boolean grupo1 = este.endsWith("TOT") && otro.endsWith("TOT"); // X-OT
		boolean grupo2 = este.endsWith("TWAN") && otro.endsWith("TWAN"); // X-WAN
		boolean grupo4 = este.startsWith("WAN") && otro.startsWith("WAN"); // WAN-X
		boolean grupo5 = este.equals(otro); // WAN-X

		//Comprueba si son agrupables
		if (!grupo1 && !grupo2 && !grupo4 && !grupo5) {

			return res;

		}

//		boolean srcDir = fr.getDirection().contains("WAN");
//		boolean dstDir = this.getDirection().contains("WAN");
//			
//		if(!fr.getDirection().equals(this.getDirection())) {
//			return res;
//		}

//		// Si una de las 2, pero no las 2 tienen WAN nunca son agrupables
//		if (srcDir ^ dstDir) {
//
////			System.out.println(fr);
////			System.out.println(this);
////			System.out.println("---------------");
//
//			return res;
//
//		} else {
//
//			System.out.println(fr);
//			System.out.println(this);
//			System.out.println("---------------");
//		}

		//Solo Vlanes source
		Set<VlanLight> sourcesWOVlan = fr.getCeldaLocal().getVlanAndGroupsSource();

//		if (sourcesWOVlan.size() == 0) {
//
//			return res;
//		}
		
		//Solo vlanes destination
		Set<VlanLight> destinysWOVlan = fr.getCeldaLocal().getVlanAndGroupsDestination();
//		if (destinysWOVlan.size() == 0) {
//
//			return res;
//		}

		boolean destiny = this.getCeldaLocal().getVlanAndGroupsDestination().equals(destinysWOVlan);

//		if (!destiny)
//			return res;

		String services = fr.getCeldaLocal().getServices();

		boolean source = this.getCeldaLocal().getVlanAndGroupsSource().equals(sourcesWOVlan);

		boolean servicesFull = this.getCeldaLocal().getServices().equals(services);
		
		boolean serviciosNoMezclados = this.getAllServices().equals(fr.getAllServices());

		// boolean serSRC = fr.getAllServices().size() == 0;
		// boolean serDST = this.getAllServices().size() == 0;

		// Si son servicios especiales solo se agrupan consigo mismo
		// boolean extra = (serSRC && serDST) || servicesFull;

		// res = (source && destiny && extra) || (destiny && servicesFull) || (source &&
		// servicesFull);
		res = (source && destiny && serviciosNoMezclados) || (destiny && servicesFull) || (source && servicesFull);
		return res;

	}

	public boolean isGrupable2(FortiRuleLight fr) {

		boolean res = false;

		Set<VlanLight> sourcesWOVlan = fr.getCeldaLocal().getSources();

		if (sourcesWOVlan.size() == 0) {

			return res;
		}

		Set<VlanLight> destinysWOVlan = fr.getCeldaLocal().getDestinys();
		if (destinysWOVlan.size() == 0) {

			return res;
		}

		boolean destiny = this.getCeldaLocal().getDestinys().equals(destinysWOVlan);

		if (!destiny)
			return res;

		String services = fr.getCeldaLocal().getServices();

		boolean source = this.getCeldaLocal().getSources().equals(sourcesWOVlan);

		boolean servicesFull = this.getCeldaLocal().getServices().equals(services);

		res = (source && destiny) || (destiny && servicesFull) || (source && servicesFull);

		return res;

	}

	public CeldaLight getCeldaLocal() {
		return celdaLocal;
	}

	public void setCeldaLocal(CeldaLight celdaLocal) {
		this.celdaLocal = celdaLocal;
	}

}
