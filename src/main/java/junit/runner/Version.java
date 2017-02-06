package junit.runner;

/**
 * Junit版本。
 * 
 * @author 注释By JavaSking 2017年2月5日
 */
public class Version {
	/**
	 * 构造一个Junit版本对象。
	 */
	private Version() {

	}

	/**
	 * 获取Junit版本号。
	 * 
	 * @return Junit版本号。
	 */
	public static String id() {

		return "4.11-SNAPSHOT";
	}

	public static void main(String[] args) {

		System.out.println(id());
	}
}
