import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class SimpleNLG {

	public static String getThirdPersonSingularPresent(String verb) {
		
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		VPPhraseSpec v = nlgFactory.createVerbPhrase(verb);
		v.setFeature(LexicalFeature.PRESENT3S, true);
		String singularPresent = realiser.realise(v).getRealisation();
		//System.out.print(singularPresent);
		
		return singularPresent;
	}
	
	public static String generateSingularPresentSentence(String verbBaseForm, String subj, String comp) {
		
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		SPhraseSpec s = nlgFactory.createClause(subj, verbBaseForm, "");
		s.setComplement(comp);
		
		s.setFeature(LexicalFeature.PRESENT3S, true);
		s.setFeature(Feature.TENSE, Tense.PRESENT);
		s.setFeature(Feature.PERSON, Person.THIRD);

		return realiser.realiseSentence(s);
	}
	
	public static String getVerbPastTense(String verb) {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		WordElement word = lexicon.getWord(verb, LexicalCategory.VERB);
		InflectedWordElement infl = new InflectedWordElement(word);
		infl.setFeature(Feature.TENSE, Tense.PAST);
		Realiser realiser = new Realiser(lexicon);
		String past = realiser.realise(infl).getRealisation();
		return past;
	}
	
	public static String generateImperativeSentence(String verbBaseForm, String comp) {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		
		VPPhraseSpec vP = nlgFactory.createVerbPhrase(verbBaseForm);
		vP.addComplement(comp);
		vP.setFeature(Feature.PERSON, Person.FIRST);
		//String progressive = realiser.realise(vP).getRealisation();

		return realiser.realiseSentence(vP);
	}
	
	public static String getPassiveTense(String subj, String verbBaseForm, String comp) {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		VPPhraseSpec vP = nlgFactory.createVerbPhrase(verbBaseForm);
		vP.addComplement(comp);
		
		vP.setFeature(Feature.PASSIVE, true);
		vP.setFeature(Feature.TENSE, Tense.FUTURE);

		return Utility.removeDefectedSpaces(subj + realiser.realiseSentence(vP).replace("Will ", " shall ") + comp).replace(".", "");
	}
	
	public static String getVerbBaseForm(String verb) {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);

		SPhraseSpec s = nlgFactory.createClause("We", verb, "");
		
		s.setFeature(LexicalFeature.PRESENT3S, true);
		s.setFeature(Feature.TENSE, Tense.PRESENT);
		s.setFeature(Feature.PERSON, Person.FIRST);
		String sent = realiser.realiseSentence(s);

		return sent.substring(sent.indexOf(" ")+1);
	}
}
