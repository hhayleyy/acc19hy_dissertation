import java.util.*;

public class RandomTesting {
    private int[] inputDomain;
    private int numTestCases;
    private int lengthOfInput;

    public RandomTesting(int[] inputDomain, int numTestCases){
        this.inputDomain = inputDomain;
        this.numTestCases = numTestCases;
        this.lengthOfInput = inputDomain.length;
    }

    public List<Integer> createTestSet() throws Exception{
        List<Integer> testSet = new ArrayList<>();

        if (numTestCases > lengthOfInput){
            throw new Exception("Amount of test sets required is bigger than input domain");
        }

        for (int position = 0; position < numTestCases ; position ++){
            boolean testSetContains = true;
            int testValue = 0;

            while(testSetContains){
                Random random = new Random();
                int positionInDomain = random.nextInt(this.lengthOfInput);
                testValue = this.inputDomain[positionInDomain];
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



