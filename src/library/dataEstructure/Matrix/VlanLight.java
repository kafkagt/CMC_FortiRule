package library.dataEstructure.Matrix;

import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

import library.dataEstructure.Data.GrupoIPsLight;
import library.dataEstructure.tools.GestionInformacion;
import reglas.Rule;

public class VlanLight implements Comparable<VlanLight> {

	private String vlanName = "";
	private String vlanID = "";
	private String network = "";
	private String cidr = "";
	private String gateway = "";
	private String zone = "";
	private String netmask = "";
	private String broadcast = "";
	private GrupoIPsLight group;
	private String fabrica = "";
	private boolean isGroup = false;
	private String directionPartial = "WAN";
	private int hashCode;

	public VlanLight(CSVRecord row) {

		this.vlanName = row.get(0);
		this.vlanID = row.get(1);
		this.network = row.get(2);
		this.gateway = row.get(4);
		// this.zone = row.get(0);
		this.netmask = row.get(3);

		this.setFabrica(this.network);

		// calculamos el CIDR en base a la subred
		this.cidr = GestionInformacion.getIPCidr(network, netmask);
		this.setBroadcast();
		this.directionPartial = getPartialPlant(row.get(5));
		
		this.setHashCode();
	}

	private String getPartialPlant(String s) {

		if (!Rule.isWAN(this)) {
			return s;
		} else {

			return "WAN";
		}

	}

	public VlanLight(String vlan) {

		this.createVlanfromName(vlan);

	}

	public VlanLight() {

		this.createVlanfromName("0.0.0.0");
		this.setVlanName("Internet");
		this.directionPartial = "WAN";

	}

	private void createVlanfromName(String vlan) {
		// versión 3 octetos
		// String subVlan = vlan.substring(0, vlan.lastIndexOf('.'));
		// this.vlanName = subVlan;
		// this.network = subVlan + ".0";
		// this.cidr = subVlan + ".0/24";

		// 4 Octetos
		this.vlanName = vlan;
		this.network = vlan;
		this.cidr = vlan + "/32";

		this.vlanID = "-1";

		this.gateway = "N/A";
		this.zone = "N/A";
		this.netmask = "255.255.255.0";

		this.fabrica = this.getVlanName();
		this.isGroup = true; // A ver que pasa...
		this.group = new GrupoIPsLight(vlan);
		this.setHashCode();

	}

	public VlanLight(String rango, String cidr) {

		// versión 3 octetos
		// String subVlan = vlan.substring(0, vlan.lastIndexOf('.'));
		// this.vlanName = subVlan;
		// this.network = subVlan + ".0";
		// this.cidr = subVlan + ".0/24";

		String[] tmp = rango.split("\\.");

		if (!Rule.isPlant(rango)) {
			this.directionPartial = "WAN";
		} else {
			this.directionPartial = "OT";
		}

		// 4 Octetos
		this.vlanName = tmp[0] + "." + tmp[1] + "." + tmp[2] + ".0";
		this.cidr = tmp[0] + "." + tmp[1] + "." + tmp[2] + ".0/24";
		this.netmask = "255.255.255.0";

		this.network = vlanName;

		this.vlanID = "-1";

		this.gateway = "N/A";
		this.zone = "N/A";

		this.fabrica = this.getVlanName();

		// calculamos el CIDR en base a la subred

		this.setBroadcast();
		this.setHashCode();

	}

	public VlanLight(GrupoIPsLight g) {

		this.vlanName = g.getName();
		this.vlanID = "-2";
		this.network = "N/A";
		this.gateway = "N/A";
		this.zone = "N/A";
		this.netmask = "N/A";
		this.broadcast = "N/A";
		this.group = g;
		this.cidr = g.getName();
		this.fabrica = this.getVlanName();
		this.isGroup = true;
		this.setHashCode();

	}

	public String getDirectionPartial() {
		return directionPartial;
	}

	public void setDirectionPartial(String directionPartial) {
		this.directionPartial = directionPartial;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public String getVlanName() {
		return vlanName;
	}

	public void setVlanName(String vlanName) {
		this.vlanName = vlanName;
	}

	public String getVlanID() {
		return vlanID;
	}

	public void setVlanID(String vlanID) {
		this.vlanID = vlanID;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String toStringDetailed() {

		return "CIDR: " + cidr + " Gateway: " + gateway + " netmask: " + netmask + " Broadcast: " + this.broadcast
				+ " VlanID: " + vlanID + " VlanName: " + vlanName + " Zone: " + zone;
	}

	@Override
	public String toString() {

		return this.getVlanName();
	}

	@Override
	public boolean equals(Object obj) {

		VlanLight v = (VlanLight) obj;

		if (this.isGroup == true && v.isGroup == this.isGroup) {

			return this.group.equals(v.group);

		} else {

			return this.hashCode() == obj.hashCode();

		}

	}

	@Override
	public int hashCode() {

		return this.hashCode;

	}

	private String setBroadcast() {

		String octetos[] = network.split("\\.");
		String OctetosMascara[] = netmask.split("\\.");
		Short bloqueRed[] = new Short[4];
		Short bloqueMascara[] = new Short[4];

		for (int i = 0; i < 4; i++) {

			/*
			 * Hay que crear la máscara de red invirtiendo la máscara y haciendo un or sobre
			 * la subred
			 * 
			 * Problemas con los bytes
			 * 
			 */

			bloqueRed[i] = new Short(octetos[i]);

			bloqueMascara[i] = new Short(OctetosMascara[i]);

			this.broadcast += Integer.toString(~bloqueMascara[i] + 256 ^ bloqueRed[i]) + ".";

			/*
			 * if(bloqueMascara[i] != 0 && bloqueMascara[i] != 255)
			 * System.out.println("Broadcast: " + this.getNetwork() + "--"
			 * +this.getNetmask() + "-->" + this.broadcast);
			 */
		}

		this.broadcast = this.broadcast.substring(0, this.broadcast.length() - 1);

		// System.out.println("Broadcast: " + this.getNetwork() + "--"
		// +this.getNetmask() + "-->" + this.broadcast);
		return this.broadcast;
	}

	// Método de ordenación/comparación de las vlans

	@Override
	public int compareTo(VlanLight o) {
		int res;

		if (!this.isGroup && !o.isGroup) {

			String s1 = o.getVlanName();
			s1 = s1.substring(s1.length() - 2) + s1;

			String s2 = this.getVlanName();
			s2 = s2.substring(s2.length() - 2) + s2;

			res = s1.compareTo(s2);
		} else {

			res = o.getVlanName().compareTo(this.getVlanName());

		}

		return res;

	}

	public boolean isContained(String ip) {

		if (this.group != null) {

			return this.group.getIps().contains(ip);

		} else {

			Ipv4Range subred = null;
			Ipv4 ip4 = Ipv4.parse(ip);

			subred = Ipv4Range.parse(this.cidr);

			if (subred.contains(ip4)) {

				return true;
			}

		}

		return false;

	}

	public boolean isAllContained(Set<String> ips) {

		for (String ip : ips) {

			if (!this.isContained(ip)) {
				return false;
			}

		}

		return true;

	}

	public void addIP(String ip) {

		this.group.addIP(ip);

	}

	public String getFabrica() {
		return fabrica;
	}

	public void setFabrica(String network) {

		String subfijo = this.network.substring(0, network.lastIndexOf('.'));

		subfijo = subfijo.substring(0, subfijo.lastIndexOf('.'));

		this.fabrica = this.getVlanName() + "_(" + subfijo + ")";

	}

	public GrupoIPsLight getGroup() {

		if (this.group == null)
			throw new IllegalArgumentException("Esto no es un grupo: " + this.getCidr());

		return group;
	}
	
	private void setHashCode() {
		this.hashCode = this.cidr.hashCode();
	}

}
