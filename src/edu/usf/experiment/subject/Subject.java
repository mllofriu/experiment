package edu.usf.experiment.subject;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;

public class Subject extends PropertyHolder {

	private String name;
	private String group;

	public Subject(String name, String group, ElementWrapper modelParams) {
		this.name = name;
		this.group = group;
	}

	/**
	 * Returns the name of the subject
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Advances one cycle in the internal model of the brain usually resulting
	 * in a decision being taken
	 */
	public void stepCycle() {
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setName(String subjectName) {
		this.name = subjectName;
	}

}
