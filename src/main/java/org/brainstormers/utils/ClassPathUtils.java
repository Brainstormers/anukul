package org.brainstormers.utils;

import java.io.InputStream;
import java.net.URL;

public class ClassPathUtils {
	private static final ClassPathUtils _SELF = new ClassPathUtils();
	private ClassPathUtils(){}
	public static ClassPathUtils getInstance() {
		return _SELF;
	}
	
	public InputStream getStream(String file) {
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
			return in;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return this.getClass().getResourceAsStream("/"+file);
			} catch (Exception ex) {
				e.printStackTrace();
				return null;
			}
		}
	}
    
    public String getPath(String file) {
		try {
			URL in = this.getClass().getClassLoader().getResource(file);
			return in.getPath();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return this.getClass().getResource("/"+file).getPath();
			} catch (Exception ex) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
