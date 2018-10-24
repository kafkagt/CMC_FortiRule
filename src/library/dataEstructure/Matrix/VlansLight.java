package library.dataEstructure.Matrix;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;

import library.dataEstructure.Data.GrupoIPsLight;
import reglas.Rule;

public class VlansLight {

	private Map<String, VlanLight> vlans;
	
	static public VlanLight internet = new VlanLight();
	
	private VlanLight mpls = new VlanLight("255.255.255.255");

	public VlansLight(Iterable<CSVRecord> listadoVlans) {

		this.vlans = new HashMap<String, VlanLight>();

		for (CSVRecord csv : listadoVlans) {

			this.addCSV(csv);
			// System.out.println("CIDR: " + vl1.getCidr());
		}

	}

	public VlansLight() {
		this.vlans = new HashMap<String, VlanLight>();
		this.vlans.put(internet.getVlanName(), internet);
	}

	private void addCSV(CSVRecord csv) {

		VlanLight vl1 = new VlanLight(csv);
		this.addVlan(vl1);

	}

	private void addVlan(VlanLight v) {

		vlans.put(v.getCidr(), v);
	}

	public VlanLight getVlanAssignedMasked(String ip) {

		VlanLight vlanTMP;

		if (Rule.isBSEU(ip) && !Rule.isPlant(ip)) {

			vlanTMP = mpls;

		} else {

			vlanTMP = this.getVlanAssigned(ip);
		}

		return vlanTMP;

	}
	
	public VlanLight getVlanLigh(String ip) {
		
		SubnetUtils subnet;
		SubnetInfo subInfo;

		VlanLight vlanTMP = new VlanLight();
		
		for (String cidr : vlans.keySet()) {
			
			try {
				// tmp1 = System.currentTimeMillis();

				subnet = new SubnetUtils(cidr);

				subInfo = subnet.getInfo();

				if (subInfo.isInRange(ip) || subInfo.getBroadcastAddress().equals(ip)) {

					vlanTMP = vlans.get(cidr);
					break;

				}
			} catch (Exception e) {



			}
		}
		
		return vlanTMP;
		
	}

	public VlanLight getVlanAssigned(String ip) {

		// Long start1 = System.currentTimeMillis();

		// Long tmp1 = 0L;
		// Long tmp2 = 0L;
		// Long start3 = 0L;
		// Long start4 = 0L;
		// Ipv4Range subred;

		SubnetUtils subnet;
		SubnetInfo subInfo;

		VlanLight vlanTMP = null;

		if (!Rule.isBSEU(ip)) {

			vlanTMP = internet;
			// System.out.println("Internet: " + ip);

		} else {
			// tart3 = System.currentTimeMillis();

			for (String cidr : vlans.keySet()) {

				try {
					// tmp1 = System.currentTimeMillis();

					subnet = new SubnetUtils(cidr);

					subInfo = subnet.getInfo();

					if (subInfo.isInRange(ip) || subInfo.getBroadcastAddress().equals(ip)) {

						vlanTMP = vlans.get(cidr);

					}
				} catch (Exception e) {

					// System.out.println(e);

					VlanLight v = this.vlans.get(cidr);

					if (v.isContained(ip)) {

						return v;
					}

				}

			}

			// start4 = System.currentTimeMillis();
			// System.out.println("TMP2: " + tmp2);
		}

		if (vlanTMP == null) {
			vlanTMP = new VlanLight(ip, "16");
			this.addVlan(vlanTMP);
		}

		return vlanTMP;

	}

	@Override
	public String toString() {

		String ret = "";

		for (String cidr : vlans.keySet()) {

			ret += this.vlans.get(cidr).toString();

		}

		return ret;

	}

	public void addGrupos(Iterable<CSVRecord> csv) {

		for (CSVRecord objeto : csv) {

			this.addCSVForti(objeto);

		}

	}

	private void addCSVForti(CSVRecord csv) {

		VlanLight vlanGrupo = this.vlans.get(csv.get(0));

		if (vlanGrupo != null) {

			vlanGrupo.addIP(csv.get(1));

		} else {

			GrupoIPsLight vl1 = new GrupoIPsLight(csv);
			VlanLight v = new VlanLight(vl1);

			this.addVlan(v);

		}

	}

	public VlanLight getGroup(String nameGroup) {

		VlanLight v = this.vlans.get(nameGroup);

		// if (v == null || !v.isGroup())
		// throw new IllegalArgumentException("No es un grupo: " + nameGroup);

		// ATENCIÓN!!!!!
		if (v == null && nameGroup.contains(".")) {

			v = new VlanLight(nameGroup, "16"); 

		}

		return v;

	}
	
	public VlanLight getVlan(String name) {
		
		VlanLight v = null;
		
		for(VlanLight vTMP: this.vlans.values()) {
			
			if(vTMP.getVlanName().equals(name)) {
				
				return vTMP;
			}
			
		}
		
		return v;
		
	}

	public Map<String, VlanLight> getVlans() {
		return vlans;
	}

	public void setVlans(Map<String, VlanLight> vlans) {
		this.vlans = vlans;
	}
	
	public Set<VlanLight> getSetVlan(){
		
		Set<VlanLight> s =  new HashSet<VlanLight>();
		s.addAll(this.vlans.values());
		return s;
		
		
	}
	
	

}
