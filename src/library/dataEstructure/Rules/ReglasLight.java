package library.dataEstructure.Rules;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import library.dataEstructure.Logs.LineaLogLight;
import library.dataEstructure.Logs.LogsLight;
import reglas.Rule;

public class ReglasLight implements Iterable<FortiRuleLight> {

	Set<FortiRuleLight> setReglas;

	public ReglasLight() {

		setReglas = new HashSet<FortiRuleLight>();

	}

	public void addLog(LineaLogLight log) {

		FortiRuleLight fr = new FortiRuleLight();
		fr.addLinea(log);
		setReglas.add(fr);
		// System.out.println(fr);

	}

	private void addLogGrouped(LineaLogLight log) {

		FortiRuleLight fr = new FortiRuleLight();
		fr.addLinea(log);

		if (setReglas.size() == 0) {

			setReglas.add(fr);

		} else {
			
			boolean add = true;

			for (FortiRuleLight frTEMP : setReglas) {
				
				if (frTEMP.isGrupable(fr)) {

					// System.out.println("Grupable");
					frTEMP.addLinea(log); 
					add = false;
					//System.out.println("Grupado");
					break;
				} 

			}
			
			if(add) {
				
				setReglas.add(fr);
				//System.out.println("No grupado");
			}

		}

		// System.out.println(fr);

	}

	public Set<FortiRuleLight> getReglas() {
		return setReglas;
	}
	

	public Set<FortiRuleLight> getSetReglas() {
		return setReglas;
	}

	public void setSetReglas(Set<FortiRuleLight> setReglas) {
		this.setReglas = setReglas;
	}

	public int addLogs(LogsLight logs) {

		int count = 0;

		Set<LineaLogLight> logTMP = logs.getLogs(); //lisT

		for (LineaLogLight linea : logTMP) {

			if (Rule.registrar(linea))
				this.addLog(linea);
			//else
				//System.out.println(linea);

			count++;
		}

		return count; 
	}

	public int addLogsGrouped(LogsLight logs) {

		int count = 0;

		Set<LineaLogLight> logTMP = logs.getLogs(); //lisT

		for (LineaLogLight linea : logTMP) {

			//Cumple las reglas de limpieza

			 
			if (Rule.registrar(linea)) {

				this.addLogGrouped(linea);
				// System.out.println("Registrar");
				

			}
			
			
			count++;
			
			if(count % 1000 ==  0)
				System.out.println("Lineas grupadas: " + this.setReglas.size() + " de " + count + "/" + logTMP.size() );

		}

		return count;

	}

	@Override
	public Iterator<FortiRuleLight> iterator() {

		return this.getReglas().iterator();
	}

}
