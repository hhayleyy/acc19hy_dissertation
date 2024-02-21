import java.util.List;

public class DistanceMetric {
    
    public static double euclideanDistance(List<Float> valueOne, List<Float> valueTwo){
        double totalSum = 0;
        int numOfDimensions = valueOne.size();

        for (int dimensionCount = 0 ; dimensionCount < numOfDimensions ; dimensionCount ++){
            float currentValueOne = valueOne.get(dimensionCount);
            float currentValueTwo = valueTwo.get(dimensionCount);
            totalSum += (currentValueOne - currentValueTwo) * (currentValueOne - currentValueTwo);
        }




        return Math.sqrt(totalSum);
    }
}
