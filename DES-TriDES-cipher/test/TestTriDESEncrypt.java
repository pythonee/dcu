package test;

import encrypt.TriDESEncrypt;

public class TestTriDESEncrypt {
	// 测试明文输入
	static String plainMsg = "Hello world...中文测试...as;dfaskjdfhlkasdfhljkasdfhlkasjdfhkljasdf";
	static byte[] firstKey = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
	static byte[] secondKey = {0x08,0x07,0x06,0x05,0x04,0x03,0x02,0x01};
	
	private TriDESEncrypt encryptor;
	
	public TestTriDESEncrypt(){
		encryptor = new TriDESEncrypt();
	}
	
	public void testEncrypt(){
		encryptor.tridesEncrypt(plainMsg, firstKey, secondKey);
	}
	
	public static void main(String args[]){
		TestTriDESEncrypt test = new TestTriDESEncrypt();
		test.testEncrypt();
	}
}
