package ReqStructure;

import java.util.ArrayList;



enum ComponentType { Condition, Trigger, Action, ActionScope, PreConditionalScope, ReqScope;
	public static ComponentType getCompType(String typeCode) {
		switch(typeCode) {
		
		case "cond":
			return ComponentType.Condition;
		case "act":
			return ComponentType.Action;
		case "trig":
			return ComponentType.Trigger;
		case "condScope":
			return ComponentType.ReqScope;
		case "precondScope":
			return ComponentType.PreConditionalScope;
		case "actScope":
			return ComponentType.ActionScope;
		}
		return null;
	}	
}

public class ReqComponent{
	
	
	String compText;
	ArrayList<SubComponent> subComponents;
	ComponentType type;
	
	
	public ReqComponent() {
		super();
		subComponents = new ArrayList<SubComponent>();
	}
	
	public ReqComponent(String compText, int sIndex) {
		super();
		subComponents = new ArrayList<SubComponent>();
		this.compText = compText;
	}
	
	public String getCompText() {
		return compText;
	}
	
	public void setCompText(String compText) {
		this.compText = compText;
	}
	
	public ComponentType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = ComponentType.getCompType(type);
	}
	
	public ArrayList<SubComponent> getSubComponents() {
		return subComponents;
	}
	
	public void setSubComponents(ArrayList<SubComponent> subComponents) {
		this.subComponents = subComponents;
	}
	public void addSubComponents(SubComponent subComponent) {
		this.subComponents.add(subComponent);
	}
	
	public boolean isAction() {
		return type.equals(ComponentType.Action);
	}
	
	public String toString() {
		return "comp= " + compText + "}";
	}

}
