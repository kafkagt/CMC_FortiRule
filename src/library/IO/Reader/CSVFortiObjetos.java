package library.IO.Reader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVFortiObjetos {
	
	Reader in ;
	Iterable<CSVRecord> records;

	public  CSVFortiObjetos(String fileLocation) {
		
		inicializa(fileLocation, ';');
	}
	
	public  CSVFortiObjetos(String fileLocation, char delimiter) {
		
		inicializa(fileLocation, delimiter);
	}
	
	private void inicializa(String fileLocation, char delimiter) {
		
		try {

			in = new FileReader(fileLocation);
			records = CSVFormat.EXCEL.withNullString("N/A").withDelimiter(delimiter).parse(in);
			
			//System.out.println(records.iterator().next());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al abrir el fichero: " + fileLocation);
		}
	}
	
	public Iterable<CSVRecord> getSheet() {
	 return records;
	}
	
	

	

	public void closeFile() {
		
		try {
			in.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al cerrar el fcihero");
		}
		
	}


}
