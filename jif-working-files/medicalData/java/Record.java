package medicalData;

public class Record {

    protected char[] data;

    public Record(char[] medData) {
        data = medData;
    }

    public char[] getData() {
        return data;
    }
}