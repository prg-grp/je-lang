@Enclave
public class Encryptor {

	@Secret
	private static String key = "SECRET_KEY";


	@Gateway
	public static String encrypt (String plaintext) {

		String plaintextE = endorse(plaintext);

		String result = plaintextE+key;

		String result1 = declassify(result);

		return result1;
	}
}