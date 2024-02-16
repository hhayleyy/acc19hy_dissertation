import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectTestFromCandidate extends RandomTesting{

    private int[] inputDomain;
    private int numTestCases;

    public SelectTestFromCandidate(int[] inputDomain, int numTestCases) {
        super(inputDomain, numTestCases);
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
    }

    public List<Integer> performSTFCS() throws Exception{
        int testCase = generateRandomTestCase();
        List<Integer> executedSet = new ArrayList<>();
        executedSet.add(testCase);

        while (executedSet.size() < numTestCases){
            List<Integer> candidateSet = super.createTestSet();
            float highestDistance = 0;
            int nextTestCase = 0;

            for (int candidate: candidateSet){
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

    public int generateRandomTestCase(){
        int lengthOfInput = super.getLengthOfInput();
        Random random = new Random();
        int position = random.nextInt(lengthOfInput);

        int randomTestCase = this.inputDomain[position];

        return randomTestCase;
        
    }




    
}
