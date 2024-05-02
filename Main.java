import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main { 
    public static void main(String[] args) throws FileNotFoundException, IOException {
        long startTime = System.currentTimeMillis();
        if(args.length != 10){
            System.out.println("Usage: java CommandLineArguments <inputDomain: String> <testSetSize: Int> <sbsPopulationSize: Int> <sbsGenerationAmount: Int> <sbsParentsAmount: Int> <sbsMutationProbability: Double> <failureRegionType: String> <totalFailureRegions: Int> <blockSize: Int> <outputFile: String>");
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
                int blockSize = Integer.parseInt(args[8]);

                List<TestCase> inputDomain = parseInputFile("input_domains/"+fileName);
                String outputFile = "results/"+args[9];


                RandomTesting randomTesting = new RandomTesting(inputDomain, testSetSize);
                SearchBased sbsTesting = new SearchBased(inputDomain, testSetSize, sbsPopulationSize, sbsGenerationAmount,sbsParentsAmount,sbsMutationProbability);
                SelectTestFromCandidate stfcsTesting = new SelectTestFromCandidate(inputDomain, testSetSize);
        
                try {
                    List<TestCase> randomTestCases = randomTesting.createTestSet();
                    List<TestCase> sbsTestCases = sbsTesting.performSBS();
                    List<TestCase> stfcsTestCases = stfcsTesting.performSTFCS();
        
                    SimulatedSystem system = new SimulatedSystem(totalFailureRegions, blockSize, failureRegionType, inputDomain);
                    String[] randomCasesResults = system.failureCasesFound(randomTestCases);
                    String[] searchCasesResults = system.failureCasesFound(sbsTestCases);
                    String[] stfcsCasesResults = system.failureCasesFound(stfcsTestCases);

                    List<String[]> results = new ArrayList<>(){{
                        add(randomCasesResults);
                        add(searchCasesResults);
                        add(stfcsCasesResults);
                    }};

                    System.out.println("--------------- Random Test Results ---------------");
                    System.out.println("First failure found at test case: "+randomCasesResults[0]);
                    System.out.println("Amount of failures found: "+ randomCasesResults[1]);
                    System.out.println("--------------- Search Based Results ---------------");
                    System.out.println("First failure found at test case: "+searchCasesResults[0]);
                    System.out.println("Amount of failures found: "+ searchCasesResults[1]);
                    System.out.println("--------------- Select Test from Candidate Set Results ---------------");
                    System.out.println("First failure found at test case: "+stfcsCasesResults[0]);
                    System.out.println("Amount of failures found: "+ stfcsCasesResults[1]);

                    outputToCSV(results, outputFile, startTime);

        
                    
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

            reader.close();
        
        }catch (Exception e) {
            System.out.println("There has been an error");
            System.out.println(e.getMessage());
        }
        return inputDomain;

    }

    private static void outputToCSV(List<String[]> results, String outputFileName, long startTime){

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");
        String[] headings = {"","First Failure Found","Amount of Failures Found"};
        char comma = ',';

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true))){
            writer.newLine();
            writer.write(now.format(formatter));
            writer.newLine();
            for (String header: headings){
                writer.write(header);
                writer.write(comma);
            }
            writer.newLine();
            for (String[] resultSet: results){
                if(results.indexOf(resultSet) == 0){
                    writer.write("Random Test Results");
                }else if(results.indexOf(resultSet) == 1){
                    writer.write("Search Based Results");
                }else{
                    writer.write("STFCS Results");
                }
                writer.write(comma);
                for(String result : resultSet){
                    if(result == null){
                        writer.write("null");
                    }else {
                    writer.write(result);
                    }
                    writer.write(comma);
                }
                writer.newLine();
            }

            long endTime = System.currentTimeMillis();
            writer.write("Runtime: "+(endTime-startTime));
            System.out.println("--------------- Runtime in milliseconds ---------------");
            System.out.println(endTime-startTime);
            writer.close();
            
        } catch(IOException e){
            System.out.println("There has been an error");
            System.out.println(e.getMessage());
        }
    }
}
