package encrypt;

import java.security.spec.ECField;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import com.sun.java_cup.internal.internal_error;

import algorithm.DESAlgorithm;
import utils.Utils;

public class DESEncrypt {
	
	private DESAlgorithm des;
	
	public DESEncrypt(){
		des = new DESAlgorithm();
	}
	
	
	
	public String desEncrypt(String plainMsg,byte[] key){
		// 先对明文初始化
		byte[] plainByte = des.sysInit(plainMsg);
		
		byte[] cipherBlock = new byte[8];
		
		byte[] encrypted = plainByte;
		
		for(int i = 0 ; i < plainByte.length/8 ; i++){	
			int block = i+1;
			Utils.debug("================================第"+block+"块=================================");
			// 每次从明文中拷贝出64位进行加密
			System.arraycopy(plainByte, i*8, cipherBlock, 0, 8);
			cipherBlock = des.DESMainProcess(cipherBlock, key, true);
			System.arraycopy(cipherBlock, 0, encrypted, i*8, 8);
		}
		
		System.out.println("===============================最后加密结果================================");
		System.out.println("加密结果的二进制形式："+Utils.byteArrayToBinaryString(encrypted));
		System.out.println("加密结果的整数形式："+ Arrays.toString(encrypted));
		System.out.println("加密结果的字符串形式：" + Base64.encodeBase64String(encrypted));
		
		return Base64.encodeBase64String(encrypted);
	}
}
