import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectTestFromCandidate extends RandomTesting{

    private List<List<Float>> inputDomain;
    private int numTestCases;

    public SelectTestFromCandidate(List<List<Float>> inputDomain, int numTestCases) {
        super(inputDomain, numTestCases);
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
    }

    public List<List<Float>> performSTFCS() throws Exception{
        List<Float>  testCase = generateRandomTestCase();
        List<List<Float>> executedSet = new ArrayList<>();
        executedSet.add(testCase);

        while (executedSet.size() < numTestCases){
            List<List<Float>> candidateSet = super.createTestSet();
            float highestDistance = 0;
            List<Float> nextTestCase = new ArrayList<>();

            for (List<Float> candidate: candidateSet){
                double euclideanDistance = DistanceMetric.euclideanDistance(testCase, candidate);
                if (euclideanDistance > highestDistance){
                    nextTestCase = candidate;
                }
            }

            executedSet.add(nextTestCase);
            testCase = nextTestCase;
        }

        return executedSet;

    }

    public List<Float> generateRandomTestCase(){
        int lengthOfInput = super.getLengthOfInput();
        Random random = new Random();
        int position = random.nextInt(lengthOfInput);

        List<Float> randomTestCase = inputDomain.get(position);

        return randomTestCase;
        
    }




    
}
