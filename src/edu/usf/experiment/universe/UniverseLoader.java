package edu.usf.experiment.universe;

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
 * Loads a universe based on their non-fully qualified class name (simple name)
 * 
 * @author ludo
 * 
 */
public class UniverseLoader {

	private static UniverseLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static UniverseLoader getInstance() {
		if (instance == null)
			instance = new UniverseLoader();
		return instance;
	}

	private UniverseLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Universe>> allClasses = reflections
				.getSubTypesOf(Universe.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public Universe load(ElementWrapper root) {
		ElementWrapper universeNode = root.getChild("universe");
		try {
			Constructor constructor;
			constructor = classBySimpleName.get(
					universeNode.getChildText("name")).getConstructor(
					ElementWrapper.class);
			Universe universe = (Universe) constructor.newInstance(universeNode
					.getChild("params"));
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
