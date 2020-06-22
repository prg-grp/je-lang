package de.tuda.stg.jsgx.checkerTest;

public class Driver {

    public static void main (String[] args) {
        System.out.println("Hello World !");
        PasswordHandler pwdHandler = new PasswordHandler("abc");
        String guess = "xyz";
        System.out.println(pwdHandler.isMatched(guess));
    }
}
