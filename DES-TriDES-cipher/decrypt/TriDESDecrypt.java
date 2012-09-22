package decrypt;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import algorithm.DESAlgorithm;
import utils.Utils;


public class TriDESDecrypt
{
	private DESAlgorithm des;
	
	public TriDESDecrypt(){
		des = new DESAlgorithm();
	}
	
	public String tridesDecrypt(String cipherMsg,byte[] firsteKey,byte[] secondKey){
		
		byte[] cipherByte = Base64.decodeBase64(cipherMsg);
		
		byte[] decryptBlock = new byte[8];
		
		byte[] plainByte = cipherByte; // 解密后的明文byte
		
		// 第一轮先用K1，des的解密算法
		for (int i = 0; i < cipherByte.length/8; i++) {
			System.arraycopy(cipherByte, i*8, decryptBlock, 0, 8);
			decryptBlock = des.DESMainProcess(decryptBlock, firsteKey, false);
			System.arraycopy(decryptBlock, 0, plainByte, i*8, 8);
		}
		
		// 第二次用K2，des的加密算法
		for (int i = 0; i < plainByte.length/8; i++) {
			System.arraycopy(plainByte, i*8, decryptBlock, 0, 8);
			decryptBlock = des.DESMainProcess(decryptBlock, secondKey, true);
			System.arraycopy(decryptBlock, 0, plainByte, i*8, 8);
		}
		
		// 第三次用K1，des的解密算法
		for (int i = 0; i < plainByte.length/8; i++) {
			System.arraycopy(plainByte, i*8, decryptBlock, 0, 8);
			decryptBlock = des.DESMainProcess(decryptBlock, firsteKey, false);
			System.arraycopy(decryptBlock, 0, plainByte, i*8, 8);
		}


		
		System.out.println("===============================最后解密结果================================");
		System.out.println("解密结果的二进制形式："+Utils.byteArrayToBinaryString(Utils.clearPadding(plainByte)));
		System.out.println("解密结果的整数形式："+ Arrays.toString(Utils.clearPadding(plainByte)));
		System.out.println("解密结果的字符串形式：" + new String(Utils.clearPadding(plainByte)));
		
		// 解密完之后要去填充
		return new String(Utils.clearPadding(plainByte));
	}
}
