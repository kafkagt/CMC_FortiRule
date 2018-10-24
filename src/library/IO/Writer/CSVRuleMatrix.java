package library.IO.Writer;

import java.util.Map;
import java.util.Set;

import library.dataEstructure.Matrix.CeldaLight;
import library.dataEstructure.Matrix.TablaComunicacionesLight;
import library.dataEstructure.Matrix.VlanLight;
import library.dataEstructure.Rules.FortiRuleLight;

public class CSVRuleMatrix {
	
	private String matrixOut;
	private String rulesOut;
	
	
	public CSVRuleMatrix(String matrixCSV, String rulesCSV) {
		
		this.matrixOut = matrixCSV;
		this.rulesOut = rulesCSV;
		
		
	}
	
	public void writeMatrix(TablaComunicacionesLight tc) {
		
		Set<VlanLight> vlanSources =  tc.getSrcSet();
		Set<VlanLight> vlanDestiny = tc.getDstSet();
		
		String[][] tabla = new String[vlanSources.size()+1][vlanDestiny.size()+1];
		
		Map<VlanLight, Map<VlanLight, CeldaLight>> tLocal = tc.getMatriz();
		
		int x = 1;
		int y = 1;
		int z = 0;
		
		for(VlanLight vs: vlanSources) {
			
			y = 1;
			
			for(VlanLight vd: vlanDestiny) {
				
				
				
				CeldaLight celda = tLocal.get(vs).get(vd);
				
				if(celda != null) {
					
					FortiRuleLight fr = new FortiRuleLight(celda);
					
					tabla[x][y] = fr.toString();
					
					//System.out.println(fr.toString());
					
					
				}
				
				
				
				
				y++;
				z++;
				
			}
			x++;
			
		}
		
		
		
		
	}

}
