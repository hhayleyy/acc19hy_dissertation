public class Main {
    public static void main(String[] args) {
        int[] inputDomain = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15, 16, 17, 18, 19, 20}; 
        RandomTesting testing = new RandomTesting(inputDomain, 20);
        
        try {
            System.out.println(testing.createTestSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
