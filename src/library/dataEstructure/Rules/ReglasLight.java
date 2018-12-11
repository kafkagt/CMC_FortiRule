package library.dataEstructure.Rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import library.dataEstructure.Logs.LineaLogLight;
import library.dataEstructure.Logs.LogsLight;
import reglas.Rule;

public class ReglasLight implements Iterable<FortiRuleLight> {

	Set<FortiRuleLight> setReglas;
	Map<String, FortiRuleLight> setReglasMap;

	public ReglasLight() {

		setReglas = new HashSet<FortiRuleLight>();
		setReglasMap = new HashMap<String, FortiRuleLight>();

	}

	public void addLog(LineaLogLight log) {

		FortiRuleLight fr = new FortiRuleLight();
		fr.addLinea(log);

		String key = fr.getKey();
		FortiRuleLight frd = setReglasMap.get(key);
		//System.out.println(fr.getKey());

		if (frd == null) {

			setReglasMap.put(key, fr);
		} else {

			frd.addLinea(log);

		}

		// System.out.println(fr);

	}
	
	public void addLog2(LineaLogLight log) {

		FortiRuleLight fr = new FortiRuleLight();
		fr.addLinea(log);
		this.setReglas.add(fr);

		

		// System.out.println(fr);

	}

	private void addLogGrouped(LineaLogLight log) {

		FortiRuleLight fr = new FortiRuleLight();
		fr.addLinea(log);

		// Long l1 = System.currentTimeMillis();
		int add = -1;
		int tipoActual = 0;
		FortiRuleLight frTEMPSelected = null;

		int cuenta = 0;

		for (FortiRuleLight frTEMP : setReglas) {

			int tipo = frTEMP.isGrupable(fr);

			if (tipo > tipoActual) {

				tipoActual = tipo;

				if (tipo == frTEMP.MAX_VALUE_GROUP) {

					frTEMPSelected = frTEMP;
					frTEMPSelected.addLinea(log);
					add = 2;
					break;

				} else {

					frTEMPSelected = frTEMP;
					add = 1;

				}

			}
			cuenta++;
		}

		// System.out.println("Vueltas: " + cuenta);
		// Long l2 = System.currentTimeMillis();
		// No se ha encontrado match
		if (add == -1) {

			setReglas.add(fr);

			// se ha encontrado un match no óptimo
		} else if (add == 1) {

			frTEMPSelected.addLinea(log);

		}
		// Long l3 = System.currentTimeMillis();

		// System.out.println("Tiempo1: " + (l3-l2) + "-" + (l2-l1));

	}

	public Map<String, FortiRuleLight> getReglas() {
		return setReglasMap;
	}

	public Set<FortiRuleLight> getSetReglas() {
		return setReglas;
	}

	public void setSetReglas(Set<FortiRuleLight> setReglas) {
		this.setReglas = setReglas;
	}

	public int addLogs(LogsLight logs) {

		int count = 0;

		Set<LineaLogLight> logTMP = logs.getLogs(); // lisT

		for (LineaLogLight linea : logTMP) {

			if (Rule.registrar(linea))
				this.addLog(linea);
		
			// System.out.println(linea);

			count++;

			if (count % 1000 == 0) {
				System.out.println("Añadidos sin agrupar: " + this.setReglasMap.size());
			}
		}

		return count;
	}
	
	public int addLogs2(LogsLight logs) {

		int count = 0;

		Set<LineaLogLight> logTMP = logs.getLogs(); // lisT

		for (LineaLogLight linea : logTMP) {

			if (Rule.registrar(linea)) {
				this.addLog2(linea);
			}
				
		
			// System.out.println(linea);

			count++;

			if (count % 1000 == 0) {
				System.out.println("Añadidos sin agrupar: " + this.setReglasMap.size());
			}
		}

		return count;
	}

	public int addLogsGrouped(LogsLight logs) {

		int count = 0;

		Set<LineaLogLight> logTMP = logs.getLogs(); // lisT

		for (LineaLogLight linea : logTMP) {

//			if (Rule.registrar(linea)) {

			this.addLogGrouped(linea);
			// System.out.println("Tiempo: " + (l - System.currentTimeMillis()));

//			} 

			count++;

			if (count % 1000 == 0)
				System.out.println("Lineas grupadas: " + this.setReglas.size() + " de " + count + "/" + logTMP.size());

		}

		return count;

	}

	@Override
	public Iterator<FortiRuleLight> iterator() {

		if (this.setReglasMap.size() == 0) {

			return this.setReglas.iterator();
		} else {

			return this.getReglas().values().iterator();

		}

	}

}
