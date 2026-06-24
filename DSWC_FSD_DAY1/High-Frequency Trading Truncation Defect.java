public class TradingTruncation {
    public static void main(String[] args) {
        double transactionValue = 130.99;
        int intValue = (int) transactionValue;
        byte byteValue = (byte) intValue;
        
        System.out.println("Original double value: " + transactionValue);
        System.out.println("After casting to int: " + intValue);
        System.out.println("After casting to byte: " + byteValue);
        System.out.println("Loss due to truncation: " + (transactionValue - intValue));
        System.out.println("Loss due to byte overflow: " + (intValue - byteValue));
        System.out.println("Total loss: " + (transactionValue - byteValue));
    }
}