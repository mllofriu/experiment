package edu.usf.experiment;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import edu.usf.experiment.plot.Plotter;
import edu.usf.experiment.plot.PlotterLoader;
import edu.usf.experiment.subject.ExpSubject;
import edu.usf.experiment.task.Task;
import edu.usf.experiment.task.TaskLoader;
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
public class Experiment extends PropertyHolder implements Runnable {

	private List<Trial> trials;
	private List<Task> beforeTasks;
	private List<Task> afterTasks;
	private List<Plotter> plotters;

	public Experiment(String experimentFile, String logPath, String groupName,
			String subjectName) {
		System.out.println("Starting group " + groupName + " individual "
				+ " in log " + logPath);

		logPath = logPath + File.separator + groupName + File.separator
				+ subjectName + File.separator;

		setProperty("log.directory", logPath);

		// Halt execution if log folder is already there
		File file = new File(logPath);
		if (file.exists()) {
			System.err.println("Logpath already exists. Cannot execute.");
//			System.exit(1);
		} else {
			file.mkdirs();
		}

		// Read experiments from xml file
		String fullExpFileName = logPath + "experiment.xml";
		IOUtils.copyFile(experimentFile, fullExpFileName);

		// Read experiment file
		Document doc = XMLDocReader.readDocument(fullExpFileName);
		ElementWrapper root = new ElementWrapper(doc.getDocumentElement());

		// Load the especified subject and its trials
		ExpSubject subject = loadSubjec(root, groupName, subjectName);
		trials = loadTrials(root, subject);

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
	 * @return
	 */
	private ExpSubject loadSubjec(ElementWrapper root, String gName,
			String subName) {
		List<ElementWrapper> groupNodes = root.getChildren("group");
		// Look for the group of the individual to execute
		for (ElementWrapper gNode : groupNodes) {
			if (gNode.getChildText("name").equals(gName)) {
				// Found the group
				return new ExpSubject(subName, gName, gNode.getChild("model"));
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
	 * @return
	 */
	private List<Trial> loadTrials(ElementWrapper root, ExpSubject subject) {
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
					res.add(new Trial(trialNode, subject.getGroup(), subject));
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
}
