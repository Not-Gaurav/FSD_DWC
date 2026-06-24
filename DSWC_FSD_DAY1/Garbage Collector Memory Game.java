public class GarbageCollectionDemo {
    public static void main(String[] args) {
        int[] array1 = new int[1000];
        int[] array2 = new int[2000];
        int[] array3 = new int[3000];
        
        for (int i = 0; i < array1.length; i++) array1[i] = i;
        for (int i = 0; i < array2.length; i++) array2[i] = i + 1000;
        for (int i = 0; i < array3.length; i++) array3[i] = i + 3000;
        
        int[][] container = new int[3][];
        container[0] = array1;
        container[1] = array2;
        container[2] = array3;
        
        System.out.println("Arrays stored in container");
        
        System.out.println("Severing references to array1...");
        array1 = null;
        container[0] = null;
        System.out.println("array1 is now unreachable and eligible for GC");
        
        System.out.println("Severing references to array2...");
        array2 = null;
        container[1] = null;
        System.out.println("array2 is now unreachable and eligible for GC");
        
        System.out.println("Severing references to array3...");
        array3 = null;
        container[2] = null;
        System.out.println("array3 is now unreachable and eligible for GC");
        
        System.out.println("All arrays are now unreachable");
        System.gc();
        System.out.println("Garbage collection requested");
    }
}