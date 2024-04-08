import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchBased extends RandomTesting {

    private int populationSize;
    private int numGenerations;
    private int numParents;
    private double mutationProbability;
    private int numTestCases;
    private List<TestCase> inputDomain;

    public SearchBased(List<TestCase> inputDomain, int numTestCases, int populationSize, int numGenerations, int numParents, double mutationProbability) {
        super(inputDomain, numTestCases);
        this.populationSize = populationSize;
        this.numGenerations = numGenerations;
        this.numParents = numParents;
        this.mutationProbability = mutationProbability;
        this.numTestCases = numTestCases;
        this.inputDomain = inputDomain;
    }

    public List<TestCase> performSBS() throws Exception{
        List<List<TestCase>> population = generateRandomPopulation();
        int generation = 1;
        while (generation <= numGenerations){
            double totalDiversity = 0;
            List<List<TestCase>> parents = new ArrayList<>();
            for (List<TestCase> testSet: population){
                double diversity = calculateDiversity(testSet);
                totalDiversity += diversity;
            }

            for (int parent = 1 ; parent <= numParents; parent ++ ){
                Random random = new Random();
                boolean foundDiversity = false;
                int counter = 0;
                double currentProbability = 0;
                while(foundDiversity == false){
                    double relativeDiversity = 0;
                    double randomProbability = random.nextDouble();
                    List<TestCase> testSet = population.get(counter);
                    relativeDiversity = calculateDiversity(testSet)/totalDiversity;
                    currentProbability += relativeDiversity;
                    if (randomProbability <= currentProbability){
                        parents.add(testSet);
                        foundDiversity = true;
                    }else{
                        counter += 1;
                    }
                }
            }
            


            population = performMutation(reproduction(performOnePointCrossover(parents)));
            generation += 1;
        }
        
        double highestDiversity = 0;
        List<TestCase> bestTestSet = new ArrayList<>();
        for(List<TestCase> testSet : population){
            double diversity = calculateDiversity(testSet);
            if (diversity > highestDiversity){
                bestTestSet = testSet;
            }
        }

        return bestTestSet;

    }

    public List<List<TestCase>> generateRandomPopulation() throws Exception{
        List<List<TestCase>> randomTestSets = new ArrayList<>();

        for (int testSetCount = 0; testSetCount < this.populationSize; testSetCount++){
            List<TestCase> randomTestSet = super.createTestSet();
            randomTestSets.add(randomTestSet);
        }
        
        return randomTestSets;
        
    }

    public double calculateDiversity(List<TestCase> testSet){
        // sum of the distance between the test datum and its nearest neighbour
        double diversity = 0;

        for (TestCase testCase : testSet){
            double smallestNeighbourDifference = 0;
            for (TestCase neighbour: testSet){
                double neighbourDifference = DistanceMetric.euclideanDistance(testCase, neighbour);

                if((smallestNeighbourDifference == 0) ||(testCase != neighbour) && (neighbourDifference < smallestNeighbourDifference)){
                    smallestNeighbourDifference = neighbourDifference;
                }
            }

            diversity += smallestNeighbourDifference;

        }

        return diversity;
    }

    public List<List<TestCase>> performOnePointCrossover(List<List<TestCase>> parents){

        for ( int parentCount = 1; parentCount <= numParents ; parentCount +=2 ){
            List<TestCase> firstParent = parents.get(parentCount-1);
            List<TestCase> secondParent = parents.get(parentCount);
            int midpoint = firstParent.size()/2;

            List<TestCase> firstHalfFirstParent = firstParent.subList(0, midpoint);
            List<TestCase> secondHalfSecondParent = secondParent.subList(midpoint, secondParent.size());

            List<TestCase> combinedOffspringOne = new ArrayList<>(firstHalfFirstParent);
            combinedOffspringOne.addAll(secondHalfSecondParent);

            List<TestCase> firstHalfSecondParent = secondParent.subList(0, midpoint);
            List<TestCase> secondHalfFirstParent = firstParent.subList(midpoint, firstParent.size());

            List<TestCase> combinedOffspringTwo = new ArrayList<>(firstHalfSecondParent);
            combinedOffspringTwo.addAll(secondHalfFirstParent);
            
            parents.add(combinedOffspringOne);
            parents.add(combinedOffspringTwo);
        }

        
        return parents;
    }

    public List<List<TestCase>> performMutation(List<List<TestCase>> population){

        for (List<TestCase> testSet: population){
            Random random = new Random();
            double mutationChance = random.nextDouble();

            if (mutationChance < mutationProbability){
                int mutationPosition = random.nextInt(0, (numTestCases-1));
                int randomInputDomainPosition = random.nextInt(inputDomain.size());
                TestCase mutation = inputDomain.get(randomInputDomainPosition);
                while(testSet.contains(mutation)){
                    randomInputDomainPosition = random.nextInt(inputDomain.size());
                    mutation = inputDomain.get(randomInputDomainPosition);
                }
                testSet.set(mutationPosition, mutation);
            }
        }


        return population;
    }
    public List<List<TestCase>> reproduction(List<List<TestCase>> parents){
        List<List<TestCase>> population = new ArrayList<>(parents);

        while(population.size() < populationSize ){
            int position = population.size() % parents.size();
            population.add(parents.get(position));
        }

        return population;
    }
    
}
