package ReqStructure;

import java.util.ArrayList;

import com.sun.javadoc.Type;


enum SubCompType {CoreSegment, ValidTime, PreElapsedTime, InBetweenTime, HiddenConstraint;
	public static SubCompType getSubCompType(String typeCode) {
		switch(typeCode) {
		
		case "cond":
		case "act":
		case "trig":
		case "condScope":
		case "precondScope":
		case "actScope":
			return SubCompType.CoreSegment;
		case "hidden":
			return SubCompType.HiddenConstraint;
		case "v-time":
			return SubCompType.ValidTime;
		case "pre-time":
			return SubCompType.PreElapsedTime;
		case "in-time":
			return SubCompType.InBetweenTime;
		}
		return null;
	}	
}

public class SubComponent{
	
	SubCompType subCompType;
	String subCompText;
	ArrayList<ArrayList<String>> subCompArgPOS;
	
	ArrayList<String> subCompArgs;
	ArrayList<String> vrbArgs;
	
	

	public SubComponent(SubCompType subCompType, String slotText, String slotPOS) {
		super();
		this.subCompType = subCompType;
		this.subCompText = slotText;
		this.subCompArgPOS = new ArrayList<ArrayList<String>>();
		this.subCompArgs = new ArrayList<String>();
		this.vrbArgs = new ArrayList<String>();
	}

	
	public SubComponent(String type, String text) {
		subCompType = SubCompType.getSubCompType(type);
		this.subCompText = text;
		this.subCompArgPOS = new ArrayList<ArrayList<String>>();
		this.subCompArgs = new ArrayList<String>();
		this.vrbArgs = new ArrayList<String>();
	}


	public String getSubCompText() {
		return subCompText;
	}

	public void setSubCompText(String text) {
		this.subCompText = text;
	}

	public SubCompType getSubCompType() {
		return subCompType;
	}
	public ArrayList<ArrayList<String>> getSlotArgPOS() {
		return subCompArgPOS;
	}

	public void addSlotArgPOS(ArrayList<String> slotArgPOS) {
		this.subCompArgPOS.add(slotArgPOS);
	}
	public ArrayList<String> getSlotArgs() {
		return subCompArgs;
	}

	public void setSlotArgs(ArrayList<String> slotArgs) {
		this.subCompArgs = slotArgs;
	}

	public void addVerbArg(String vrbArg) {
		this.vrbArgs.add(vrbArg);
	}
	public void addSlotArgs(String slotArg) {
		this.subCompArgs.add(slotArg);
	}
	
	public ArrayList<String> getVrbArgs() {
		return vrbArgs;
	}

	public void setVrbArgs(ArrayList<String> arg) {
		this.vrbArgs = arg;
	}
	
	public boolean isTimeSubComp() {
		return subCompType.equals(SubCompType.ValidTime) || subCompType.equals(SubCompType.PreElapsedTime) || subCompType.equals(SubCompType.InBetweenTime);
	}
	
	public boolean isHiddenConstraint() {
		return subCompType.equals(SubCompType.HiddenConstraint);
	}
	@Override
	public String toString() {
		return "SubCompType= "+subCompType.toString() + "|| Text= " + subCompText +"|| POS= " + subCompArgPOS + "\n";
	}

}