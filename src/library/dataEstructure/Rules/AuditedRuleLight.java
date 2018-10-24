package library.dataEstructure.Rules;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.Matrix.VlanLight;
import library.dataEstructure.Matrix.VlansLight;
import reglas.Rule;

public class AuditedRuleLight implements Comparator<AuditedRuleLight> {

	private Set<String> sourcesIPs;
	private Set<String> destinysIPs;
	private List<VlanLight> sourcesGroup;
	private List<VlanLight> destinysGroup;
	private Set<VlanLight> sourcesVlan;
	private Set<VlanLight> destinysVlan;
	private List<String> services;
	private int id;
	private VlanLight internet;
	private String[] ruleID;
	private String direction;
	private String allServices;

	private Set<VlanLight> ipNoGrupable;

	public AuditedRuleLight(String[] rule, VlansLight onlyVlan, VlansLight onlyGroup) {

		this.inicializa(rule, onlyVlan, onlyGroup);

	}

	public AuditedRuleLight(CSVRecord rule, VlansLight onlyVlan, VlansLight onlyGroup) {

		String[] ruleS = new String[8];

		ruleS[0] = rule.get(0).replaceAll(" ", "");
		
		
		ruleS[1] = rule.get(1).replaceAll(" ", "");
		ruleS[2] = rule.get(2).replaceAll(" ", "");
		ruleS[3] = rule.get(3).replaceAll(" ", "");
		ruleS[4] = rule.get(4).replaceAll(" ", "");
		ruleS[5] = rule.get(5).replaceAll(" ", "");
		ruleS[6] = Rule.limpia(rule.get(6).replaceAll(" ", ""));
		ruleS[7] = Rule.limpia(rule.get(7));
		ruleID = ruleS;

		this.inicializa(ruleS, onlyVlan, onlyGroup);

	}
	


	public String[] getRuleID() {
		return ruleID;
	}

	public void setRuleID(String[] ruleID) {
		this.ruleID = ruleID;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public VlanLight getVlanOrGroup(String s, VlansLight onlyVlan, VlansLight onlyGroup) {

		VlanLight vlan = onlyGroup.getGroup(s);

		if (vlan == null) {

			return onlyVlan.getVlan(s);

		} else {
			
			return vlan;
		}

	}

	private void inicializa(String[] rule, VlansLight onlyVlan, VlansLight onlyGroup) {

		// identificador
		this.id = Integer.parseInt(rule[0]);
		this.ipNoGrupable = new HashSet<VlanLight>();
		this.sourcesGroup = new ArrayList<VlanLight>();
		this.destinysGroup = new ArrayList<VlanLight>();
		this.internet = VlansLight.internet;
		this.sourcesVlan = new HashSet<VlanLight>();
		this.destinysVlan = new HashSet<VlanLight>();
		this.direction = rule[6];
		this.allServices = rule[7];

		// SOURCES
		String[] sourcesGroupTMP = rule[1].replaceAll("\\[", "").replaceAll("\\]", "").split(",");

		for (String s : sourcesGroupTMP) {

			VlanLight vlan = this.getVlanOrGroup(s, onlyVlan, onlyGroup);

			if (vlan.isGroup()) {
				sourcesGroup.add(vlan);
			} else {
				sourcesVlan.add(vlan);
			}
 
		}

		// DESTINYS
		String[] destinysGroupTMP = rule[4].replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		for (String s : destinysGroupTMP) {

			VlanLight vlan = this.getVlanOrGroup(s, onlyVlan, onlyGroup);

			if ( vlan.isGroup()) {
				destinysGroup.add(vlan);
			} else {
				destinysVlan.add(vlan);
			}

		}

		// Services
		String[] servicesGroupTMP = rule[5].replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		services = new ArrayList<String>();
		services.addAll(Arrays.asList(servicesGroupTMP));

		sourcesIPs = this.getIPsFromGroup(sourcesGroup, onlyGroup);
		destinysIPs = this.getIPsFromGroup(destinysGroup, onlyGroup);

		//sourcesVlan.addAll(this.getVlanFromIPs(sourcesIPs, onlyVlan));
		//destinysVlan.addAll(this.getVlanFromIPs(destinysIPs, onlyVlan));

	}

	private Set<String> getIPsFromGroup(List<VlanLight> groups, VlansLight onlyGroup) {

		Set<String> sVlan = new HashSet<String>();

		for (VlanLight v : groups) {

			try {

				// Si el grupo es una IP, añadimos la IP
				if (v.isGroup()) {

					sVlan.addAll(v.getGroup().getIps());

				}

			} catch (IllegalArgumentException e) {

				// System.out.println("No exite grupo para: " + group);
				this.ipNoGrupable.add(v);

			}

		}

		return sVlan;

	}

	private Set<VlanLight> getVlanFromIPs(Set<String> ips, VlansLight onlyVlan) {

		Set<VlanLight> sVlan = new HashSet<VlanLight>();

		for (String ip : ips) {

			VlanLight v = onlyVlan.getVlanAssigned(ip);

			if (!Rule.isBSEU(ip)) {

				sVlan.add(internet);

			} else {

				sVlan.add(v);

			}

		}

		return sVlan;

	}

	public Set<String> getSourcesIPs() {
		return sourcesIPs;
	}

	public void setSourcesIPs(Set<String> sourcesIPs) {
		this.sourcesIPs = sourcesIPs;
	}

	public Set<String> getDestinysIPs() {
		return destinysIPs;
	}

	public void setDestinysIPs(Set<String> destinysIPs) {
		this.destinysIPs = destinysIPs;
	}

	public List<VlanLight> getSourcesGroup() {
		return sourcesGroup;
	}

	public void setSourcesGroup(List<VlanLight> sourcesGroup) {
		this.sourcesGroup = sourcesGroup;
	}

	public List<VlanLight> getDestinysGroup() {
		return destinysGroup;
	}

	public void setDestinysGroup(List<VlanLight> destinysGroup) {
		this.destinysGroup = destinysGroup;
	}

	public VlanLight getInternet() {
		return internet;
	}

	public void setInternet(VlanLight internet) {
		this.internet = internet;
	}

	public Set<VlanLight> getSourcesVlan() {
		return sourcesVlan;
	}

	public void setSourcesVlan(Set<VlanLight> sourcesVlan) {
		this.sourcesVlan = sourcesVlan;
	}

	public Set<VlanLight> getDestinysVlan() {
		return destinysVlan;
	}

	public void setDestinysVlan(Set<VlanLight> destinysVlan) {
		this.destinysVlan = destinysVlan;
	}

	public List<String> getServices() {
		return services;
	}

	public void setServices(List<String> services) {
		this.services = services;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<VlanLight> getIpNoGrupable() {
		return ipNoGrupable;
	}

	public void setIpNoGrupable(Set<VlanLight> ipNoGrupable) {
		this.ipNoGrupable = ipNoGrupable;
	}

	public void groupAuditedRule(AuditedRuleLight arl) {

		this.destinysVlan.addAll(arl.getDestinysVlan());
		this.destinysGroup.addAll(arl.getDestinysGroup());
		this.destinysIPs.addAll(arl.getDestinysIPs());

		this.sourcesGroup.addAll(arl.getSourcesGroup());
		this.sourcesIPs.addAll(arl.getSourcesIPs());
		this.sourcesVlan.addAll(arl.getSourcesVlan());

		this.services.addAll(arl.getServices());

	}

	@Override
	public int compare(AuditedRuleLight o1, AuditedRuleLight o2) {

		int res = 0;

		boolean sGroup = o1.getSourcesGroup().equals(o2.getSourcesGroup());
		boolean sVlan = o1.getSourcesVlan().equals(o2.getSourcesVlan());

		boolean dGroup = o1.getDestinysGroup().equals(o2.getDestinysGroup());
		boolean dVlan = o1.getDestinysVlan().equals(o2.getDestinysVlan());

		boolean service = o1.getServices().equals(o2.getServices());

		if (sGroup)
			res += 3;
		if (sVlan)
			res += 5;
		if (dGroup)
			res += 7;
		if (dVlan)
			res += 13;
		if (service)
			res += 17;

		return res;

	}

	public String[] toArray() {

		return this.ruleID;

	}

	public String getAllServices() {
		return allServices;
	}

	public void setAllServices(String allServices) {
		this.allServices = allServices;
	}
	
	

}
