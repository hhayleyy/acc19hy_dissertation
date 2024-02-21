import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main { 
    public static void main(String[] args) throws FileNotFoundException, IOException {
        List<List<Float>> inputDomain = parseInputFile("input_domains/2d.txt");


        RandomTesting testing = new RandomTesting(inputDomain, 10);
        SelectTestFromCandidate testing2 = new SelectTestFromCandidate(inputDomain, 10);
        SearchBased testing3 = new SearchBased(inputDomain, 10, 10, 20,3,0.2);

        try {
            System.out.println(testing.createTestSet());
            System.out.println(testing2.performSTFCS());
            System.out.println(testing3.performSBS());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<List<Float>> parseInputFile(String fileName) throws FileNotFoundException, IOException{
        List<List<Float>> inputDomain = new ArrayList<>();
        List<Float> inputCase = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while(line != null){
                String[] inputCases = line.split(" ");
                for (String input: inputCases){
                    String[] inputDimensionValues = input.split(",");
                    inputCase = new ArrayList<>();

                    for (String dimensionValue: inputDimensionValues){
                        float inputCaseDimension = Float.parseFloat(dimensionValue);
                        inputCase.add(inputCaseDimension);
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
