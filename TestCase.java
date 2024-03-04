import java.util.ArrayList;
import java.util.List;

public class TestCase {
    private List<Float> inputCaseValues = new ArrayList<>();

    public float getInputCaseValue(int dimension){
        return inputCaseValues.get(dimension);
    }

    public List<Float> getInputCase(){
        return inputCaseValues;
    }

    public int getNumberOfDimensions(){
        return inputCaseValues.size();
    }

    public void addInputCaseValue(float value){
        inputCaseValues.add(value);
    }

}
