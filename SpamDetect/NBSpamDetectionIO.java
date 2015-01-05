/*
	Spam detection using a Naive Bayes classifier.

	The program is incomplete, it only reads in messages
	and creates the dictionary together
	with the word counts for each class (spam and ham).
 */

import java.io.*;
import java.util.*;
import java.lang.*;

public class NBSpamDetectionIO {
	Hashtable<String, conditionalP> wordP;

	// This a class with two counters (for ham and for spam)
	static class Multiple_Counter {
		int counterHam = 0;
		int counterSpam = 0;
	}

	static class conditionalP {
		double CPHam = 0;
		double CPSpam = 0;
	}

	public void train(File[] ham, File[] spam, String s, boolean lowercase)
			throws IOException {
		// Create a hash table for the vocabulary (word searching is very fast
		// in a hash table)
		Hashtable<String, Multiple_Counter> vocab = new Hashtable<String, Multiple_Counter>();
		Multiple_Counter old_cnt = new Multiple_Counter();

		// Read the e-mail messages
		// The ham mail
		for (int i = 0; i < ham.length; i++) {
			FileInputStream i_s = new FileInputStream(ham[i]);
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;

			while ((line = in.readLine()) != null) { // read a line
				StringTokenizer st = new StringTokenizer(line);
				if (s.equals("h") && st.hasMoreTokens()) {
					String header = st.nextToken();
					boolean hasValue = header.equals("To:")
							|| header.equals("From:") || header.equals("Cc:")
							|| header.equals("Subject:");
					while (hasValue && st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						if (!word.equals("")) { // if string isn't empty
							if (vocab.containsKey(word)) {
								old_cnt = vocab.get(word);
								old_cnt.counterHam++; // and increment it
								vocab.put(word, old_cnt);
							} else {
								Multiple_Counter fresh_cnt = new Multiple_Counter();
								fresh_cnt.counterHam = 1;
								fresh_cnt.counterSpam = 0;
								vocab.put(word, fresh_cnt);
							}
						}
					}
				} else {
					while (st.hasMoreTokens()) {
						String init = st.nextToken()
								.replaceAll("[^a-zA-Z]", "");
						if (!lowercase) {
							word = init.toLowerCase();
						} else {
							word = init;
						}
						if (!word.equals("")) { // if string isn't empty
							if (vocab.containsKey(word)) {
								old_cnt = vocab.get(word);
								old_cnt.counterHam++; // and increment it
								vocab.put(word, old_cnt);
							} else {
								Multiple_Counter fresh_cnt = new Multiple_Counter();
								fresh_cnt.counterHam = 1;
								fresh_cnt.counterSpam = 0;
								vocab.put(word, fresh_cnt);
							}
						}
					}
				}
			}
			in.close();
		}
		// The spam mail
		for (int i = 0; i < spam.length; i++) {
			FileInputStream i_s = new FileInputStream(spam[i]);
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;

			while ((line = in.readLine()) != null) // read a line
			{
				StringTokenizer st = new StringTokenizer(line);
				if (s.equals("h") && st.hasMoreTokens()) {
					String header = st.nextToken();
					boolean hasValue = header.equals("To:")
							|| header.equals("From:") || header.equals("Cc:")
							|| header.equals("Subject:");

					while (hasValue && st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						if (!word.equals("")) {
							if (vocab.containsKey(word)) {
								old_cnt = vocab.get(word);
								old_cnt.counterSpam++;
								vocab.put(word, old_cnt);
							} else {
								Multiple_Counter fresh_cnt = new Multiple_Counter();
								fresh_cnt.counterHam = 0;
								fresh_cnt.counterSpam = 1;
								vocab.put(word, fresh_cnt);
							}
						}
					}

				} else {
					while (st.hasMoreTokens()) {
						String init = st.nextToken()
								.replaceAll("[^a-zA-Z]", "");
						if (!lowercase) {
							word = init.toLowerCase();
						} else {
							word = init;
						}

						if (!word.equals("")) {
							if (vocab.containsKey(word)) {
								old_cnt = vocab.get(word);
								old_cnt.counterSpam++;
								vocab.put(word, old_cnt);
							} else {
								Multiple_Counter fresh_cnt = new Multiple_Counter();
								fresh_cnt.counterHam = 0;
								fresh_cnt.counterSpam = 1;
								vocab.put(word, fresh_cnt);
							}
						}
					}
				}
			}
			in.close();
		}

		Set<String> keys = vocab.keySet();
		int hamTotal = 0;
		int spamTotal = 0;
		for (String key : keys) {
			Multiple_Counter total = vocab.get(key);
			hamTotal = hamTotal + total.counterHam;
			spamTotal = spamTotal + total.counterSpam;
		}
		wordP = new Hashtable<String, conditionalP>();
		for (String key : keys) {
			Multiple_Counter total = vocab.get(key);
			conditionalP conditionalP = new conditionalP();
			conditionalP.CPHam = Math.log(((total.counterHam + 1))
					/ ((double) (hamTotal + keys.size())));
			conditionalP.CPSpam = Math.log(((total.counterSpam + 1))
					/ ((double) (spamTotal + keys.size())));
			wordP.put(key, conditionalP);
		}

	}

	public void classify(File[] ham, File[] spam, String s, boolean lowercase)
			throws IOException {

		int hamtest = 0;
		for (int i = 0; i < ham.length; i++) {
			FileInputStream i_s = new FileInputStream(ham[i]);
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;
			ArrayList<String> words = new ArrayList<String>();

			while ((line = in.readLine()) != null) // read a line
			{
				StringTokenizer st = new StringTokenizer(line); // parse it into
																// words
				if (s.equals("h") && st.hasMoreTokens()) {
					String firstword = st.nextToken();
					boolean header = firstword.equals("From:")
							|| firstword.equals("To:")
							|| firstword.equals("Cc:")
							|| firstword.equals("Subject:");

					while (header && st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						words.add(word);
					}

				} else {
					while (st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						if (!lowercase) {
							words.add(word.toLowerCase());
						} else {
							words.add(word);
						}
					}
				}
			}

			double hamMessage = Math.log(ham.length
					/ (double) (spam.length + ham.length));
			;
			double spamMessage = Math.log(spam.length
					/ (double) (spam.length + ham.length));
			;
			for (String w : words) {
				if (wordP.containsKey(w)) {
					hamMessage = hamMessage + wordP.get(w).CPHam;
					spamMessage = spamMessage + wordP.get(w).CPSpam;
				}
			}
			if (spamMessage <= hamMessage)
				hamtest++;
			in.close();
		}

		int spamtest = 0;
		// The spam mail
		for (int i = 0; i < spam.length; i++) {
			FileInputStream i_s = new FileInputStream(spam[i]);
			BufferedReader in = new BufferedReader(new InputStreamReader(i_s));
			String line;
			String word;
			ArrayList<String> words = new ArrayList<String>();

			while ((line = in.readLine()) != null) // read a line
			{
				StringTokenizer st = new StringTokenizer(line); //
				if (s.equals("h") && st.hasMoreTokens()) {
					String header = st.nextToken();
					boolean hasValue = header.equals("From:")
							|| header.equals("To:") || header.equals("Cc:")
							|| header.equals("Subject:");

					while (hasValue && st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						words.add(word);

					}
				} else {
					while (st.hasMoreTokens()) {
						word = st.nextToken().replaceAll("[^a-zA-Z]", "");
						if (!lowercase) {
							words.add(word.toLowerCase());
						} else {
							words.add(word);
						}
					}
				}
			}
			double hamMessage = Math.log(ham.length
					/ (double) (spam.length + ham.length));
			;
			double spamMessage = Math.log(spam.length
					/ (double) (spam.length + ham.length));
			;
			for (String w : words) {
				if (wordP.containsKey(w)) {
					hamMessage = hamMessage + wordP.get(w).CPHam;
					spamMessage = spamMessage + wordP.get(w).CPSpam;
				}
			}
			if (spamMessage > hamMessage)
				spamtest++;
			in.close();
		}
		System.out.println("True Positives:" + spamtest);
		System.out.println("True Negatives:" + hamtest);
		System.out.println("False Positives:" + (ham.length - hamtest));
		System.out
				.println("False Negatives:" + (spam.length - spamtest) + "\n");

	}

}