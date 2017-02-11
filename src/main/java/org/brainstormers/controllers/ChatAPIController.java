package org.brainstormers.controllers;

import java.io.IOException;

import org.brainstormers.beans.ChaterBean;
import org.brainstormers.domain.Request;
import org.brainstormers.domain.Response;
import org.brainstormers.utils.ClassPathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ChatAPIController {
	@Autowired
	private ChaterBean chatter;

	@RequestMapping(value = { "/api", "/api/" }, method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
	public ResponseEntity<Response> api(@RequestParam(name = "input", defaultValue = "Hello Anukul!") String input,
			@RequestParam(name = "debug", defaultValue = "false") boolean debug,
			@RequestParam(name = "login", defaultValue = "chrome-demo") String login,
			@RequestParam(name = "key", defaultValue = "") String key,
			@RequestParam(name = "locale", defaultValue = "") String locale,
			@RequestParam(name = "timeZone", defaultValue = "330") Integer timeZone,
			@RequestParam(name = "location", defaultValue = "") String location,
			@RequestParam(name = "safeSearch", defaultValue = "false") boolean safeSearch,
			@RequestParam(name = "clientFeatures", defaultValue = "say,show-images,open-url,show-urls,selector,show-emails,reminder,skype,composer,google-login") String[] clientFeatures) {
		try {
			final Request request = new Request(input, debug, login, key, locale, timeZone, location, safeSearch,
					clientFeatures);
			final ObjectMapper mapper = new ObjectMapper();
			final Response response = mapper.readValue(ClassPathUtils.getInstance().getStream("response.json"), Response.class);
			final String res = chatter.respond(request.getInput());
			response.getInfo().setInput(input);
			response.getOutput().get(0).getActions().getSay().setText(res);
			return new ResponseEntity<Response>(response, HttpStatus.OK);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Response>(HttpStatus.NOT_FOUND);
	}

	public void setChatter(ChaterBean chatter) {
		this.chatter = chatter;
	}
	
}
