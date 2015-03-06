package edu.usf.experiment.plot;

import edu.usf.experiment.utils.ElementWrapper;

public class DummyPlotter extends Plotter {

	public DummyPlotter(ElementWrapper params) {
		super(params);
		System.out.println("Creating a dummy plotter");
	}

	@Override
	public void plot() {
		System.out.println("Plotting...");
	}

}
