package edu.usf.experiment.plot;

import java.net.URL;

import edu.usf.experiment.PropertyHolder;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;

public class SummarizeRuntimes extends Plotter {

	public SummarizeRuntimes(ElementWrapper params) {
		super(params);
	}

	@Override
	public void plot() {
		PropertyHolder props = PropertyHolder.getInstance();
		String logPath = props.getProperty("log.directory");
		URL resource = getClass().getResource("/edu/usf/experiment/plot/summarize.r");
		IOUtils.copyResource(resource,logPath + "summarize.r");
		IOUtils.exec("Rscript summarize.r", logPath);
		IOUtils.delete(logPath + "summarize.r");
	}

}
