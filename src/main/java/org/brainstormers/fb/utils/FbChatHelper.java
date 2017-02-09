package org.brainstormers.fb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.brainstormers.beans.ChaterBean;
import org.brainstormers.fb.contract.Message;
import org.brainstormers.fb.contract.Messaging;
import org.brainstormers.fb.contract.Recipient;
import org.brainstormers.fb.profile.FbProfile;
import org.brainstormers.fb.servlet.WebHookServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * A Utility class which replies to fb messgaes (or postbacks).<br/>
 * If you already have a service which takes string as input and gives some
 * output, you can easily embed that service in this utility to make your own AI
 * bot.<br/>
 * 
 * A Service can be a search engine for music or a help desk of a company.
 * 
 * @author siddharth
 *
 */
@Component
public class FbChatHelper {

	@Value("{fb.page.token}")
	private String fbPageToken;
	@Value("{fb.profile.link}")
	private String profileLink;
	@Autowired
	private ChaterBean chatter;

	public FbChatHelper() {
	}

	/**
	 * method which analyze the postbacks ie. the button clicks sent by senderId
	 * and replies according to it.
	 * 
	 * @param senderId
	 * @param text
	 * @return
	 */
	public List<String> getPostBackReplies(final String senderId, final String text) {
		final List<String> postbackReplies = new ArrayList<String>();
		final String msg = "received postback msg: " + text;
		System.out.println(msg);
		final String res = chatter.respond(text);
		System.out.println(res);
		final Message fbMsg = getMsg(res);
		final String fbReply = getJsonReply(senderId, fbMsg);
		postbackReplies.add(fbReply);
		return postbackReplies;
	}

	/**
	 * method which analyze the simple texts sent by senderId and replies
	 * according to it.
	 * 
	 * @param senderId
	 * @param text
	 * @return
	 */
	public List<String> getReplies(final String senderId, final String text) {
		final List<String> replies = new ArrayList<String>();
		final String link = StringUtils.replace((profileLink + profileLink), "SENDER_ID", senderId);
		final FbProfile profile = getObjectFromUrl(link, FbProfile.class);
		final String msg = "Hello " + profile.getFirstName() + ", I've received msg: " + text;
		System.out.println(msg);
		final String res = chatter.respond(text);
		System.out.println(res);
		final Message fbMsg = getMsg(res);
		final String fbReply = getJsonReply(senderId, fbMsg);
		replies.add(fbReply);
		return replies;
	}

	private Message getMsg(String msg) {
		Message message = new Message();
		message.setText(msg);
		return message;
	}

	/**
	 * final body which will be sent to fb messenger api through a post call
	 * 
	 * @see WebHookServlet#FB_MSG_URL
	 * @param senderId
	 * @param message
	 * @return
	 */
	private String getJsonReply(String senderId, Message message) {
		Recipient recipient = new Recipient();
		recipient.setId(senderId);
		Messaging reply = new Messaging();
		reply.setRecipient(recipient);
		reply.setMessage(message);

		String jsonReply = new Gson().toJson(reply);
		return jsonReply;
	}

	/**
	 * Returns object of type clazz from an json api link
	 * 
	 * @param link
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private <T> T getObjectFromUrl(final String link, final Class<T> clazz) {
		final StringBuffer jb = new StringBuffer();
		try {
			final URL url = new URL(link);
			final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				jb.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!StringUtils.isEmpty(jb.toString())) {
			final Gson gson = new Gson();
			return gson.fromJson(jb.toString(), clazz);
		}
		return null;
	}
}
