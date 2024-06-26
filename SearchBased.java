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
        int generation = 0;
        while (generation < numGenerations){
            List<List<TestCase>> parents = new ArrayList<>();
            while(parents.size() < numParents){
                List<TestCase> potentialParent = selectParent(population);
                if (!parents.contains(potentialParent)){
                    parents.add(potentialParent);
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

    private List<TestCase> selectParent(List<List<TestCase>> population){
        double totalDiversity = 0;
        Random random = new Random();
        for (List<TestCase> testSet: population){
            double diversity = calculateDiversity(testSet);
            totalDiversity += diversity;
        }

        double randomProbability = random.nextDouble()*totalDiversity;

        double currentDiversity = 0.0;
        
        for (List<TestCase> testSet  : population) {
            currentDiversity += calculateDiversity(testSet);
            if (currentDiversity >= randomProbability) {
                return testSet;
            }
        }

        return population.get(population.size() - 1);

    }

    private List<List<TestCase>> generateRandomPopulation() throws Exception{
        List<List<TestCase>> randomTestSets = new ArrayList<>();

        for (int testSetCount = 0; testSetCount < this.populationSize; testSetCount++){
            List<TestCase> randomTestSet = super.createTestSet();
            randomTestSets.add(randomTestSet);
        }
        
        return randomTestSets;
        
    }

    private double calculateDiversity(List<TestCase> testSet){
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

    private List<List<TestCase>> performOnePointCrossover(List<List<TestCase>> parents){
        if(parents.size() < 2){
            return parents;
        }
        for ( int parentCount = 0; parentCount < numParents ; parentCount +=2 ){
            List<TestCase> firstParent = parents.get(parentCount);
            List<TestCase> secondParent = parents.get(parentCount+1);
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

    private List<List<TestCase>> performMutation(List<List<TestCase>> population){

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
    private List<List<TestCase>> reproduction(List<List<TestCase>> parents){
        List<List<TestCase>> population = new ArrayList<>(parents);

        while(population.size() < populationSize ){
            int position = population.size() % parents.size();
            population.add(parents.get(position));
        }

        return population;
    }
    
}
