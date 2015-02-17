package edu.usf.experiment.subject.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import edu.usf.experiment.utils.ElementWrapper;

public class ModelLoader {

	private static ModelLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static ModelLoader getInstance() {
		if (instance == null)
			instance = new ModelLoader();
		return instance;
	}

	private ModelLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Model>> allClasses = reflections
				.getSubTypesOf(Model.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public Model load(ElementWrapper modelNode) {
		try {
			Constructor constructor;
			constructor = classBySimpleName.get(modelNode.getChildText("name"))
					.getConstructor();
			Model model = (Model) constructor.newInstance();
			return model;
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