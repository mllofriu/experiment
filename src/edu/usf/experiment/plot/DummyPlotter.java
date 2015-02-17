package edu.usf.experiment.plot;

import edu.usf.experiment.utils.ElementWrapper;

public class DummyPlotter implements Plotter {

	public DummyPlotter(ElementWrapper params) {
		System.out.println("Creating a dummy plotter");
	}

	@Override
	public void plot() {
		System.out.println("Plotting...");
	}

}
