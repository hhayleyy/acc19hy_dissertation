public class DistanceMetric {
    
    public static double euclideanDistance(TestCase valueOne, TestCase valueTwo){
        double totalSum = 0;
        int numOfDimensions = valueOne.getNumberOfDimensions();

        for (int dimensionCount = 0 ; dimensionCount < numOfDimensions ; dimensionCount ++){
            double currentValueOne = valueOne.getInputCaseValue(dimensionCount);
            double currentValueTwo = valueTwo.getInputCaseValue(dimensionCount);
            totalSum += (currentValueOne - currentValueTwo) * (currentValueOne - currentValueTwo);
        }




        return Math.sqrt(totalSum);
    }
}
