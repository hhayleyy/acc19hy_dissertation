import java.util.ArrayList;
import java.util.List;

public class TestCase {
    private List<Double> inputCaseValues = new ArrayList<>();

    public TestCase(){
        
    }

    public TestCase(List<Double> inputCaseValues){
        this.inputCaseValues = inputCaseValues;
    }

    public double getInputCaseValue(int dimension){
        return inputCaseValues.get(dimension);
    }

    public List<Double> getInputCase(){
        return inputCaseValues;
    }

    public int getNumberOfDimensions(){
        return inputCaseValues.size();
    }

    public void addInputCaseValue(double value){
        inputCaseValues.add(value);
    }

    @Override
    public String toString() {
        return inputCaseValues.toString();
    }

}
