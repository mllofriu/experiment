package edu.usf.experiment.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import edu.usf.experiment.utils.ElementWrapper;

public class TaskLoader {

	private static TaskLoader instance = null;
	private Map<String, Class<?>> classBySimpleName;

	private TaskLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Task>> allClasses = reflections
				.getSubTypesOf(Task.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public static TaskLoader getInstance() {
		if (instance == null)
			instance = new TaskLoader();
		return instance;
	}

	public List<Task> load(ElementWrapper taskNodes) {
		List<Task> res = new LinkedList<Task>();
		List<ElementWrapper> taskList = taskNodes.getChildren("task");
		for (ElementWrapper taskNode : taskList) {
			try {
				Constructor constructor;
				constructor = classBySimpleName.get(
						taskNode.getChildText("name")).getConstructor(
						ElementWrapper.class);
				Task task = (Task) constructor.newInstance(taskNode
						.getChild("params"));
				res.add(task);
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
		}
		return res;
	}

}
