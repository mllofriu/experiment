package edu.usf.experiment.plot;

import java.net.URL;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class CSVConverter extends Plotter {

	public CSVConverter(ElementWrapper params) {
		super(params);
	}

	@Override
	public void plot() {
		PropertyHolder props = PropertyHolder.getInstance();
		String logPath = props.getProperty("log.directory");
		URL resource = getClass().getResource("/edu/usf/experiment/plot/convert.r");
		IOUtils.copyResource(resource,logPath + "convert.r");
		IOUtils.exec("Rscript convert.r", logPath);
		IOUtils.delete(logPath + "convert.r");
	}

}
