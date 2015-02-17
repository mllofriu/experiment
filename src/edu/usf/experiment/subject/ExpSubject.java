package edu.usf.experiment.subject;

import edu.usf.experiment.Experiment;
import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.subject.model.Model;
import edu.usf.experiment.subject.model.ModelLoader;
import edu.usf.experiment.utils.ElementWrapper;

public class ExpSubject extends PropertyHolder {

	private String name;
	private Experiment experiment;
	private String group;
	private Model model;

	public ExpSubject(String name, String group, ElementWrapper modelParams) {
		this.name = name;
		this.group = group;

		this.model = ModelLoader.getInstance().load(modelParams);
	}

	public void initModel() {
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

	public Model getModel() {
		return model;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
