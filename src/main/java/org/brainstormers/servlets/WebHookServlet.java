package org.brainstormers.servlets;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.brainstormers.beans.FbChatHelper;
import org.brainstormers.fb.contract.FbMsgRequest;
import org.brainstormers.fb.contract.Messaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * 
 * @author Sanjeeb Talukdar
 * 
 *
 */

public class WebHookServlet extends HttpServlet {
	private static final long serialVersionUID = -2326475169699351010L;
	private static final Logger log = LoggerFactory.getLogger(WebHookServlet.class);
	private FbChatHelper helper;

	private String fbPageToken = "EAAXtm2VhEDsBAMe8fj5gkiieRPJrq3V983EPKXM2vf0rZCcLVz1yszK0v7roteOPwAorYNgNTz85nm6TPCwuElZCfKUEYWxzanqf9xvv4XK9LatnZCyIPHqbDpsyvVd3yZCpNMjkDpZAZB2ENQVGreGGk054NKdy7wg7OhewvdtAZDZD";
	private String fbVerifyToken = "1668626560127035";
	private String fbMessageURL = "https://graph.facebook.com/v2.6/me/messages?access_token=";

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}

	/**
	 * get method is used by fb messenger to verify the webhook
	 */
	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final String queryString = request.getQueryString();
		final String verifyToken = request.getParameter("hub.verify_token");
		final String challenge = request.getParameter("hub.challenge");
		final String mode = request.getParameter("hub.mode");
		final String msg = message(queryString, verifyToken, challenge);
		log.error("queryString [" + queryString + "] hub.verify_token = [" + verifyToken
				+ "] hub.challenge = [" + challenge + "] hub.mode = [" + mode + "]" + " fbVerifyToken = ["+fbVerifyToken+"]");
		response.getWriter().write(msg);
		response.getWriter().flush();
		response.getWriter().close();
		response.setStatus(HttpServletResponse.SC_OK);
		return;
	}

	private String message(final String queryString, final String verifyToken, final String challenge) {
		return (queryString == null) ? "Error, wrong token"
				: (StringUtils.equals(fbVerifyToken, verifyToken) && !isEmpty(challenge)) ? challenge : "";
	}

	private void processRequest(final HttpServletRequest httpRequest, final HttpServletResponse response)
			throws IOException, ServletException {
		// store the request body in stringbuffer
		final StringBuffer jb = new StringBuffer();
		try {
			String line = null;
			final BufferedReader reader = httpRequest.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.error("Request received = [" + jb.toString()+"]");
		// convert the string request body in java object
		final FbMsgRequest fbMsgRequest = new Gson().fromJson(jb.toString(), FbMsgRequest.class);
		if (fbMsgRequest == null) {
			log.error("fbMsgRequest was null");
			return;
		}
		if (CollectionUtils.isEmpty(fbMsgRequest.getEntry())) {
			log.error("fbMsgRequest.getEntry() was null or empty");
			return;
		}
		if (CollectionUtils.isEmpty(fbMsgRequest.getEntry().get(0).getMessaging())) {
			log.error("fbMsgRequest.getEntry().get(0).getMessaging() was null");
			return;
		}
		final List<Messaging> messagings = fbMsgRequest.getEntry().get(0).getMessaging();
		for (final Messaging event : messagings) {
			final String sender = event.getSender().getId();
			if (event.getMessage() != null && event.getMessage().getText() != null) {
				final String text = event.getMessage().getText();
				log.error("message received: " + text);
				sendTextMessage(sender, text, false);
			} else if (event.getPostback() != null) {
				final String text = event.getPostback().getPayload();
				log.error("postback received: " + text);
				sendTextMessage(sender, text, true);
			}
		}
		return;
	}

	/**
	 * get the text given by senderId and check if it's a postback (button
	 * click) or a direct message by senderId and reply accordingly
	 * 
	 * @param senderId
	 * @param text
	 * @param isPostBack
	 */
	private void sendTextMessage(final String senderId, final String text, final boolean isPostBack) {
		final List<String> jsonReplies = reply(senderId, text, isPostBack);
		for (final String jsonReply : jsonReplies) {
			try {
				final HttpEntity entity = new ByteArrayEntity(jsonReply.getBytes("UTF-8"));
				/******* for making a post call to fb messenger api **********/
				final HttpClient client = HttpClientBuilder.create().build();
				final HttpPost httppost = new HttpPost(fbMessageURL + fbPageToken);
				httppost.setHeader("Content-Type", "application/json");
				httppost.setEntity(entity);
				final HttpResponse response = client.execute(httppost);
				final String result = EntityUtils.toString(response.getEntity());
				log.error("Result " +result);
			} catch (final Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> reply(final String senderId, final String text, final boolean isPostBack) {
		return isPostBack ? helper.getPostBackReplies(senderId, text) : helper.getReplies(senderId, text);
	}

	@Override
	public void destroy() {
		log.error("webhook Servlet Destroyed");
	}

	@Override
	public void init() throws ServletException {
		log.error("webhook servlet created!!");
	}

	public void setHelper(final FbChatHelper helper) {
		this.helper = helper;
	}
	
	
}
