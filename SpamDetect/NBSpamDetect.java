import java.io.File;
import java.io.IOException;


public class NBSpamDetect {
    public static void main(String [] args) throws  IOException{
		// Location of the directory (the path) taken from the cmd line (first
		// arg)
		File dir_locationTrain = new File(args[0]);
		File dir_locationTest = new File(args[1]);
		// Listing of the directory (should contain 2 subdirectories: ham/ and
		// spam/)
		File[] dir_listingTrain = new File[0];
		File[] dir_listingTest = new File[0];

		// Check if the cmd line arg is a directory and list it
		if (dir_locationTrain.isDirectory()) {
			dir_listingTrain = dir_locationTrain.listFiles();

		} else {
			System.out.println("- Error: cmd line arg not a directory.\n");
			Runtime.getRuntime().exit(0);
		}
		if (dir_locationTest.isDirectory()) {
			dir_listingTest = dir_locationTest.listFiles();

		} else {
			System.out.println("- Error: cmd line arg not a directory.\n");
			Runtime.getRuntime().exit(0);
		}
		// Listings of the two sub-directories (ham/ and spam/)
		File[] listing_hamTrain = new File[0];
		File[] listing_spamTrain = new File[0];
		File[] listing_hamTest = new File[0];
		File[] listing_spamTest = new File[0];
		// Check that there are 2 sub-directories
		boolean hamFoundTrain = false;
		boolean spamFoundTrain = false;
		boolean hamFoundTest = false;
		boolean spamFoundTest = false;
		for (int i = 0; i < dir_listingTrain.length; i++) {
			if (dir_listingTrain[i].getName().equals("ham")) {
				listing_hamTrain = dir_listingTrain[i].listFiles();
				hamFoundTrain = true;
			} else if (dir_listingTrain[i].getName().equals("spam")) {
				listing_spamTrain = dir_listingTrain[i].listFiles();
				spamFoundTrain = true;
			}
		}
		if (!hamFoundTrain || !spamFoundTrain) {
			System.out.println("- Error: specified directory does not contain ham and spam subdirectories.\n");
			Runtime.getRuntime().exit(0);
		}
		
		for (int i = 0; i < dir_listingTest.length; i++) {
			if (dir_listingTest[i].getName().equals("ham")) {
				listing_hamTest = dir_listingTest[i].listFiles();
				hamFoundTest = true;
			} else if (dir_listingTest[i].getName().equals("spam")) {
				listing_spamTest = dir_listingTest[i].listFiles();
				spamFoundTest = true;
			}
		}
		if (!hamFoundTest || !spamFoundTest) {
			System.out.println("- Error: specified directory does not contain ham and spam subdirectories.\n");
			Runtime.getRuntime().exit(0);
		}
		
        NBSpamDetectionIO allWords = new NBSpamDetectionIO();
        NBSpamDetectionIO lowercase = new NBSpamDetectionIO();
        NBSpamDetectionIO header = new NBSpamDetectionIO();

        System.out.println("All words:");
        allWords.train(listing_hamTrain,listing_spamTrain,"a",true);
        allWords.classify(listing_hamTest, listing_spamTest,"a",true);

        System.out.println("Lowercase:");
        lowercase.train(listing_hamTrain,listing_spamTrain,"a",false);
        lowercase.classify(listing_hamTest, listing_spamTest,"a",false);

        System.out.println("To:/From:/Cc:/Subject:");
        header.train(listing_hamTrain,listing_spamTrain,"h",true);
        header.classify(listing_hamTest, listing_spamTest,"h",true);

    }
}
