package edu.usf.experiment.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ElementWrapper {

	private Element e;

	public ElementWrapper(Element e) {
		this.e = e;
	}

	public ElementWrapper getChild(String name) {
		NodeList elems = e.getElementsByTagName(name);
		for (int i = 0; i < elems.getLength(); i++)
			// Only return direct sibling
			if (elems.item(i).getParentNode() == e)
				return new ElementWrapper((Element) elems.item(i));

		return null;
	}

	public List<ElementWrapper> getChildren(String name) {
		List<ElementWrapper> res = new LinkedList<ElementWrapper>();

		NodeList elems = e.getElementsByTagName(name);
		for (int i = 0; i < elems.getLength(); i++) {
			if (elems.item(i).getParentNode() == e) {
				res.add(new ElementWrapper((Element) elems.item(i)));
			}
		}

		return res;
	}

	public List<ElementWrapper> getChildren() {
		List<ElementWrapper> res = new LinkedList<ElementWrapper>();
		NodeList elems = e.getElementsByTagName("*");
		for (int i = 0; i < elems.getLength(); i++) {
			if (elems.item(i).getParentNode() == e) {
				res.add(new ElementWrapper((Element) elems.item(i)));
			}
		}
		return res;
	}

	public String getChildText(String name) {
		NodeList elems = e.getElementsByTagName(name);
		for (int i = 0; i < elems.getLength(); i++)
			// Only return direct sibling
			if (elems.item(i).getParentNode() == e)
				return elems.item(i).getTextContent();

		return null;
	}

	public int getChildInt(String name) {
		return Integer.parseInt(getChildText(name));
	}

	public float getChildFloat(String name) {
		return Float.parseFloat(getChildText(name));
	}

	public String getText() {
		return e.getTextContent();
	}

	public boolean getChildBoolean(String name) {
		return Boolean.parseBoolean(getChildText(name));
	}

	public long getChildLong(String name) {
		return Long.parseLong(getChildText(name));
	}

	public List<Float> getChildFloatList(String name) {
		String listString = getChildText(name);
		List<Float> list = new LinkedList<Float>();
		StringTokenizer tok = new StringTokenizer(listString, ",");
		while (tok.hasMoreTokens())
			list.add(Float.parseFloat(tok.nextToken()));
		return list;
	}

	public List<Integer> getChildIntList(String name) {
		String listString = getChildText(name);
		List<Integer> list = new LinkedList<Integer>();
		StringTokenizer tok = new StringTokenizer(listString, ",");
		while (tok.hasMoreTokens())
			list.add(Integer.parseInt(tok.nextToken()));
		return list;
	}

	public Map<String, List<String>> getCalibrationList(
			ElementWrapper calibrationRoot) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (ElementWrapper param : calibrationRoot.getChild("model")
				.getChildren()) {
			result.put(param.getName(), new LinkedList<String>());
			for (ElementWrapper value : param.getChildren("value")) {
				result.get(param.getName()).add(value.getText());
			}
		}
		return result;
	}

	private String getName() {
		return e.getNodeName();
	}

	public void changeModelParam(String param, String value, ElementWrapper root) {
		root.getChild("model").getChild("params")
				.getChild(param).setText(value);
	}

	private void setText(String text) {
		e.setTextContent(text);
	}
}
