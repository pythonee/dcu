package decrypt;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import algorithm.DESAlgorithm;
import utils.Utils;

public class DESDecrypt {
	private DESAlgorithm des;
	
	public DESDecrypt(){
		des = new DESAlgorithm();
	}
	
	public String desDecrypt(String cipherMsg,byte[] key){
		byte[] cipherByte = Base64.decodeBase64(cipherMsg);
		
		byte[] decryptBlock = new byte[8];
			
		for (int i = 0; i < cipherByte.length/8; i++) {
			int block = i+1;
			Utils.debug("================================第"+block+"块=================================");
			System.arraycopy(cipherByte, i*8, decryptBlock, 0, 8);
			decryptBlock = des.DESMainProcess(decryptBlock, key, false);
			System.arraycopy(decryptBlock, 0, cipherByte, i*8, 8);
		}
		
		System.out.println("===============================最后解密结果================================");
		System.out.println("解密结果的二进制形式："+Utils.byteArrayToBinaryString(Utils.clearPadding(cipherByte)));
		System.out.println("解密结果的整数形式："+ Arrays.toString(Utils.clearPadding(cipherByte)));
		System.out.println("解密结果的字符串形式：" + new String(Utils.clearPadding(cipherByte)));
		
		// 解密完之后要去填充
		return new String(Utils.clearPadding(cipherByte));
	}
}
