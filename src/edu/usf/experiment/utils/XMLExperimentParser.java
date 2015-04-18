package edu.usf.experiment.utils;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import edu.usf.experiment.Trial;
import edu.usf.experiment.robot.Robot;
import edu.usf.experiment.subject.Subject;
import edu.usf.experiment.subject.SubjectLoader;
import edu.usf.experiment.universe.Universe;

public class XMLExperimentParser {

	/***
	 * Loads the especified subject from the xml file
	 * 
	 * @param root
	 * @param gName
	 * @param subName
	 * @param robot 
	 * @return
	 */
	public static Subject loadSubject(ElementWrapper root, String gName,
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
	public static List<Trial> loadTrials(ElementWrapper root, Subject subject,
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

	public static ElementWrapper loadRoot(String logPath, String experimentFile) {
		// Read experiments from xml file
		String fullExpFileName = logPath + "experiment.xml";
		//IOUtils.copyFile(experimentFile, fullExpFileName);

		// Read experiment file
		Document doc = XMLDocReader.readDocument(experimentFile);
		return new ElementWrapper(doc.getDocumentElement());
	}
}
