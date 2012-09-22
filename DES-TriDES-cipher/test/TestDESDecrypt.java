package test;

import java.util.logging.Logger;

import decrypt.DESDecrypt;
import encrypt.DESEncrypt;
import utils.Utils;


public class TestDESDecrypt
{
	static byte[] cipherMsg = { 27,-124,-84,102,-90,-38,43,-72 };
	static byte[] key = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
	static String cipherStr = "lOO+7lYGkn5jf+MH31yLBiXSjW/s+xpdXJ9nofEBDojSZdg44QzKBg==";
	
	static DESDecrypt desDecryptor;
	
	public TestDESDecrypt(){
		desDecryptor = new DESDecrypt();
	}
	
	public static void main(String[] args) {
		TestDESDecrypt test = new TestDESDecrypt();
		test.testDecrypt();
	}

	public void testDecrypt(){
		desDecryptor.desDecrypt(cipherStr, key);
	}

}
