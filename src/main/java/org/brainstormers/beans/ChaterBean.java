package org.brainstormers.beans;

import javax.annotation.PostConstruct;

import org.brainstormers.bots.Bot;
import org.brainstormers.bots.GraphMaster;
import org.brainstormers.chats.Chat;
import org.brainstormers.utils.ClassPathUtils;
import org.brainstormers.utils.MagicBooleans;
import org.brainstormers.utils.MagicStrings;
import org.brainstormers.utils.UnZipUtils;
import org.springframework.stereotype.Component;
@Component
public class ChaterBean {
	public Chat chatSession;

	@PostConstruct
	public void postConstruct() {
		String path = System.getProperty("java.io.tmpdir")+"/anukul";
		String zip = ClassPathUtils.getInstance().getPath("anukul-aiml.zip");
		UnZipUtils.getInstance().unzip(zip, path);
		MagicStrings.root_path = System.getProperty("java.io.tmpdir")+"/anukul/anukul-aiml";
		MagicBooleans.trace_mode = true;
		GraphMaster.enableShortCuts = true;
		Bot bot = new Bot("anukulv1.0", MagicStrings.root_path, "chat");
		chatSession = new Chat(bot);
		bot.brain.nodeStats();
		System.out.println("Anukul Started successfully...");
		System.out.println(toString());
	}

	public String respond(String request) {
		return chatSession.multisentenceRespond(request).replaceAll("&lt;", "<").replaceAll("&gt;", ">");
	}

	@Override
	public String toString() {
		return "ChetterBean [rootPath=" + MagicStrings.root_path + ", botPath=" + MagicStrings.bot_path + ", botNamePath=" + MagicStrings.bot_name_path
				+ ", aimlifPath=" + MagicStrings.aimlif_path + ", aimlPath=" + MagicStrings.aiml_path + ", configPath=" + MagicStrings.config_path
				+ ", logPath=" + MagicStrings.log_path + ", setsPath=" + MagicStrings.sets_path + ", mapsPath=" + MagicStrings.maps_path + "]";

	}

}

