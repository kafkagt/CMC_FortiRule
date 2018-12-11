package Inicio;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.IO.Reader.CSVFortiObjetos;
import library.IO.Reader.FilesFolder;
import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;
import library.dataEstructure.Logs.LogsLight;
import library.dataEstructure.Matrix.VlansLight;
import library.dataEstructure.Rules.FortiRuleLight;
import library.dataEstructure.Rules.ReglasLight;
import library.dataEstructure.tools.PositionLocator;
import reglas.Rule;

public class A1_GeneraReglasNotGrouped {

	public static void main(String[] args) throws IOException {

		String fichero = Rule.plantaActual; // A modificar por el nombre del fichero

		String vlanPath = "src/sources/direccionamiento.csv";
		String objPath = "src/sources/objetos.csv";
		String salidaCSV = "src/output/" + fichero + "/Reglas/Rules-Clean_A1.csv";
		String pathFolder = "src/output/" + fichero + "/";

		String[] files = FilesFolder.getFiles(pathFolder);
 
		// Apertura del fichero de Vlans
		FortiReaderCSV vlanFile = new FortiReaderCSV(vlanPath, ';');
		Iterable<CSVRecord> recordsVlan = vlanFile.getSheet();

		// Apertura de listado de objetos
		CSVFortiObjetos objFile = new CSVFortiObjetos(objPath);
		Iterable<CSVRecord> recordsObj = objFile.getSheet();

		// Tabla de Vlans
		VlansLight vlans = new VlansLight(recordsVlan);
		vlans.addGrupos(recordsObj);

		ReglasLight reglas = new ReglasLight();

		// apertura del fichero de LOGS Fortinet
		for (String p : files) {

			FortiReaderCSV frc = new FortiReaderCSV(p, ';');
			Iterable<CSVRecord> records = frc.getSheet();

			// crea un localizador de columnas
			PositionLocator pl = new PositionLocator(records.iterator().next());

			LogsLight logs = new LogsLight(records, pl, vlans);

			reglas.addLogs2(logs);

		}
		
		// Escribe las reglas
		CSVWriter csvWriter = new CSVWriter(salidaCSV);
		csvWriter.addMatrix(reglas);
		csvWriter.close();
		

		
	}

}
