import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectTestFromCandidate extends RandomTesting{

    private List<TestCase> inputDomain;
    private int numTestCases;

    public SelectTestFromCandidate(List<TestCase> inputDomain, int numTestCases) {
        super(inputDomain, numTestCases);
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
    }

    public List<TestCase> performSTFCS() throws Exception{
        TestCase  testCase = generateRandomTestCase();
        List<TestCase> executedSet = new ArrayList<>();
        executedSet.add(testCase);

        while (executedSet.size() < numTestCases){
            List<TestCase> candidateSet = super.createTestSet();
            while (candidateSet.contains(testCase)){
                candidateSet = super.createTestSet();
            }

            double highestDistance = 0;
            TestCase nextTestCase = new TestCase();

            for (TestCase candidate: candidateSet){
                double euclideanDistance = DistanceMetric.euclideanDistance(testCase, candidate);
                if (euclideanDistance > highestDistance && !executedSet.contains(candidate)){
                    nextTestCase = candidate;
                }
            }

            executedSet.add(nextTestCase);
            testCase = nextTestCase;
        }

        return executedSet;

    }

    public TestCase generateRandomTestCase(){
        int lengthOfInput = super.getLengthOfInput();
        Random random = new Random();
        int position = random.nextInt(lengthOfInput);

        TestCase randomTestCase = inputDomain.get(position);

        return randomTestCase;
        
    }




    
}
