package edu.usf.experiment.log;

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
 * Loads conditions based on their non-fully qualified class name (simple name)
 * @author ludo
 *
 */
public class LoggerLoader {

	private static LoggerLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static LoggerLoader getInstance() {
		if (instance == null)
			instance = new LoggerLoader();
		return instance;
	}

	private LoggerLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Logger>> allClasses = reflections
				.getSubTypesOf(Logger.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public List<Logger> load(ElementWrapper conditionNodes) {
		List<Logger> res = new LinkedList<Logger>();
		List<ElementWrapper> conditionList = conditionNodes
				.getChildren("logger");
		for (ElementWrapper conditionNode : conditionList) {
			try {
				Constructor constructor;
				constructor = classBySimpleName.get(
						conditionNode.getChildText("name")).getConstructor(
						ElementWrapper.class);
				Logger plotter = (Logger) constructor
						.newInstance(conditionNode.getChild("params"));
				res.add(plotter);
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
