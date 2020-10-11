package sf.codingcompetition2020.structures;

public class Agent {
	
	private int agentId;
	private String area;
	private String language;
	private String firstName;
	private String lastName;

	public Agent(int agentId, String area, String language, String firstName, String lastName) {
		this.agentId = agentId;
		this.area = area;
		this.language = language;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getAgentId() {
		return agentId;
	}

	public String getArea() {
		return area;
	}

	public String getLanguage() {
		return language;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
}
