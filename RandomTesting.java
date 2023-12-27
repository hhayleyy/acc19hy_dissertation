import java.util.*;

public class RandomTesting {
    private int[] inputDomain;
    private int numTestSets;

    public RandomTesting(int[] inputDomain, int numTestSets){
        this.inputDomain = inputDomain;
        this.numTestSets = numTestSets;
    }

    public List<Integer> createTestSet() throws Exception{
        int lengthOfInput = this.inputDomain.length;
        List<Integer> testSet = new ArrayList<>();

        if (numTestSets > lengthOfInput){
            throw new Exception("Amount of test sets required is bigger than input domain");
        }

        for (int position = 0; position < numTestSets ; position ++){
            boolean testSetContains = true;
            int testValue = 0;

            while(testSetContains){
                Random random = new Random();
                int positionInDomain = random.nextInt(lengthOfInput);
                testValue = this.inputDomain[positionInDomain];
                testSetContains = testSet.contains(testValue);
            }

            testSet.add(testValue);
        }

        return testSet;

    }
}



