package org.brainstormers.bots;

import java.util.ArrayList;
import java.util.HashSet;

import org.brainstormers.aimls.Category;
import org.brainstormers.aimls.NodeMapper;
import org.brainstormers.aimls.NodeMapperOperator;
import org.brainstormers.aimls.StarBindings;
import org.brainstormers.chats.Chat;
import org.brainstormers.collections.AIMLSet;
import org.brainstormers.collections.Path;
import org.brainstormers.utils.MagicBooleans;
import org.brainstormers.utils.MagicNumbers;
import org.brainstormers.utils.MagicStrings;
import org.brainstormers.utils.MisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AIML Pattern matching algorithm and data structure.
 *
 */
public class GraphMaster {
	private static final Logger log = LoggerFactory.getLogger(GraphMaster.class);
    public Bot bot;
    public final NodeMapper root;
    public int matchCount = 0;
    public int upgradeCnt = 0;
    public HashSet<String> vocabulary;
    public String resultNote = "";
    public int categoryCnt = 0;
    public static boolean enableShortCuts = false;

    /**
     * Constructor
     *
     * @param bot  the bot the graph belongs to.
     */
    public GraphMaster (Bot bot) {
        root = new NodeMapper();
        this.bot = bot;
        vocabulary = new HashSet<String>();
    }

    /**
     * Convert input, that and topic to a single sentence having the form
     * {@code input <THAT> that <TOPIC> topic}
     *
     * @param input  input (or input pattern)
     * @param that   that (or that pattern)
     * @param topic  topic (or topic pattern)
     * @return
     */
    public static String inputThatTopic (String input, String that, String topic)  {
        return input.trim()+" <THAT> "+that.trim()+" <TOPIC> "+topic.trim();
    }

    /**
     * add an AIML category to this graph.
     *
     * @param category            AIML Category
     */
    public void addCategory (Category category) {
        Path p = Path.sentenceToPath(inputThatTopic(category.getPattern(), category.getThat(), category.getTopic()));
        addPath(p, category);
        categoryCnt++;
    }

    boolean thatStarTopicStar(Path path) {
        String tail = Path.pathToSentence(path).trim();
        //log.info("thatStarTopicStar "+tail+" "+tail.equals("<THAT> * <TOPIC> *"));
        return tail.equals("<THAT> * <TOPIC> *");
    }
    void addSets (String type, Bot bot, NodeMapper node) {
        //log.info("adding Set "+type+" from "+bot.setMap);
        String typeName = MisUtils.tagTrim(type, "SET").toLowerCase();
        //AIMLSet aimlSet;
        if (bot.setMap.containsKey(typeName)) {
            if (node.sets == null) node.sets = new ArrayList<String>();
            node.sets.add(typeName);
            // log.info("sets = "+node.sets);
        }
        else {
            log.info("AIML Set "+typeName+" not found.");
        }
    }
    /**
     * add a path to the graph from the root to a Category
     *
     * @param path            Pattern path
     * @param category        AIML category
     */
    void addPath(Path path, Category category) {
        addPath(root, path, category);

    }

    /**
     * add a Path to the graph from a given node.
     * Shortcuts: Replace all instances of paths "<THAT> * <TOPIC> *" with a direct link to the matching category
     *
     * @param node     starting node in graph
     * @param path     Pattern path to be added
     * @param category    AIML Category
     */
    void addPath(NodeMapper node, Path path, Category category) {
        if (path == null) {
            node.category = category;
            node.height = 0;
        }
        else if (enableShortCuts && thatStarTopicStar(path)) {
            node.category = category;
            node.height = Math.min(4, node.height);
            node.shortCut = true;
        }
        else if (NodeMapperOperator.containsKey(node, path.word)) {
            if (path.word.startsWith("<SET>")) addSets(path.word, bot, node);
            NodeMapper nextNode = NodeMapperOperator.get(node, path.word);
            addPath(nextNode, path.next, category);
            int offset = 1;
            if (path.word.equals("#") || path.word.equals("^")) offset = 0;
            node.height = Math.min(offset + nextNode.height, node.height);
        }
        else {
            NodeMapper nextNode = new NodeMapper();
            if (path.word.startsWith("<SET>")) {
                addSets(path.word, bot, node);
            }
            if (node.key != null)  {
                NodeMapperOperator.upgrade(node);
                upgradeCnt++;
            }
            NodeMapperOperator.put(node, path.word, nextNode);
            addPath(nextNode, path.next, category);
            int offset = 1;
            if (path.word.equals("#") || path.word.equals("^")) offset = 0;
            node.height = Math.min(offset + nextNode.height, node.height);
        }
    }
    /**
     *   test if category is already in graph
     *
     *   @return true or false
     */
    public boolean existsCategory(Category c) {
       return (findNode(c) != null);
    }
    /**
     *   test if category is already in graph
     *
     *   @return true or false
     */
    public NodeMapper findNode(Category c) {
        return findNode(c.getPattern(), c.getThat(), c.getTopic());
    }

    /** Given an input pattern, that pattern and topic pattern, find the leaf node associated with this path.
     *
     * @param input    input pattern
     * @param that     that pattern
     * @param topic    topic pattern
     * @return         leaf node or null if no matching node is found
     */
    public NodeMapper findNode(String input, String that, String topic) {
        NodeMapper result = findNode(root, Path.sentenceToPath(inputThatTopic(input, that, topic)));
        if (verbose) log.info("findNode "+inputThatTopic(input, that, topic)+" "+result);
        return result;
    }
    public static boolean verbose = false;

    /**
     * Recursively find a leaf node given a starting node and a path.
     *
     * @param node         string node
     * @param path         string path
     * @return             the leaf node or null if no leaf is found
     */
    NodeMapper findNode(NodeMapper node, Path path) {
        if (path == null && node != null) {
            if (verbose) log.info("findNode: path is null, returning node "+node.category.inputThatTopic());
            return node;
        }
        else if (Path.pathToSentence(path).trim().equals("<THAT> * <TOPIC> *") && node.shortCut && path.word.equals("<THAT>")) {
            if (verbose) log.info("findNode: shortcut, returning "+node.category.inputThatTopic());
            return node;
        }
        else if (NodeMapperOperator.containsKey(node, path.word)) {
            if (verbose) log.info("findNode: node contains "+path.word);
            NodeMapper nextNode = NodeMapperOperator.get(node, path.word.toUpperCase());
            return findNode(nextNode, path.next);
        }

        else {
            if (verbose) log.info("findNode: returning null");
            return null;
        }
    }

    /**
     * Find the matching leaf node given an input, that state and topic value
     *
     * @param input              client input
     * @param that               bot's last sentence
     * @param topic              current topic
     * @return                   matching leaf node or null if no match is found
     */
    public final NodeMapper match(String input, String that, String topic) {
        NodeMapper n = null;
        try {
         String inputThatTopic = inputThatTopic(input, that, topic);
         //log.info("Matching: "+inputThatTopic);
         Path p = Path.sentenceToPath(inputThatTopic);
         //p.print();
         n = match(p, inputThatTopic);
         if (MagicBooleans.trace_mode) {
             if (n != null) {
                log.debug("Matched: "+n.category.inputThatTopic()+" "+n.category.getFilename());
             }
             else log.debug("No match.");
         }
        } catch (Exception ex) {
            //log.info("Match: "+input);
            ex.printStackTrace();
            n = null;
        }
        if (MagicBooleans.trace_mode && Chat.matchTrace.length() < MagicNumbers.max_trace_length) {
            if (n != null) {
				Chat.setMatchTrace(Chat.matchTrace + n.category.inputThatTopic()+"\n");
			}
        }
        return n;
    }

    /**
     * Find the matching leaf node given a path of the form "{@code input <THAT> that <TOPIC> topic}"
     * @param path
     * @param inputThatTopic
     * @return  matching leaf node or null if no match is found
     */
    final NodeMapper match(Path path, String inputThatTopic) {
        try {
        String[] inputStars = new String[MagicNumbers.max_stars];
        String[] thatStars = new String[MagicNumbers.max_stars];
        String[] topicStars = new String[MagicNumbers.max_stars];
        String starState = "inputStar";
        String matchTrace = "";
        NodeMapper n = match(path, root, inputThatTopic, starState, 0, inputStars, thatStars, topicStars, matchTrace);
        if (n != null) {
            StarBindings sb = new StarBindings();
            for (int i=0; inputStars[i] != null && i < MagicNumbers.max_stars; i++) sb.inputStars.add(inputStars[i]);
            for (int i=0; thatStars[i] != null && i < MagicNumbers.max_stars; i++) sb.thatStars.add(thatStars[i]);
            for (int i=0; topicStars[i] != null && i < MagicNumbers.max_stars; i++) sb.topicStars.add(topicStars[i]);
            n.starBindings = sb;
        }
        //if (!n.category.getPattern().contains("*")) log.info("adding match "+inputThatTopic);
        if (n != null) n.category.addMatch(inputThatTopic);
        return n;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Depth-first search of the graph for a matching leaf node.
     * At each node, the order of search is
     * 1. $WORD  (high priority exact word match)
     * 2. # wildcard  (zero or more word match)
     * 3. _ wildcard (one or more words match)
     * 4. WORD (exact word match)
     * 5. {@code <set></set>} (AIML Set match)
     * 6. shortcut (graph shortcut when that pattern = * and topic pattern = *)
     * 7. ^ wildcard  (zero or more words match)
     * 8. * wildcard (one or more words match)
     *
     * @param path      remaining path to be matched
     * @param node      current search node
     * @param inputThatTopic  original input, that and topic string
     * @param starState       tells whether wildcards are in input pattern, that pattern or topic pattern
     * @param starIndex       index of wildcard
     * @param inputStars      array of input pattern wildcard matches
     * @param thatStars       array of that pattern wildcard matches
     * @param topicStars      array of topic pattern wildcard matches
     * @param matchTrace      trace of match path for debugging purposes
     * @return  matching leaf node or null if no match is found
     */
    final NodeMapper match(Path path, NodeMapper node, String inputThatTopic, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        NodeMapper matchedNode;
        //log.info("Match: Height="+node.height+" Length="+path.length+" Path="+Path.pathToSentence(path));
        matchCount++;
        if ((matchedNode = nullMatch(path, node, matchTrace)) != null) return matchedNode;
        else if (path.length < node.height) {
           return null;}

        else if ((matchedNode = dollarMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = sharpMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = underMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = wordMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = setMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = shortCutMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = caretMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else if ((matchedNode = starMatch(path, node, inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null) return matchedNode;
        else {
            return null;
        }
    }

    /**
     * print out match trace when search fails
     *
     * @param mode   Which mode of search
     * @param trace  Match trace info
     */
    void fail (String mode, String trace) {
       // log.info("Match failed ("+mode+") "+trace);
    }

    /**
     * a match is found if the end of the path is reached and the node is a leaf node
     *
     * @param path     remaining path
     * @param node     current search node
     * @param matchTrace   trace of match for debugging purposes
     * @return         matching leaf node or null if no match found
     */
    final NodeMapper nullMatch(Path path, NodeMapper node, String matchTrace) {
        if (path == null && node != null && NodeMapperOperator.isLeaf(node) && node.category != null) return node;
        else {
            fail("null", matchTrace);
            return null;
        }
    }


    final NodeMapper shortCutMatch(Path path, NodeMapper node, String inputThatTopic, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        if (node != null && node.shortCut && path.word.equals("<THAT>") && node.category != null) {
            String tail = Path.pathToSentence(path).trim();
            //log.info("Shortcut tail = "+tail);
            String that = tail.substring(tail.indexOf("<THAT>")+"<THAT>".length(), tail.indexOf("<TOPIC>")).trim();
            String topic = tail.substring(tail.indexOf("<TOPIC>")+"<TOPIC>".length(), tail.length()).trim();
            //log.info("Shortcut that = "+that+" topic = "+topic);
            //log.info("Shortcut matched: "+node.category.inputThatTopic());
            thatStars[0] = that;
            topicStars[0] = topic;
            return node;
        }
        else {
            fail("shortCut", matchTrace);
            return null;
        }
    }
    final NodeMapper wordMatch(Path path, NodeMapper node, String inputThatTopic, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        NodeMapper matchedNode;
        try {
            String uword = path.word.toUpperCase();
            if (uword.equals("<THAT>")) {starIndex = 0; starState = "thatStar";}
            else if (uword.equals("<TOPIC>")) {starIndex = 0; starState = "topicStar";}
            //log.info("path.next= "+path.next+" node.get="+node.get(uword));
            matchTrace += "["+uword+","+uword+"]";
            if (path != null && NodeMapperOperator.containsKey(node, uword) &&
                    (matchedNode = match(path.next, NodeMapperOperator.get(node, uword), inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null)  {
                 return matchedNode;
            } else {
                fail("word", matchTrace);
                return null;
            }
        } catch (Exception ex) {
            log.info("wordMatch: "+Path.pathToSentence(path)+": "+ex);
            ex.printStackTrace();
            return null;
        }
    }
    final NodeMapper dollarMatch(Path path, NodeMapper node, String inputThatTopic, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        String uword = "$"+path.word.toUpperCase();
        NodeMapper matchedNode;
        if (path != null && NodeMapperOperator.containsKey(node, uword) && (matchedNode = match(path.next, NodeMapperOperator.get(node, uword), inputThatTopic, starState, starIndex, inputStars, thatStars, topicStars, matchTrace)) != null)  {
            return matchedNode;
        } else {
            fail("dollar", matchTrace);
            return null;
        }
    }
    final NodeMapper starMatch(Path path, NodeMapper node, String input, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        return wildMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "*", matchTrace);
    }
    final NodeMapper underMatch(Path path, NodeMapper node, String input, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        return wildMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "_", matchTrace);
    }
    final NodeMapper caretMatch(Path path, NodeMapper node, String input, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        NodeMapper matchedNode;
        matchedNode = zeroMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "^", matchTrace);
        if (matchedNode != null) return matchedNode;
        else return wildMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "^", matchTrace);
    }
    final NodeMapper sharpMatch(Path path, NodeMapper node, String input, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
        //log.info("Entering sharpMatch with path.word="+path.word); NodemapperOperator.printKeys(node);
        NodeMapper matchedNode;
        matchedNode = zeroMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "#", matchTrace);
        if (matchedNode != null) return matchedNode;
        else
        return wildMatch(path, node, input, starState, starIndex, inputStars, thatStars, topicStars, "#", matchTrace);
    }
    final NodeMapper zeroMatch(Path path, NodeMapper node, String input, String starState, int starIndex,
                               String[] inputStars, String[] thatStars, String[] topicStars, String wildcard, String matchTrace) {
        // log.info("Entering zeroMatch on "+path.word+" "+NodemapperOperator.get(node, wildcard));
        matchTrace += "["+wildcard+",]";
        if (path != null && NodeMapperOperator.containsKey(node, wildcard)) {
            setStars(bot.properties.get(MagicStrings.null_star), starIndex, starState, inputStars, thatStars, topicStars);
            NodeMapper nextNode = NodeMapperOperator.get(node, wildcard);
            return match(path, nextNode, input, starState, starIndex+1, inputStars, thatStars, topicStars, matchTrace);
        }
        else {
            fail("zero "+wildcard, matchTrace);
            return null;
        }

    }
    final NodeMapper wildMatch(Path path, NodeMapper node, String input, String starState, int starIndex,
                               String[] inputStars, String[] thatStars, String[] topicStars, String wildcard, String matchTrace) {
        NodeMapper matchedNode;
        if (path.word.equals("<THAT>") || path.word.equals("<TOPIC>")) {
            fail("wild1 "+wildcard, matchTrace);
            return null;
        }
        try {
            if (path != null && NodeMapperOperator.containsKey(node, wildcard)) {
                matchTrace += "["+wildcard+","+path.word+"]";
                String currentWord;
                String starWords;
                Path pathStart;
                currentWord = path.word;
                starWords = currentWord+" ";
                pathStart = path.next;
                NodeMapper nextNode = NodeMapperOperator.get(node, wildcard);
                if (NodeMapperOperator.isLeaf(nextNode) && !nextNode.shortCut) {
                    matchedNode = nextNode;
                    starWords = Path.pathToSentence(path);
                    //log.info(starIndex+". starwords="+starWords);
                    setStars(starWords, starIndex, starState, inputStars, thatStars, topicStars);
                    return matchedNode;
                }
                else {
                    for (path = pathStart; path != null && !currentWord.equals("<THAT>") && !currentWord.equals("<TOPIC>"); path = path.next) {
                        matchTrace += "["+wildcard+","+path.word+"]";
                        if ((matchedNode = match(path, nextNode, input, starState, starIndex + 1, inputStars, thatStars, topicStars, matchTrace)) != null) {
                            setStars(starWords, starIndex, starState, inputStars, thatStars, topicStars);
                            return matchedNode;
                        }
                        else {
                            currentWord = path.word;
                            starWords += currentWord + " ";
                        }
                    }
                    fail("wild2 "+wildcard, matchTrace);
                    return null;
                }
            }
        } catch (Exception ex) {
            log.info("wildMatch: "+Path.pathToSentence(path)+": "+ex);
        }
        fail("wild3 "+wildcard, matchTrace);
        return null;
    }

   final NodeMapper setMatch(Path path, NodeMapper node, String input, String starState, int starIndex, String[] inputStars, String[] thatStars, String[] topicStars, String matchTrace) {
       if (node.sets == null || path.word.equals("<THAT>") || path.word.equals("<TOPIC>")) return null;
       //log.info("setMatch sets ="+node.sets);
       for (String setName : node.sets) {
           //log.info("setMatch trying type "+setName);
           NodeMapper nextNode = NodeMapperOperator.get(node, "<SET>"+setName.toUpperCase()+"</SET>");
           AIMLSet aimlSet = bot.setMap.get(setName);
           //log.info(aimlSet.setName + "="+ aimlSet);
           NodeMapper matchedNode;
           String currentWord = path.word;
           String starWords = currentWord+" ";
           int length = 1;
           matchTrace += "[<set>"+setName+"</set>,"+path.word+"]";
           //log.info("setMatch starWords =\""+starWords+"\"");
           for (Path qath = path.next; qath != null &&  !currentWord.equals("<THAT>") && !currentWord.equals("<TOPIC>") && length <= aimlSet.getMaxLength(); qath = qath.next) {
               //log.info("qath.word = "+qath.word);
               String phrase = bot.preProcessor.normalize(starWords.trim()).toUpperCase();
               //log.info("setMatch trying \""+phrase+"\" in "+setName);
               if (aimlSet.contains(phrase) && (matchedNode = match(qath, nextNode, input, starState, starIndex + 1, inputStars, thatStars, topicStars, matchTrace)) != null) {
                   setStars(starWords, starIndex, starState, inputStars, thatStars, topicStars);
                   //log.info("setMatch found "+phrase+" in "+setName);
                   return matchedNode;
               }
           //    else if (qath.word.equals("<THAT>") || qath.word.equals("<TOPIC>")) return null;
               else {
                   length = length + 1;
                   currentWord = qath.word;
                   starWords += currentWord + " ";
               }
           }
       }
       fail("set", matchTrace);
       return null;
   }

    public void setStars(String starWords, int starIndex, String starState, String[] inputStars, String[] thatStars, String[] topicStars) {
    if (starIndex < MagicNumbers.max_stars) {
        starWords = starWords.trim();
        if (starState.equals("inputStar")) inputStars[starIndex] = starWords;
        else if (starState.equals("thatStar")) thatStars[starIndex] = starWords;
        else if (starState.equals("topicStar")) topicStars[starIndex] = starWords;
        }
    }
    public void printgraph () {
        printgraph(root, "");
    }
    void printgraph(NodeMapper node, String partial) {
        if (node == null) log.info("Null graph");
        else {
            String template = "";
            if (NodeMapperOperator.isLeaf(node) || node.shortCut) {
                template = Category.templateToLine(node.category.getTemplate());
                template = template.substring(0, Math.min(16, template.length()));
                if (node.shortCut) log.info(partial+"("+NodeMapperOperator.size(node)+"["+node.key+","+node.value+"])--<THAT>-->X(1)--*-->X(1)--<TOPIC>-->X(1)--*-->"+template+"...");
                else log.info(partial+"("+NodeMapperOperator.size(node)+"["+node.key+","+node.value+"]) "+template+"...");
            }
            for (String key : NodeMapperOperator.keySet(node)) {
                //log.info(key);
                printgraph(NodeMapperOperator.get(node, key), partial+"("+NodeMapperOperator.size(node)+"["+node.height+"])--"+key+"-->");
            }
        }
    }
    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        getCategories(root, categories);
        //for (Category c : categories) log.info("getCategories: "+c.inputThatTopic()+" "+c.getTemplate());
        return categories;
    }
    void getCategories(NodeMapper node, ArrayList<Category> categories) {
        if (node == null) return;

        else {
            //String template = "";
            if (NodeMapperOperator.isLeaf(node) || node.shortCut) {
                if (node.category != null) categories.add(node.category);   // node.category == null when the category is deleted.
            }
            for (String key : NodeMapperOperator.keySet(node)) {
                //log.info(key);
                getCategories(NodeMapperOperator.get(node, key), categories);
            }
        }
    }

    int leafCnt;
    int nodeCnt;
    long nodeSize;
    int singletonCnt;
    int shortCutCnt;
    int naryCnt;
    public void nodeStats() {
        leafCnt = 0;
        nodeCnt = 0;
        nodeSize = 0;
        singletonCnt = 0;
        shortCutCnt = 0;
        naryCnt = 0;
        nodeStatsGraph(root);
        resultNote = nodeCnt+" nodes "+singletonCnt+" singletons "+leafCnt+" leaves "+shortCutCnt+" shortcuts "+naryCnt+" n-ary "+nodeSize+" branches "+(float)nodeSize/(float)nodeCnt+" average branching ";
        log.info(resultNote);
    }
    public void nodeStatsGraph(NodeMapper node) {
        if (node != null) {
            //log.info("Counting "+node.key+ " size="+NodemapperOperator.size(node));
            nodeCnt++;
            nodeSize += NodeMapperOperator.size(node);
            if (NodeMapperOperator.size(node) == 1) singletonCnt += 1;
            if (NodeMapperOperator.isLeaf(node) && !node.shortCut) {
                leafCnt++;
            }
            if (NodeMapperOperator.size(node) > 1) naryCnt += 1;
            if (node.shortCut) {shortCutCnt += 1;}
            for (String key : NodeMapperOperator.keySet(node)) {
                    nodeStatsGraph(NodeMapperOperator.get(node, key));
            }
        }
    }

    public HashSet<String> getVocabulary () {
        vocabulary = new HashSet<String>();
        getBrainVocabulary(root);
        for (String set : bot.setMap.keySet()) vocabulary.addAll(bot.setMap.get(set));
        return vocabulary;
    }
    public void getBrainVocabulary(NodeMapper node) {
        if (node != null) {
            //log.info("Counting "+node.key+ " size="+NodemapperOperator.size(node));
            for (String key : NodeMapperOperator.keySet(node)) {
                vocabulary.add(key);
                getBrainVocabulary(NodeMapperOperator.get(node, key));
            }
        }
    }
}
