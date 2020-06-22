package de.tuda.stg.jsgx.combChecker;

import de.tuda.stg.jsgx.checker.qual.nonSecret;
import de.tuda.stg.jsgx.checker.qual.secret;

public class CheckerTest {

    public void testHierarchy(@nonSecret int ns, @secret int s) {
        @secret int secretInt = ns;    // Fine
        secretInt = ns;    // Fine

        @nonSecret int nonSecretInt = ns;  // Fine
        nonSecretInt = s;  // Error
    }

}
