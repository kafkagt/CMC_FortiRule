package Inicio;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;

import library.IO.Reader.CSVFortiObjetos;
import library.IO.Reader.FortiReaderCSV;
import library.IO.Writer.CSVWriter;
import library.dataEstructure.Matrix.VlansLight;
import library.dataEstructure.Rules.AuditedRulesLight;
import reglas.Rule;


public class D_GeneraMatrizLight {

	public static void main(String[] args) throws IOException {

		String fichero = Rule.plantaActual + "/Reglas/"; // A modificar por el nombre del fichero

		String vlanPath = "src/sources/direccionamiento.csv";
		String objPath = "src/sources/objetos.csv";
		String rules = "src/output/" + fichero + "/Rules-OBJ-CLEAN-Grouped_C.csv";

		String matrizSalidaOO = "src/output/" + fichero + "OTOT/MATRIX_OT-OT.csv";
		String matrizSalidaOW = "src/output/" + fichero + "OTWAN/MATRIX_OT-WAN.csv";
		String matrizSalidaWO = "src/output/" + fichero + "WANOT/MATRIX_WAN-OT.csv";
		String matrizSalidaIO = "src/output/" + fichero + "ITOT/MATRIX_IT-OT.csv";
		String matrizSalidaOI = "src/output/" + fichero + "OTIT/MATRIX_OT-IT.csv";
		String matrizSalidaWI = "src/output/" + fichero + "WANIT/MATRIX_WAN-IT.csv";
		String matrizSalidaIW = "src/output/" + fichero + "ITWAN/MATRIX_IT-WAN.csv";

		String reglasSalidaOO = "src/output/" + fichero + "OTOT/Rules_OT-OT.csv";
		String reglasSalidaOW = "src/output/" + fichero + "OTWAN/Rules_OT-WAN.csv";
		String reglasSalidaWO = "src/output/" + fichero + "WANOT/Rules_WAN-OT.csv";
		String reglasSalidaIO = "src/output/" + fichero + "ITOT/Rules_IT-OT.csv";
		String reglasSalidaOI = "src/output/" + fichero + "OTIT/Rules_OT-IT.csv";
		String reglasSalidaWI = "src/output/" + fichero + "WANIT/Rules_WAN-IT.csv";
		String reglasSalidaIW = "src/output/" + fichero + "ITWAN/Rules_IT-WAN.csv";
		

		// Apertura del fichero de Vlans
		FortiReaderCSV vlanFile = new FortiReaderCSV(vlanPath, ';');
		Iterable<CSVRecord> recordsVlan = vlanFile.getSheet();

		// Apertura de listado de objetos
		CSVFortiObjetos objFile = new CSVFortiObjetos(objPath);
		Iterable<CSVRecord> recordsObj = objFile.getSheet();

		// Tabla de Vlans
		VlansLight onlyVlan = new VlansLight(recordsVlan);

		VlansLight onlyGroup = new VlansLight();
		onlyGroup.addGrupos(recordsObj);


		FortiReaderCSV frcsv = new FortiReaderCSV(rules, ';');
		Iterable<CSVRecord> recordsRules = frcsv.getSheet();

		AuditedRulesLight ars = new AuditedRulesLight(recordsRules, onlyVlan, onlyGroup);

		// Crear lineas Vlan vs Vlan (OT - OT)
		CSVWriter lineWriter = new CSVWriter(matrizSalidaOO);
		lineWriter.addMatrix(ars.toMatrixOTOT());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaOO);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(OT - OT)");

		// Crear lineas Vlan VS Objetos (OT - WAN )
		lineWriter = new CSVWriter(matrizSalidaOW);
		lineWriter.addMatrix(ars.toMatrixOTWAN());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaOW);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(OT - WAN)");

		// Crear lineas Vlan VS Objetos (WAN - OT)
		lineWriter = new CSVWriter(matrizSalidaWO);
		lineWriter.addMatrix(ars.toMatrixWANOT());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaWO);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(WAN - OT)");

		// Crear lineas Vlan VS Objetos (OT - IT )
		lineWriter = new CSVWriter(matrizSalidaOI);
		lineWriter.addMatrix(ars.toMatrixOTIT());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaOI);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(OT - IT)");

		// Crear lineas Vlan VS Objetos (IT - OT )
		lineWriter = new CSVWriter(matrizSalidaIO);
		lineWriter.addMatrix(ars.toMatrixITOT());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaIO);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(IT - OT)");

		// Crear lineas Vlan VS Objetos (IT - WAN )
		lineWriter = new CSVWriter(matrizSalidaIW);
		lineWriter.addMatrix(ars.toMatrixITWAN());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaIW);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(IT - WAN)");

		// Crear lineas Vlan VS Objetos (WAN - IT )
		lineWriter = new CSVWriter(matrizSalidaWI);
		lineWriter.addMatrix(ars.toMatrixWANIT());
		lineWriter.close();
		lineWriter = new CSVWriter(reglasSalidaWI);
		lineWriter.addLine(ars.getLastRules());
		lineWriter.close();

		System.out.println("(WAN - IT)");

		// System.out.println(ars.getNoGrupables());

	}

}
