package library.IO.Writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.Rules.AuditedRuleLight;
import library.dataEstructure.Rules.FortiRuleLight;
import library.dataEstructure.Rules.ReglasLight;
import reglas.ReglasAgrupableFile;
import reglas.SDP;

public class CSVWriter {

	String path = "";
	BufferedWriter writer;
	CSVPrinter csvPrinter;
	public static Long write = 0L;
	public static Long flush = 0L;
	public static int cuentaLinea = 0;

	public CSVWriter(String path) {

		this.path = path;

		try {

			writer = Files.newBufferedWriter(Paths.get(path));
			csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(';').withQuote(' '));

		} catch (IOException e) {

			System.out.println("La ruta: " + path + " no existe");
			e.printStackTrace();

		}

	}

	public void addLine(String linea[]) throws IOException {

		List<String> it = Arrays.asList(linea);

		//System.out.println(it);

		csvPrinter.printRecord(it);
		csvPrinter.flush(); 

	}
	
	public void addLine(SDP linea) throws IOException {

		List<String> it = Arrays.asList(linea.toArray());

		//System.out.println(it);

		csvPrinter.printRecord(it);
		csvPrinter.flush();

	}
	
	public void addLine(CSVRecord csvr) throws IOException {

		List<String> it = new ArrayList<String>();
		
		for(int i = 6; i< csvr.size(); i++) {
			
			it.add("'"+csvr.get(i).replaceAll("\"", "")+"'");
			
		}
		

		//System.out.println(it);

		csvPrinter.printRecord(it.toString());
		csvPrinter.flush();

	}
	
	public void addLine(String linea) throws IOException {
		
		


		cuentaLinea++;

		//System.out.println(it);
		long inicio = System.currentTimeMillis();
		
		csvPrinter.printRecord(linea.toString());
		write = write + inicio - System.currentTimeMillis();
		
		
		
		
		if(cuentaLinea % 10 == 0) {
			
			inicio = System.currentTimeMillis();
			csvPrinter.flush();
			flush = flush + inicio - System.currentTimeMillis();
			
		}
			
		
		
		

	}

	public void addLine(FortiRuleLight fr) throws IOException {


		csvPrinter.printRecord(fr.toArray());

	}
	
	public void addLine(FortiRuleLight fr, int nRegla) throws IOException {

		String[] reglaTMP = fr.toArray();
		reglaTMP[0] = "" + nRegla;

		csvPrinter.printRecord(reglaTMP);

	}

	public void addMatrix(String[][] tabla) throws IOException {

		for (int i = 0; i < tabla.length; i++) {
			this.addLine(tabla[i]);
		}

	}
	
	public void addLine(Set<AuditedRuleLight> sARL) throws IOException {

		for (AuditedRuleLight arl: sARL) {
			this.addLine(arl.toArray());
		}

	}
	
	public void addSet(Set<ReglasAgrupableFile> sARL) throws IOException {

		for (ReglasAgrupableFile arl: sARL) {
			this.addLine(arl.toArray());
		}

	}
	public void addMatrix(String[] tabla) throws IOException {

			this.addLine(tabla);


	}
	
	
	

	public void addMatrix(ReglasLight reglas) throws IOException {
		
		int nRegla = 1; 

		for (FortiRuleLight r : reglas) {

			this.addLine(r, nRegla); 
			nRegla++; 
 
		}

	}

	public void addMatrix(Set<String[]> tabla) throws IOException {

		for (String[] s : tabla) {

			this.addLine(s);
		}

	}

	public void close() {
		
		try {
			csvPrinter.flush();
			csvPrinter.close();
		} catch (IOException e) {
			System.out.println("Error al cerrar o flushear el fichero");
			e.printStackTrace();
		}
	}

}
