package edu.usf.experiment.log;


import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.ElementWrapper;

public class DummyLogger implements Logger {

	public DummyLogger(ElementWrapper params){
		
	}
	
	@Override
	public void log(Episode episode, Subject subject) {
		System.out.println("Logging");
	}

	@Override
	public void finalizeLog() {

	}

}
