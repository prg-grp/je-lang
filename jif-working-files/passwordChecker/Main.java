public class Main {
    public static void main(String[] args) {
        String guess = "myPassword";
        boolean b = PasswordChecker.checkPassword(guess);
        System.out.println("Password "+guess+" was "+b);
        guess = "mySecurePassword";
        b = PasswordChecker.checkPassword(guess);
        System.out.println("Password "+guess+" was "+b);
    }
}
