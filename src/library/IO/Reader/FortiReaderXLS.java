package library.IO.Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FortiReaderXLS {

	private Workbook workbook;
	private Sheet sheet;

	public  FortiReaderXLS(String fileLocation) {
		FileInputStream file;

		try {

			// Apertura del fichero
			file = new FileInputStream(new File(fileLocation));
			this.workbook = new XSSFWorkbook(file);

		} catch (FileNotFoundException e) {

			System.out.println("El fichero:" + fileLocation + " no existe");

		} catch (IOException e) {

			System.out.println("El fichero: " + fileLocation + " parece abierto");

		}
	}

	public Sheet getSheet(int number) {

		// eligiendo la hoja
		this.sheet = workbook.getSheetAt(number);

		return sheet;

	}

	public void closeFile() {
		try {

			// cerrando el fcihero
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("El fichero no pudo cerrarse");
		}
	}

}
