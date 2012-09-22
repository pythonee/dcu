package test;


import encrypt.DESEncrypt;

public class TestDESEncrypt {
	static byte[] plainMsg = { 0x01,0x02,0x03 };
	static byte[] key = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
	static String plainStr = "Hello world..中文测试..dfgdfgdfgdfgdf";
	
	static DESEncrypt desEncryptor;
	
	public TestDESEncrypt(){
		desEncryptor = new DESEncrypt();
	}
	public static void main(String[] args) {
		TestDESEncrypt test = new TestDESEncrypt();
		test.testEncrypt();
	}
	
	public void testEncrypt(){
		desEncryptor.desEncrypt(plainStr, key);
	}
	
}
