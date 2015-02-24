package edu.usf.experiment.robot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import edu.usf.experiment.universe.Universe;
import edu.usf.experiment.utils.ElementWrapper;

/**
 * Loads a universe based on their non-fully qualified class name (simple name)
 * 
 * @author ludo
 * 
 */
public class RobotLoader {

	private static RobotLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static RobotLoader getInstance() {
		if (instance == null)
			instance = new RobotLoader();
		return instance;
	}

	private RobotLoader() {
//		Reflections reflections = new Reflections();
//		Set<Class<? extends Robot>> allClasses = reflections
//				.getSubTypesOf(Robot.class);
//		classBySimpleName = new HashMap<>();
//
//		for (Class<?> c : allClasses) {
//			classBySimpleName.put(c.getSimpleName(), c);
//		}
	}

	public Robot load(ElementWrapper root) {
		ElementWrapper robotNode = root.getChild("robot");
		try {
			Constructor constructor;
//			constructor = classBySimpleName.get(
//					robotNode.getChildText("name")).getConstructor(
//					ElementWrapper.class);
			constructor = Class.forName(
					robotNode.getChildText("name")).getConstructor(
					ElementWrapper.class);
			Robot robot = (Robot) constructor.newInstance(robotNode
					.getChild("params"));
			return robot;
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
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
