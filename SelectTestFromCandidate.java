import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectTestFromCandidate extends RandomTesting{

    private List<Float> inputDomain;
    private int numTestCases;

    public SelectTestFromCandidate(List<Float> inputDomain, int numTestCases) {
        super(inputDomain, numTestCases);
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
    }

    public List<Float> performSTFCS() throws Exception{
        float  testCase = generateRandomTestCase();
        List<Float> executedSet = new ArrayList<>();
        executedSet.add(testCase);

        while (executedSet.size() < numTestCases){
            List<Float> candidateSet = super.createTestSet();
            float highestDistance = 0;
            float nextTestCase = 0;

            for (float candidate: candidateSet){
                float euclideanDistance = DistanceMetric.euclideanDistance(testCase, candidate);
                if (euclideanDistance > highestDistance){
                    nextTestCase = candidate;
                }
            }

            executedSet.add(nextTestCase);
            testCase = nextTestCase;
        }

        return executedSet;

    }

    public float generateRandomTestCase(){
        int lengthOfInput = super.getLengthOfInput();
        Random random = new Random();
        int position = random.nextInt(lengthOfInput);

        float randomTestCase = inputDomain.get(position);

        return randomTestCase;
        
    }




    
}
