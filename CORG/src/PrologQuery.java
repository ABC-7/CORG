import java.util.ArrayList;

import org.jpl7.Query;
import org.jpl7.Term;

import ReqStructure.*;

public class PrologQuery {

	public static ArrayList<ArrayList<ReqComponent>> generateComponentsBasedRequirements(int reqCount){
		
		
		Query q = new Query("consult('src/SentenceGenerator.pl')");
		System.out.println((q.hasSolution() ? "succeeded" : "failed"));
		
		String query = "generateSentences("+ reqCount+", "+ "ReqL)";
	//	System.out.println(query);
		
		
		do {
			q= new Query(query);
		}while(q == null);
		
		Term requiremens = (Term) q.oneSolution().get("ReqL");
		
		ArrayList<ArrayList<ReqComponent>> comps = convertPrologComponents(requiremens);
		
		return comps;
	}
	
	private static ArrayList<ArrayList<ReqComponent>> convertPrologComponents(Term requiremens) {
		
		ArrayList<SubComponent> subComponents;
		ArrayList<ArrayList<ReqComponent>> res = new ArrayList<ArrayList<ReqComponent>>();
		ArrayList<ReqComponent> comps;
		ReqComponent component;
		String text;
		for (Term  reqCompList: requiremens.toTermArray()) {
			comps = new ArrayList<ReqComponent>();
			if(reqCompList.name().equals("end_of_file"))
				break;
			
			Term[] compArr = reqCompList.arg(1).toTermArray();
			for (Term  c: compArr) {
				if(!c.isCompound())
					continue;
				while(!c.name().equals("comp")) 
					c = c.arg(1);
				subComponents = convertPrologSubComponents(c.arg(1));
				text = getPrologAtomicString(c.arg(2));
				component = new ReqComponent();
				component.setSubComponents(subComponents);
				component.setCompText(text);
				component.setType(subComponents.get(0).getSlotArgs().get(0));
				comps.add(component);
			}
			res.add(comps);
			
		}
			
		return res;
	}
	
	private static ArrayList<SubComponent> convertPrologSubComponents(Term slotList) {
		SubComponent subComponent;
		String text;
		String tag;
		ArrayList<SubComponent> res = new ArrayList<SubComponent>();
		ArrayList<String> args;
		ArrayList<String> vrbArgs;
		for (Term  s: slotList.toTermArray()) {
			if(!s.isCompound())
				continue;
			while(!s.name().equals("slot"))
				s = s.arg(1);
				
			args = new ArrayList<String>();
			vrbArgs = new ArrayList<String>();
			int i = 1;
			Term[]arg ;
			//add primitive terms
			for (; i < 6; i++) {
				arg = s.arg(i).toTermArray();
				if(arg.length > 0)
					args.add(getPrologAtomicString(arg[0]));
				else
					args.add(null);
			}
			String type = args.get(0);
			//add verb arguments
			arg = s.arg(i).toTermArray();
			for (Term item : arg) {
				vrbArgs.add(getPrologAtomicString(item));
			}
			
			text = getPrologAtomicString(s.arg(i+1));
			subComponent = new SubComponent(type, text);
			
			subComponent.setSlotArgs(args);
			subComponent.setVrbArgs(vrbArgs);
			
			res.add(subComponent);	
		}
		return res;
	}
	
	private static String getPrologAtomicString(Term text) {
		String plainTerm = text.toString().replace("'", "");
		plainTerm = plainTerm.replace("(", "");
		plainTerm = plainTerm.replace(")", "");
		plainTerm = plainTerm.replace("+", "");
		plainTerm = plainTerm.replace(",", "");
		plainTerm = plainTerm.replace("\\s+", " ");
		return plainTerm;
	}
	

}
