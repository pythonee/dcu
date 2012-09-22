package algorithm;

import utils.Boxes;
import utils.Utils;

import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import com.sun.java_cup.internal.internal_error;
import sun.print.resources.serviceui;

public class DESAlgorithm {
	
	SubKeyGenerator subKeyGenerator;
	
	public DESAlgorithm(){
		subKeyGenerator = new SubKeyGenerator();
	}
	// 加密前初始化
	public byte[] sysInit(String plainMsg){
		byte[] binaryCode = plainMsg.getBytes(); // 得到字符串的二进制表示
		byte[] purMsg = new byte[binaryCode.length];
		
		// 判断是否需要填充
		if(binaryCode.length % 8 == 0) 
			purMsg = binaryCode;
		else
			purMsg = Utils.padding(binaryCode);
		return purMsg;
	}

	// 数据初始置换（IP置换）
	public char[] dataInitPermute(String binaryStr){
		char[] resultIP = new char[64];
		
		for(int i = 0; i < 64 ; i++){
			resultIP[i] = binaryStr.charAt(Boxes.DataInitPermuteBox[i] -1);
		}
		
		return resultIP;
	}
	
	// 最终数据逆置换（IP^1置换）
	public char[] dataInverseInitPermute(String binaryStr){
		char[] finalReverseIP = new char[64];
		
		for (int i = 0; i < 64; i++) {
			finalReverseIP[i] = binaryStr.charAt(Boxes.DataInverseInitPermuteBox[i] -1);
		}
		
		return finalReverseIP;
	}
	
	
	// 扩充置换(32 -> 48)
	public char[] dataExtendPermute(String binaryStr32){
		char[] extended = new char[48];
		for (int i = 0; i < 48; i++) {
			extended[i] = binaryStr32.charAt(Boxes.DataExtendPermuteBox[i] - 1);
		}
		
		return extended;
	}
	
	// S盒操作
	public char[] dataSBoxPermute(char[] xorResult){
		
		String xorRestultStr48 = new String(xorResult);
		
		StringBuilder outPutStrbld32 = new StringBuilder();	
		
		String binaryStr4;
		
		int xPos;        // S盒的x轴坐标
		
		int yPos;		 // s盒的y轴坐标 
		
		int value;		 // 对应S盒中(x,y)的值	
		
		for(int i = 0;i < 8; i++) {
			// 对每个S盒输入的6位
			char[] inputArrary6 = xorRestultStr48.substring(i*6, (i+1)*6).toCharArray();
			
			// 计算x轴坐标,第0位和第5位
			xPos = (inputArrary6[0] == '1' ? 1:0)*(int)Math.pow(2,1)+ 
				   (inputArrary6[5] == '1' ? 1:0)*(int)Math.pow(2,0); 
			
			// 计算y轴坐标，中间4位
			yPos = (inputArrary6[1] == '1' ? 1:0)*(int)Math.pow(2, 3) +
				   (inputArrary6[2] == '1' ? 1:0)*(int)Math.pow(2, 2) +
				   (inputArrary6[3] == '1' ? 1:0)*(int)Math.pow(2, 1) +
				   (inputArrary6[4] == '1' ? 1:0)*(int)Math.pow(2, 0);
			
			value = Boxes.S_Box[i][xPos][yPos];
			
			binaryStr4 = Utils.byteArrayToBinaryString( new byte[] { (byte)value } );
			
			// 拼接结果
			outPutStrbld32.append(binaryStr4.substring(binaryStr4.length()-4,binaryStr4.length()));	
	
		}
		return outPutStrbld32.toString().toCharArray();
	}
	
	// P置换操作
	public char[] dataCompressPermute(String binaryStr32){
		char[] compressed = new char[32];
		
		for (int i = 0; i < 32; i++) {
			compressed[i] = binaryStr32.charAt(Boxes.DataCompressPermute[i] - 1 );
		}
		return compressed;
	}
	
	// 数组异或操作
	public char[] xor(char[] byteArrayA,char[] byteArrayB){
		
		if(byteArrayA.length != byteArrayB.length){
			return null;
		}
		else{
			char[] xorResult = new char[byteArrayA.length];
			
			for(int i = 0; i < byteArrayA.length; i++){
				if (byteArrayA[i] == byteArrayB[i])
					xorResult[i] = '0';
				else 
					xorResult[i] = '1';
			}

			return xorResult;	
		}
	}
	
	// F函数
	public char[] fFunction(char[] dataRightArray32,char[] subKey48) {
		// 先扩展置换
		char[] extended = dataExtendPermute(new String(dataRightArray32));
		
		// 对异或结果做S盒操作
		char[] result = dataSBoxPermute(xor(extended, subKey48));
		
		// 再对结果进行P盒置换
		result = dataCompressPermute(new String(result));
		
		return result;
	}
	
	// DES算法主过程
	public byte[] DESMainProcess(byte[] msg, byte[] key, boolean isEncrypted) {
		Utils.debug("===============================输入数据=================================");
		Utils.debug("初始输入数据: " + Utils.byteArrayToBinaryString(msg));
		
		// 初始置换（IP置换）
		char[] result = dataInitPermute(Utils.byteArrayToBinaryString(msg));
		
		// 打印个人定制调试信息
		Utils.debug("===============================初始置换=================================");
		Utils.debug("初始置换结果: "+String.copyValueOf(result));
		
		char[] leftMsg = new char[32]; // 数据左32位
		char[] rightMsg = new char[32]; // 数据右32位
		char[] tempLeft = new char[32]; // 左右互换时的临时变量
		
		byte[] output = new byte[8];

		// 获得所有16轮的子密钥
		String[] subKeys = subKeyGenerator.getSubKey(key);
		// 每轮需要使用的子密钥
		String useSubKey;

		System.arraycopy(result, 0, leftMsg, 0, 32);
		System.arraycopy(result, 32, rightMsg, 0, 32);
		Utils.debug("===============================16轮开始================================");
		for (int i = 0; i < 16; i++) {
			System.arraycopy(leftMsg, 0, tempLeft, 0, 32);
			System.arraycopy(rightMsg, 0, leftMsg, 0, 32);

			if (isEncrypted)
				useSubKey = subKeys[i];
			else
				useSubKey = subKeys[15 - i];
			
			// 下面这句话就是每轮里真正做的事情
			rightMsg = xor(tempLeft, fFunction(rightMsg, useSubKey.toCharArray()));
			
			// 打印每轮输出的信息
			Utils.debug("第"+ i + "轮：");
			Utils.debug("子密钥的二进制形式: " + useSubKey);
			Utils.debug("子密钥的整数形式：" + Arrays.toString(Utils.BinaryStringToByteArray(useSubKey)));
			Utils.debug("右32位二进制形式：" + String.copyValueOf(rightMsg));
			Utils.debug("右32位整数形式：" + Arrays.toString(Utils.BinaryStringToByteArray(new String(rightMsg))));
			Utils.debug("\n");
		}
		System.arraycopy(leftMsg, 0, result, 32, 32);
		System.arraycopy(rightMsg, 0, result, 0, 32);
		// 初始逆置换
		output = Utils.BinaryStringToByteArray(new String(dataInverseInitPermute(new String(result))));
		
		// 打印16轮完之后的输出信息
		Utils.debug("===============================16轮结束后================================");
		Utils.debug("左32位二进制形式："+String.copyValueOf(leftMsg));
		Utils.debug("右32位二进制形式："+String.copyValueOf(rightMsg));
		Utils.debug("\n");

		return output;
	}
}
