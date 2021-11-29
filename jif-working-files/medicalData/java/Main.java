import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;

public class Main {

    String rec1 = "{
        dateRep : 24/11/2021,
        day : 24,
        month : 11,
        year : 2021,
        cases : 66884,
        deaths : 335,
        countriesAndTerritories : Germany,
        geoId : DE,
        countryterritoryCode : DEU,
        popData2020 : 83166711,
        continentExp : Europe
     }";
     String rec2 = "{
        dateRep : 23/11/2021,
        day : 23,
        month : 11,
        year : 2021,
        cases : 45326,
        deaths : 309,
        countriesAndTerritories : Germany,
        geoId : DE,
        countryterritoryCode : DEU,
        popData2020 : 83166711,
        continentExp : Europe
     }";
     String rec3 = "{
        dateRep : 22/11/2021,
        day : 22,
        month : 11,
        year : 2021,
        cases : 30643,
        deaths : 62,
        countriesAndTerritories : Germany,
        geoId : DE,
        countryterritoryCode : DEU,
        popData2020 : 83166711,
        continentExp : Europe
     }";
     String rec4 = "{
        dateRep : 21/11/2021,
        day : 21,
        month : 11,
        year : 2021,
        cases : 42727,
        deaths : 75,
        countriesAndTerritories : Germany,
        geoId : DE,
        countryterritoryCode : DEU,
        popData2020 : 83166711,
        continentExp : Europe
     }";
     String rec5 = "{
        dateRep : 20/11/2021,
        day : 20,
        month : 11,
        year : 2021,
        cases : 63924,
        deaths : 248,
        countriesAndTerritories : Germany,
        geoId : DE,
        countryterritoryCode : DEU,
        popData2020 : 83166711,
        continentExp : Europe
     }";


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
        for (int i=0; i<5; i++) {
            switch (i) {
                case 0:
                    message = rec1;
                case 1:
                    message = rec2;
                case 2:
                    message = rec3;
                case 3:
                    message = rec4;
                case 4:
                    message = rec5;
                default:
                    break;
            }
            lst.add(encrypt(message, 5));
        }
        return lst;
    }

    private static EncRecord encrypt(String message, int key) {
        int len = message.length();
        String result = ;
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
