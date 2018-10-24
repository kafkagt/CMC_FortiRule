package library.IO.Writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class HTMLWriter {

	FileWriter fichero = null;
	PrintWriter pw = null;
	
	int cuentaTR = 0;
	int cuentaTD = 0;

	public HTMLWriter(String path) {

		try {

			fichero = new FileWriter(path);

			pw = new PrintWriter(fichero);

			pw.print("<html>");
			pw.print("<head>");
			pw.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"src/bseu.css\">");
			pw.println("<script type=\"text/javascript\" src=\"src/jquery-3.3.1.min.js\"></script>");
			pw.println("<script type=\"text/javascript\" src=\"src/bseu.js\"></script>");
			pw.println("<script src=\"https://unpkg.com/sticky-table-headers\"></script>");
			pw.println("");
			
			pw.println("</head>");
			pw.print("<body>");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private void addLine(String[] linea) {

		List<String> it = Arrays.asList(linea);

		pw.println("<tr id='"+cuentaTR+"'>");
		cuentaTR++;
		

		for (String celda : it) {
			
			if(celda == null) {
				
				pw.print("<td id='"+cuentaTD+"'> </td>");
				
			}else {
				
				pw.print("<td id='"+cuentaTD+"'>" + celda.replaceAll(" ", "<br>") + "</td>");
			}

			cuentaTD++;

		}
		
		pw.println("</tr>");

	}

	public void close() {

		
		pw.println("</body>");
		pw.println("</html>");
		pw.flush();
		pw.close();
	}

	public void addMatrix(String[][] tabla) {

		pw.println("<div>");
		pw.print("<table>");
		
		pw.println("<thead>");
		this.addLine(tabla[0]);
		pw.println("</thead>");
		

		for (int i = 1; i < tabla.length; i++) {
		

			this.addLine(tabla[i]);
			
		}
		pw.flush();

		pw.println("</table>");
		pw.println("</div>");

	}
}
