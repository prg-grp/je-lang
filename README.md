# The J<sub>E</sub> Secure Programming Language 


J<sub>E</sub> provides language-level abstractions to write Java applications that can run with Intel SGX.
The abstractions allow specifying privacy constraints for data and are statically checked by the J<sub>E</sub> compiler.  

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



