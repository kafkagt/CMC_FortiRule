package Inicio;

import java.io.IOException;

public class Ejecutable {

	public static void main(String[] args) {

		try {
			if (args == null || args.length == 0 || args[0] == "") {

				A1_GeneraReglasNotGrouped.main(args);
				A2_ServiceChanger.main(args);
				B_GeneraReglasLight.main(args);
				C_GeneraReglasGruperLight.main(args);
				D_GeneraMatrizLight.main(args);

			} else {
				A2_ServiceChanger.main(args);
				B_GeneraReglasLight.main(args);
				C_GeneraReglasGruperLight.main(args);
				D_GeneraMatrizLight.main(args);

			}
		} catch (IOException e) {
			System.out.println("Error en la lectura de ficheros de entrada");
			e.printStackTrace();
		}

	}

}
