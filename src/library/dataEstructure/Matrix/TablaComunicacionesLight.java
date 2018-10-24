package library.dataEstructure.Matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import library.dataEstructure.Logs.LineaLogLight;
import library.dataEstructure.tools.PositionLocator;
import reglas.Rule;

public class TablaComunicacionesLight {

	private Map<VlanLight, Map<VlanLight, CeldaLight>> matriz;
	private Set<VlanLight> dstSet;

	private int mismaVlanCount = 0;
	private int ipV6DeleteCount = 0;
	private int cuentaTotal = 0;
	private int lineasCuenta = 0;
	private int timeOutCount = 0;

	public TablaComunicacionesLight() {

		dstSet = new HashSet<VlanLight>();
		matriz = new HashMap<VlanLight, Map<VlanLight, CeldaLight>>();

	}

	public void rellenarTabla(Iterable<CSVRecord> csv, PositionLocator pl, VlansLight vlans) {

		for (CSVRecord csvIt : csv) {

			LineaLogLight log = new LineaLogLight(csvIt, pl, vlans);

			this.addConnection(log);

			// System.out.println(log.toString());

			if (cuentaTotal % 1000 == 0) {
				System.out.println(cuentaTotal);

			}
			if (cuentaTotal % 5000 == 4999) {
				System.out.println(cuentaTotal);
				//return;
			}

			cuentaTotal++;
		}

	}

	public boolean addConnection(LineaLogLight log) {

		Map<VlanLight, CeldaLight> linea = this.matriz.get(log.getSrcVlan());

		// System.out.println(linea);

		CeldaLight celda;

		// La Vlan es la misma
		if (Rule.registrar(log)) {

			// Si no está esta casilla devolvemos fase
			if (linea == null) {

				this.addLinea(log);
				lineasCuenta++;
				// System.out.println("Fila+Columna");

				return true;

			} else {

				// Encontramos el SRC y procedemos a buscar DST
				celda = linea.get(log.getDstVlan());

				// SRC encontrado y DST no creado
				if (celda == null) {

					// Añadimos columna
					this.addCelda(log, linea);
					lineasCuenta++;
					return true;

				}

				lineasCuenta++;
				return celda.add(log);
			}

		} else {

			// System.out.println("Misma Vlan: " + log.getSrcVlan().getVlanName() + " == " +
			// log.getDstVlan().getVlanName() );
			mismaVlanCount++;

		}
		return false;

	}

	public int getLineasCuenta() {
		return lineasCuenta;
	}

	public void setLineasCuenta(int lineasCuenta) {
		this.lineasCuenta = lineasCuenta;
	}

	private void addCelda(LineaLogLight log, Map<VlanLight, CeldaLight> linea) {

		CeldaLight celda = new CeldaLight(log);

		linea.put(log.getDstVlan(), celda);

		this.addDstElement(log.getDstVlan());

	}

	private void addLinea(LineaLogLight log) {

		CeldaLight celda = new CeldaLight(log);
		Map<VlanLight, CeldaLight> linea = new HashMap<VlanLight, CeldaLight>();

		linea.put(log.getDstVlan(), celda);
		matriz.put(log.getSrcVlan(), linea);

		this.addDstElement((VlanLight) log.getSrcVlan());

	}

	private int addDstElement(VlanLight vlan) {

		this.dstSet.add(vlan);
		// System.out.println("Nº:" + this.dstSet.size());
		return this.dstSet.size();
	}

	public String toString() {

		String res = "";

		for (Map<VlanLight, CeldaLight> it : this.matriz.values()) {

			for (CeldaLight celda : it.values()) {

				res += celda.toString();

			}

		}

		return res;

	}

	public Map<VlanLight, Map<VlanLight, CeldaLight>> getMatriz() {
		return matriz;
	}

	public void setMatriz(Map<VlanLight, Map<VlanLight, CeldaLight>> matriz) {
		this.matriz = matriz;
	}

	public Set<VlanLight> getSrcSet() {
		return this.matriz.keySet();
	}

	public Set<VlanLight> getDstSet() {
		return dstSet;
	}

	public void setDstSet(Set<VlanLight> dstSet) {
		this.dstSet = dstSet;
	}

	public boolean mismaVlan(VlanLight a, VlanLight b) {
		return a.equals(b);
	}


	public void soloRangoInterno() {
	}

	public int getMismaVlanCount() {
		return mismaVlanCount;
	}

	public void setMismaVlanCount(int mismaVlanCount) {
		this.mismaVlanCount = mismaVlanCount;
	}

	public int getIpV6DeleteCount() {
		return ipV6DeleteCount;
	}

	public void setIpV6DeleteCount(int ipV6DeleteCount) {
		this.ipV6DeleteCount = ipV6DeleteCount;
	}

	public String[][] toArrays() {

		int nSources = this.matriz.keySet().size();
		int nDestiny = this.getDstSet().size();

		String tabla[][] = new String[nSources + 1][nDestiny + 1];

		Set<VlanLight> sources = this.matriz.keySet();
		Set<VlanLight> destinys = this.getDstSet();

		int i = 1;
		int j = 1;
		int k = 1;

		// Poniendo cabeceras
		for (VlanLight vlanDestiny : destinys) {

			tabla[0][k] = vlanDestiny.getFabrica();
			k++;

		}

		for (VlanLight vlanSource : sources) {

			for (VlanLight vlanDestiny : destinys) {

				if (j == 1) {

					tabla[i][j - 1] = vlanSource.getFabrica();
				}

				CeldaLight celdaDestinoBuscada = matriz.get(vlanSource).get(vlanDestiny);

				if (celdaDestinoBuscada != null) {

					tabla[i][j] = celdaDestinoBuscada.getServices();

				}

				j++;
			}

			i++;
			j = 1;

		}

		return tabla;
	}

	public Set<String[]> toLines() {

		Set<String[]> tabla = new HashSet<String[]>();

		Set<VlanLight> sources = this.matriz.keySet();
		Set<VlanLight> destinys = this.getDstSet();

		// Poniendo cabeceras

		String tmp[] = new String[3];

		tmp[0] = "Source";
		tmp[1] = "Destiny";
		tmp[2] = "Service";

		tabla.add(tmp);
		String linea[] = null;
		for (VlanLight vlanSource : sources) {

			for (VlanLight vlanDestiny : destinys) {

				CeldaLight celdaDestinoBuscada = matriz.get(vlanSource).get(vlanDestiny);

				if (celdaDestinoBuscada != null) {

					linea = new String[3];
					linea[0] = vlanSource.getFabrica();
					linea[1] = vlanDestiny.getFabrica();
					linea[2] = celdaDestinoBuscada.getServices();

					tabla.add(linea);

				}

			}

		}

		return tabla;
	}

	public int getTimeOutCount() {
		return timeOutCount;
	}

	public void setTimeOutCount(int timeOutCount) {
		this.timeOutCount = timeOutCount;
	}
	


}
