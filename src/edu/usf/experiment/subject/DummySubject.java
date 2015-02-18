package edu.usf.experiment.subject;

import edu.usf.experiment.utils.ElementWrapper;

public class DummySubject extends Subject {

	public DummySubject(String name, String group, ElementWrapper modelParams) {
		super(name, group, modelParams);
		System.out.println("Dummy subject created");
	}

}
