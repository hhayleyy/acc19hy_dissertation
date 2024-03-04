public class DistanceMetric {
    
    public static double euclideanDistance(TestCase valueOne, TestCase valueTwo){
        double totalSum = 0;
        int numOfDimensions = valueOne.getNumberOfDimensions();

        for (int dimensionCount = 0 ; dimensionCount < numOfDimensions ; dimensionCount ++){
            float currentValueOne = valueOne.getInputCaseValue(dimensionCount);
            float currentValueTwo = valueTwo.getInputCaseValue(dimensionCount);
            totalSum += (currentValueOne - currentValueTwo) * (currentValueOne - currentValueTwo);
        }




        return Math.sqrt(totalSum);
    }
}
