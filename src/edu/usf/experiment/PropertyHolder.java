package edu.usf.experiment;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * This class mantains the properties assigned to an object.
 * 
 * @author ludo
 *
 */
public class PropertyHolder {

	private Dictionary<String, Object> props;

	public PropertyHolder() {
		props = new Hashtable<String, Object>();
	}

	public Object getProperty(String name) {
		return props.get(name);
	}

	public Object setProperty(String name, Object property) {
		return props.put(name, property);
	}
}
