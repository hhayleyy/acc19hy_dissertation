import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchBased extends RandomTesting {

    private int populationSize;
    private int numGenerations;
    private int numParents;
    private double mutationProbability;
    private int numTestCases;
    private List<List<Float>> inputDomain;

    public SearchBased(List<List<Float>> inputDomain, int numTestCases, int populationSize, int numGenerations, int numParents, double mutationProbability) {
        super(inputDomain, numTestCases);
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.numParents = numParents;
        this.mutationProbability = mutationProbability;
        this.numTestCases = numTestCases;
        this.inputDomain = inputDomain;
    }

    public List<List<Float>> performSBS() throws Exception{
        List<List<List<Float>>> population = generateRandomPopulation();
        int generation = 1;
        while (generation <= numGenerations){
            float totalDiversity = 0;
            List<List<List<Float>>> parents = new ArrayList<>();
            for (List<List<Float>> testSet: population){
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
                    List<List<Float>> testSet = population.get(counter);
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
        List<List<Float>> bestTestSet = new ArrayList<>();
        for(List<List<Float>> testSet : population){
            float diversity = calculateDiversity(testSet);
            if (diversity > highestDiversity){
                bestTestSet = testSet;
            }
        }

        return bestTestSet;

    }

    public List<List<List<Float>>> generateRandomPopulation() throws Exception{
        List<List<List<Float>>> randomTestSets = new ArrayList<>();

        for (int testSetCount = 0; testSetCount < this.populationSize; testSetCount++){
            List<List<Float>> randomTestSet = super.createTestSet();
            randomTestSets.add(randomTestSet);
        }
        
        return randomTestSets;
        
    }

    public float calculateDiversity(List<List<Float>> population){
        // sum of the distance between the test datum and its nearest neighbour
        float diversity = 0;

        for (List<Float> testCase : population){
            double smallestNeighbourDifference = 0;
            for (List<Float> neighbour: population){
                double neighbourDifference = DistanceMetric.euclideanDistance(testCase, neighbour);

                if((smallestNeighbourDifference == 0) ||(testCase != neighbour) && (neighbourDifference < smallestNeighbourDifference)){
                    smallestNeighbourDifference = neighbourDifference;
                }
            }

            diversity += smallestNeighbourDifference;

        }

        return diversity;
    }

    public List<List<List<Float>>> performOnePointCrossover(List<List<List<Float>>> parents){

        for ( int parentCount = 1; parentCount <= numParents ; parentCount +=2 ){
            List<List<Float>> firstParent = parents.get(parentCount-1);
            List<List<Float>> secondParent = parents.get(parentCount);
            int midpoint = firstParent.size()/2;

            List<List<Float>> firstHalfFirstParent = firstParent.subList(0, midpoint);
            List<List<Float>> secondHalfSecondParent = secondParent.subList(midpoint, secondParent.size());

            List<List<Float>> combinedOffspringOne = new ArrayList<>(firstHalfFirstParent);
            combinedOffspringOne.addAll(secondHalfSecondParent);

            List<List<Float>> firstHalfSecondParent = secondParent.subList(0, midpoint);
            List<List<Float>> secondHalfFirstParent = firstParent.subList(midpoint, firstParent.size());

            List<List<Float>> combinedOffspringTwo = new ArrayList<>(firstHalfSecondParent);
            combinedOffspringTwo.addAll(secondHalfFirstParent);
            
            parents.add(combinedOffspringOne);
            parents.add(combinedOffspringTwo);
        }

        
        return parents;
    }

    public List<List<List<Float>>> performMutation(List<List<List<Float>>> population){

        for (List<List<Float>> testSet: population){
            Random random = new Random();
            double mutationChance = random.nextDouble();

            if (mutationChance < mutationProbability){
                int mutationPosition = random.nextInt(0, (numTestCases-1));
                int randomInputDomainPosition = random.nextInt(inputDomain.size());
                List<Float> mutation = inputDomain.get(randomInputDomainPosition);
                while(testSet.contains(mutation)){
                    randomInputDomainPosition = random.nextInt(inputDomain.size());
                    mutation = inputDomain.get(randomInputDomainPosition);
                }
                testSet.set(mutationPosition, mutation);
            }
        }


        return population;
    }
    public List<List<List<Float>>> reproduction(List<List<List<Float>>> parents){
        List<List<List<Float>>> population = new ArrayList<>(parents);

        while(population.size() < populationSize ){
            int position = population.size() % parents.size();
            population.add(parents.get(position));
        }

        return population;
    }
    
}
