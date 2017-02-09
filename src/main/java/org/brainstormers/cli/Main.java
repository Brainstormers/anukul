package org.brainstormers.cli;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.brainstormers.aimls.AIMLProcessor;
import org.brainstormers.aimls.Category;
import org.brainstormers.aimls.PCAIMLProcessorExtension;
import org.brainstormers.beans.ChaterBean;
import org.brainstormers.bots.Bot;
import org.brainstormers.bots.GraphMaster;
import org.brainstormers.chats.Chat;
import org.brainstormers.chats.ChatBot;
import org.brainstormers.utils.IOUtils;
import org.brainstormers.utils.MagicBooleans;
import org.brainstormers.utils.MagicStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main (String[] args) {
        MagicStrings.root_path = System.getProperty("user.dir");
        log.info("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        mainFunction(args);
    }
    public static void mainFunction (String[] args) {
    	ChaterBean b = new ChaterBean();
    	b.postConstruct();
        //String botName = "super";
        String action = "chat";
        log.info(MagicStrings.programNameVersion);
        for (String s : args) {
            log.info(s);
            String[] splitArg = s.split("=");
            if (splitArg.length >= 2) {
                String option = splitArg[0];
                String value = splitArg[1];
               // if (option.equals("bot")) botName = value;
                if (option.equals("action")) action = value;
                if (option.equals("trace") && value.equals("true")) MagicBooleans.trace_mode = true;
                else MagicBooleans.trace_mode = false;
            }
        }
        log.info("trace mode = "+MagicBooleans.trace_mode);
        GraphMaster.enableShortCuts = true;
        //Timer timer = new Timer();
        //Bot bot = new Bot(botName, MagicStrings.root_path, action); //
        //bot.preProcessor.normalizeFile("c:/ab/log1.txt", "c:/ab/data/lognormal.txt");
        //if (bot.brain.getCategories().size() < 100) bot.brain.printgraph();
        if (action.equals("chat")) testChat(b.chatSession, MagicBooleans.trace_mode);
        //else if (action.equals("test")) testSuite(bot, MagicStrings.root_path+"/data/find.txt");
        //else if (action.equals("ab")) testAB(bot);
        //else if (action.equals("aiml2csv") || action.equals("csv2aiml")) convert(bot, action);
        //else if (action.equals("abwq")) ChatBot.abwq(bot);
    }
    public static void convert(Bot bot, String action) {
        if (action.equals("aiml2csv")) bot.writeAIMLIFFiles();
        else if (action.equals("csv2aiml")) bot.writeAIMLFiles();
    }
    public static void testAB (Bot bot) {
        MagicBooleans.trace_mode = true;
        ChatBot.ab(bot);
        ChatBot.terminalInteraction(bot) ;
    }
    public static void testShortCuts () {
        //testChat(new Bot("alice"));
        //Graphmaster.enableShortCuts = false;
        //Bot bot = new Bot("alice");
        //bot.brain.printgraph();
        //bot.brain.nodeStats();
        //Graphmaster.enableShortCuts = true;
        //bot = new Bot("alice");
        //bot.brain.printgraph();
        //bot.brain.nodeStats();
    }
    public static void testChat (Chat chatSession, boolean traceMode) {
        //Chat chatSession = new Chat(bot);
//        bot.preProcessor.normalizeFile("c:/ab/bots/super/aiml/thats.txt", "c:/ab/bots/super/aiml/normalthats.txt");
        //bot.brain.nodeStats();
        MagicBooleans.trace_mode = traceMode;
        String textLine="";
        while (true) {
            System.out.print("Human: ");
			textLine = IOUtils.readInputTextLine();
            if (textLine == null || textLine.length() < 1)  textLine = MagicStrings.null_input;
            if (textLine.equals("q")) System.exit(0);
            else if (textLine.equals("wq")) {
               // bot.writeQuit();
                System.exit(0);
            }
            //else if (textLine.equals("ab")) testAB(bot);
            else {
                String request = textLine;
                log.debug("STATE="+request+":THAT="+chatSession.thatHistory.get(0).get(0)+":TOPIC="+chatSession.predicates.get("topic"));
                String response = chatSession.multisentenceRespond(request);
                while (response.contains("&lt;")) response = response.replace("&lt;","<");
                while (response.contains("&gt;")) response = response.replace("&gt;",">");
                log.info("Robot: "+response);

            }

        }
    }
    public static void testBotChat () {
        Bot bot = new Bot("alice");
        log.info(bot.brain.upgradeCnt+" brain upgrades");
        bot.brain.nodeStats();
        //bot.brain.printgraph();
        Chat chatSession = new Chat(bot);
        String request = "Hello.  How are you?  What is your name?  Tell me about yourself.";
        String response = chatSession.multisentenceRespond(request);
        log.info("Human: "+request);
        log.info("Robot: "+response);
    }
    public static void testSuite (Bot bot, String filename) {
        try{
            ChatBot.passed.readAIMLSet(bot);
            ChatBot.testSet.readAIMLSet(bot);
            log.info("Passed "+ChatBot.passed.size()+" samples.");
            String textLine="";
            Chat chatSession = new Chat(bot);
            FileInputStream fstream = new FileInputStream(filename);
            // Get the object
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            //Read File Line By Line
            int count = 0;
            HashSet<String> samples = new HashSet<String>();
            while ((strLine = br.readLine())!= null)   {
                samples.add(strLine);
            }
            ArrayList<String> sampleArray = new ArrayList<String>(samples);
            Collections.sort(sampleArray);
            for (String request : sampleArray) {
                if (request.startsWith("Human: ")) request = request.substring("Human: ".length(), request.length());
                Category c = new Category(0, bot.preProcessor.normalize(request), "*", "*", MagicStrings.blank_template, MagicStrings.null_aiml_file);
                if (ChatBot.passed.contains(request)) log.info("--> Already passed "+request);
                else if (!bot.deletedGraph.existsCategory(c) && !ChatBot.passed.contains(request)) {
                    String response = chatSession.multisentenceRespond(request);
                    log.info(count+". Human: "+request);
                    log.info(count+". Robot: "+response);
					textLine = IOUtils.readInputTextLine();
                    ChatBot.terminalInteractionStep(bot, request, textLine, c);
                    count += 1;
                }
            }
            //Close the input stream
            br.close();
        } catch (Exception e){//Catch exception if any
            log.error("testSuite Error: " + e, e);
        }
    }

}
