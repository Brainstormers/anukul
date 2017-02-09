package org.brainstormers.domain;

import java.util.Arrays;

public class Request {
	private String input = "Hi";
	private boolean debug = true;
	private String login = "chrome-demo";
	private String key;
	private String locale = "en_US";
	private Integer timeZone = 330;
	private String location;
	private boolean safeSearch = false;
	private String[] clientFeatures = { "say", "show-images", "open-url", "show-urls", "selector", "show-emails",
			"reminder", "skype", "composer", "google-login" };

	public Request(String input, boolean debug, String login, String key, String locale, Integer timeZone,
			String location, boolean safeSearch, String[] clientFeatures) {
		super();
		this.input = input;
		this.debug = debug;
		this.login = login;
		this.key = key;
		this.locale = locale;
		this.timeZone = timeZone;
		this.location = location;
		this.safeSearch = safeSearch;
		this.clientFeatures = clientFeatures;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isSafeSearch() {
		return safeSearch;
	}

	public void setSafeSearch(boolean safeSearch) {
		this.safeSearch = safeSearch;
	}

	public String[] getClientFeatures() {
		return clientFeatures;
	}

	public void setClientFeatures(String[] clientFeatures) {
		this.clientFeatures = clientFeatures;
	}

	@Override
	public String toString() {
		return "Request [input=" + input + ", debug=" + debug + ", login=" + login + ", key=" + key + ", locale="
				+ locale + ", timeZone=" + timeZone + ", location=" + location + ", safeSearch=" + safeSearch
				+ ", clientFeatures=" + Arrays.toString(clientFeatures) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		return true;
	}

}
