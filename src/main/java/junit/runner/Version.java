package junit.runner;

/**
 * Junit�汾��
 * 
 * @author ע��By JavaSking 2017��2��5��
 */
public class Version {
	/**
	 * ����һ��Junit�汾����
	 */
	private Version() {

	}

	/**
	 * ��ȡJunit�汾�š�
	 * 
	 * @return Junit�汾�š�
	 */
	public static String id() {

		return "4.11-SNAPSHOT";
	}

	public static void main(String[] args) {

		System.out.println(id());
	}
}
