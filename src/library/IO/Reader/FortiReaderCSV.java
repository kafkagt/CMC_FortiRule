package library.IO.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class FortiReaderCSV {

	Reader in ;
	Iterable<CSVRecord> records;

	public  FortiReaderCSV(String fileLocation, char separator) {
		try {

			in = new FileReader(fileLocation);
			records = CSVFormat.EXCEL.withNullString("N/A").withDelimiter(separator).parse(in);
			
			//System.out.println(records.iterator().next());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error al abrir el fichero: " + fileLocation);
		}

	}
	public  FortiReaderCSV(String fileLocation, char separator, char encapsulator) {
		try {

			in = new FileReader(fileLocation);
			records = CSVFormat.EXCEL.withNullString("N/A").withIgnoreEmptyLines().withDelimiter(separator).withQuote(encapsulator).parse(in);
			
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
