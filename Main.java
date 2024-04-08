import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main { 
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if(args.length != 8){
            System.out.println("Usage: java CommandLineArguments <inputDomain: String> <testSetSize: Int> <sbsPopulationSize: Int> <sbsGenerationAmount: Int> <sbsParentsAmount: Int> <sbsMutationProbability: Double> <failureRegionType: String> <totalFailureRegions: Int>");
        }else{
            try{
                String fileName = args[0];
                int testSetSize = Integer.parseInt(args[1]);
                int sbsPopulationSize = Integer.parseInt(args[2]);
                int sbsGenerationAmount = Integer.parseInt(args[3]);
                int sbsParentsAmount = Integer.parseInt(args[4]);
                double sbsMutationProbability = Double.parseDouble(args[5]);
                String failureRegionType = args[6];
                int totalFailureRegions = Integer.parseInt(args[7]); 

                List<TestCase> inputDomain = parseInputFile("input_domains/"+fileName);


                RandomTesting randomTesting = new RandomTesting(inputDomain, testSetSize);
                SelectTestFromCandidate stfcsTesting = new SelectTestFromCandidate(inputDomain, testSetSize);
                SearchBased sbsTesting = new SearchBased(inputDomain, testSetSize, sbsPopulationSize, sbsGenerationAmount,sbsParentsAmount,sbsMutationProbability);
        
                try {
                    List<TestCase> randomTestCases = randomTesting.createTestSet();
                    List<TestCase> stfcsTestCases = stfcsTesting.performSTFCS();
                    List<TestCase> sbsTestCases = sbsTesting.performSBS();
        
                    SimulatedSystem system = new SimulatedSystem(totalFailureRegions, failureRegionType, inputDomain);
        
                    System.out.println("Random Test Results");
                    System.out.println(system.failureCasesFound(randomTestCases));
                    System.out.println("Search Based Results");
                    System.out.println(system.failureCasesFound(sbsTestCases));
                    System.out.println("Select Test from Candidate Set Results");
                    System.out.println(system.failureCasesFound(stfcsTestCases));
        
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                System.out.println("Incorrect arguments. Arguments should be in the form: <inputDomain: String> <testSetSize: Int> <sbsPopulationSize: Int> <sbsGenerationAmount: Int> <sbsParentsAmount: Int> <sbsMutationProbability: Double> <failureRegionType: String> <totalFailureRegions: Int> ");
                e.getMessage();
            }

       
        }
        
    }

    private static List<TestCase> parseInputFile(String fileName) throws FileNotFoundException, IOException{
        List<TestCase> inputDomain = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while(line != null){
                String[] inputCases = line.split(" ");
                for (String input: inputCases){
                    String[] inputDimensionValues = input.split(",");
                    TestCase inputCase = new TestCase();

                    for (String dimensionValue: inputDimensionValues){
                        double inputCaseDimension = Double.parseDouble(dimensionValue);
                        inputCase.addInputCaseValue(inputCaseDimension);
                    }
                    inputDomain.add(inputCase);
                    
                }
                line = reader.readLine();
            }
        
        }catch (Exception e) {
            System.out.println("There has been an error");
            System.out.println(e.getMessage());
        }
        return inputDomain;

    }
}
