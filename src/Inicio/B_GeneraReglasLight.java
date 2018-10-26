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

		ReglasLight reglas = new ReglasLight();

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

		LogsLight logs = new LogsLight(records, pl, vlans);

		reglas.addLogsGrouped(logs);

		// Escribe las reglas
		CSVWriter csvWriter = new CSVWriter(salidaCSV);
		csvWriter.addMatrix(reglas);
		csvWriter.close();

		Set<FortiRuleLight> sFrl = new HashSet<FortiRuleLight>();

		for (FortiRuleLight reglasCreadas : reglas.getReglas()) {

			int grupable = 0;
			int actual = 0;
			FortiRuleLight frlTMP = null;

			for (FortiRuleLight frl : sFrl) {

				grupable = frl.isGrupable(reglasCreadas);

				// si aumentamos de categoria de agrupación, cambiamos el temporal
				if (actual < grupable) {

					actual = grupable;
					frlTMP = frl;

					// Si tenemos la máxima categoría, agrupamos y salimos
					if (actual == frlTMP.MAX_VALUE_GROUP) {

						frl.addLinea(reglasCreadas.getCeldaLocal().getLogs());

						break;
					}

				}

			}

			// agrupación de una categoría no máxima
			if (frlTMP != null) {

				frlTMP.addLinea(reglasCreadas.getCeldaLocal().getLogs());

			} else {

				//No ha sido posible agrupar
				sFrl.add(reglasCreadas);

			}

		}

		ReglasLight reglasClean = new ReglasLight();
		reglasClean.setSetReglas(sFrl);

		csvWriter = new CSVWriter(salidaCSVClean);
		csvWriter.addMatrix(reglasClean);
		csvWriter.close();
	}

}
