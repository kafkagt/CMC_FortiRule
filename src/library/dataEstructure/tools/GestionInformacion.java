package library.dataEstructure.tools;

public class GestionInformacion {

	public static final String srcIP = "srcip="; // IP de origen
	public static final String dstIP = "dstip="; // IP de destino
	public static final String dstPort = "dstport="; // Puerto de destino
	public static final String service = "service="; // Servicio
	public static final String rcvdByte = "rcvdbyte="; // Bytes recibidos
	public static final String sentByte = "sentbyte="; // Bytes enviados
	public static final String error = "threattyps="; // Bytes enviados
	public static final String NOT_VLAN = "-1"; // Vlan para las que no tienen VLAN
	public static final String IS_GROUP = "-2"; // Es un grupo
	public static final String direction = "-3"; // Es un grupo
	public static final String wan = "WAN"; // Es un grupo"
	public static final String ot = "OT"; // Es un grupo"

	public static String getCIDR(String subnet) {

		// System.out.println(subnet);

		if ("".equals(subnet)) {

			throw new IllegalArgumentException();

		} else if ("255.255.255.255".equals(subnet)) {

			return "/32";

		} else if ("255.255.255.254".equals(subnet)) {

			return "/31";
		} else if ("255.255.255.252".equals(subnet)) {

			return "/30";
		} else if ("255.255.255.248".equals(subnet)) {

			return "/29";
		} else if ("255.255.255.240".equals(subnet)) {

			return "/28";
		} else if ("255.255.255.224".equals(subnet)) {

			return "/27";
		} else if ("255.255.255.192".equals(subnet)) {

			return "/26";
		} else if ("255.255.255.128".equals(subnet)) {

			return "/25";
		} else if ("255.255.255.0".equals(subnet)) {

			return "/24";

		} else if ("255.255.255.0".equals(subnet)) {

			return "/23";

		} else if ("255.255.254.0".equals(subnet)) {

			return "/22";

		} else if ("255.255.252.0".equals(subnet)) {

			return "/21";

		} else if ("255.255.248.0".equals(subnet)) {

			return "/20";

		} else if ("255.255.224.0".equals(subnet)) {

			return "/19";

		} else if ("255.255.192.0".equals(subnet)) {

			return "/18";

		} else if ("255.255.128.0".equals(subnet)) {

			return "/17";

		} else if ("255.255.0.0".equals(subnet)) {

			return "/16";

		}else if ("240.0.0.0".equals(subnet)) {

			return "/4";

		} else {

			throw new IllegalArgumentException();
		}

	}

	public static String getIPCidr(String network, String netmask) {

		return network + getCIDR(netmask);
	}

}
