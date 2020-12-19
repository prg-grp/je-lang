@Enclave
public class Encryptor {

	@Secret
	private String key = "SECRET_KEY";


	@Gateway
	public String encrypt (String plaintext) {

		String plaintextE = endorse(plaintext);

		String result = plaintextE+key;

		String result1 = declassify(result);

		return result1;
	}
}