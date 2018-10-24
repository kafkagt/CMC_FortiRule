package Inicio;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;


import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;

import reglas.ReglasAgrupableFile;
import reglas.Rule;

public class C_GeneraReglasGruperLight {

	public static void main(String[] args) throws IOException {

		String fichero = Rule.plantaActual; // A modificar por el nombre del fichero

		String CSVClean = "src/output/" + fichero + "/Reglas/Rules-OBJ-CLEAN_B.csv";
		String CSVGroup = "src/output/" + fichero + "/Reglas/Rules-OBJ-CLEAN-Grouped_C.csv";
		
		
		FortiReaderCSV frcsv = new FortiReaderCSV(CSVClean, ';');
		Iterable<CSVRecord> recordsRules = frcsv.getSheet(); 
		
		ReglasAgrupableFile raf;
		
		Set<ReglasAgrupableFile> sRAF = new HashSet<ReglasAgrupableFile>();
		
		
		
		for(CSVRecord csv : recordsRules) {
			
			boolean status = true;
			
			raf = new ReglasAgrupableFile(csv);
			
			for(ReglasAgrupableFile iterator: sRAF) {
				
				if(iterator.isGrupable(raf)) {
					
					iterator.group(raf);
					status = false;
					//System.out.println("Agrupado1: "+ iterator);
					//System.out.println("Agrupado2:" + raf);
					//System.out.println("-----------------------");
					
					break;
					
				}
				
				
			}
			
			if(status)
				sRAF.add(raf);
		}

		CSVWriter lineWriter = new CSVWriter(CSVGroup); 
		lineWriter.addSet(sRAF);
		lineWriter.close();

		
	}

}
