import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SearchBased extends RandomTesting {

    private int populationSize;
    private int numGenerations;
    private int numParents;
    private double mutationProbability;
    private int numTestCases;
    private int[] inputDomain;

    public SearchBased(int[] inputDomain, int numTestCases, int populationSize, int numGenerations, int numParents, double mutationProbability) {
        super(inputDomain, numTestCases);
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.numParents = numParents;
        this.mutationProbability = mutationProbability;
        this.numTestCases = numTestCases;
        this.inputDomain = inputDomain;
    }

    public List<Integer> performSBS() throws Exception{
        List<List<Integer>> population = generateRandomPopulation();
        int generation = 1;
        while (generation <= numGenerations){
            float totalDiversity = 0;
            List<List<Integer>> parents = new ArrayList<>();
            for (List<Integer> testSet: population){
                float diversity = calculateDiversity(testSet);
                totalDiversity += diversity;
            }

            for (int parent = 1 ; parent <= numParents; parent ++ ){
                Random random = new Random();
                boolean foundDiversity = false;
                int counter = 0;
                float currentProbability = 0;
                while(foundDiversity == false){
                    double randomProbability = random.nextDouble();
                    float relativeDiversity = 0;
                    List<Integer> testSet = population.get(counter);
                    relativeDiversity = calculateDiversity(testSet)/totalDiversity;
                    currentProbability += relativeDiversity;
                    if (randomProbability <= currentProbability && !parents.contains(testSet)){
                        parents.add(testSet);
                        foundDiversity = true;
                    }else{
                        if (counter == numTestCases-1){
                            foundDiversity = true;
                        } else{
                            counter += 1;
                        }
                    }
                }
            }
            


            population = performMutation(reproduction(performOnePointCrossover(parents)));
            generation += 1;
        }
        
        float highestDiversity = 0;
        List<Integer> bestTestSet = new ArrayList<>();
        for(List<Integer> testSet : population){
            float diversity = calculateDiversity(testSet);
            if (diversity > highestDiversity){
                bestTestSet = testSet;
            }
        }

        return bestTestSet;

    }

    public List<List<Integer>> generateRandomPopulation() throws Exception{
        List<List<Integer>> randomTestSets = new ArrayList<>();

        for (int testSetCount = 0; testSetCount < this.populationSize; testSetCount++){
            List<Integer> randomTestSet = super.createTestSet();
            randomTestSets.add(randomTestSet);
        }
        
        return randomTestSets;
        
    }

    public float calculateDiversity(List<Integer> population){
        // sum of the distance between the test datum and its nearest neighbour
        float diversity = 0;

        for (int testCase : population){
            float smallestNeighbourDifference = 0;
            for (int neighbour: population){
                float neighbourDifference = DistanceMetric.euclideanDistance(testCase, neighbour);

                if((smallestNeighbourDifference == 0) ||(testCase != neighbour) && (neighbourDifference < smallestNeighbourDifference)){
                    smallestNeighbourDifference = neighbourDifference;
                }
            }

            diversity += smallestNeighbourDifference;

        }

        return diversity;
    }

    public List<List<Integer>> performOnePointCrossover(List<List<Integer>> parents){

        for ( int parentCount = 1; parentCount <= numParents ; parentCount +=2 ){
            List<Integer> firstParent = parents.get(parentCount-1);
            List<Integer> secondParent = parents.get(parentCount);
            int midpoint = firstParent.size()/2;

            List<Integer> firstHalfFirstParent = firstParent.subList(0, midpoint);
            List<Integer> secondHalfSecondParent = secondParent.subList(midpoint, secondParent.size());

            List<Integer> combinedOffspringOne = new ArrayList<>(firstHalfFirstParent);
            combinedOffspringOne.addAll(secondHalfSecondParent);

            List<Integer> firstHalfSecondParent = secondParent.subList(0, midpoint);
            List<Integer> secondHalfFirstParent = firstParent.subList(midpoint, firstParent.size());

            List<Integer> combinedOffspringTwo = new ArrayList<>(firstHalfSecondParent);
            combinedOffspringTwo.addAll(secondHalfFirstParent);
            
            parents.add(combinedOffspringOne);
            parents.add(combinedOffspringTwo);
        }

        
        return parents;
    }

    public List<List<Integer>> performMutation(List<List<Integer>> population){

        for (List<Integer> testSet: population){
            Random random = new Random();
            double mutationChance = random.nextDouble();
            int maximumValue = Arrays.stream(inputDomain).max().getAsInt();

            if (mutationChance < mutationProbability){
                int mutationPosition = random.nextInt(0, (numTestCases-1));
                int mutationValue = random.nextInt(1, maximumValue);
                while(testSet.contains(mutationValue)){
                    mutationValue = random.nextInt(1, maximumValue);
                }
                testSet.set(mutationPosition, mutationValue);
            }
        }


        return population;
    }
    public List<List<Integer>> reproduction(List<List<Integer>> parents){
        List<List<Integer>> population = new ArrayList<>(parents);

        while(population.size() < populationSize ){
            int position = population.size() % parents.size();
            population.add(parents.get(position));
        }

        return population;
    }
    
}
