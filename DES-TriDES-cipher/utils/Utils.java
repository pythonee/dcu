package utils;

import java.util.Arrays;

/*
 * 这个文件定义了此次DES作业中用到的辅助函数 
 */
public class Utils {

	// 是否打印中间过程中的调试信息
	public static boolean debug = true;
	
	public static void debug(String str){
		// 如果需要跟踪中间变量，就需要把debug置成true
		if (Utils.debug == true)
		{
			System.out.println(str);
		}
	}
	
	// 二进制字符串转化成byte数组
	public static byte[] BinaryStringToByteArray(String binaryString) {
		int bytes = binaryString.length() / 8;
		byte[] arrayByte = new byte[bytes];
		String byteStr;
		for (int i = 0; i < bytes; i++) {
			byteStr = binaryString.substring(i * 8, (i + 1) * 8);
			byte s = 0;
			for (int j = 0; j < 8; j++) {
				if (byteStr.charAt(j) == '1')
					s += Math.pow(2, 7 - j);
			}
			arrayByte[i] = (byte) ((s <= 127) ? s : (s - 256));
		}
		return arrayByte;

	}

	// 字节数组转化为字符串
	public static String byteArrayToBinaryString(byte[] binary) {
		StringBuffer strBuf = new StringBuffer();
		String intStr;
		for (int i = 0; i < binary.length; i++) {
			intStr = Integer.toBinaryString(binary[i]);
			if (intStr.length() == 8)
				strBuf.append(intStr);
			else if (intStr.length() > 8) {
				strBuf.append(intStr.substring(intStr.length() - 8, intStr
						.length()));
			} else {
				char[] padding = new char[8 - intStr.length()];
				for (int j = 0; j < padding.length; j++)
					padding[j] = '0';
				strBuf.append(padding);
				strBuf.append(intStr);
			}
		}
		return strBuf.toString();
	}
	
	// 填充函数
	public static byte[] padding(byte[] plainMsg) {
		int length = plainMsg.length;
		int padding = 0;
		byte[] paddingByte;
		if ((padding = length % 8) != 0)
			padding = 8 - padding;
		paddingByte = new byte[padding];
		paddingByte[0] = -128;
		for (int i = 1; i < padding; i++) {
			paddingByte[i] = 0;
		}
		byte[] doByte = new byte[plainMsg.length + paddingByte.length];
		System.arraycopy(plainMsg, 0, doByte, 0, plainMsg.length);
		System.arraycopy(paddingByte, 0, doByte, plainMsg.length, paddingByte.length);
		return doByte;
	}

	
	// 去除填充函数
	public static byte[] clearPadding(byte[] plainMsg) {
		int padPos = 0, length = plainMsg.length;
		for (int i = length - 1; i >= 0; i--) {
			if (plainMsg[i] == -128) {
				padPos = i;
				break;
			}
		}
		if (padPos == 0)
			return plainMsg;
		else {
			byte[] data = new byte[padPos];
			System.arraycopy(plainMsg, 0, data, 0, padPos);
			
			return data;
		}
	}
}
