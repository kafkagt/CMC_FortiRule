package reglas;

import java.util.Comparator;

import library.dataEstructure.Matrix.VlanLight;

public class OrdenaVlanGrupo implements Comparator<VlanLight>{

	@Override
	public int compare(VlanLight o1, VlanLight o2) {
		
		if(o1.isGroup() && o2.isGroup() || !o1.isGroup() && !o2.isGroup()) {
			
			return o2.compareTo(o1);
			
		}
		
		if(!o1.isGroup()) {
			
			return Math.abs(o1.compareTo(o2));
			
		}else {
			
			return - Math.abs(o1.compareTo(o2));
		}
		
	
	}

}
