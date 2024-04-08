import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedSystem {
    private int totalFailureRegions;
    private List<TestCase> inputDomain;
    private int lengthOfInput;
    private List<TestCase> failureRegions;
    private String failureRegionType;


    public SimulatedSystem(int totalFailureRegions, String failureRegionType, List<TestCase> inputDomain){
        this.totalFailureRegions = totalFailureRegions;
        this.inputDomain = inputDomain;
        this.lengthOfInput = inputDomain.size();
        this.failureRegionType = failureRegionType;
        this.failureRegions = createFailureRegions();
    }

    private List<TestCase> createFailureRegions(){
        List<TestCase> failureRegions = new ArrayList<>();
        if (failureRegionType.equals("point")){
            failureRegions = createPointFailureRegions();
        }else if (failureRegionType.equals("box")){
            failureRegions = createBoxFailureRegions();
        }else {
            System.err.println("Incorrect failure region type. Please input either of the following: point, box, strip");
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

    private List<TestCase> createBoxFailureRegions(){
        List<TestCase> failureRegions = new ArrayList<>();
        double boxSize = Math.pow(totalFailureRegions, (1/inputDomain.get(0).getNumberOfDimensions()));

        for (int boxCount = 0 ; boxCount < totalFailureRegions; boxCount++){
            Random random = new Random();
            int position = random.nextInt(lengthOfInput);
            TestCase randomFailure = inputDomain.get(position);

            while(!isInRange(randomFailure, boxSize) || failureRegions.contains(randomFailure)){
                position = random.nextInt(lengthOfInput);
                randomFailure = inputDomain.get(position);
            }

            int amountOfDimensions = randomFailure.getNumberOfDimensions();

            List<List<Double>> boxDimensions = new ArrayList<>();

            for (int dimensionCount = 0 ; dimensionCount < amountOfDimensions; dimensionCount++){
                List<Double> boxDimension = new ArrayList<>();
                double startOfBox = randomFailure.getInputCaseValue(dimensionCount);
                double endOfBox = startOfBox + boxSize;

                boxDimension.add(startOfBox);
                boxDimension.add(endOfBox);

                boxDimensions.add(boxDimension);
            }

            for (List<Double> boxDimension : boxDimensions){
                TestCase failurePoints = new TestCase(boxDimension);
                failureRegions.add(failurePoints);
            }
           
        }
        return failureRegions;
    }

    private boolean isInRange(TestCase testCase, double boxSize){
        boolean isInRange = true;
        List<Double> maximumValues = findMaximumInputDomainValuesForEachDimension();

        for (int dimensionCount = 0 ; dimensionCount < testCase.getNumberOfDimensions(); dimensionCount ++){
            double maxValue = maximumValues.get(dimensionCount);
            double dimensionalValue = testCase.getInputCaseValue(dimensionCount);

            if( (dimensionalValue + boxSize > maxValue)){
                isInRange = false;
            }
        }

        return isInRange;
    }

    private List<Double> findMaximumInputDomainValuesForEachDimension(){
        int numDimensions = inputDomain.get(0).getNumberOfDimensions();
        List<Double> maxInputs = new ArrayList<>();

        for (int dimension = 0; dimension < numDimensions; dimension ++){
            double maxValue = inputDomain.get(0).getInputCaseValue(dimension);
            for (TestCase testCase: inputDomain){
                double dimensionValue = testCase.getInputCaseValue(dimension);

                if (dimensionValue > maxValue){
                    maxValue = dimensionValue;
                }

            }
            maxInputs.add(maxValue);
        }

        return maxInputs;
    }

    public int failureCasesFound(List<TestCase> testCases){
        List<TestCase> failurePointsFound = new ArrayList<>();

        if (failureRegionType.equals("point")){
            for (TestCase testCase : testCases){
                if (failureRegions.contains(testCase)){
                    failurePointsFound.add(testCase);
                }
            }
        }else if (failureRegionType.equals("box")){
            int numDimensions = testCases.get(0).getNumberOfDimensions();

            for (int boxCount = 0 ; boxCount < failureRegions.size() ; boxCount = boxCount + numDimensions ) {
                for(TestCase testCase: testCases){
                    boolean inFailureRegion = true;
                    for (int dimCount = 0 ; dimCount < numDimensions ; dimCount ++){
                        double startOfFailure = failureRegions.get(dimCount+boxCount).getInputCaseValue(0);
                        double endOfFailure = failureRegions.get(dimCount+boxCount).getInputCaseValue(1);



                        if (testCase.getInputCaseValue(dimCount) < startOfFailure || testCase.getInputCaseValue(dimCount) > endOfFailure  || failurePointsFound.contains(testCase)){
                            inFailureRegion = false;
                        }
                    }

                    if (inFailureRegion){
                        failurePointsFound.add(testCase);
                    }
                }
            }
        }
        return failurePointsFound.size();
    }
}
