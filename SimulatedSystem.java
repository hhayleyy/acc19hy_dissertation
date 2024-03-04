import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedSystem {
    private int totalFailureRegions;
    private String failureRegionType;
    private List<TestCase> inputDomain;
    private int lengthOfInput;


    public SimulatedSystem(int totalFailureRegions, String failureRegionType, List<TestCase> inputDomain){
        this.totalFailureRegions = totalFailureRegions;
        this.failureRegionType = failureRegionType;
        this.inputDomain = inputDomain;
        this.lengthOfInput = inputDomain.size();
    }

    public List<TestCase> createFailureRegions(){
        List<TestCase> failureRegions = new ArrayList<>();
        if (failureRegionType == "point" ){
            failureRegions = createPointFailureRegions();
        }


        return failureRegions;

    }

    private List<TestCase> createPointFailureRegions(){
        List<TestCase> failureRegions = new ArrayList<>();

        for (int count = 0; count < totalFailureRegions; count++){
            Random random = new Random();
            int position = random.nextInt(lengthOfInput);
            TestCase randomFailure = inputDomain.get(position);

            failureRegions.add(randomFailure);

        }

        return failureRegions;
    }
}
