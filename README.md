# sgx-language-support-impl


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
The class-level annotation @Enclave denotes that the annotated class should be placed inside the enclave. The field-level @Secret annotation denotes secret fields whose values must not leak outside the SGX enclave. @Gateway annotation denotes methods which are accessible from the non-enclave environment.
## Prerequisites
#### 1. Jif installation
	1. Visit https://www.cs.cornell.edu/jif/
	2. Download the zip file of Jif 3.5
	3. Unzip the file and install Jif following the installation steps provided in the README file
	4. We refer to the Jif root directory as $JIF_ROOT
	5. Compile the principals in the the $JIF_ROOT/tests/jif/principals/
	6. We denote Jif bin directory as JIF_BIN = $JIF_ROOT/bin/ and Jif principals directory as JIFC_PRINCIPALS = $JIF_ROOT/tests/jif/principals/

#### 2. Setting up Intel SGX (if not set up already)
##### 	2.1. Installing Intel SGX drivers
	1. Visit https://01.org/intel-software-guard-extensions/downloads
	2. Download the files

#### 3. Install [Maven](https://maven.apache.org/)

## Building the compiler

	1. Make sure that you have the environment variable JAVA_HOME set to JDK 9 or higher. (\TODO, instead add compiler-plugin to pom file)
	2. Chage to the root directory of the project
	3. Run `mvn package`
	4. A jar file named *je-to-jiff-compiler-jar-with-dependencies* will be compiled in the */je-to-jiff-compiler/target* directory.

## Running the compiler
	1. In */compile.sh* file, set the variables `JIF_BIN` and `JIFC_PRINCIPALS`. These are should hold the some directory paths as described in the *Jif installation* process.
	2. In */compile.sh* file, set the variable `JAVA_HOME`. It represents the root directory of JDK. The compiler has been tested with Java 8.
