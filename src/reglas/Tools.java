package reglas;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

import library.dataEstructure.Data.GrupoIPsLight;
import library.dataEstructure.Matrix.VlanLight;

public class Tools {

	static public Set<VlanLight> getVlan(GrupoIPsLight g, Set<VlanLight> sVlan) {

		SubnetUtils subnet;
		SubnetInfo subInfo;

		Set<VlanLight> sVlanNueva = new HashSet<VlanLight>();
 
		for (VlanLight vlan : sVlan) {

			subnet = new SubnetUtils(vlan.getCidr());
			subInfo = subnet.getInfo();

			for (String ip : g.getIps()) {

				if (subInfo.isInRange(ip) || subInfo.getBroadcastAddress().equals(ip)) {

					sVlanNueva.add(vlan);

				}

			}

		}

		return sVlanNueva;

	}
	
	static public Set<VlanLight> getVlan(GrupoIPsLight g, List<VlanLight> sVlan) {

		SubnetUtils subnet;
		SubnetInfo subInfo;

		Set<VlanLight> sVlanNueva = new HashSet<VlanLight>();

		for (VlanLight vlan : sVlan) {

			subnet = new SubnetUtils(vlan.getCidr());
			subInfo = subnet.getInfo();

			for (String ip : g.getIps()) {

				if (subInfo.isInRange(ip)) {

					sVlanNueva.add(vlan);

				}

			}

		}

		return sVlanNueva;

	}
	
	static public Set<VlanLight> getVlans(List<VlanLight> g, List<VlanLight> sVlan) {
		
		Set<VlanLight> setNuevo = new HashSet<VlanLight>();
		
		for(VlanLight gl : g) {
			
			setNuevo.addAll(Tools.getVlan(gl.getGroup(), sVlan));
			
		}
		
		return setNuevo;
		
		
	}

}
