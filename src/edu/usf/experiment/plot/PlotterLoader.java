package edu.usf.experiment.plot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import edu.usf.experiment.utils.ElementWrapper;

public class PlotterLoader {

	private static PlotterLoader instance;
	private Map<String, Class<?>> classBySimpleName;

	public static PlotterLoader getInstance() {
		if (instance == null)
			instance = new PlotterLoader();
		return instance;
	}

	private PlotterLoader() {
		Reflections reflections = new Reflections();
		Set<Class<? extends Plotter>> allClasses = reflections
				.getSubTypesOf(Plotter.class);
		classBySimpleName = new HashMap<>();

		for (Class<?> c : allClasses) {
			classBySimpleName.put(c.getSimpleName(), c);
		}
	}

	public List<Plotter> load(ElementWrapper plotterNodes) {
		List<Plotter> res = new LinkedList<Plotter>();
		List<ElementWrapper> plotterList = plotterNodes.getChildren("plotter");
		for (ElementWrapper plotterNode : plotterList) {
			try {
				Constructor constructor;
				constructor = classBySimpleName.get(
						plotterNode.getChildText("name")).getConstructor(
						ElementWrapper.class);
				Plotter plotter = (Plotter) constructor.newInstance(plotterNode
						.getChild("params"));
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
