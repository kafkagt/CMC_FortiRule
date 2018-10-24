package library.dataEstructure.Logs;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.tools.PositionLocator;
import reglas.SDP;

public class LogLineLight {

	Set<SDP> ss = new HashSet<SDP>();

	public LogLineLight() {

	}

	public SDP addRecord(CSVRecord csvR, PositionLocator pl) {
		
		SDP line;
		
		if("".equals(csvR.get(pl.getService()))){
			
			 line = new SDP(csvR.get(pl.getSrcIP()),csvR.get(pl.getDstIP()), csvR.get(pl.getService()-1), csvR.get(pl.getRcvdByte()) );
		}else {
			 line = new SDP(csvR.get(pl.getSrcIP()),csvR.get(pl.getDstIP()), csvR.get(pl.getService()), csvR.get(pl.getRcvdByte()) );
			
		}

		


		if (ss.contains(line)) {
			
			//System.out.println("Repetido: " + line.toString());
			return null;
			
			
		} else {
			
			//System.out.println("Añadido: " + line.toString());
			this.ss.add(line);
			return line;
			
			
		}

	}

	public Set<SDP> getSs() {
		return ss;
	}

	public void setSs(Set<SDP> ss) {
		this.ss = ss;
	}
	
	

}
