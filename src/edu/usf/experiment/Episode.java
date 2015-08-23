package edu.usf.experiment;

import java.io.File;
import java.util.List;

import edu.usf.experiment.condition.Condition;
import edu.usf.experiment.condition.ConditionLoader;
import edu.usf.experiment.log.Logger;
import edu.usf.experiment.log.LoggerLoader;
import edu.usf.experiment.plot.Plotter;
import edu.usf.experiment.plot.PlotterLoader;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.task.TaskLoader;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.Debug;
import edu.usf.experiment.utils.ElementWrapper;

/**
 * This class represents an actual run. For learning experiments, for example,
 * it might model the period of time between the start and the reaching of a
 * certain goal. An episode consists of cycles. In each cycle, the stepCycle of
 * the subject and universe are called, in that order. Then, stop conditions are
 * evaluated to determine if the episode should end. Tasks and plotters are
 * executed at the end.
 * 
 * @author mllofriu
 * 
 */
public class Episode {

	private Trial trial;
	private int episodeNumber;
	private List<Condition> stopConds;
	private List<Task> beforeCycleTasks;
	private List<Task> afterCycleTasks;
	private int sleep;
	private String logPath;
	private List<Task> beforeEpisodeTasks;
	private List<Task> afterEpisodeTasks;
	private List<Plotter> beforeEpisodePlotters;
	private List<Plotter> afterEpisodePlotters;
	private List<Logger> beforeEpisodeLoggers;
	private List<Logger> beforeCycleLoggers;
	private List<Logger> afterCycleLoggers;
	private List<Logger> afterEpisodeLoggers;

	public Episode(ElementWrapper episodeNode, String parentLogPath, Trial trial, int episodeNumber) {
		this.trial = trial;
		this.episodeNumber = episodeNumber;
		this.sleep = episodeNode.getChildInt("sleep");

		logPath = parentLogPath
				+ File.separator + episodeNumber + File.separator
				+ getSubject().getGroup() + File.separator
				+ getSubject().getName() + File.separator;

		File file = new File(logPath);
		file.mkdirs();

		beforeEpisodeTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("beforeEpisodeTasks"));
		beforeCycleTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("beforeCycleTasks"));
		afterCycleTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("afterCycleTasks"));
		afterEpisodeTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("afterEpisodeTasks"));
		stopConds = ConditionLoader.getInstance().load(
				episodeNode.getChild("stopConditions"));
		beforeEpisodePlotters = PlotterLoader.getInstance().load(
				episodeNode.getChild("beforeEpisodePlotters"), logPath);
		afterEpisodePlotters = PlotterLoader.getInstance().load(
				episodeNode.getChild("afterEpisodePlotters"), logPath);
		beforeEpisodeLoggers = LoggerLoader.getInstance().load(
				episodeNode.getChild("beforeEpisodeLoggers"), logPath);
		beforeCycleLoggers = LoggerLoader.getInstance().load(
				episodeNode.getChild("beforeCycleLoggers"), logPath);
		afterCycleLoggers = LoggerLoader.getInstance().load(
				episodeNode.getChild("afterCycleLoggers"), logPath);
		afterEpisodeLoggers = LoggerLoader.getInstance().load(
				episodeNode.getChild("afterEpisodeLoggers"), logPath);
	}

	public void run() {
		PropertyHolder props = PropertyHolder.getInstance();
		props.setProperty("episode", new Integer(episodeNumber).toString());
		props.setProperty("log.directory", logPath);

		System.out.println("[+] Episode " + trial.getName() + " "
				+ trial.getGroup() + " " + trial.getSubjectName() + " "
				+ episodeNumber + " started.");

		getSubject().newEpisode();
		
		// Do all before trial tasks
		for (Logger logger : beforeEpisodeLoggers)
			logger.log(this);
		for (Task task : beforeEpisodeTasks)
			task.perform(this);
		for (Plotter plotter : beforeEpisodePlotters)
			plotter.plot();

		// Execute cycles until stop condition holds
		boolean finished = false;
		int numCycles = 0;
		while (!finished) {
			for (Logger l : beforeCycleLoggers)
				l.log(this);
			for (Task t : beforeCycleTasks)
				t.perform(this);

			getSubject().stepCycle();
			// TODO: universe step cycle

			for (Logger l : afterCycleLoggers)
				l.log(this);
			for (Task t : afterCycleTasks)
				t.perform(this);

			// Evaluate stop conditions
			for (Condition sc : stopConds)
				finished = finished || sc.holds(this);

			if (Debug.printEndCycle)
				System.out.println("End cycle");

			numCycles++;
			if (numCycles % 1000 == 0)
				System.out.print(".");
			if (numCycles % 5000 == 0)
				System.out.println("");

			if (!finished && sleep != 0)
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}

		System.out.println();

		
		// Finalize loggers
		for (Logger l : afterCycleLoggers)
			l.finalizeLog();
		for (Logger l : beforeCycleLoggers)
			l.finalizeLog();
		for (Logger l : beforeEpisodeLoggers) 
			l.finalizeLog();
		for (Logger l : afterEpisodeLoggers) {
			l.log(this);
			l.finalizeLog();
		}

		// After trial tasks
		for (Task task : afterEpisodeTasks)
			task.perform(this);

		// Plotters
		for (Plotter p : afterEpisodePlotters)
			p.plot();

		System.out.println("[+] Episode " + trial.getName() + " "
				+ trial.getGroup() + " " + trial.getSubjectName() + " "
				+ episodeNumber + " finished.");
	}

	public Subject getSubject() {
		return trial.getSubject();
	}

	public Universe getUniverse() {
		return trial.getUniverse();
	}

}
