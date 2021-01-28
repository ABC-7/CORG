
import java.io.StringReader;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
public class Dependency {
	
	private static StanfordCoreNLP pipeline;
	
	public static void loadEnvironment(){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
		 props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		    props.setProperty("parse.maxlen", "100");
		pipeline = new StanfordCoreNLP(props);
	}
	
	public static Tree getParsingTree(String text){
		// build annotation for a review
	    Annotation annotation = new Annotation(text);
	    // annotate
	    pipeline.annotate(annotation);
	    // get tree
	    Tree tree = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
	    
		return tree;
	}
	
}
