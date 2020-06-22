package Jif_Files.PasswordChecker;

public class NonSGX[principal NonSGX, principal SGX] {
        /* {Owner->*} : Can only be accessed by the principal Owner
    {A -> B} : Confidentiality policy : owned by A and can be only be read by (of course) A and B and any principals that can act for them.
    (Reading <-> Confidentiality)
    {A <- B} : Integrity policy : owned by A and allows A and B and any principals that can act for them to modify.
    (Modifying <-> integrity)
    _ => Everyone, * => No one

    Thus label, {A -> _, A <- *} means everyone can read (confidentiality) but no one can modify except A (Integrity)
         */

//


        public static void main(String[] args) {

        }

}
