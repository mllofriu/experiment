package edu.usf.experiment.log;


import edu.usf.experiment.Episode;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.utils.ElementWrapper;

public class DummyLogger extends Logger {

	public DummyLogger(ElementWrapper params){
		super(params);
	}
	
	@Override
	public void log(Episode episode) {
		System.out.println("Logging");
	}

	@Override
	public void finalizeLog() {

	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

}
