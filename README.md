# The J<sub>E</sub> Secure Programming Language 


J<sub>E</sub> provides language-level abstractions to write Java applications that can run with Intel SGX.
Parts of the program to be run inside the SGX can be specified using J<sub>E</sub> annotations. J<sub>E</sub> uses an implicit security type system to detect invalid information flows.  

J<sub>E</sub> follows the tierless programming approach. The program partition and security analysis are automatically performed by the J<sub>E</sub> compiler.


Example:
```
@Enclave
class Enclave {
	@Secret
	private static String password;
		
	@Gateway
	public static Boolean checkPassword(String guess) {
		return declassify(password.equals(guess));
	} 
}
```
In this snippet, the *@Enclave*, *@Secret* and *@Gateway* annotations and the 'declassify' method are the J<sub>E</sub> abstractions. The class-level *@Enclave* annotation denotes that the annotated class is to be placed inside the enclave. The field-level *@Secret* annotation denotes secret fields whose values must not leak outside the SGX enclave. The *@Gateway* annotation specifies that the anotated methods are accessible from the non-enclave environment.



