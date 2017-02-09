package org.brainstormers.fb.servlet;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
import org.brainstormers.fb.contract.FbMsgRequest;
import org.brainstormers.fb.contract.Messaging;
import org.brainstormers.fb.utils.FbChatHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;

/**
 * 
 * @author Sanjeeb Talukdar
 * 
 *
 */

@WebServlet(name="WebHookServlet", urlPatterns={"/webhook"})
public class WebHookServlet extends HttpServlet {
	private static final long serialVersionUID = -2326475169699351010L;

	@Autowired
	private FbChatHelper helper;

	@Value("{fb.page.token}")
	private String fbPageToken;
	@Value("{fb.verify.token}")
	private String fbVerifyToken;
	@Value("{fb.message.url}")
	private String fbMessageURL;

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
		System.out.println("queryString [" + queryString + "] hub.verify_token = [" + verifyToken
				+ "] hub.challenge = [" + challenge + "] hub.mode = [" + mode + "]");
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
		// convert the string request body in java object
		final FbMsgRequest fbMsgRequest = new Gson().fromJson(jb.toString(), FbMsgRequest.class);
		if (fbMsgRequest == null) {
			System.out.println("fbMsgRequest was null");
			return;
		}
		if (CollectionUtils.isEmpty(fbMsgRequest.getEntry())) {
			System.out.println("fbMsgRequest.getEntry() was null or empty");
			return;
		}
		if (CollectionUtils.isEmpty(fbMsgRequest.getEntry().get(0).getMessaging())) {
			System.out.println("fbMsgRequest.getEntry().get(0).getMessaging() was null");
			return;
		}
		final List<Messaging> messagings = fbMsgRequest.getEntry().get(0).getMessaging();
		for (final Messaging event : messagings) {
			final String sender = event.getSender().getId();
			if (event.getMessage() != null && event.getMessage().getText() != null) {
				final String text = event.getMessage().getText();
				System.out.println("message received: " + text);
				sendTextMessage(sender, text, false);
			} else if (event.getPostback() != null) {
				final String text = event.getPostback().getPayload();
				System.out.println("postback received: " + text);
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
				System.out.println(result);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> reply(final String senderId, final String text, final boolean isPostBack) {
		return isPostBack ? helper.getPostBackReplies(senderId, text) : helper.getReplies(senderId, text);
	}

	@Override
	public void destroy() {
		System.out.println("webhook Servlet Destroyed");
	}

	@Override
	public void init() throws ServletException {
		System.out.println("webhook servlet created!!");
	}
}
