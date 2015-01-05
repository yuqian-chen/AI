import java.util.ArrayList;
import java.util.List;


public class examples {
	List<Instance> allInst;
    String parent;
    public examples(List sample, String parentAttrValue){
        allInst = sample;
        parent = parentAttrValue;
    }
    public examples(String parentAttrValue){
        allInst = new ArrayList<Instance>();
        parent = parentAttrValue;
    }
}
