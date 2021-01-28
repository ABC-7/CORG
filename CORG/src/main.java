
import java.util.ArrayList;

import ReqStructure.*;;

public class main {
		
	public static void main(String[] args) {
		Dependency.loadEnvironment();
		
		ArrayList<Requirement> generatedReq = RequirementsGeneration.generateRequirements(10);
		saveRequirements(generatedReq);
		
		System.out.println("Done");
		

	}

	public static void saveRequirements(ArrayList<Requirement> requirements) {
		int testi = 1;
		ArrayList<String> res = new ArrayList<String>();
		for (Requirement r : requirements) {
			
			String ttt = "Req-"+testi+": ";
			ttt += r.getReqText()+"\n[\n";
			
			for (ReqComponent c : r.getComponents()) {
				ttt += "comp( \n[ \n";
				for (SubComponent s : c.getSubComponents()) {
					
						ttt += "subComp(";
						for (int i = 0; i < s.getSlotArgs().size(); i++) {
							String a = s.getSlotArgs().get(i);
							if(a == null)
								ttt += "Nil, ";
							
							else
								ttt += a+", ";
						}
						ttt += "[";
						for (String a : s.getVrbArgs()) {
							ttt += a+", ";
						}
						ttt += "], ";
						ttt +=  s.getSubCompText()+"),\n";
					
				}
				ttt = ttt.substring(0, ttt.length()-2) + "\n] ),\n\n";
			}
			ttt = ttt.substring(0, ttt.length()-3) + "\n\n]\n";
			ttt += "===========================================\n";
			
			res.add(ttt);
			ttt = "";
			testi++;
		}
		Utility.writeToFile("src/GenReqOut.txt", res);
	}
}