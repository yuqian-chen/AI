import java.util.List;

/**
 * You should implement your Perceptron in this class. Any methods, variables,
 * or secondary classes could be added, but will only interact with the methods
 * or variables in this framework.
 * 
 * You must add code for at least the 3 methods specified below. Because we
 * don't provide the weights of the Perceptron, you should create your own data
 * structure to store the weights.
 * 
 */
public class Perceptron {

	/**
	 * The initial value for ALL weights in the Perceptron. We fix it to 0, and
	 * you CANNOT change it.
	 */
	public final double INIT_WEIGHT = 0.0;

	/**
	 * Learning rate value. You should use it in your implementation. You can
	 * set the value via command line parameter.
	 */
	public final double ALPHA;

	/**
	 * Training iterations. You should use it in your implementation. You can
	 * set the value via command line parameter.
	 */
	public final int EPOCH;

	// TODO: create weights variables, input units, and output units.
	public double input[];
	public double output[];
	public double weight[][];

	/**
	 * Constructor. You should initialize the Perceptron weights in this method.
	 * Also, if necessary, you could do some operations on your own variables or
	 * objects.
	 * 
	 * @param alpha
	 *            The value for initializing learning rate.
	 * 
	 * @param epoch
	 *            The value for initializing training iterations.
	 * 
	 * @param featureNum
	 *            This is the length of input feature vector. You might use this
	 *            value to create the input units.
	 * 
	 * @param labelNum
	 *            This is the size of label set. You might use this value to
	 *            create the output units.
	 */
	public Perceptron(double alpha, int epoch, int featureNum, int labelNum) {
		this.ALPHA = alpha;
		this.EPOCH = epoch;

		// TODO: add your code here
		input = new double[featureNum + 1];
		input[0] = 1;
		output = new double[labelNum];
		weight = new double[labelNum][(featureNum + 1)];
		for (int i = 0; i < weight.length; i++) {
			for (int j = 0; j < weight[i].length; j++)
				weight[i][j] = INIT_WEIGHT;
		}
	}

	/**
	 * Train your Perceptron in this method.
	 * 
	 * @param trainingData
	 */
	public void train(Dataset trainingData) {

		// TODO: add your code here
		List<Instance> training = trainingData.instanceList;
		for (int i = 0; i < EPOCH; i++) {
			for (int j = 0; j < training.size(); j++) {
				int label = Integer.valueOf(training.get(j).getLabel());
				for (int k = 0; k < training.get(j).getFeatureValue().size(); k++) {
					input[k + 1] = training.get(j).getFeatureValue().get(k);
				}// end of first input unit
				output = new double[weight.length];
				for (int k = 0; k < output.length; k++) {
					for (int m = 0; m < weight[k].length; m++) {
						output[k] += weight[k][m] * input[m];
					}
					output[k] = 1/(1+Math.exp(-output[k]));
				}

				for (int k = 0; k < weight.length; k++) {
					int target = 0;
					if (k == label)
						target = 1;
					for (int m = 0; m < weight[k].length; m++) {
						weight[k][m] += ALPHA * (target - output[k])
								* output[k] * (1 - output[k]) * input[m];
					}
				}
			} // end of training once
		}
	}

	/**
	 * Test your Perceptron in this method. Refer to the homework documentation
	 * for implementation details and requirement of this method.
	 * 
	 * @param testData
	 */
	public void classify(Dataset testData) {

		// TODO: add your code here
		List<Instance> test = testData.instanceList;
		double correctPred = 0.0;
		for (int j = 0; j < test.size(); j++) {
			int label = Integer.valueOf(test.get(j).getLabel());
			for (int k = 0; k < test.get(j).getFeatureValue().size(); k++) {
				input[k + 1] = test.get(j).getFeatureValue().get(k);
			}// end of first input unit
			output = new double[weight.length];
			for (int k = 0; k < output.length; k++) {
				for (int m = 0; m < weight[k].length; m++) {
					output[k] += weight[k][m] * input[m];
					//System.out.println(weight[k][m]);

				}
				output[k] = 1/(1+Math.exp(-output[k]));
			}
			int index = 0;
			double max = 0.0;
			for (int k = 0; k < output.length; k++) {
				if (output[k] >max) {
					index = k;
					max = output[k];
				}
			}
			System.out.println(index);
			if(label == index) correctPred ++;
		}
		System.out.println(Math.round(correctPred/test.size()*10000)/10000.0);
	}

}