import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectTestFromCandidate extends RandomTesting{

    private int[] inputDomain;
    private int numTestSets;

    public SelectTestFromCandidate(int[] inputDomain, int numTestSets) {
        super(inputDomain, numTestSets);
        this.inputDomain = inputDomain;
        this.numTestSets = numTestSets;
    }

    public List<Integer> performSTFCS() throws Exception{
        int testCase = generateRandomTestCase();
        List<Integer> executedSet = new ArrayList<>();
        executedSet.add(testCase);

        while (executedSet.size() < numTestSets){
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
