import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.ArrayList;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;



public class Main {

    static String rec1 = "dateRep : 24/11/2021, day : 24, month : 11, year : 2021, cases : 66884, deaths : 335, countriesAndTerritories : Germany, geoId : DE, countryterritoryCode : DEU, popData2020 : 83166711, continentExp : Europe";
    static String rec2 = "dateRep : 23/11/2021,day : 23, month : 11, year : 2021, cases : 45326, deaths : 309, countriesAndTerritories : Germany, geoId : DE, countryterritoryCode : DEU, popData2020 : 83166711, continentExp : Europe";
    static String rec3 = "dateRep : 22/11/2021, day : 22, month : 11, year : 2021, cases : 30643, deaths : 62, countriesAndTerritories : Germany, geoId : DE, countryterritoryCode : DEU, popData2020 : 83166711, continentExp : Europe";
    static String rec4 = "dateRep : 21/11/2021, day : 21, month : 11, year : 2021, cases : 42727, deaths : 75, countriesAndTerritories : Germany, geoId : DE, countryterritoryCode : DEU, popData2020 : 83166711, continentExp : Europe";
    static String rec5 = "dateRep : 20/11/2021, day : 20, month : 11, year : 2021, cases : 63924, deaths : 248, countriesAndTerritories : Germany, geoId : DE, countryterritoryCode : DEU, popData2020 : 83166711, continentExp : Europe";


    public static void main(String[] args) {

        List<EncRecord> encrypted = getEncryptedRecords();
        List<StatRecord> stat = new ArrayList();
        int i = 0;
        for (EncRecord e : encrypted) {
            System.out.println("----Enc Data Process of Patient : "+i+" ----");
            stat.add(StatUtil.process(e));
            i++;
        }

        for (int j=0; j<stat.size(); j++) {
            System.out.println("----Stat Data of Patient : "+j+" ----");
            System.out.println(stat.get(j).getStatData());
        }

        /*JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("StatUtil"));
        JavaRDD<EncRecord> reclist = sc.parallelize(getEncryptedRecords());
        JavaRDD<StatRecord> recListStat = reclist.map(x -> StatUtil.process(x));*/
        //List<StatRecord> array = recListStat.collect();
        //System.out.println(recListStat.toString());
        System.out.println("finished with "+stat.size()+" records");
    }

    private static List<EncRecord> getEncryptedRecords() {
        ArrayList<EncRecord> lst = new ArrayList();

        Path filePath1 = Paths.get("/Users/robert/je-lang/jif-working-files/medicalData/data_json/Wilburn655_Wiegand701_a4fa1d02-c64b-87b7-47fd-4f5f2e127050.json");
        Path filePath2 = Paths.get("/Users/robert/je-lang/jif-working-files/medicalData/data_json/Elizbeth263_Rogahn59_6958c19c-71da-5540-d47f-fd95153df03d.json");
        Charset charset = StandardCharsets.UTF_8;
        try {
            List<String> lines = Files.readAllLines(filePath1, charset);
            String file = "";
            for(String line: lines) {
                file = file + line;
            }
            lst.add(encrypt(file, 5));

            lines = Files.readAllLines(filePath2, charset);
            file = "";
            for(String line: lines) {
                file = file + line;
            }
            lst.add(encrypt(file, 5));
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
        }
        

        /*String message = "";
        for (int i=0; i<5; i++) {
            switch (i) {
                case 0:
                    message = rec1;
                    break;
                case 1:
                    message = rec2;
                    break;
                case 2:
                    message = rec3;
                    break;
                case 3:
                    message = rec4;
                    break;
                case 4:
                    message = rec5;
                    break;
                default:
                    break;
            }
            System.out.println("Message "+i+" : "+message);
            lst.add(encrypt(message, 5));
        }*/
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
