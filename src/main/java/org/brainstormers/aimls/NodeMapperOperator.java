package org.brainstormers.aimls;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMapperOperator {
	private static final Logger log = LoggerFactory.getLogger(NodeMapperOperator.class);

	/**
	 * number of branches from node
	 *
	 * @param node
	 *            Nodemapper object
	 * @return number of branches
	 */
	public static int size(NodeMapper node) {
		HashSet<String> set = new HashSet<String>();
		if (node.shortCut)
			set.add("<THAT>");
		if (node.key != null)
			set.add(node.key);
		if (node.map != null)
			set.addAll(node.map.keySet());
		return set.size();
	}

	/**
	 * insert a new link from this node to another, by adding a key, value pair
	 *
	 * @param node
	 *            Nodemapper object
	 * @param key
	 *            key word
	 * @param value
	 *            word maps to this next node
	 */
	public static void put(NodeMapper node, String key, NodeMapper value) {
		if (node.map != null) {
			node.map.put(key, value);
		} else { // node.type == unary_node_mapper
			node.key = key;
			node.value = value;

		}
	}

	/**
	 * get the node linked to this one by the word key
	 *
	 * @param node
	 *            Nodemapper object
	 * @param key
	 *            key word to map
	 * @return the mapped node or null if the key is not found
	 */
	public static NodeMapper get(NodeMapper node, String key) {
		if (node.map != null) {
			return node.map.get(key);
		} else {// node.type == unary_node_mapper
			if (key.equals(node.key))
				return node.value;
			else
				return null;
		}

	}

	/**
	 * check whether a node contains a particular key
	 *
	 * @param node
	 *            Nodemapper object
	 * @param key
	 *            key to test
	 * @return true or false
	 */
	public static boolean containsKey(NodeMapper node, String key) {
		// log.info("containsKey: Node="+node+" Map="+node.map);
		if (node.map != null) {
			return node.map.containsKey(key);
		} else {// node.type == unary_node_mapper
			if (key.equals(node.key))
				return true;
			else
				return false;
		}
	}

	/**
	 * print all node keys
	 *
	 * @param node
	 *            Nodemapper object
	 */
	public static void printKeys(NodeMapper node) {
		Set<String> set = keySet(node);
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			log.info("" + iter.next());
		}
	}

	/**
	 * get key set of a node
	 *
	 * @param node
	 *            Nodemapper object
	 * @return set of keys
	 */
	public static Set<String> keySet(NodeMapper node) {
		if (node.map != null) {
			return node.map.keySet();
		} else {// node.type == unary_node_mapper
			Set<String> set = new HashSet<String>();
			if (node.key != null)
				set.add(node.key);
			return set;
		}

	}

	/**
	 * test whether a node is a leaf
	 *
	 * @param node
	 *            Nodemapper object
	 * @return true or false
	 */
	public static boolean isLeaf(NodeMapper node) {
		return (node.category != null);
	}

	/**
	 * upgrade a node from a singleton to a multi-way map
	 *
	 * @param node
	 *            Nodemapper object
	 */
	public static void upgrade(NodeMapper node) {
		// log.info("Upgrading "+node.id);
		// node.type = MagicNumbers.hash_node_mapper;
		node.map = new HashMap<String, NodeMapper>();
		node.map.put(node.key, node.value);
		node.key = null;
		node.value = null;
	}
}
