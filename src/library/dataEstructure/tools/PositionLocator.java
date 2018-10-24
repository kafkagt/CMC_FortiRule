package library.dataEstructure.tools;


import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class PositionLocator {
	
	
	private int srcIP = 0; //IP de origen
	private int dstIP = 1; // IP de destino
	private int dstPort =  2; // Puerto de destino
	private int service = 2; // Servicio
	private int rcvdByte = 0; // Bytes recibidos
	private int sentByte = 0; // Bytes enviados
	private int error = -1;
	private int direction = -1;
	private int allServices = -1;
	

	public PositionLocator(Sheet sheet) {
		
		int position = 0;
		
		Row row = sheet.getRow(0);
		
		for (Cell cell : row) {
			
			
			//Busca la posición de diferentes columnas
			if(cell.getStringCellValue().contains(GestionInformacion.srcIP)) {
				srcIP = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.dstIP)) {
				dstIP = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.dstPort)) {
				dstPort = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.service)) {
				service = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.rcvdByte)) {
				rcvdByte = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.sentByte)) {
				sentByte = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.error)) {
				error = position;
			}else if (cell.getStringCellValue().contains(GestionInformacion.error)) {
				error = position;
			}
			
			position++;
		}
			
		
	}
	
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	
	public int getAllServices() {
		return allServices;
	}

	public void setAllServices(int allServices) {
		this.allServices = allServices;
	}

	public PositionLocator(CSVParser csv) {
		
		int position = 0;
		
		CSVRecord row = csv.iterator().next();
		long numberColum = row.getRecordNumber();
		

		for(int i = 0; i< numberColum; i++) {
			
			String valor = row.get(i);
			
			//Busca la posición de diferentes columnas
			if(row.get(i).contains(GestionInformacion.srcIP)) {
				srcIP = position;
			}else if (valor.contains(GestionInformacion.dstIP)) {
				dstIP = position;
			}else if (valor.contains(GestionInformacion.dstPort)) {
				dstPort = position;
			}else if (valor.contains(GestionInformacion.service)) {
				service = position;
			}else if (valor.contains(GestionInformacion.rcvdByte)) {
				rcvdByte = position;
			}else if (valor.contains(GestionInformacion.sentByte)) {
				sentByte = position;
			}else if (valor.contains(GestionInformacion.error)) {
				error = position;
			}
			
			position++;
		}
			
		
	}
	
	public PositionLocator() {
		
	}
	
	public PositionLocator(CSVRecord csv) {
		
		int position = 0;
		
		CSVRecord row = csv;
		long numberColum = row.size();
		

		for(int i = 0; i<numberColum; i++) {
			
			String valor = row.get(i);
			
			//Busca la posición de diferentes columnas
			if(valor.contains(GestionInformacion.srcIP)) {
				srcIP = position;
			}else if (valor.contains(GestionInformacion.dstIP)) {
				dstIP = position;
			}else if (valor.contains(GestionInformacion.dstPort)) {
				dstPort = position;
			}else if (valor.contains(GestionInformacion.service)) {
				service = position;
			}else if (valor.contains(GestionInformacion.rcvdByte)) {
				rcvdByte = position;
			}else if (valor.contains(GestionInformacion.sentByte)) {
				sentByte = position;
			}else if (valor.contains(GestionInformacion.error)) {
				error = position;
			}
			
			//System.out.println(valor);
			
			position++;
		}
			
		//System.out.println(this.toString());
	}


	public int getSrcIP() {
		return srcIP;
	}


	public void setSrcIP(int srcIP) {
		this.srcIP = srcIP;
	}


	public int getDstIP() {
		return dstIP;
	}


	public void setDstIP(int dstIP) {
		this.dstIP = dstIP;
	}


	public int getDstPort() {
		return dstPort;
	}


	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}


	public int getService() {
		return service;
	}


	public void setService(int service) {
		this.service = service;
	}


	public int getRcvdByte() {
		return rcvdByte;
	}


	public void setRcvdByte(int rcvdByte) {
		this.rcvdByte = rcvdByte;
	}


	public int getSentByte() {
		return sentByte;
	}


	public void setSentByte(int sentByte) {
		this.sentByte = sentByte;
	}
	
	public String toString() {
		return "Posiciones -- srcIP: " + srcIP + " dstIP: "+ dstIP +" dstport: "+ dstPort + " service: " + service + " rcvdByte: "+ rcvdByte + " sentByte: "+ sentByte;
		
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}
	
	

}
