import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main { 
    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<TestCase> inputDomain = parseInputFile("input_domains/2d.txt");


        RandomTesting randomTesting = new RandomTesting(inputDomain, 10);
        SelectTestFromCandidate stfcsTesting = new SelectTestFromCandidate(inputDomain, 10);
        SearchBased sbsTesting = new SearchBased(inputDomain, 10, 10, 20,3,0.2);

        try {
            List<TestCase> randomTestCases = randomTesting.createTestSet();
            List<TestCase> stfcsTestCases = stfcsTesting.performSTFCS();
            List<TestCase> sbsTestCases = sbsTesting.performSBS();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<TestCase> parseInputFile(String fileName) throws FileNotFoundException, IOException{
        List<TestCase> inputDomain = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while(line != null){
                String[] inputCases = line.split(" ");
                for (String input: inputCases){
                    String[] inputDimensionValues = input.split(",");
                    TestCase inputCase = new TestCase();

                    for (String dimensionValue: inputDimensionValues){
                        float inputCaseDimension = Float.parseFloat(dimensionValue);
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
