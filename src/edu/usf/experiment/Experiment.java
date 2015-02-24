package edu.usf.experiment;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import edu.usf.experiment.plot.Plotter;
import edu.usf.experiment.plot.PlotterLoader;
import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.robot.RobotLoader;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.SubjectLoader;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.task.TaskLoader;
import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.universe.UniverseLoader;
import edu.usf.experiment.utils.ElementWrapper;
import edu.usf.experiment.utils.IOUtils;
import edu.usf.experiment.utils.XMLDocReader;

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

	public Experiment(String experimentFile, String logPath, String groupName,
			String subjectName) {
		System.out.println(System.getProperty("java.class.path"));
		System.out.println("Starting group " + groupName + " individual "
				+ " in log " + logPath);

		logPath = logPath + File.separator + groupName + File.separator
				+ subjectName + File.separator;

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

		// Read experiments from xml file
		String fullExpFileName = logPath + "experiment.xml";
		IOUtils.copyFile(experimentFile, fullExpFileName);

		// Read experiment file
		Document doc = XMLDocReader.readDocument(fullExpFileName);
		ElementWrapper root = new ElementWrapper(doc.getDocumentElement());

		universe = UniverseLoader.getInstance().load(root);
		
		Robot robot = RobotLoader.getInstance().load(root);

		// Load the subject using reflection and assign name and group
		Subject subject = loadSubject(root, groupName, subjectName, robot);

		// Load trials that apply to the subject
		trials = loadTrials(root, subject, universe);

		// Load tasks and plotters
		beforeTasks = TaskLoader.getInstance().load(
				root.getChild("beforeTasks"));
		afterTasks = TaskLoader.getInstance().load(root.getChild("afterTasks"));
		plotters = PlotterLoader.getInstance().load(root.getChild("plotters"));
	}

	/***
	 * Loads the especified subject from the xml file
	 * 
	 * @param root
	 * @param gName
	 * @param subName
	 * @param robot 
	 * @return
	 */
	private Subject loadSubject(ElementWrapper root, String gName,
			String subName, Robot robot) {
		List<ElementWrapper> groupNodes = root.getChildren("group");
		// Look for the group of the individual to execute
		for (ElementWrapper gNode : groupNodes) {
			if (gNode.getChildText("name").equals(gName)) {
				// Found the group
				return SubjectLoader.getInstance().load(subName, gName, gNode, robot);
			}
		}

		throw new RuntimeException("Group " + gName
				+ " not specified in experiment xml file");
	}

	/***
	 * Load the trials for the especified subject form the xml file
	 * 
	 * @param root
	 * @param subject
	 * @param universe
	 * @return
	 */
	private List<Trial> loadTrials(ElementWrapper root, Subject subject,
			Universe universe) {
		List<Trial> res = new LinkedList<Trial>();

		List<ElementWrapper> trialNodes = root.getChildren("trial");
		// For each trial
		for (ElementWrapper trialNode : trialNodes) {
			// For each group
			List<ElementWrapper> trialGroups = trialNode.getChild("groups")
					.getChildren("group");
			for (ElementWrapper groupNode : trialGroups) {
				String groupName = groupNode.getText();
				// For each subject in the group
				if (groupName.equals(subject.getGroup())) {
					res.add(new Trial(trialNode, subject, universe));
				}
			}
		}

		return res;
	}

	/***
	 * Runs the experiment for the especified subject. Just goes over trials and
	 * runs them all. It also executes tasks and plotters.
	 */
	public void run() {
		// Do all before trial tasks
		for (Task task : beforeTasks)
			task.perform(this);

		// Run each trial in order
		for (Trial t : trials)
			t.run();

		// After trial tasks
		for (Task task : afterTasks)
			task.perform(this);

		// Plotters
		for (Plotter p : plotters)
			p.plot();
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
}
