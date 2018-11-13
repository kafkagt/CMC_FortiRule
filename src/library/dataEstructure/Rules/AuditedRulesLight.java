package library.dataEstructure.Rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.Matrix.VlanLight;
import library.dataEstructure.Matrix.VlansLight;
import reglas.OrdenaVlanGrupo;
import reglas.Rule;
import reglas.Tools;

public class AuditedRulesLight {

	Set<AuditedRuleLight> sAudit;

	Set<AuditedRuleLight> lastRules;

	Set<String> noGrupables;

	public AuditedRulesLight(Iterable<CSVRecord> reglasAuditadasTXT, VlansLight onlyVlan, VlansLight onlyGroup) {

		sAudit = new HashSet<AuditedRuleLight>();
		noGrupables = new HashSet<String>();

		lastRules = new HashSet<AuditedRuleLight>();

		for (CSVRecord rule : reglasAuditadasTXT) {

			AuditedRuleLight ar = new AuditedRuleLight(rule, onlyVlan, onlyGroup);

			sAudit.add(ar);
			// this.noGrupables.addAll(ar.getIpNoGrupable())

		}

	}

	public Set<AuditedRuleLight> getsAudit() {
		return sAudit;
	}

	public void setsAudit(Set<AuditedRuleLight> sAudit) {
		this.sAudit = sAudit;
	}

	public Set<String> getNoGrupables() {
		return noGrupables;
	}

	public void setNoGrupables(Set<String> noGrupables) {
		this.noGrupables = noGrupables;
	}

	public String[][] toMatrixOTOT() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getLocalOTVlan(this.getVlanSources())); // Vlan locales
		sourcesV.addAll(Rule.getLocalGroup(this.getGroupSource())); // Vlan locales
		destinyV.addAll(Rule.getLocalGroup(this.getGroupDestiny())); // Vlan locales
		destinyV.addAll(Rule.getLocalOTVlan(this.getVlanDestiny())); // Vlan locales
		
		Set<String> s = new HashSet<String>();
		s.add("OTOT");
		s.add("XTOT");

		return creaMatriz(sourcesV, destinyV, s);

	}

	public String[][] toMatrixOTWAN() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();
		//List<VlanLight> grupoSource = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas pertinentes
		sourcesV.addAll(Rule.getLocalOTVlan(this.getVlanSources()));
		sourcesV.addAll(Rule.getLocalGroup(this.getGroupSource()));
		//grupoSource.addAll(Rule.getLocalGroup(this.getGroupSource()));

		destinyV.addAll(Rule.getExternalGroup(this.getGroupDestiny()));
		destinyV.addAll(Rule.getExternalVlan(this.getVlanDestiny()));
		destinyV.add(VlansLight.internet);
		
		Set<String> s = new HashSet<String>();
		s.add("XWAN");
		s.add("OTWAN");

		return creaMatrizVG(sourcesV, destinyV,  s);

	}

	public String[][] toMatrixOTIT() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getLocalOTVlan(this.getVlanSources()));
		sourcesV.addAll(Rule.getLocalGroup(this.getGroupSource()));
		destinyV.addAll(Rule.getLocalITVlan(this.getVlanDestiny()));
		destinyV.addAll(Rule.getLocalGroup(this.getGroupDestiny()));

		Set<String> s = new HashSet<String>();
		s.add("OTIT");

		
		return creaMatriz(sourcesV, destinyV, s);

	}

	public String[][] toMatrixITOT() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getLocalITVlan(this.getVlanSources()));
		sourcesV.addAll(Rule.getLocalGroup(this.getGroupSource()));
		destinyV.addAll(Rule.getLocalGroup(this.getGroupDestiny()));
		destinyV.addAll(Rule.getLocalOTVlan(this.getVlanDestiny()));

		Set<String> s = new HashSet<String>();
		s.add("ITOT");
		s.add("XTOT");
		
		return creaMatriz(sourcesV, destinyV, s);

	}

	public String[][] toMatrixWANOT() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getExternalGroup(this.getGroupSource()));
		sourcesV.addAll(Rule.getExternalVlan(this.getVlanSources()));
		sourcesV.add(VlansLight.internet);

		destinyV.addAll(Rule.getLocalOTVlan(this.getVlanDestiny()));
		destinyV.addAll(Rule.getLocalGroup(this.getGroupDestiny()));
		//destinyV.addAll(Rule.getLocalOTGroup(this.getGroupDestiny()));
		
		Set<String> s = new HashSet<String>();
		s.add("WANOT");
		s.add("WANX");
		
		return creaMatrizGV(sourcesV, destinyV, s);

	}

	public String[][] toMatrixWANIT() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();

		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getExternalGroup(this.getGroupSource()));
		sourcesV.addAll(Rule.getExternalVlan(this.getVlanSources()));
		sourcesV.add(VlansLight.internet);
		
		destinyV.addAll(Rule.getLocalITVlan(this.getVlanDestiny()));
		destinyV.addAll(Rule.getLocalGroup(this.getGroupDestiny()));
		
		Set<String> s = new HashSet<String>();
		s.add("WANIT");
		s.add("WANX");

		return creaMatrizGV(sourcesV, destinyV, s);

	}

	public String[][] toMatrixITWAN() {

		List<VlanLight> sourcesV = new ArrayList<VlanLight>();
		List<VlanLight> destinyV = new ArrayList<VlanLight>();


		// Me quedo solo con las reglas locales
		sourcesV.addAll(Rule.getLocalITVlan(this.getVlanSources()));
		sourcesV.addAll(Rule.getLocalGroup(this.getGroupSource()));
		
		destinyV.addAll(Rule.getExternalGroup(this.getGroupDestiny()));
		destinyV.addAll(Rule.getExternalVlan(this.getVlanDestiny()));
		destinyV.add(VlansLight.internet);
		
		Set<String> s = new HashSet<String>();
		s.add("ITWAN");
		s.add("XWAN");

		return creaMatrizVG(sourcesV, destinyV, s);

	}

	private Set<VlanLight> getVlanSources() {

		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (AuditedRuleLight ar : sAudit) {
			setV.addAll(ar.getSourcesVlan());

		}

		return setV;

	}

	private Set<VlanLight> getVlanDestiny() {

		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (AuditedRuleLight ar : sAudit) {
			setV.addAll(ar.getDestinysVlan());

		}

		return setV;

	}

	private Set<VlanLight> getGroupDestiny() {

		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (AuditedRuleLight ar : sAudit) {
			setV.addAll(ar.getDestinysGroup());

		}

		return setV;

	}

	private Set<VlanLight> getGroupSource() {

		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (AuditedRuleLight ar : sAudit) {
			setV.addAll(ar.getSourcesGroup());

		}

		return setV;

	}

	private String[][] creaMatriz(List<VlanLight> sourcesV, List<VlanLight> destinyV, Set<String> direccion) {

		String[][] matrix = new String[sourcesV.size() + 1][destinyV.size() + 1];

		// Borro las últimas reglas
		lastRules = new HashSet<AuditedRuleLight>();

		OrdenaVlanGrupo ovg = new OrdenaVlanGrupo();
		Collections.sort(sourcesV, ovg);
		Collections.sort(destinyV, ovg);

		matrix = setMarcos(matrix, sourcesV, destinyV);

		// Relleno tabla
		for (AuditedRuleLight adr : this.sAudit) {
			if (direccion.contains(adr.getDirection())) {
				
				if(adr.getId() == 539) {
					
					System.out.println("SEVEN");
					
				}

				// Crea una lista con las VLANes y grupos fuera de la planta
				Set<VlanLight> sAuditTMPDestiny = new HashSet<VlanLight>();
				sAuditTMPDestiny.addAll(adr.getDestinysVlan());
				sAuditTMPDestiny.addAll(adr.getDestinysGroup());

				Set<VlanLight> sAuditTMPSource = new HashSet<VlanLight>();
				sAuditTMPSource.addAll(adr.getSourcesVlan());
				sAuditTMPSource.addAll(adr.getSourcesGroup());
				//sAuditTMPSource.addAll(Tools.getVlans(adr.getSourcesGroup(), sourcesV));

				matrix = rellenaMatriz(sourcesV, destinyV, matrix, sAuditTMPSource, sAuditTMPDestiny, adr);
			}
		}

		// System.out.println(matrix);

		return matrix;

	}

	private String[][] creaMatrizVG(List<VlanLight> sourcesV, List<VlanLight> destinyV, Set<String> direccion) {

		String[][] matrix = new String[sourcesV.size() + 2][destinyV.size() + 2];
		this.lastRules = new HashSet<AuditedRuleLight>();

		OrdenaVlanGrupo ovg = new OrdenaVlanGrupo();
		Collections.sort(sourcesV, ovg);
		Collections.sort(destinyV, ovg);

		// Añadimos internet

		matrix = setMarcos(matrix, sourcesV, destinyV);

		// Relleno tabla
		for (AuditedRuleLight adr : this.sAudit) {

			if (direccion.contains(adr.getDirection())) {

				// Crea una lista con las VLANes y grupos fuera de la planta
				Set<VlanLight> sAuditTMPDestiny = new HashSet<VlanLight>();
				sAuditTMPDestiny.addAll(adr.getDestinysVlan());
				sAuditTMPDestiny.addAll(adr.getDestinysGroup());
				//sAuditTMPDestiny.add(VlansLight.internet);

				Set<VlanLight> sAuditTMPSource = new HashSet<VlanLight>();
				sAuditTMPSource.addAll(adr.getSourcesVlan());
				sAuditTMPSource.addAll(adr.getSourcesGroup());
				//sAuditTMPSource.addAll(Tools.getVlans(adr.getSourcesGroup(), sourcesV));

				matrix = rellenaMatriz(sourcesV, destinyV, matrix, sAuditTMPSource, sAuditTMPDestiny, adr);

			}
		}


		return matrix;

	}

	private String[][] creaMatrizGV(List<VlanLight> sourcesV, List<VlanLight> destinyV, Set<String> direccion) {

		String[][] matrix = new String[sourcesV.size() + 1][destinyV.size() + 1];
		this.lastRules = new HashSet<AuditedRuleLight>();
		
		OrdenaVlanGrupo ovg = new OrdenaVlanGrupo();
		Collections.sort(sourcesV, ovg);
		Collections.sort(destinyV, ovg);

		matrix = setMarcos(matrix, sourcesV, destinyV);

		// Relleno tabla
		for (AuditedRuleLight adr : this.sAudit) {
			if (direccion.contains(adr.getDirection())) {
				// Crea una lista con las VLANes y grupos fuera de la planta

				Set<VlanLight> sAuditTMPSource = new HashSet<VlanLight>();
				sAuditTMPSource.addAll(adr.getSourcesVlan());
				sAuditTMPSource.addAll(adr.getSourcesGroup());
				//sAuditTMPSource.add(VlansLight.internet);

				Set<VlanLight> sAuditTMPDestiny = new HashSet<VlanLight>();
				sAuditTMPDestiny.addAll(adr.getDestinysVlan());
				sAuditTMPDestiny.addAll(adr.getDestinysGroup());
				//sAuditTMPDestiny.addAll(Tools.getVlans(adr.getDestinysGroup(), destinyV));

				matrix = rellenaMatriz(sourcesV, destinyV, matrix, sAuditTMPSource, sAuditTMPDestiny, adr);

			}
		}

		// System.out.println(matrix);

		return matrix;

	}

	public Set<AuditedRuleLight> getLastRules() {
		return lastRules;
	}

	public void setLastRules(Set<AuditedRuleLight> lastRules) {
		this.lastRules = lastRules;
	}

	private String[][] rellenaMatriz(List<VlanLight> sourcesV, List<VlanLight> destinyV, String[][] matrix,
			Set<VlanLight> sAuditTMPSource, Set<VlanLight> sAuditTMPDestiny, AuditedRuleLight adr) {

		String id = "" + adr.getId();
		
		
		int s;
		int d;
		
		
		// En caso de ser servicios se una el nombre
		if(adr.getAllServices().length() != 0) {
			
			id = adr.getServices().get(0);
		}
		

		for (VlanLight vs : sAuditTMPSource) {

			for (VlanLight vd : sAuditTMPDestiny) {

				Set<String> duplicados = new HashSet<String>();

				s = sourcesV.indexOf(vs);
				d = destinyV.indexOf(vd);

				if (s != -1 && d != -1) {

					String ids = matrix[s + 1][d + 1];
					
	

					if (ids == null || "".equals(ids)) {

						matrix[s + 1][d + 1] = id;
						this.lastRules.add(adr);

					} else {

						// Elimina los duplicados
						duplicados.addAll(Arrays.asList(ids.split(" - ")));

						if (!duplicados.contains(id)) {
							matrix[s + 1][d + 1] = ids + " - " + id;
							this.lastRules.add(adr);
						}

					}

				} 
//				else {
//
//					//System.out.println("Rule: " + adr.getId() + " VS: " + vs + " VD: " + vd);
//
//				}

			}
		}

		return matrix;
	}

	private String[][] setMarcos(String[][] matrix, List<VlanLight> sourcesV, List<VlanLight> destinyV) {

		int count = 1;

		for (VlanLight v : sourcesV) {

			// if(Rule.isPlant(v) || Rule.isPlant(v.getGroup())) {
			matrix[count][0] = v.getVlanName();
			count++;
			// }

		}

		// Columnas

		count = 1;
		for (VlanLight v : destinyV) {

			// if(Rule.isWAN(v) || Rule.isWAN(v.getGroup())) {
			matrix[0][count] = v.getVlanName();
			count++;
			// }

		}

		return matrix;

	}

}
