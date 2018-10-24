package Inicio;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;

import library.IO.Reader.FilesFolder;
import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;
import library.dataEstructure.Logs.LogLineLight;
import library.dataEstructure.tools.PositionLocator;
import reglas.Rule;
import reglas.SDP;

public class A_LimpiadorLight {

	public static void main(String[] args) throws IOException {

		String fichero = Rule.plantaActual; // A modificar por el nombre del fichero

		String pathFolder = "src/input/" + fichero + "/";
		String salidaCSV = "src/output/" + fichero + "/Rules-Clean_A.csv";

		String[] files = FilesFolder.getFiles(pathFolder);
		
		// Escribe las reglas
		CSVWriter csvWriter = new CSVWriter(salidaCSV);  

		
		//Contadores de avance 
		int nFile = files.length;
		int count = 1;
	
		
		// apertura del fichero de LOGS Fortinet
		for (String p : files) {
			
			System.out.println("Fichero: " + p + " % Avance: " + count/nFile );
			
			
			FortiReaderCSV frc = new FortiReaderCSV(p, ',','"');
			Iterable<CSVRecord> records = frc.getSheet();

			// crea un localizador de columnas
			PositionLocator pl = new PositionLocator(records.iterator().next());


			

			LogLineLight ll = new LogLineLight();
			
			int cuentaFichero = 0;

			
			long startTime = 0L;
			

			for (CSVRecord c : records) {
				
				SDP tmp = ll.addRecord(c, pl);

				if (tmp != null && Rule.registrar(tmp)) {
					
					
					csvWriter.addLine(tmp);
					
					
				}
				
				if(cuentaFichero%10000 == 0) {
					
					long endTime = System.currentTimeMillis() - startTime;
					System.out.println( "Tiempo: " + endTime/1e3 +" Lineas: " + cuentaFichero + " Lineas de: " + count +"/" + nFile + " Ficheros");
					System.out.println("Lineas por segundo: " + 10000/(endTime/1e3) );
					
					startTime = System.currentTimeMillis();
					
					//return;

				}
					
				
				cuentaFichero++;

			}
			count++;
			//break;
		}

		csvWriter.close();

	}

}
