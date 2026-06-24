public class VideoChunkingBuffer {
    public static int[][] generateJaggedArray() {
        int[] fib = {1, 1, 2, 3, 5};
        int[][] jaggedArray = new int[5][];
        int counter = 1;
        
        for (int i = 0; i < jaggedArray.length; i++) {
            jaggedArray[i] = new int[fib[i]];
            for (int j = 0; j < jaggedArray[i].length; j++) {
                jaggedArray[i][j] = counter++;
            }
        }
        return jaggedArray;
    }
    
    public static int calculateTotalSum(int[][] array) {
        int total = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                total += array[i][j];
            }
        }
        return total;
    }
    
    public static void main(String[] args) {
        int[][] jaggedArray = generateJaggedArray();
        System.out.println("Jagged Array:");
        for (int i = 0; i < jaggedArray.length; i++) {
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < jaggedArray[i].length; j++) {
                System.out.print(jaggedArray[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Total sum: " + calculateTotalSum(jaggedArray));
    }
}