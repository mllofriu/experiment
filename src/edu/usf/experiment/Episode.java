package edu.usf.experiment;

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
	private List<Task> beforeTasks;
	private List<Task> afterTasks;
	private List<Plotter> plotters;
	private List<Condition> stopConds;
	private List<Task> beforeCycleTasks;
	private List<Task> afterCycleTasks;
	private List<Logger> loggers;
	private List<Logger> afterLoggers;

	public Episode(ElementWrapper episodeNode, Trial trial, int episodeNumber) {
		this.trial = trial;
		this.episodeNumber = episodeNumber;

		beforeTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("beforeTasks"));
		beforeCycleTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("beforeCycleTasks"));
		afterCycleTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("afterCycleTasks"));
		afterTasks = TaskLoader.getInstance().load(
				episodeNode.getChild("afterTasks"));
		plotters = PlotterLoader.getInstance().load(
				episodeNode.getChild("plotters"));
		stopConds = ConditionLoader.getInstance().load(
				episodeNode.getChild("stopConditions"));
		loggers = LoggerLoader.getInstance().load(episodeNode.getChild("loggers"));
		afterLoggers = LoggerLoader.getInstance().load(episodeNode.getChild("afterLoggers"));
	}

	public void run() {
		PropertyHolder props = PropertyHolder.getInstance();
		props.setProperty("episode", new Integer(episodeNumber).toString());
		
		System.out.println("Episode " + trial.getName() + " "
				+ trial.getGroup() + " " + trial.getSubjectName() + " "
				+ episodeNumber + " started.");

		// Do all before trial tasks
		for (Task task : beforeTasks)
			task.perform(this);
		
		getSubject().newEpisode();

		// Execute cycles until stop condition holds
		boolean finished = false;
		while (!finished) {
			for (Task t : beforeCycleTasks)
				t.perform(this);
			
			getSubject().stepCycle();
			// TODO: universe step cycle

			for (Task t : afterCycleTasks)
				t.perform(this);
			
			for (Logger l : loggers)
				l.log(this);
			
			// Evaluate stop conditions
			for (Condition sc : stopConds)
				finished = finished || sc.holds(this);
		}
		
		for (Logger l : afterLoggers){
			l.log(this);
			l.finalizeLog();
		}
		
		// Finalize loggers
		for (Logger l : loggers)
			l.finalizeLog();

		// After trial tasks
		for (Task task : afterTasks)
			task.perform(this);

		// Plotters
		for (Plotter p : plotters)
			p.plot();

		System.out.println("Episode " + trial.getName() + " "
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
