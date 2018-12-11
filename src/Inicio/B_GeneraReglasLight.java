package Inicio;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.IO.Reader.CSVFortiObjetos;

import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;
import library.dataEstructure.Logs.LogsLight;
import library.dataEstructure.Matrix.VlansLight;
import library.dataEstructure.Rules.FortiRuleLight;
import library.dataEstructure.Rules.ReglasLight;
import library.dataEstructure.tools.PositionLocator;
import reglas.Rule;

public class B_GeneraReglasLight {

	public static void main(String[] args) throws IOException {

		String fichero = Rule.plantaActual; // A modificar por el nombre del fichero

		String vlanPath = "src/sources/direccionamiento.csv";
		String objPath = "src/sources/objetos.csv";
		String salidaCSV = "src/output/" + fichero + "/Reglas/Rules-OBJ_B.csv";
		String salidaCSVClean = "src/output/" + fichero + "/Reglas/Rules-OBJ-CLEAN_B.csv";
		String pathFolder = "src/output/" + fichero + "/Reglas/Rules-Clean-SG_A2.csv";

		// Apertura del fichero de Vlans
		FortiReaderCSV vlanFile = new FortiReaderCSV(vlanPath, ';');
		Iterable<CSVRecord> recordsVlan = vlanFile.getSheet();

		// Apertura de listado de objetos
		CSVFortiObjetos objFile = new CSVFortiObjetos(objPath);
		Iterable<CSVRecord> recordsObj = objFile.getSheet();

		// Tabla de Vlans
		VlansLight vlans = new VlansLight(recordsVlan);
		vlans.addGrupos(recordsObj);

		ReglasLight reglasSinAgrupar = new ReglasLight();

		FortiReaderCSV frc = new FortiReaderCSV(pathFolder, ';');
		Iterable<CSVRecord> records = frc.getSheet();

		// crea un localizador de columnas
		PositionLocator pl = new PositionLocator();

		pl.setSrcIP(2);
		pl.setDstIP(3);
		pl.setDstPort(5);
		pl.setService(5);
		pl.setDirection(6);
		pl.setAllServices(7);

		// Carga los registros del fichero con los metadatos
		LogsLight logs = new LogsLight(records, pl, vlans);

		reglasSinAgrupar.addLogs(logs);

		// Escribe las reglas
		CSVWriter csvWriter = new CSVWriter(salidaCSV);
		csvWriter.addMatrix(reglasSinAgrupar);
		csvWriter.close();

		// Nuevo conjunto de fortiRules
		Set<FortiRuleLight> reglasAgrupadas = new HashSet<FortiRuleLight>();

		int count = 0;
		long l = System.currentTimeMillis();

		long p = System.currentTimeMillis();
		long p1 = 0L;
		long p2 = 0L;
		long p3 = 0L;
		int cuenta = 0;
		// Iteramos sobre las reglas creadas
		for (FortiRuleLight reglaCreada : reglasSinAgrupar) {

			int grupable = 0;
			int actual = 0;
			FortiRuleLight frlTMP = null;
			

			if (count++ % 1000 == 0) {

				System.out.println("Lineas: " + count + " tiempo: " + (System.currentTimeMillis() - l) + " Vueltas: " + cuenta + " reglasActuales: " + reglasAgrupadas.size());
				l = System.currentTimeMillis();
				cuenta = 0;

			}

			for (FortiRuleLight reglaAgrupada : reglasAgrupadas) {

				cuenta ++;
				grupable = reglaAgrupada.isGrupable(reglaCreada);

				// si aumentamos de categoria de agrupación, cambiamos el temporal
				if (actual < grupable) {

					actual = grupable;
					frlTMP = reglaAgrupada;

					// Si tenemos la máxima categoría, agrupamos y salimos
					if (actual == frlTMP.MAX_VALUE_GROUP) {

						frlTMP.addLinea(reglaCreada.getCeldaLocal().getLogs());
						
						break;
					}

				}
				


			}

			// Se ha encontrado una puntuación no máxima
			if (frlTMP != null && actual != frlTMP.MAX_VALUE_GROUP) {
				// Agrupar 2 reglas
				frlTMP.addLinea(reglaCreada.getCeldaLocal().getLogs());

			} 

			if(frlTMP == null){

				// No ha sido posible agrupar + Nueva regla
				reglasAgrupadas.add(reglaCreada);

			}
 
		}

		ReglasLight reglasClean = new ReglasLight();
		reglasClean.setSetReglas(reglasAgrupadas);

		csvWriter = new CSVWriter(salidaCSVClean);
		csvWriter.addMatrix(reglasClean);
		csvWriter.close();
	}

}
