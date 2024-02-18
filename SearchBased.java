import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchBased extends RandomTesting {

    private int populationSize;
    private int numGenerations;
    private int numParents;
    private double mutationProbability;
    private int numTestCases;
    private List<Float> inputDomain;

    public SearchBased(List<Float> inputDomain, int numTestCases, int populationSize, int numGenerations, int numParents, double mutationProbability) {
        super(inputDomain, numTestCases);
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.numParents = numParents;
        this.mutationProbability = mutationProbability;
        this.numTestCases = numTestCases;
        this.inputDomain = inputDomain;
    }

    public List<Float> performSBS() throws Exception{
        List<List<Float>> population = generateRandomPopulation();
        int generation = 1;
        while (generation <= numGenerations){
            float totalDiversity = 0;
            List<List<Float>> parents = new ArrayList<>();
            for (List<Float> testSet: population){
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
                    List<Float> testSet = population.get(counter);
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
        List<Float> bestTestSet = new ArrayList<>();
        for(List<Float> testSet : population){
            float diversity = calculateDiversity(testSet);
            if (diversity > highestDiversity){
                bestTestSet = testSet;
            }
        }

        return bestTestSet;

    }

    public List<List<Float>> generateRandomPopulation() throws Exception{
        List<List<Float>> randomTestSets = new ArrayList<>();

        for (int testSetCount = 0; testSetCount < this.populationSize; testSetCount++){
            List<Float> randomTestSet = super.createTestSet();
            randomTestSets.add(randomTestSet);
        }
        
        return randomTestSets;
        
    }

    public float calculateDiversity(List<Float> population){
        // sum of the distance between the test datum and its nearest neighbour
        float diversity = 0;

        for (float testCase : population){
            float smallestNeighbourDifference = 0;
            for (float neighbour: population){
                float neighbourDifference = DistanceMetric.euclideanDistance(testCase, neighbour);

                if((smallestNeighbourDifference == 0) ||(testCase != neighbour) && (neighbourDifference < smallestNeighbourDifference)){
                    smallestNeighbourDifference = neighbourDifference;
                }
            }

            diversity += smallestNeighbourDifference;

        }

        return diversity;
    }

    public List<List<Float>> performOnePointCrossover(List<List<Float>> parents){

        for ( int parentCount = 1; parentCount <= numParents ; parentCount +=2 ){
            List<Float> firstParent = parents.get(parentCount-1);
            List<Float> secondParent = parents.get(parentCount);
            int midpoint = firstParent.size()/2;

            List<Float> firstHalfFirstParent = firstParent.subList(0, midpoint);
            List<Float> secondHalfSecondParent = secondParent.subList(midpoint, secondParent.size());

            List<Float> combinedOffspringOne = new ArrayList<>(firstHalfFirstParent);
            combinedOffspringOne.addAll(secondHalfSecondParent);

            List<Float> firstHalfSecondParent = secondParent.subList(0, midpoint);
            List<Float> secondHalfFirstParent = firstParent.subList(midpoint, firstParent.size());

            List<Float> combinedOffspringTwo = new ArrayList<>(firstHalfSecondParent);
            combinedOffspringTwo.addAll(secondHalfFirstParent);
            
            parents.add(combinedOffspringOne);
            parents.add(combinedOffspringTwo);
        }

        
        return parents;
    }

    public List<List<Float>> performMutation(List<List<Float>> population){

        for (List<Float> testSet: population){
            Random random = new Random();
            double mutationChance = random.nextDouble();

            if (mutationChance < mutationProbability){
                int mutationPosition = random.nextInt(0, (numTestCases-1));
                int randomInputDomainPosition = random.nextInt(inputDomain.size());
                float mutation = inputDomain.get(randomInputDomainPosition);
                testSet.set(mutationPosition, mutation);
            }
        }


        return population;
    }
    public List<List<Float>> reproduction(List<List<Float>> parents){
        List<List<Float>> population = new ArrayList<>(parents);

        while(population.size() < populationSize ){
            int position = population.size() % parents.size();
            population.add(parents.get(position));
        }

        return population;
    }
    
}
