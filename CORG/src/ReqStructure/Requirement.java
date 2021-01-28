package ReqStructure;

import java.util.ArrayList;


public class Requirement {
	private String reqText;
	private int Id;
	
	private ArrayList<ReqComponent> components;
	
	public Requirement(String reqText, int id) {
		this.reqText = reqText;
		this.Id = id;
	}
	
	public String getReqText() {
		return reqText;
	}
	
	
	public int getId() {
		return Id;
	}

	public ArrayList<ReqComponent> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<ReqComponent> res) {
		this.components = res;
	}
	

}
