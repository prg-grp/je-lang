# The J<sub>E</sub> Secure Programming Language 

### What is J<sub>E</sub>?  
J<sub>E</sub> is a programming language to develop secure enclave applications.  
J<sub>E</sub> provides high-level abstractions to specify the code to be placed inside
the enclaves and different privacy levels for data objects.
The static type system checks information flow and guarantees that the
secret data is not leaked outside the encvlave.

### Background 
Confidential computing is a promising technology
for securing code and data-in-use on untrusted host machines,
e.g., the cloud. Many hardware vendors offer the confidential
computing services using Trusted Execution Environments (TEEs).
A TEE is a hardware protected execution environment that allows
performing confidential computations over sensitive data on
untrusted hosts. Despite the appeal of achieving strong security
guarantees against low-level attackers, two challenges hinder
the adoption of TEEs. First, developing software in high-level
managed languages, e.g., Java or Scala, taking advantage of
existing TEEs is complex and error-prone. Second, partitioning
an application into components that run inside and outside a
TEE may break application-level security policies, resulting in
an insecure application when facing a realistic attacker.  

J<sub>E</sub> tries to address both these challenges.
J<sub>E</sub> is a programming model that integrates a TEE,
abstracting away low-level programming details such as initialization
and loading of data into the TEE. J<sub>E</sub> only requires
developers to add annotations to their programs to enable the
execution within the TEE. It has a security type system that
checks confidentiality and integrity policies against realistic
attackers with full control over code running outside the TEE.
Presently, J<sub>E</sub> works with Intel SGX as a target TEE.
For the formal details of the type-system and security guarantees,
have a look at the technical report.


### A sample J<sub>E</sub> code 
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



