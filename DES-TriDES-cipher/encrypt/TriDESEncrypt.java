package encrypt;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import algorithm.DESAlgorithm;
import utils.Utils;


public class TriDESEncrypt
{
	private DESAlgorithm des;
	
	public TriDESEncrypt(){
		des = new DESAlgorithm();
	}
	
	public String tridesEncrypt(String plainMsg,byte[] firstKey,byte[] secondKey) {
		byte[] plainByte = des.sysInit(plainMsg);
		
		byte[] cipherBlock = new byte[8];
		
		byte[] encrypted = plainByte; //加密后的密文byte
		
		// 第一轮先用K1，des的加密算法
		for(int i = 0 ; i < plainByte.length/8 ; i++){
			System.arraycopy(plainByte, i*8, cipherBlock, 0, 8);
			cipherBlock = des.DESMainProcess(cipherBlock, firstKey, true);
			System.arraycopy(cipherBlock, 0, encrypted, i*8, 8);
		}
		// 第二次用K2，des的解密算法
		for(int i = 0 ; i < encrypted.length/8 ; i++){
			System.arraycopy(encrypted, i*8, cipherBlock, 0, 8);
			cipherBlock = des.DESMainProcess(cipherBlock, secondKey, false);
			System.arraycopy(cipherBlock, 0, encrypted, i*8, 8);
		}
		// 第三次用K1，des的加密算法
		for(int i = 0 ; i < encrypted.length/8 ; i++){
			System.arraycopy(encrypted, i*8, cipherBlock, 0, 8);
			cipherBlock = des.DESMainProcess(cipherBlock, firstKey,true);
			System.arraycopy(cipherBlock, 0, encrypted, i*8, 8);
		}
		
		System.out.println("===============================最后加密结果================================");
		System.out.println("加密结果的二进制形式："+Utils.byteArrayToBinaryString(encrypted));
		System.out.println("加密结果的整数形式："+ Arrays.toString(encrypted));
		System.out.println("加密结果的字符串形式：" + Base64.encodeBase64String(encrypted));

		return Base64.encodeBase64String(encrypted);
	}
}
