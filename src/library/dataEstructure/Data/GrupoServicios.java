package library.dataEstructure.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

public class GrupoServicios {

	String name;
	String services;
	int rangeMax;
	int rangeMin;
	boolean isRange = false;
	String protocol;
	Set<String> validSources;
	Set<String> validDestinys;

	public GrupoServicios(CSVRecord csv) {

		String[] serviceRange = { "tcp", "0" };
		String[] serviceRange2;



		name = csv.get(0);
		services = csv.get(1);
		
		//Ips a las que se deben aplicar
		validSources = new HashSet<String>();
		validDestinys = new HashSet<String>();
		validSources.addAll(Arrays.asList(csv.get(2).split(",")));
		validDestinys.addAll(Arrays.asList(csv.get(3).split(",")));

		if (this.isNotKnow(services)) {

			serviceRange = services.split("/");

			if (services.contains("-")) {

				serviceRange2 = serviceRange[1].split("-");

				rangeMax = Integer.parseInt(serviceRange2[1]);
				rangeMin = Integer.parseInt(serviceRange2[0]);
				isRange = true;

			}

		}

		if (this.isNotKnow(services)) {

			this.protocol = serviceRange[0];

		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public boolean filtro(String ipSource, String ipDestiny) {

		boolean allSource = this.validSources.contains("*");
		boolean allDestiny = this.validDestinys.contains("*");
		boolean ipSourceB = this.validSources.contains(ipSource);
		boolean ipDestinyB = this.validDestinys.contains(ipDestiny);

		if ((allSource || ipSourceB) && (allDestiny || ipDestinyB)) {

			return true;

		}

		return false;

	}

	public boolean isCointained(String service, String ipSource, String ipDestiny) {

		boolean res = false;

		if (this.filtro(ipSource, ipDestiny)) {

			if (this.isNotKnow(service) && this.isRange) {

				String[] serviceStripped = getStripperService(service);

				res = serviceStripped[0].equals(this.protocol) && this.isInRange(serviceStripped[1]);

			} else if (services.equals(service)) {

				res = true;

			}

		}

		return res;

	}

	private boolean isInRange(String port) {

		boolean res = false;
		
		if(port.equals("*"))
			return false;

		int portExt = Integer.parseInt(port);

		res = rangeMax >= portExt && portExt >= rangeMin;

		return res;

	}

	private boolean isNotKnow(String service) {
		
		boolean not = service.contains("/");

		boolean prot = service.contains("UDP") || service.contains("TCP");
		

		return not && !prot;

	}

	private String[] getStripperService(String service) {

		return service.split("[/|_]");
	}

}
