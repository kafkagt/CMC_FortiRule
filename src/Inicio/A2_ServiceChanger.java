package Inicio;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream;

import library.IO.Reader.CSVFortiObjetos;
import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;
import library.dataEstructure.Data.GruposServicios;
import reglas.Rule;

public class A2_ServiceChanger {

	public static void main(String[] args) {

		String fichero = Rule.plantaActual; // A modificar por el nombre del fichero

		String pathS = "src/output/" + fichero + "/Reglas/Rules-Clean_A1.csv";
		String services = "src/sources/Servicios.csv";
		String pathOutput = "src/output/" + fichero + "/Reglas/Rules-Clean-SG_A2.csv";

		// Grupos de servicios
		CSVFortiObjetos objServices = new CSVFortiObjetos(services,';');
		Iterable<CSVRecord> recordsServices = objServices.getSheet();
		GruposServicios gsS = new GruposServicios(recordsServices);

		// Reglas
		CSVFortiObjetos objRules = new CSVFortiObjetos(pathS,';');
		Iterable<CSVRecord> recordsRules = objRules.getSheet();
		
		CSVWriter csvWriter = new CSVWriter(pathOutput);
		
		int cuenta = 0;
		
		for(CSVRecord csv: recordsRules) {
			
			String[] line = new String[8];
			
			String s = Rule.limpia(csv.get(5));
			
			line[0] = csv.get(0); //ID
			line[1] = Rule.limpia(csv.get(1)); //INT_Source	
			line[2] = Rule.limpia(csv.get(2)); //Source	
			line[3] = Rule.limpia(csv.get(3)); //Destination
			line[4] = Rule.limpia(csv.get(4)); //INT_Destination
			line[5] = gsS.getNameGroup(s, line[1], line[4]); // Servicios - Grupos
			line[6] = Rule.limpia(csv.get(6)); //Direction
			
			
			//Si es un grupo pon los puertos desglosados
			if(!s.equals(line[5])) {
				line[7] = Rule.limpia(csv.get(5));
			}else {
				
				line[7] = "";
			}
			
			

			
			try {
				
				csvWriter.addLine(line);
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			
			
			
			if(cuenta %10000 == 0) {
				System.out.println("Linea: " + cuenta);
				//break;
			}
				
			
			cuenta++;
		}
		
		
		
		csvWriter.close();

	}
	
	

}
