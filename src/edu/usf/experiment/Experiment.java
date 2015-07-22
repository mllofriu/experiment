package edu.usf.experiment;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import edu.usf.experiment.log.Logger;
import edu.usf.experiment.log.LoggerLoader;
import edu.usf.experiment.plot.Plotter;
import edu.usf.experiment.plot.PlotterLoader;
import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.robot.RobotLoader;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.SubjectLoader;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.task.TaskLoader;
import edu.usf.experiment.universe.Feeder;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.universe.UniverseLoader;
import edu.usf.experiment.universe.Wall;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;
import edu.usf.experiment.utils.XMLDocReader;
import edu.usf.experiment.utils.XMLExperimentParser;

/**
 * This holds a set of trials over a group of individuals. Group parameters, and
 * trial parameters are loaded from an xml file. See helloworld.xml for details.
 * 
 * When executed, this class only executes one subject's trials, loading from
 * the xml only the information needed for this subject.
 * 
 * @author gtejera,mllofriu
 * 
 */
public class Experiment implements Runnable {

	private List<Trial> trials;
	private List<Task> beforeTasks;
	private List<Task> afterTasks;
	private List<Plotter> plotters;
	private Universe universe;
	private Subject subject;
	private List<Plotter> beforePlotters;
	private List<Plotter> afterPlotters;
	private List<Logger> beforeLoggers;
	private List<Logger> afterLoggers;

	public Experiment(String experimentFile, String logPath, String groupName,
			String subjectName) {
		// System.out.println(System.getProperty("java.class.path"));
		System.out.println("Starting group " + groupName + " individual "
				+ " in log " + logPath);

		// logPath = logPath + File.separator + groupName + File.separator
		// + subjectName + File.separator;

		// Halt execution if log folder is already there
		File file = new File(logPath);
		if (file.exists()) {
			System.err.println("Logpath already exists. Cannot execute.");
			// System.exit(1);
		} else {
			file.mkdirs();
		}

		PropertyHolder props = PropertyHolder.getInstance();
		props.setProperty("log.directory", logPath);
		props.setProperty("group", groupName);
		props.setProperty("subject", subjectName);

		IOUtils.copyFile(experimentFile, logPath + "/experiment.xml");
		ElementWrapper root = XMLExperimentParser.loadRoot(logPath,
				experimentFile);

		String mazeFile = root.getChild("universe").getChild("params")
				.getChildText("maze");
		IOUtils.copyFile(mazeFile, logPath + "/maze.xml");
		props.setProperty("maze.file", logPath + "/maze.xml");

		universe = UniverseLoader.getInstance().load(root);

		Robot robot = RobotLoader.getInstance().load(root);

		// Load the subject using reflection and assign name and group
		subject = SubjectLoader.getInstance().load(subjectName, groupName, root.getChild("model"), robot);

		// Load trials that apply to the subject
		trials = XMLExperimentParser.loadTrials(root, logPath, subject,
				universe);

		// Load tasks and plotters
		ElementWrapper params = root;
		beforeTasks = TaskLoader.getInstance().load(
				params.getChild("beforeExperimentTasks"));
		afterTasks = TaskLoader.getInstance().load(
				params.getChild("afterExperimentTasks"));
		beforePlotters = PlotterLoader.getInstance().load(
				params.getChild("beforeExperimentPlotters"), logPath);
		afterPlotters = PlotterLoader.getInstance().load(
				params.getChild("afterExperimentPlotters"), logPath);
		beforeLoggers = LoggerLoader.getInstance().load(
				params.getChild("beforeExperimentLoggers"), logPath);
		afterLoggers = LoggerLoader.getInstance().load(
				params.getChild("afterExperimentLoggers"), logPath);
	}

	/***
	 * Runs the experiment for the especified subject. Just goes over trials and
	 * runs them all. It also executes tasks and plotters.
	 */
	public void run() {
		// Do all before trial tasks
		for (Task task : beforeTasks)
			task.perform(this);
		for (Logger logger : beforeLoggers)
			logger.log(this);
		for (Plotter plotter : beforePlotters)
			plotter.plot();

		// Run each trial in order
		for (Trial t : trials)
			t.run();

		// Do all after trial tasks
		for (Task task : afterTasks)
			task.perform(this);
		// Log and finalize
		for (Logger logger : afterLoggers)
			logger.log(this);
		for (Logger logger : beforeLoggers)
			logger.finalizeLog();
		for (Logger logger : afterLoggers)
			logger.finalizeLog(); 
		// Plot
		for (Plotter plotter : afterPlotters)
			plotter.plot();
		
		
	}

	public static void main(String[] args) {
		if (args.length < 4)
			System.out.println("Usage: java edu.usf.experiment "
					+ "exprimentLayout logPath individual group");

		Experiment e = new Experiment(args[0], args[1], args[2], args[3]);
		e.run();

		System.exit(0);
	}

	public Universe getUniverse() {
		return universe;
	}

	public Subject getSubject() {
		return subject;
	}
}
