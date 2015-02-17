package edu.usf.experiment;

import java.util.LinkedList;
import java.util.List;

import edu.usf.experiment.plot.Plotter;
import edu.usf.experiment.plot.PlotterLoader;
import edu.usf.experiment.subject.ExpSubject;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.task.TaskLoader;
import edu.usf.experiment.utils.ElementWrapper;

/**
 * This class models a runnable trial of an experiment. It consists of N
 * episodes. When run, the trial runs initial tasks, then all episodes, and then
 * final tasks and plotters.
 * 
 * Each trial contains a set of episodes.
 * 
 * @author gtejera, mllofriu
 * 
 */
public class Trial implements Runnable {
	public static boolean cont;
	private String name;
	private ExpSubject subject;

	private List<Task> beforeTasks;
	private List<Plotter> plotters;
	private List<Episode> episodes;
	private List<Task> afterTasks;

	public Trial(ElementWrapper trialNode, String group, ExpSubject subject) {
		super();
		// Trial is identified by its logpath
		this.name = trialNode.getChildText("name");
		this.subject = subject;
		Trial.cont = true;

		beforeTasks = TaskLoader.getInstance().load(
				trialNode.getChild("beforeTasks"));
		afterTasks = TaskLoader.getInstance().load(
				trialNode.getChild("afterTasks"));
		plotters = PlotterLoader.getInstance().load(
				trialNode.getChild("plotters"));

		episodes = new LinkedList<Episode>();
		int numEpisodes = trialNode.getChild("episodes").getChildInt("number");
		for (int i = 0; i < numEpisodes; i++)
			episodes.add(new Episode(trialNode, this, i));
	}

	public void run() {

		// Lock on the subject to ensure mutual exclusion for the same rat
		// Assumes is fifo
		synchronized (getSubject()) {
			// Do all before trial tasks
			for (Task task : beforeTasks)
				task.perform(this);

			// Run each episode
			for (Episode episode : episodes) {
				episode.run();
			}

			// After trial tasks
			for (Task task : afterTasks)
				task.perform(this);

			// Plotters
			for (Plotter p : plotters)
				p.plot();
		}

	}

	public String getName() {
		return name;
	}

	public ExpSubject getSubject() {
		return subject;
	}

	public String getSubjectName() {
		return getSubject().getName();
	}

	public String getGroup() {
		return getSubject().getGroup();
	}

	public String toString() {
		return name;
	}
}
