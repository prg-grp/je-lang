import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("Foo"));
        JavaRDD<EncRecord> reclist = sc.parallelize(getEncryptedRecords());
        List<StatRecord> recListStat = recList.map(x -> StatUtil.process(x));
        for (StatRecord s: recList) {
            System.out.println(s.statData);
        }
    }

    private static List<EncRecord> getEncryptedRecords() {
        ArrayList<EncRecord> lst = new ArrayList();

        String message = "";
        for (int i=0; i<30; i++) {
            message = RandomStringUtils.random(10, true, false);
            lst.add(encrypt(message, 5));
        }
        return lst;
    }

    private static EncRecord encrypt(String message, int key) {
        int len = message.length();
        String result = "";
        for (int i = 0; i < len; i++) {
            char character = message.charAt(i);
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result += newCharacter;
            } else {
                result += character;
            }
        }
        return new EncRecord(result);
    }
}
