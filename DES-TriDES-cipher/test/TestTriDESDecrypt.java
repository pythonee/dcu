package test;

import decrypt.TriDESDecrypt;
import encrypt.TriDESEncrypt;

public class TestTriDESDecrypt {
	static String cipherMsg = "3wFpzw4DxXhCQkS+9pVMcd02j0SCIo50ahsbcQxaYYt9DdmzvagvROg6evgRZ4wEhb+R6qevUYL/nIWU9dsWfVol0AXbe8oW";
	static byte[] firstKey = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
	static byte[] secondKey = {0x08,0x07,0x06,0x05,0x04,0x03,0x02,0x01};
	
	private TriDESDecrypt decryptor;
	
	public TestTriDESDecrypt()
	{
		decryptor = new TriDESDecrypt();
	}
	
	public static void main(String[] args){
		TestTriDESDecrypt test = new TestTriDESDecrypt();
		test.testDecrypt();
		
	}

	public void testDecrypt(){
		decryptor.tridesDecrypt(cipherMsg, firstKey, secondKey);
	}
}
