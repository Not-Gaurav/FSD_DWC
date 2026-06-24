public class SecurityWipe {
    public static void badWipe(int[] data) {
        data = new int[3];
    }
    
    public static void goodWipe(int[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
    }
    
    public static void main(String[] args) {
        int[] sensitiveData = {100, 200, 300, 400, 500};
        
        System.out.println("Original data:");
        printArray(sensitiveData);
        
        badWipe(sensitiveData);
        System.out.println("After badWipe (data still present):");
        printArray(sensitiveData);
        
        goodWipe(sensitiveData);
        System.out.println("After goodWipe (data wiped):");
        printArray(sensitiveData);
    }
    
    private static void printArray(int[] array) {
        for (int val : array) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}