# The J<sub>E</sub> Secure Programming Language 


J<sub>E</sub> provides language-level abstractions to write Java applications that can run with Intel SGX.
Parts of the program to be run inside the SGX can be specified using JE annotations. JE uses an implicit security type system to detect invalid information flows.
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
The class-level *@Enclave* annotation denotes that the annotated class should be placed inside the enclave.
The field-level *@Secret* annotation denotes secret fields whose values must not leak outside the SGX enclave.
The *@Gateway* annotation specifies methods which are accessible from the non-enclave environment

More examples are provided in `/test-cases/src/je/de/tuda/prg`
## Prerequisites
### 1. Jif installation
1. Visit https://www.cs.cornell.edu/jif/ and download the zip file of Jif 3.5
2. Unzip the file and install Jif following the installation steps provided in the README file
3. We refer to the Jif root directory (it is the directory unzipped in the step 2) as `$JIF_HOME`
4. Compile the principals in the `$JIF_HOME/tests/jif/principals/` according to the README file

### 2. Setting up Intel SGX (only needed for running the application)
##### 2.1. Installing Intel SGX drivers
1. Visit https://download.01.org/intel-sgx/sgx-linux/2.9.1/docs/Intel_SGX_Installation_Guide_Linux_2.9.1_Open_Source.pdf and install the SGX drivers.

### 3. Install [Maven](https://maven.apache.org/)

## Building the compiler
1. Make sure that you have the JAVA_HOME environment variable set to JDK 9 or higher.
2. Change to the root directory of the project.
3. Run `mvn package`
4. A jar file named `je-to-jiff-compiler-jar-with-dependencies.jar` will be compiled in the `/je-to-jiff-compiler/target` directory.

## Running the compiler
1. Make sure that JDK 8 is in your system path.
2. In the *compile.sh* file, set the `JIF_HOME` variable. (same as described in the *Jif installation* process above)
3. Run ```./compile.sh <path-to-the-JE-files>```
