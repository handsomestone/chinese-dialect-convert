/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * ��������ĸ���ӷִ���������һ�·�Χ
 * 1.Ӣ�ĵ��ʡ�Ӣ�ļӰ��������֡�ר�����ʣ���˾����
 * 2.IP��ַ��Email��URL
 * 
 * @author ������
 *
 */
public class LetterSegmenter implements ISegmenter {
	
	//���ӷ���
	public static final char[] Sign_Connector = new char[]{'+','-','_','.','@','&','/','\\'};
	/*
	 * ��Ԫ�Ŀ�ʼλ�ã�
	 * ͬʱ��Ϊ�ӷִ���״̬��ʶ
	 * ��start > -1 ʱ����ʶ��ǰ�ķִ������ڴ����ַ�
	 */
	private int start;
	/*
	 * ��¼��Ԫ����λ��
	 * end��¼�����ڴ�Ԫ�����һ�����ֵ�Letter����Sign_Connector���ַ���λ��
	 */
	private int end;
	
	/*
	 * ��ĸ��ʼλ��
	 */
	private int letterStart;

	/*
	 * ��ĸ����λ��
	 */
	private int letterEnd;
	
//	/*
//	 * ������������ʼλ��
//	 */
//	private int numberStart;
//	
//	/*
//	 * ���������ֽ���λ��
//	 */
//	private int numberEnd;

	
	public LetterSegmenter(){
		start = -1;
		end = -1;
		letterStart = -1;
		letterEnd = -1;
//		numberStart = -1;
//		numberEnd = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public void nextLexeme(char[] segmentBuff , Context context) {

		//��ȡ��ǰλ�õ�char	
		char input = segmentBuff[context.getCursor()];
		
		boolean bufferLockFlag = false;
		//��������ĸ
		bufferLockFlag = this.processMixLetter(input, context) || bufferLockFlag;
		//����Ӣ����ĸ
		bufferLockFlag = this.processEnglishLetter(input, context) || bufferLockFlag;
//		//����������ĸ
//		bufferLockFlag = this.processPureArabic(input, context) || bufferLockFlag;
		
		//�ж��Ƿ�����������
		if(bufferLockFlag){
			context.lockBuffer(this);
		}else{
			//�Ի���������
			context.unlockBuffer(this);
		}
	}
	
	/**
	 * ����������ĸ������
	 * �磺windos2000 | linliangyi2005@gmail.com
	 * @param input
	 * @param context
	 * @return
	 */
	private boolean processMixLetter(char input , Context context){
		boolean needLock = false;
		
		if(start == -1){//��ǰ�ķִ�����δ��ʼ�����ַ�			
			if(isAcceptedCharStart(input)){
				//��¼��ʼָ���λ��,�����ִ������봦��״̬
				start = context.getCursor();
				end = start;
			}
			
		}else{//��ǰ�ķִ������ڴ����ַ�			
			if(isAcceptedChar(input)){
				//���벻�����ӷ�
//				if(!isLetterConnector(input)){
					//��¼�¿��ܵĽ���λ�ã���������ӷ���β�������
//					end = context.getCursor();					
//				}
				//���ں���β���������ַ�
				end = context.getCursor();					
				
			}else{
				//�������зֵĴ�Ԫ
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , start , end - start + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
				//���õ�ǰ�ִ���״̬Ϊ��������
				start = -1;
				end = -1;
			}			
		}
		
		//context.getCursor() == context.getAvailable() - 1��ȡ���������һ���ַ���ֱ�����
		if(context.getCursor() == context.getAvailable() - 1){
			if(start != -1 && end != -1){
				//�������зֵĴ�Ԫ
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , start , end - start + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
			}
			//���õ�ǰ�ִ���״̬Ϊ��������
			start = -1;
			end = -1;
		}
		
		//�ж��Ƿ�����������
		if(start == -1 && end == -1){
			//�Ի���������
			needLock = false;
		}else{
			needLock = true;
		}
		return needLock;
	}
	
//	/**
//	 * �����������ַ����
//	 * @param input
//	 * @param context
//	 * @return
//	 */
//	private boolean processPureArabic(char input , Context context){
//		boolean needLock = false;
//		
//		if(numberStart == -1){//��ǰ�ķִ�����δ��ʼ���������ַ�	
//			if(CharacterHelper.isArabicNumber(input)){
//				//��¼��ʼָ���λ��,�����ִ������봦��״̬
//				numberStart = context.getCursor();
//				numberEnd = numberStart;
//			}
//		}else {//��ǰ�ķִ������ڴ��������ַ�	
//			if(CharacterHelper.isArabicNumber(input)){
//				//��¼��ǰָ��λ��Ϊ����λ��
//				numberEnd =  context.getCursor();
//			}else{
//				//�������зֵĴ�Ԫ
//				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , numberStart , numberEnd - numberStart + 1 , Lexeme.TYPE_LETTER);
//				context.addLexeme(newLexeme);
//				//���õ�ǰ�ִ���״̬Ϊ��������
//				numberStart = -1;
//				numberEnd = -1;
//			}
//		}
//		
//		//context.getCursor() == context.getAvailable() - 1��ȡ���������һ���ַ���ֱ�����
//		if(context.getCursor() == context.getAvailable() - 1){
//			if(numberStart != -1 && numberEnd != -1){
//				//�������зֵĴ�Ԫ
//				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , numberStart , numberEnd - numberStart + 1 , Lexeme.TYPE_LETTER);
//				context.addLexeme(newLexeme);
//			}
//			//���õ�ǰ�ִ���״̬Ϊ��������
//			numberStart = -1;
//			numberEnd = -1;
//		}
//		
//		//�ж��Ƿ�����������
//		if(numberStart == -1 && numberEnd == -1){
//			//�Ի���������
//			needLock = false;
//		}else{
//			needLock = true;
//		}
//		return needLock;		
//	}
	
	/**
	 * ����Ӣ����ĸ���
	 * @param input
	 * @param context
	 * @return
	 */
	private boolean processEnglishLetter(char input , Context context){
		boolean needLock = false;
		
		if(letterStart == -1){//��ǰ�ķִ�����δ��ʼ���������ַ�	
			if(CharacterHelper.isEnglishLetter(input)){
				//��¼��ʼָ���λ��,�����ִ������봦��״̬
				letterStart = context.getCursor();
				letterEnd = letterStart;
			}
		}else {//��ǰ�ķִ������ڴ��������ַ�	
			if(CharacterHelper.isEnglishLetter(input)){
				//��¼��ǰָ��λ��Ϊ����λ��
				letterEnd =  context.getCursor();
			}else{
				//�������зֵĴ�Ԫ
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , letterStart , letterEnd - letterStart + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
				//���õ�ǰ�ִ���״̬Ϊ��������
				letterStart = -1;
				letterEnd = -1;
			}
		}
		
		//context.getCursor() == context.getAvailable() - 1��ȡ���������һ���ַ���ֱ�����
		if(context.getCursor() == context.getAvailable() - 1){
			if(letterStart != -1 && letterEnd != -1){
				//�������зֵĴ�Ԫ
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , letterStart , letterEnd - letterStart + 1 , Lexeme.TYPE_LETTER);
				context.addLexeme(newLexeme);
			}
			//���õ�ǰ�ִ���״̬Ϊ��������
			letterStart = -1;
			letterEnd = -1;
		}
		
		//�ж��Ƿ�����������
		if(letterStart == -1 && letterEnd == -1){
			//�Ի���������
			needLock = false;
		}else{
			needLock = true;
		}
		return needLock;			
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private boolean isLetterConnector(char input){
		for(char c : Sign_Connector){
			if(c == input){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �ж�char�Ƿ��ǿɽ��ܵ���ʼ�ӷ�
	 * @return
	 */
	private boolean isAcceptedCharStart(char input){
		return CharacterHelper.isEnglishLetter(input) 
				|| CharacterHelper.isArabicNumber(input);
	}
	
	/**
	 * �ж�char�Ƿ��ǿɽ��ܵ��ַ�
	 * @return
	 */
	private boolean isAcceptedChar(char input){
		return isLetterConnector(input) 
				|| CharacterHelper.isEnglishLetter(input) 
				|| CharacterHelper.isArabicNumber(input);
	}

	public void reset() {
		start = -1;
		end = -1;
		letterStart = -1;
		letterEnd = -1;
//		numberStart = -1;
//		numberEnd = -1;		
	}


	public String getLanType() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
