package reglas;

import java.util.HashSet;
import java.util.Set;


import library.dataEstructure.Data.GrupoIPsLight;
import library.dataEstructure.Logs.LineaLogLight;
import library.dataEstructure.Matrix.VlanLight;


public class Rule {

	// Bilbao
	static final private String[] planta = { "10.43", "N/A", "10.143" };
	static final private String[] ot = { "10.143" };
	static final private String[] it = { "10.43" };
	static final public String plantaActual = "BSBI";

//	//Usánsolo
//	static final private String[] planta = { "10.54", "N/A" };
//	static final private String[] ot = { "10.54" };
//	static final private String[] it = { "10.54" };
//	static final public String plantaActual = "BSUS";

	/*
	 * //Usánsolo static final private String[] planta = { "10.54", "N/A", "10.154"
	 * }; static final private String[] ot = {"10.154"}; static final private
	 * String[] it = { "10.54"};
	 */
	static final private String[] BSEU = { "10.", // Rango 10.x.x.x
			"192.168.", // Rango 192.168.x.x
			"192.218.126.136", // BSEU DCs
			"172.16.", "172.17.", "172.18.", "172.20.", "172.19.", "172.21.", "172.19.", "172.22.", "172.23.", // Rango
																												// 172
			"172.24.", "172.25.", "172.26", "172.27", "172.28", "172.29", "172.30", "172.31", //
			"224.", "225.", "226.", "227.", "228.", "229.", "230.", "231.", "232.", "233.", "234.", "235.", "236.",
			"237.", "238.", "239.", // Multicast
			"255.255.255.255", // BroadCast
			"169.254." // Automatic Private Internet Protocol Addressing
	};



	public static boolean isIPV4(LineaLogLight log) {

		return !(log.getDstIP().contains(":") || log.getSrcIP().contains(":"));
	}

	public static boolean isMismaVlan(LineaLogLight log) {

		boolean res = log.getSrcVlanForce().equals(log.getDstVlanForce());

		return res;
	}

	public static boolean isTimeout(LineaLogLight log) {

		// System.out.println(log.getError().equals(""));

		boolean res = !log.getError().equals("");
		return res;
	}

	public static boolean isPuertoFiltrado(LineaLogLight log) {

		return isPuertoFiltrado(log.getService());

	}

	public static boolean isPuertoFiltrado(String service) {

		boolean res = false;

		Set<String> puertos = new HashSet<String>();

		puertos.add("PING");
		puertos.add("udp/137");

		res = service.startsWith("icmp");
		res = res || service.startsWith("igmp");
		res = res || service.startsWith("TRACEROUTE");

		res = res || puertos.contains(service);

		return res;

	}



	public static boolean isBSEU(String subred) {

		// System.out.println("Es ip: " + subred);

		boolean res = false;

		for (String s : BSEU) {

			res = res || subred.startsWith(s);
		}

		return res;

	}

	public static boolean isPlant(String ip) {

		boolean res = false;

		for (String s : planta) {

			res = res || ip.startsWith(s);
		}

		return res;

	}

	public static boolean isPlant(GrupoIPsLight g) {

		if (g == null)
			throw new IllegalArgumentException("grupo Nulo, posible Vlan");

		boolean res = false;

		for (String s : planta) {

			for (String st : g.getIps()) {

				res = res || st.startsWith(s);

			}
		}

		return res;

	}




	public static boolean isWAN(GrupoIPsLight g) {

		if (g == null)
			throw new IllegalArgumentException("grupo Nulo, posible Vlan");

		boolean res;

		for (String st : g.getIps()) {

			res = false;

			for (String s : planta) {

				res = res || st.startsWith(s);

			}

			if (!res) {
				// System.out.println("isWAN: " + st);
				return true;
			}

		}

		// System.out.println("NOT: " + g.getName());
		return false;

	}

	public static boolean isPlant(VlanLight v) {

		boolean res = false;

		for (String s : planta) {

			res = res || v.getCidr().startsWith(s);
		}

		return res;

	}

	public static boolean isWAN(VlanLight v) {

		boolean res = false;

		for (String s : planta) {

			res = res || v.getCidr().startsWith(s);
		}

		return !res;

	}

	public static boolean isMPLS(String ip) {

		boolean res = false;

		for (String s : planta) {

			res = res || ip.startsWith(s);
		}

		return !res;

	}

	public static boolean isMPLS(VlanLight v) {

		boolean res = false;

		for (String s : planta) {

			res = res || v.getCidr().startsWith(s);
		}

		return !res;

	}

	public static boolean isOT(VlanLight v) {

		return v.getDirectionPartial().equals("OT");

	}

	public static boolean isIT(VlanLight v) {

		return v.getDirectionPartial().equals("IT");

	}

	public static Set<VlanLight> getLocalVlan(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (Rule.isPlant(v)) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static Set<VlanLight> getLocalOTVlan(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (Rule.isOT(v)) {

				limpio.add(v);

			}

		}

		return limpio;

	}



	public static Set<VlanLight> getLocalITVlan(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (Rule.isIT(v)) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static Set<VlanLight> getLocalGroup(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (v.isGroup() && Rule.isPlant(v.getGroup())) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static Set<VlanLight> getExternalGroup(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (v.isGroup() && Rule.isWAN(v.getGroup())) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static Set<VlanLight> getExternalVlan(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (!v.isGroup() && (Rule.isWAN(v) || !Rule.isBSEU(v.getCidr()))) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static Set<VlanLight> getGroups(Set<VlanLight> vSet) {

		Set<VlanLight> limpio = new HashSet<VlanLight>();

		for (VlanLight v : vSet) {

			if (v.isGroup()) {

				limpio.add(v);

			}

		}

		return limpio;

	}

	public static boolean isVlanIdSrc(LineaLogLight log, String id) {

		return log.getDstVlan().getVlanID().equals(id);
	}

	public static boolean registrar(LineaLogLight log) {

		boolean res = true;

		res = isIPV4(log) && !isMismaVlan(log) && !isPuertoFiltrado(log);// && !isInternetDST(log);
		// && !ipNoGrupadaWan(log.getDstVlan()) && !ipNoGrupadaWan(log.getSrcVlan());

		return res;

	}

	public static boolean registrar(SDP grupo) {

		boolean res = true;

		res = !Rule.isPuertoFiltrado(grupo.getService())
				&& (!grupo.getRcvBytes().equals("0") || !grupo.getService().startsWith("tcp"))
				&& !isPrivate192(grupo);

		// System.out.println(grupo.getRcvBytes());

		return res;

	}
	
	public static boolean isPrivate192(SDP grupo) {
		
		if(grupo.source.startsWith("192.168.") || grupo.destiny.startsWith("192.168.") ) {
			
			return true;
			
		}else {
			
			return false;
		}
		
	}



	public static String limpia(String s) {

		return s.replaceFirst("\\[", "").replaceFirst("]", "");
	}

}
