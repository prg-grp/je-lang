package de.tuda.stg.jsgx.checkerTest;


import de.tuda.stg.jsgx.checker.qual.*;
import de.tuda.stg.jsgx.combChecker.qual.LH;
import de.tuda.stg.jsgx.combChecker.qual.LL;

@SGX
public class PasswordHandler {

    @secret
    String password;

    public PasswordHandler (String password) {
        this.password = password;
    }

    @gateway
    public boolean isMatched (String guess) {
        boolean result = false;
        if (guess != null) {
            result = (guess.equals(password));
        }
        return result;
    }

    @gateway
    public boolean foo (@LL String guess) {
        boolean result = false;
        if (guess != null) {
            result = (guess.equals(password));
        }
        return result;
    }

    // public static low String declassify (arg1) {return (low) arg1;
}
