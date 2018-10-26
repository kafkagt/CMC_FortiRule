package library.dataEstructure.Logs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.Matrix.VlansLight;
import library.dataEstructure.tools.PositionLocator;




public class LogsLight {
	

	private int cuentaTotal = 0;
	
	Set<LineaLogLight> logs ; //list
	
	Long time = 0L;
	Long time2 = 0L;
	
	public  LogsLight(Iterable<CSVRecord> csv, PositionLocator pl, VlansLight vlans) {
		
		logs = new HashSet<LineaLogLight>(2000000);
		


		for (CSVRecord csvIt : csv) {

			

			LineaLogLight log = new LineaLogLight(csvIt, pl, vlans);

			//System.out.println(log);
			logs.add(log);

			if (cuentaTotal % 1000 == 0) {
				System.out.println("Total: " + cuentaTotal + " Tiempo:" + (time - time2)/1000 +" secs -- Avance: " + cuentaTotal/22500 + "%");
				time2 = time;
				time = System.currentTimeMillis();

			} 
			
			if(cuentaTotal == 1000) {
				break;
			}

			cuentaTotal++;
		}

	}

	public int getCuentaTotal() {
		return cuentaTotal;
	}

	public Set<LineaLogLight> getLogs() { //list
		return logs;
	}
	
	

}
