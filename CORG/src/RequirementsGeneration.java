
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ReqStructure.*;
import edu.stanford.nlp.trees.Tree;

public class RequirementsGeneration {
	

	public static ArrayList<Requirement> generateRequirements(int count) {
		
		long t1, t2;
		ArrayList<Requirement> reqs = null;
		
		System.out.println("Requirements generation through Prolog...");
		//Step1: generate random requirement based components 
		ArrayList<ArrayList<ReqComponent>> comps = PrologQuery.generateComponentsBasedRequirements(count);
		System.out.println("=============================================");
		
		//Step2: adjust tense for each component
		System.out.println("Realisation...");
		ArrayList<ArrayList<ReqComponent>>  adjustedComp = (ArrayList<ArrayList<ReqComponent>>) comps.parallelStream().map(object -> {
            return adjustTenses(object);
        }).collect(Collectors.toList());
		System.out.println("Completed");
		System.out.println("=============================================");
			
		//Step3: remove defected sentences
		System.out.println("Requirements Checking By Stanford...");
		reqs = removeDefectedSentences(adjustedComp);
		System.out.println("=============================================");
		
		return reqs;
	}
	
	private static ArrayList<ReqComponent> adjustTenses(ArrayList<ReqComponent> reqComps) {
		
		ArrayList<ReqComponent> reqBasedComps = new ArrayList<ReqComponent>();
			
			for (ReqComponent comp : reqComps)
				reqBasedComps.add(adjustComponentTennse(comp));
			
		return reqBasedComps;
	} 
	
	private static ReqComponent adjustComponentTennse(ReqComponent comp) {
		//Step1: prepare sentence arguments
		String oldVerb, newVerb, newSlotText, complement, oldCompPortion, newCompPortion;
		String [] args;
		
		
		for (SubComponent crrSubComponent : comp.getSubComponents()) {
			
			//verb is the last argument
			oldVerb = crrSubComponent.getSlotArgs().get(crrSubComponent.getSlotArgs().size()-1);
			
			args = crrSubComponent.getSubCompText().split("(^| )"+oldVerb+"( |$)");
			
			if(!crrSubComponent.isTimeSubComp() ) {
				//case action
				if(comp.isAction() && !crrSubComponent.isHiddenConstraint()) {
					//newSlotText = SimpleNLG.generateImperativeSentence(oldVerb, args[0]).replace(".", " ");
					String passiveSubj = Utility.removeDefectedSpaces(crrSubComponent.getSlotArgs().get(crrSubComponent.getSlotArgs().size()-2)).replace(".", "");
					complement = args[1].replace(".", " ").replace(passiveSubj, "");
					newSlotText = SimpleNLG.getPassiveTense(passiveSubj, oldVerb, complement);
					newSlotText = newSlotText.substring(0, 1).toLowerCase() + newSlotText.substring(1);
					//newVerb = newSlotText.replace(args[0], "");
					newVerb = newSlotText.replace(Utility.removeDefectedSpaces(passiveSubj), "").replace(Utility.removeDefectedSpaces(complement), "");
					complement = args[1];
					newVerb = Utility.removeDefectedSpaces(newVerb);
					oldCompPortion = crrSubComponent.getSubCompText();
					newCompPortion = " "+newSlotText + " ";
				}
				else {
					newSlotText = SimpleNLG.generateSingularPresentSentence(oldVerb, args[0], args[1]).replace(".", " ");
					newSlotText = newSlotText.substring(0, 1).toLowerCase() + newSlotText.substring(1);
					newVerb = newSlotText.replace(Utility.removeDefectedSpaces(args[0]), "").replace(Utility.removeDefectedSpaces(args[1]), "");
					complement = args[1];
					newVerb = Utility.removeDefectedSpaces(newVerb);
					oldCompPortion = oldVerb+" "+complement;
					newCompPortion = newVerb+" "+complement;
				}
				
				
				//update verb arg
				crrSubComponent.getSlotArgs().remove(crrSubComponent.getSlotArgs().size()-1);
				crrSubComponent.getSlotArgs().add(newVerb);
				
				//update slot text
				
				String oldSubCompText = Utility.removeDefectedSpaces(crrSubComponent.getSubCompText());
				crrSubComponent.setSubCompText(newSlotText);
				
				
				//update comp text
				oldCompPortion = Utility.removeDefectedSpaces(oldCompPortion);
				comp.setCompText(Utility.removeDefectedSpaces(comp.getCompText()));
				if(comp.isAction() && crrSubComponent.isHiddenConstraint()) {
					comp.setCompText(comp.getCompText().replace(oldSubCompText, ""));
					String relNoun = Utility.removeDefectedSpaces(crrSubComponent.getSlotArgs().get(1));
					comp.setCompText(comp.getCompText().replace(relNoun, newSlotText));	
				}
				else 
					comp.setCompText(comp.getCompText().replace(oldCompPortion, newCompPortion));
			}
			
			 
		}
		
		return comp;
	}

	private static ArrayList<Requirement> removeDefectedSentences(ArrayList<ArrayList<ReqComponent>> allComps) {
		ArrayList<Requirement> req = new ArrayList<Requirement>();
		
		
		String reqText;
		int i = 1;
		Requirement primReq;
		for (ArrayList<ReqComponent> reqComp : allComps) {
			reqText = "";
			//Step1: concat components text
			for (ReqComponent comp : reqComp)
				reqText = reqText + comp.getCompText() + " , ";
			
			//adjust requirement end 
			reqText = reqText.replaceFirst(", $", "");
			reqText += " .";
			
			//step2: check parsing tree of the whole sentence
			if (! isValidSyntax(reqText)) {
				System.out.println("Failed-Req["+i+"]");
				continue;
			}
			
			System.out.println("Succeeded-Req["+i+"]: "+reqText);
			primReq = new Requirement(reqText, i);
			primReq.setComponents(reqComp);
			
			req.add(primReq);
			i++;
		}
		
		
		
		return req;
	}

	public  static boolean isValidSyntax(String text)
	{
		Tree parsingTree = Dependency.getParsingTree(text);
		
		if(parsingTree.firstChild().label().value().toString().equals("S")
				|| parsingTree.firstChild().label().value().toString().equals("SBAR")
				|| parsingTree.firstChild().label().value().toString().equals("SINV")){
				return true;	
		}
		System.out.println("---------");
		System.out.println(text);
		System.out.println(parsingTree);

		return false;
	}
	
	
}
