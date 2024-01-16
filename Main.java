public class Main {
    public static void main(String[] args) {
        int[] inputDomain = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15, 16, 17, 18, 19, 20}; 
        RandomTesting testing = new RandomTesting(inputDomain, 10);
        SelectTestFromCandidate testing2 = new SelectTestFromCandidate(inputDomain, 10);

        try {
            System.out.println(testing.createTestSet());
            System.out.println(testing2.performSTFCS());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
