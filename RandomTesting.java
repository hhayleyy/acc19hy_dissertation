import java.util.*;

public class RandomTesting {
    private List<Float> inputDomain;
    private int numTestCases;
    private int lengthOfInput;

    public RandomTesting(List<Float> inputDomain, int numTestCases){
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
        this.lengthOfInput = inputDomain.size();
    }

    public List<Float> createTestSet() throws Exception{
        List<Float> testSet = new ArrayList<>();

        if (numTestCases > lengthOfInput){
            throw new Exception("Amount of test sets required is bigger than input domain");
        }

        for (int position = 0; position < numTestCases ; position ++){
            boolean testSetContains = true;
            float testValue = 0;

            while(testSetContains){
                Random random = new Random();
                int positionInDomain = random.nextInt(this.lengthOfInput);
                testValue = inputDomain.get(positionInDomain);
                testSetContains = testSet.contains(testValue);
            }

            testSet.add(testValue);
        }

        return testSet;

    }

    public int getLengthOfInput(){
        return this.lengthOfInput;
    }
}



