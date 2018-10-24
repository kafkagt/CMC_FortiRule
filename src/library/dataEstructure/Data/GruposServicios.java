package library.dataEstructure.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

public class GruposServicios {

	Set<GrupoServicios> sGruposServicios;

	public GruposServicios(Iterable<CSVRecord> records) {

		sGruposServicios = new HashSet<GrupoServicios>();

		for (CSVRecord csv : records) {

			GrupoServicios gs = new GrupoServicios(csv);

			sGruposServicios.add(gs);

		}

	}
	

	public String getNameGroup(String service, String ipSource, String ipDestiny) {

		for (GrupoServicios gs : sGruposServicios) {

			if (gs.isCointained(service, ipSource, ipDestiny)) {

				return gs.getName();

			} 

		}

		return service;

	}

}
