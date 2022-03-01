@Enclave
public class PasswordChecker {

    @Secret
    static String password = "mySecurePassword";

    @Gateway
    public static boolean checkPassword(String guess) {
        String guessE = endorse(guess);
        return declassify(guessE.equals(password));
    }
}
