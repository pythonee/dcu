package algorithm;

import utils.Boxes;
import utils.Utils;

public class SubKeyGenerator {
	
	// 密钥初始化，主要是将byte数组转化为binary string.
	public String keyInit(byte[] key){
		String keyStr = new String();
		keyStr = Utils.byteArrayToBinaryString(key);
		
		return keyStr;
	}
	
	// 置换选择一
	public char[] keyChoosePermute(String keyStr){
		char[] keyArrary56 = new char[56];
		
		for(int i = 0 ; i < 56; i++){
			keyArrary56[i] = keyStr.charAt(Boxes.KeyChoosePermuteBox[i] -1);
		}
		
		return keyArrary56;
	}
	
	// 置换选择二
	public char[] keyCompressPermute(String keyStr){
		char[] keyArray48 = new char[48];
		
		for (int i = 0; i < 48; i++) {
			keyArray48[i] = keyStr.charAt(Boxes.KeyCompressPermuteBox[i] -1);
		}
		
		return keyArray48;
	}
	
	// 密钥循环左移
	public void keyRotateLeft(char[] src,int offset){
		char[] copy = new char[src.length];
		System.arraycopy(src, 0, copy, 0, src.length);
		for(int i =0 ; i < src.length; i++){
			src[i] = copy[(i + offset)%src.length];
		}
	}
	
	// 获得16轮过程中的16个密钥，返回String数组
	public String[] getSubKey(byte[] key){
		String[] subKey = new String[16];
		String keyStr = this.keyInit(key);
		char[] keyArrary56 = this.keyChoosePermute(keyStr); 	// 初始置换选择后的56位密钥（PC-置换）
		
		char[] keyLeft28 = new char[28];				// 左28位
		char[] keyRight28 = new char[28];				// 右28位
		
		System.arraycopy(keyArrary56, 0, keyLeft28, 0, 28);   // 从56密钥中，把0-27位分配给左28位
		System.arraycopy(keyArrary56,28, keyRight28, 0, 28);  // 把28-56分配给右28位
		
		// 循环得到16轮加密所需的16个子密钥
		for (int i = 0; i < 16; i++) {
			
			this.keyRotateLeft(keyLeft28, Boxes.KeyRotateLeftMove[i]);    // 左28位密钥循环左移
			this.keyRotateLeft(keyRight28, Boxes.KeyRotateLeftMove[i]);   // 右28位密钥循环左移
			
			// 做置换/紧缩操作（置换选择二）
			char[] keyArray48 = keyCompressPermute(String.copyValueOf(keyLeft28)+String.copyValueOf(keyRight28));
			
			// 得到每轮子密钥值
			subKey[i] = String.copyValueOf(keyArray48);
		}
		
		return subKey;
	}
}
