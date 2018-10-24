package reglas;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;


public class ReglasAgrupableFile {

	String id;
	Set<String> sourceVlan;
	Set<String> sourceIP;
	Set<String> destinyIP;
	Set<String> destinyVlan;
	Set<String> services;
	String direction;
	Set<String> allServices;

	public ReglasAgrupableFile(CSVRecord csv) {

		sourceVlan = new HashSet<String>();
		sourceIP = new HashSet<String>();
		destinyIP = new HashSet<String>();
		destinyVlan = new HashSet<String>();
		services = new HashSet<String>();
		allServices = new HashSet<String>();
		
		

		id = csv.get(0);
		
		sourceVlan.addAll(Arrays.asList(csv.get(1).replaceAll("[\\]|\\[| ]", "").split(",")));
		sourceIP.addAll(Arrays.asList(csv.get(2).replaceAll("[\\]|\\[ ]", "").split(",")));
		destinyIP.addAll(Arrays.asList(csv.get(3).replaceAll("[\\]|\\[ ]", "").split(",")));
		destinyVlan.addAll(Arrays.asList(csv.get(4).replaceAll("[\\]|\\[ ]", "").split(",")));
		services.addAll(Arrays.asList(csv.get(5).replaceAll("[\\]|\\[ ]", "").split(",")));
		direction = csv.get(6);
		allServices.addAll(Arrays.asList(csv.get(7).replaceAll("[\\]|\\[ ]", "").split(",")));

	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isGrupable(ReglasAgrupableFile raf) {
		
		boolean res = Rule.directionGrupable(this.getDirection(), raf.getDirection());

		if (!res) {

			return false;

		}
		


		boolean source = sourceVlan.equals(raf.sourceVlan);

		boolean service = services.equals(raf.services);
		
		//System.out.println(sourceVlan.toString());
		
		int a = raf.allServices.size();
		int b = this.allServices.size();
		
		boolean gruposGrupables = (a == 0 && b == 0) || (a > 0 && b > 0);

		return source && service && gruposGrupables;

	}

	public void group(ReglasAgrupableFile raf) {
		
		sourceIP.addAll(raf.sourceIP);
		sourceVlan.addAll(raf.sourceVlan);
		destinyIP.addAll(raf.destinyIP);
		destinyVlan.addAll(raf.destinyVlan);
		services.addAll(services);

	}
	
	@Override
	public String toString() {
		
		return id + sourceVlan + sourceIP+destinyIP+destinyVlan+services;
	}
	

	public String[] toArray(){
		
		String[] array = new String[8];
		
		array[0] = id;
		array[1] = sourceVlan.toString();
		array[2] = sourceIP.toString();
		array[3] = destinyIP.toString();
		array[4] = destinyVlan.toString();
		array[5] = services.toString();
		array[6] = direction;
		array[7] = allServices.toString();
		
		return array;
	}

}
