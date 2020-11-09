@Enclave
public class SGXClass {

	@Secret
	private String key;


	@Gateway
	public String encrypt (String plaintext) {

		String plaintextE = endorse(plaintext);

		String result = plaintextE+key;

		String result1 = declassify(result);

		return result1;
	}
}