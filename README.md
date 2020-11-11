# The JE Secure Programming Language 


JE provides language-level abstractions to write Java applications that can run with Intel SGX.
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
The class-level *@Enclave* annotation denotes that the annotated class should be placed inside the enclave. The field-level *@Secret* annotation denotes secret fields whose values must not leak outside the SGX enclave. The *@Gateway* annotation denotes methods which are accessible from the non-enclave environment

More examples are provided in `/test-cases/src/main/je/de/tuda/prg`
## Prerequisites
#### 1. Jif installation
1. Visit https://www.cs.cornell.edu/jif/ and download the zip file of Jif 3.5
2. Unzip the file and install Jif following the installation steps provided in the README file
3. We refer to the Jif root directory as `$JIF_ROOT`
4. Compile the principals in the the `$JIF_ROOT/tests/jif/principals/`
5. We denote the Jif bin directory as `JIF_BIN` and it points to `$JIF_ROOT/bin/` and Jif principals directory as `JIFC_PRINCIPALS` and pointing to `$JIF_ROOT/tests/jif/principals/`

#### 2. Setting up Intel SGX (only needed for running the application)
#### 2.1. Installing Intel SGX drivers
1. Visit https://download.01.org/intel-sgx/sgx-linux/2.9.1/docs/Intel_SGX_Installation_Guide_Linux_2.9.1_Open_Source.pdf and install the SGX drivers.

#### 3. Install [Maven](https://maven.apache.org/)

## Building the compiler
1. Make sure that you have the environment variable JAVA_HOME set to JDK 9 or higher. (\TODO, instead add compiler-plugin to the pom file)
2. Chage to the root directory of the project
3. Run `mvn package`
4. A jar file named `je-to-jiff-compiler-jar-with-dependencies.jar` will be compiled in the `/je-to-jiff-compiler/target` directory.

## Running the compiler
1. In the *compile.sh* file, set the following variables
	* `JIF_BIN` and `JIFC_PRINCIPALS` are the same directory paths as described in the *Jif installation* process above.
	* `JAVA_HOME` : It should point to the root directory of JDK. The compiler has been tested with JDK 8.
	* `JE_PATH` : Directory of the JE source files to be compiled
	* `JIF_PATH` : Directory for the generated Jif classes to be compiled
	* `JAVA_GENERATED_PATH` : Directory for the generated Java files

2. Run ```./compile.sh```
