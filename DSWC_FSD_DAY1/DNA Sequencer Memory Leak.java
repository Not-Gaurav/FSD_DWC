public class DNASequencer {
    private StringBuilder sequence;
    
    public DNASequencer() {
        sequence = new StringBuilder(100000);
    }
    
    public void ingestSequence(char[] sensorData) {
        sequence.append(sensorData);
    }
    
    public void mutateDNA(String target, String replacement) {
        int index = sequence.indexOf(target);
        if (index != -1) {
            sequence.replace(index, index + target.length(), replacement);
        }
    }
    
    public String getSequence() {
        return sequence.toString();
    }
    
    public static void main(String[] args) {
        DNASequencer sequencer = new DNASequencer();
        
        char[] data = new char[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = "ACTG".charAt(i % 4);
        }
        sequencer.ingestSequence(data);
        sequencer.mutateDNA("ACTG", "TGAC");
        System.out.println("Sequence length: " + sequencer.getSequence().length());
    }
}