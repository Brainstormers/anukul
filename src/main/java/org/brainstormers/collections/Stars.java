package org.brainstormers.collections;

import java.util.ArrayList;

/**
 * Array of values matching wildcards
 */
public class Stars extends ArrayList<String> {
	private static final long serialVersionUID = 1L;

	public String star(int i) {
		if (i < size())
			return get(i);
		else
			return null;
	}

}
