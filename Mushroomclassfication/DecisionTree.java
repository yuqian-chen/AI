import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class provides a framework for accessing a decision tree. Put your code
 * in constructor, printInfoGain(), buildTree and buildPrunedTree() You can add
 * your own help functions or variables in this class
 */
public class DecisionTree {
	/**
	 * training data set, pruning data set and testing data set
	 */
	private DataSet train = null; // Training Data Set
	private DataSet tune = null; // Tuning Data Set
	private DataSet test = null; // Testing Data Set
	private int infogain[];
	private DecTreeNode rootnode;

	/**
	 * Constructor
	 * 
	 * @param train
	 * @param tune
	 * @param test
	 */
	DecisionTree(DataSet train, DataSet tune, DataSet test) {
		this.train = train;
		this.tune = tune;
		this.test = test;
		// TODO: you can add code here, if it is necessary
		infogain = new int[train.attr_name.length];
		rootnode = null;
	}

	/**
	 * print information gain of each possible question at root node.
	 * 
	 */
	public void printInfoGain() {
		// TODO: add code here
		for (int i = 0; i < train.attr_val.length; i++) {
			double info = infoGain(i, train.instances,
					attributeValue(train.instances));
			info = Math.round(info * 1000) / 1000.0;
			System.out.println(train.attr_name[i] + ": info gain = " + info);
		}
	}

	/**
	 * Build a decision tree given only a training set.
	 * 
	 */
	public void buildTree() {

		// TODO: add code here
//		ArrayList<Double> tmp = new ArrayList<Double>();
//		ArrayList<Double> tmp1 = new ArrayList<Double>();
//		for (int i = 0; i < train.attr_val.length; i++) {
//			double info = infoGain(i, train.instances,
//					attributeValue(train.instances));
//			info = Math.round(info * 1000) / 1000.0;
//			tmp.add(info);
//			tmp1.add(info);
//		}
//		Collections.sort(tmp);
//		Collections.reverse(tmp);
//		for (int i = 0; i < tmp.size(); i++) {
//			boolean found = false;
//			for (int j = 0; j < tmp.size() && !found; j++) {
//				if (tmp.get(i).equals(tmp1.get(j))) {
//					infogain[i] = j;
//					tmp1.set(j, -1.0);
//					found = true;
//				}
//			}
//		}
		List<String> attributes = Arrays.asList(train.attr_name);
		List<Instance> samples = train.instances;
		rootnode = build(new examples(samples, "rootnode"), attributes,
				train.labels[0]);
	}

	private DecTreeNode build(examples e, List<String> attributes,
			String defaultvalue) {
		if (e.allInst.isEmpty()) {
			return new DecTreeNode(defaultvalue, null, e.parent, true);
		}
		if (sameLabel(e.allInst)) {
			return new DecTreeNode(e.allInst.get(0).label, null,
					e.parent, true);
		}
		boolean isEmpty = true;
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i) != null) {
				isEmpty = false;
				break;
			}
		}
		if (isEmpty) {
			return new DecTreeNode(majorityVote(e.allInst), null,
					e.parent, true);
		}

		double max = -1;
		int info = -1;
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i) != null) {
				double infoGain = infoGain(i, e.allInst,
						attributeValue(e.allInst));
				if (infoGain > max) {
					max = infoGain;
					info = i;
				}
			}
		}

		String gain = train.attr_name[info];
		ArrayList<examples> child = new ArrayList<examples>();
		for (int i = 0; i < train.attr_val[info].length; i++) {
			child.add(new examples(
					train.attr_val[info][i]));
		}
		for (Instance ins : e.allInst) {
			String instanceAttributeVal = ins.attributes
					.get(info);
			for (int i = 0; i < train.attr_val[info].length; i++) {
				if (train.attr_val[info][i]
						.equals(instanceAttributeVal)) {
					child.get(i).allInst.add(ins);
					break;
				}
			}
		}

		DecTreeNode tree = new DecTreeNode(null, gain,
				e.parent, false);

		for (int i = 0; i < child.size(); i++) {

			ArrayList<String> reducedAttributes = new ArrayList<String>();
			for (int j = 0; j < attributes.size(); j++) {
				reducedAttributes.add(attributes.get(j));
			}
			reducedAttributes.set(info, null);
			tree.addChild(build(child.get(i), reducedAttributes,
					defaultvalue));
		}
		return tree;
	}

	/**
	 * Build a decision tree given a training set then prune it using a tuning
	 * set.
	 * 
	 */
	public void buildPrunedTree() {

		// TODO: add code here

	}

	/**
	 * Evaluates the learned decision tree on a test set.
	 * 
	 * @return the label predictions for each test instance according to the
	 *         order in data set list
	 */
	public String[] classify() {

		ArrayList<String> result = new ArrayList<String>();
		for (Instance ins : test.instances) {
			DecTreeNode currLayer = rootnode;
			while (!currLayer.terminal) {
				String curr_attr = currLayer.attribute;
				int curr_attr_index = -1;
				for (int i = 0; i < test.attr_name.length; i++) {
					if (test.attr_name[i].equals(curr_attr)) {
						curr_attr_index = i;
						break;
					}
				}
				String instance_curr_attr_value = ins.attributes
						.get(curr_attr_index);
				for (int i = 0; i < currLayer.children.size(); i++) {
					if (currLayer.children.get(i).parentAttributeValue
							.equals(instance_curr_attr_value)) {
						currLayer = currLayer.children.get(i);
						break;
					}
				}
			}
			result.add(currLayer.label);
		}
		String[] label = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			label[i] = result.get(i);
		}
		return label;

	}

	/**
	 * Prints the tree in specified format. It is recommended, but not
	 * necessary, that you use the print method of DecTreeNode.
	 * 
	 * Example: Root {odor?} 
	 * a (e)
	 *  m (e)
	 *   n {habitat?} 
	 *   g (e) 
	 *   l (e) 
	 *   p (p) 
	 *   s (e)
	 * 
	 */
	public void print() {
		// TODO: add code here
		rootnode.print(1);

	}

	private double infoGain(int attribute, List<Instance> examples,
			valueOfattribute[][] value) {
		double e = 0;
		double p = 0;
		String class1 = examples.get(0).label;
		for (Instance inst : train.instances) {
			if (inst.label.equals(class1))
				e++;
			else
				p++;
		}
		double Entropyclass = (-e / (e + p))
				* (Math.log(e / (e + p)) / Math.log(2)) + (-p / (e + p))
				* (Math.log(p / (e + p)) / Math.log(2));
		double classAttr = 0;
		for (int i = 0; i < train.attr_val[attribute].length; i++) {
			double all = value[attribute][i].all;
			e = value[attribute][i].e;
			p = value[attribute][i].p;
			if (all != 0) {
				if (!(e == 0 || p == 0)) {
					classAttr += all
							/ examples.size()
							* (-e / all * (Math.log(e / all) / Math.log(2)) - p
									/ all * (Math.log(p / all) / Math.log(2)));
				}
			}
		}
		double Info = Entropyclass - classAttr;
		return Info;

	}

	private String majorityVote(List<Instance> instances) {
		int e = 0;
		int p = 0;
		for (Instance ins : instances) {
			if (ins.label.equals(train.labels[0])) {
				e++;
			} else {
				p++;
			}
		}
		int ValueOfMV = (e >= p) ? 0: 1;
		return train.labels[ValueOfMV];
	}

	private valueOfattribute[][] attributeValue(List<Instance> examples) {
		valueOfattribute[][] a = new valueOfattribute[train.attr_val.length][];
		for (int i = 0; i < train.attr_val.length; i++) {
			a[i] = new valueOfattribute[train.attr_val[i].length];
		}
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] = new valueOfattribute();
			}
		}
		for (Instance instance : examples) {
			String label = instance.label;
			List<String> attributes = instance.attributes;
			for (int i = 0; i < attributes.size(); i++) {
				int attribute = -1;
				for (int j = 0; j < train.attr_val[i].length; j++) {
					if (train.attr_val[i][j].equals(attributes.get(i))) {
						attribute = j;
						break;
					}
				}
				a[i][attribute].all++;
				if (label.equals(train.labels[0]))
					a[i][attribute].e++;
				else
					a[i][attribute].p++;
			}
		}
		return a;
	}
	
	private boolean sameLabel(List<Instance> instances) {
		boolean result = true;
		for (int i = 0; i < instances.size(); i++) {
			if (!instances.get(i).label.equals(instances.get(0).label)) {
				result = false;
				break;
			}
		}
		return result;
	}

}
