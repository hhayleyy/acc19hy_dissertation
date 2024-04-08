import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import java.util.Arrays;

public class SimulatedSystem {
    private int totalFailureRegions;
    private List<TestCase> inputDomain;
    private int lengthOfInput;
    private List<TestCase> failureRegions;
    private int amountOfBoxes;
    private String failureRegionType;


    public SimulatedSystem(int totalFailureRegions, String failureRegionType, List<TestCase> inputDomain, int amountOfBoxes){
        this.totalFailureRegions = totalFailureRegions;
        this.inputDomain = inputDomain;
        this.lengthOfInput = inputDomain.size();
        this.amountOfBoxes = amountOfBoxes;
        this.failureRegionType = failureRegionType;
        this.failureRegions = createFailureRegions();
    }

    private List<TestCase> createFailureRegions(){
        List<TestCase> failureRegions = new ArrayList<>();
        if (failureRegionType == "point" ){
            failureRegions = createPointFailureRegions();
        }else if (failureRegionType == "box"){
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
        double boxSize = Math.pow((totalFailureRegions/amountOfBoxes), (1/inputDomain.get(0).getNumberOfDimensions()));

        for (int boxCount = 0 ; boxCount < amountOfBoxes; boxCount++){
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
        System.out.println(failureRegions);
        return failureRegions;
    }

    private static List<TestCase> createFailureBox(List<List<Double>> boxDimensions) {
        List<List<Double>> boxPoints = new ArrayList<>();
        generateCombinations(boxDimensions, new ArrayList<>(), 0, boxPoints);

        List<TestCase> failureRegion = new ArrayList<>();
        
        for (List<Double> boxPoint: boxPoints){
            TestCase failureCase = new TestCase(boxPoint);

            failureRegion.add(failureCase);

        }
        return failureRegion;
    }

    private static void generateCombinations(List<List<Double>> boxDimensions, List<Double> current, int index, List<List<Double>> result) {
        if (index == boxDimensions.size()) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (Double value : boxDimensions.get(index)) {
            current.add(value);
            generateCombinations(boxDimensions, current, index + 1, result);
            current.remove(current.size() - 1);
        }
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

        if (failureRegionType == "point" ){
            for (TestCase testCase : testCases){
                if (failureRegions.contains(testCase)){
                    failurePointsFound.add(testCase);
                }
            }
        }else if (failureRegionType == "box"){
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
        System.out.println(failurePointsFound);
        return failurePointsFound.size();
    }
}
