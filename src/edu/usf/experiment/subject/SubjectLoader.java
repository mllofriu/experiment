package edu.usf.experiment.subject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import edu.usf.experiment.utils.ElementWrapper;

/**
 * Loads a subject based on their non-fully qualified class name (simple name)
 * 
 * @author ludo
 * 
 */
public class SubjectLoader {

	private static SubjectLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static SubjectLoader getInstance() {
		if (instance == null)
			instance = new SubjectLoader();
		return instance;
	}

	private SubjectLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Subject>> allClasses = reflections
				.getSubTypesOf(Subject.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public Subject load(String subjectName, String groupName,
			ElementWrapper root) {
		ElementWrapper universeNode = root.getChild("subject");
		try {
			Constructor constructor;
			constructor = classBySimpleName.get(
					universeNode.getChildText("name")).getConstructor(
					String.class, String.class, ElementWrapper.class);
			Subject universe = (Subject) constructor.newInstance(subjectName,
					groupName, universeNode.getChild("params"));
			return universe;
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
