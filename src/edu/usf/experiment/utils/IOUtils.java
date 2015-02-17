package edu.usf.experiment.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class IOUtils {

	public static void copyFile(String src, String dst) {
		try {
			FileUtils.copyFile(new File(src), new File(dst));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
