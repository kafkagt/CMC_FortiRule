package library.dataEstructure.Matrix;

import java.util.HashSet;
import java.util.Set;

import library.dataEstructure.Logs.LineaLogLight;

public class CeldaLight {

	private Set<LineaLogLight> celda;
	private String direction;

	public CeldaLight() {

		celda = new HashSet<LineaLogLight>();
	}

	public CeldaLight(LineaLogLight log) {

		celda = new HashSet<LineaLogLight>();
		celda.add(log);
		this.direction = log.getDirection();
	}

	public boolean add(LineaLogLight log) {

		if (!celda.contains(log)) {
			return celda.add(log);
		}

		// celda.add(log);

		return false;
	}

	public String toString() {

		String res = "";

		for (LineaLogLight celdaIT : this.celda) {
			res += celdaIT.toString() + " ";
		}

		return res;

	}

	public String getServices() {

		String res = "";
		Set<String> listado = new HashSet<String>();

		for (LineaLogLight linea : this.celda) {

			listado.add(linea.getService());

		}

		for (String string : listado) {

			res += string + " ";

		}

		// res += "\"";

		// Eliminar espacio final
		return res.substring(0, res.length() - 1);

	}

	public String getServicesCommaSeparated() {

		String res = "";
		Set<String> listado = new HashSet<String>();

		for (LineaLogLight linea : this.celda) {

			listado.add(linea.getService());

		}

		for (String string : listado) {

			res += string + ",";

		}

		// res += "\"";

		// Eliminamos la última coma
		return res.substring(0, res.length() - 1);

	}

	public Set<LineaLogLight> getLogs() {

		return this.celda;
	}

	public Set<VlanLight> getSources() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (LineaLogLight l : setL) {

			setV.add(l.getSrcVlan());

		}

		return setV;

	}

	public Set<VlanLight> getDestinys() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();

		for (LineaLogLight l : setL) {

			setV.add(l.getDstVlan());

		}

		return setV;

	}

	public Set<String> getSourcesWOVlanName() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<String> setV = new HashSet<String>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getSrcVlan();

			if (v.isGroup()) {

				setV.add(v.getGroup().getName());
			} else {

				setV.add(l.getSrcIP());

			}

		}

		return setV;

	}

	public Set<VlanLight> getSourcesWOVlan() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getSrcVlan();

			if (v.isGroup()) {

				setV.add(v);
			} else {

				setV.add(new VlanLight(l.getSrcIP()));
			}

		}

		return setV;

	}

	public Set<VlanLight> getDestinysWOVlan() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getDstVlan();

			if (v.isGroup()) {

				setV.add(v);

			} else {

				setV.add(new VlanLight(l.getDstIP()));
			}

		}

		return setV;

	}

	public Set<VlanLight> getVlanAndGroupsSource() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getSrcVlan();

			setV.add(v);

		}

		return setV;

	}
	public Set<VlanLight> getVlanAndGroupsDestination() {

		Set<LineaLogLight> setL = this.getLogs();
		Set<VlanLight> setV = new HashSet<VlanLight>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getDstVlan();

			setV.add(v);

		}

		return setV;

	}

	public Set<String> getDestinysWOVlanName() {
		Set<LineaLogLight> setL = this.getLogs();
		Set<String> setV = new HashSet<String>();
		VlanLight v;

		for (LineaLogLight l : setL) {

			v = l.getDstVlan();

			if (v.isGroup()) {

				setV.add(v.getVlanName());
			} else {

				setV.add(l.getDstIP());

			}

		}

		return setV;
	}

	public Set<LineaLogLight> getCelda() {
		return celda;
	}

	public void setCelda(Set<LineaLogLight> celda) {
		this.celda = celda;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
