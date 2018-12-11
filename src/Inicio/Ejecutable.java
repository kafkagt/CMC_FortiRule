package Inicio;

import java.io.IOException;

public class Ejecutable {

	public static void main(String[] args) {

		try {
			if (args == null || args.length == 0 || args[0] == "" || args[0].compareToIgnoreCase("Full") == 0) {
	
				System.out.println("--------------------SUSTITUYENDO GRUPOS(\"--------------------");
				A1_GeneraReglasNotGrouped.main(args);	
			}

			System.out.println("(\"--------------------SUSTITUYENDO SERVICIOS(\"--------------------");
			A2_ServiceChanger.main(args);
			
			System.out.println("(\"--------------------AGRUPACIÓN LVL-1(\"--------------------");
			B_GeneraReglasLight.main(args);
			
			System.out.println("(\"--------------------AGRUPACIÓN LVL-2(\"--------------------");
			C_GeneraReglasGruperLight.main(args);
			
			System.out.println("(\"--------------------GENERANDO MATRICES(\"--------------------");
			D_GeneraMatrizLight.main(args);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
