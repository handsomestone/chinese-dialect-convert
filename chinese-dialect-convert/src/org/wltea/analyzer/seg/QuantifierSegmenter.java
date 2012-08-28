/**
 * 
 */
package org.wltea.analyzer.seg;

import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * �������ӷִ���������һ�·�Χ
 * 1.���������֣�����������+��������
 * 2.��������+��������
 * 3.ʱ��,����
 * 4.��������
 * 5.��ѧ���� % . / 
 * @author ������
 *
 */
public class QuantifierSegmenter implements ISegmenter {

	//����������ǰ׺�����ҷ��ţ�
//	public static String Arabic_Num_Pre = "-+$��";//Apre
//	private static Set<Character> ArabicNumPreChars = new HashSet<Character>();
//	static{
//		char[] ca = Arabic_Num_Pre.toCharArray();
//		for(char nChar : ca){
//			ArabicNumPreChars.add(nChar);
//		}
//	}
//	public static final int NC_ANP = 01;	
	//����������0-9
	public static final int NC_ARABIC = 02;
	//�������������ӷ���
	public static String Arabic_Num_Mid = ",./:Ee";//Amid
	private static Set<Character> ArabicNumMidChars = new HashSet<Character>();
	static{
		char[] ca = Arabic_Num_Mid.toCharArray();
		for(char nChar : ca){
			ArabicNumMidChars.add(nChar);
		}
	}
	public static final int NC_ANM = 03;
//	//���������ʺ�׺
//	public static String Arabic_Num_End = "%��";//Aend
//	public static final int NC_ANE = 04;
	
	//�����ʣ�����ǰ׺��
	public static String Num_Pre = "��";//Cpre
	public static final int NC_NP = 11;
	//��������
	public static String Chn_Num = "��һ�������������߰˾�ʮ��Ҽ��������½��ƾ�ʰ��ǧ����ʰ��Ǫ�f�|��ئإ";//Cnum
	private static Set<Character> ChnNumberChars = new HashSet<Character>();
	static{
		char[] ca = Chn_Num.toCharArray();
		for(char nChar : ca){
			ChnNumberChars.add(nChar);
		}
	}
	public static final int NC_CHINESE = 12;
	//�����������ӷ�
	public static String Chn_Num_Mid = "��";//Cmid
	public static final int NC_CNM = 13;
	
	//Լ���ʣ����ʽ�β��
	public static String Num_End = "�������";//Cend
	private static Set<Character> NumEndChars = new HashSet<Character>();
	static{
		char[] ca = Num_End.toCharArray();
		for(char nChar : ca){
			NumEndChars.add(nChar);
		}
	}
	public static final int NC_NE = 14;
	
//	//GB���е������ַ�(��ʼ���м䡢����)
//	public static String Rome_Num = "�����������������"; //Rnum
//	private static Set<Character> RomeNumChars = new HashSet<Character>();
//	static{
//		char[] ca = Rome_Num.toCharArray();
//		for(char nChar : ca){
//			RomeNumChars.add(nChar);
//		}
//	}
//	public static final int NC_ROME = 22;

	//�������ַ�
	public static final int NaN = -99;
	
	//���еĿ�������
	private static Set<Character> AllNumberChars = new HashSet<Character>(256);
	static{
		char[] ca = null;
		
//		AllNumberChars.addAll(ArabicNumPreChars);

		for(char nChar = '0' ; nChar <='9' ; nChar++ ){
			AllNumberChars.add(nChar);
		}
		
		AllNumberChars.addAll(ArabicNumMidChars);
		
//		ca = Arabic_Num_End.toCharArray();
//		for(char nChar : ca){
//			AllNumberChars.add(nChar);
//		}
		
		ca = Num_Pre.toCharArray();
		for(char nChar : ca){
			AllNumberChars.add(nChar);
		}
		
		AllNumberChars.addAll(ChnNumberChars);
		
		ca = Chn_Num_Mid.toCharArray();
		for(char nChar : ca){
			AllNumberChars.add(nChar);
		}

		AllNumberChars.addAll(NumEndChars);
		
//		AllNumberChars.addAll(RomeNumChars);
		
	}
	
	/*
	 * ��Ԫ�Ŀ�ʼλ�ã�
	 * ͬʱ��Ϊ�ӷִ���״̬��ʶ
	 * ��start > -1 ʱ����ʶ��ǰ�ķִ������ڴ����ַ�
	 */
	private int nStart;
	/*
	 * ��¼��Ԫ����λ��
	 * end��¼�����ڴ�Ԫ�����һ�����ֵĺ�������ʽ���
	 */
	private int nEnd;
	/*
	 * ��ǰ���ʵ�״̬ 
	 */
	private int nStatus;
	/*
	 * ����һ������
	 */
	private boolean fCaN;	
	
	/*
	 * ������ʼλ��
	 */
	private int countStart;
	/*
	 * ������ֹλ��
	 */
	private int countEnd;
	

	
	public QuantifierSegmenter(){
		nStart = -1;
		nEnd = -1;
		nStatus = NaN;
		fCaN = false;
		
		countStart = -1;
		countEnd = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public void nextLexeme(char[] segmentBuff , Context context) {
		fCaN = false;
		//���ʴ�����
		processNumber(segmentBuff , context);
		
		//���ʴ�����		
		if(countStart == -1){//δ��ʼ��������
			//��ǰ�α��λ�ý���������
			if((fCaN && nStart == -1)
					|| (nEnd != -1 && nEnd == context.getCursor() - 1)//����CNM��״̬
					){				
				//���ʴ���
				processCount(segmentBuff , context);
			
			}
		}else{//�ѿ�ʼ��������
			//���ʴ���
			processCount(segmentBuff , context);
		}

		//�ж��Ƿ�����������
		if(this.nStart == -1 && this.nEnd == -1 && NaN == this.nStatus
				&& this.countStart == -1 && this.countEnd == -1){
			//�Ի���������
			context.unlockBuffer(this);
		}else{
			context.lockBuffer(this);
		}
	}

	/**
	 * ���ʴ���
	 * @param segmentBuff
	 * @param context
	 */
	private void processNumber(char[] segmentBuff , Context context){		
		//�����ַ�ʶ��
		int inputStatus = nIdentify(segmentBuff , context);
		
		if(NaN == nStatus){
			//��ǰ�ķִ�����δ��ʼ�����ַ�
			onNaNStatus(inputStatus , context);
			
//		}else if(NC_ANP == nStatus){ 
//			//��ǰΪ����������ǰ׺	
//			onANPStatus(inputStatus , context);
			
		}else if(NC_ARABIC == nStatus){
			//��ǰΪ����������
			onARABICStatus(inputStatus , context);
			
		}else if(NC_ANM	== nStatus){
			//��ǰΪ�������������ӷ�
			onANMStatus(inputStatus , context);
			
//		}else if(NC_ANE == nStatus){
//			//��ǰΪ���������ֽ�����
//			onANEStatus(inputStatus , context);
			
		}else if(NC_NP == nStatus){
			//��ǰΪ��������ǰ׺
			onNPStatus(inputStatus , context);
			
		}else if(NC_CHINESE == nStatus){
			//��ǰΪ��������
			onCHINESEStatus(inputStatus , context);
			
		}else if(NC_CNM == nStatus){
			//��ǰΪ�����������ӷ�
			onCNMStatus(inputStatus , context);
			
		}else if(NC_NE == nStatus){
			//��ǰΪ�������ֽ�����
			onCNEStatus(inputStatus , context);
			
//		}else if(NC_ROME == nStatus){
//			//��ǰΪ��������
//			onROMEStatus(inputStatus , context);			
			
		}
		
		//�������������һ���ַ���������δ���������
		if(context.getCursor() == context.getAvailable() - 1){
			if(nStart != -1 && nEnd != -1){
				//�������
				outputNumLexeme(context);
			}
			//��������״̬
			nReset();
		}				
	}
	
	/**
	 * ��ǰΪNaN״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onNaNStatus(int inputStatus ,  Context context){
		if(NaN == inputStatus){
			return;
			
		}else if(NC_NP == inputStatus){//��������ǰ׺
			//��¼��ʼλ��
			nStart = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;	
			
		}else if(NC_CHINESE == inputStatus){//��������
			//��¼��ʼλ��
			nStart = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else if(NC_NE == inputStatus){//�������ʺ�׺
			//��¼��ʼλ��
			nStart = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
//		}else if(NC_ANP == inputStatus){//����������ǰ׺
//			//��¼��ʼλ��
//			nStart = context.getCursor();
//			//��¼��ǰ���ַ�״̬
//			nStatus = inputStatus;
			
		}else if(NC_ARABIC == inputStatus){//����������
			//��¼��ʼλ��
			nStart = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
//		}else if(NC_ROME == inputStatus){//��������
//			//��¼��ʼλ��
//			nStart = context.getCursor();
//			//��¼��ǰ���ַ�״̬
//			nStatus = inputStatus;
//			//��¼���ܵĽ���λ��
//			nEnd = context.getCursor();	
		
		}else{
			//��NC_ANM ��NC_ANE��NC_CNM ��������
		}
	}
	
	
	/**
	 * ��ǰΪANP״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
//	private void onANPStatus(int inputStatus ,  Context context){
//		if(NC_ARABIC == inputStatus){//����������
//			//��¼��ǰ���ַ�״̬
//			nStatus = inputStatus;
//			//��¼���ܵĽ���λ��
//			nEnd = context.getCursor();
//			
//		}else{
//			//������ܵ�����
//			outputNumLexeme(context);
//			//��������״̬
//			nReset();
//			//�����ʼ̬���д���
//			onNaNStatus(inputStatus , context);
//			
//		}
//	}
	
	
	/**
	 * ��ǰΪARABIC״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onARABICStatus(int inputStatus ,  Context context){
		if(NC_ARABIC == inputStatus){//����������
			//����״̬����
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else if(NC_ANM == inputStatus){//�������������ӷ�
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			
//		}else if(NC_ANE == inputStatus){//���������ֺ�׺
//			//��¼��ǰ���ַ�״̬
//			nStatus = inputStatus;
//			//��¼���ܵĽ���λ��
//			nEnd = context.getCursor();
//			//�������
//			outputNumLexeme(context);
//			//��������״̬
//			nReset();
		}else if(NC_CHINESE == inputStatus){//��������
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			
		}else if(NC_NE == inputStatus){//Լ����
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			
		}else{
			//�������
			outputNumLexeme(context);
			//��������״̬
			nReset();
			//�����ʼ̬���д���
			onNaNStatus(inputStatus , context);	
			
		}
		
	}
	
	/**
	 * ��ǰΪANM״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onANMStatus(int inputStatus ,  Context context){
		if (NC_ARABIC == inputStatus){//����������
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
//		}else if (NC_ANP == inputStatus){//����������ǰ׺
//			//��¼��ǰ���ַ�״̬
//			nStatus = inputStatus;
			
		}else{
			//������ܴ��ڵ�����
			outputNumLexeme(context);
			//��������״̬
			nReset();
			//�����ʼ̬���д���
			onNaNStatus(inputStatus , context);
			
		}		
	}

	
	/**
	 * ��ǰΪANE״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
//	private void onANEStatus(int inputStatus ,  Context context){
//		//������ܴ��ڵ�����
//		outputNumLexeme(context);
//		//��������״̬
//		nReset();
//		//�����ʼ̬���д���
//		onNaNStatus(inputStatus , context);
//				
//	}	
	
	
	/**
	 *  ��ǰΪNP״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onNPStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//��������
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;

			
		}else if(NC_ARABIC == inputStatus){//����������
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			
//		}else if(NC_ROME == inputStatus){//��������
//			//��¼���ܵĽ���λ��
//			nEnd = context.getCursor() - 1;
//			//������ܴ��ڵ�����
//			outputNumLexeme(context);
//			//��������״̬
//			nReset();
//			//�����ʼ̬���д���
//			onNaNStatus(inputStatus , context);	
			
		}else{
			//��������״̬
			nReset();
			//�����ʼ̬���д���
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * ��ǰΪCHINESE״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onCHINESEStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//��������
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else if(NC_CNM == inputStatus){//�����������ӷ�
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			
		}else if(NC_NE == inputStatus){//�������ֽ�����
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else{//��������
			//������ܴ��ڵ�����
			outputNumLexeme(context);
			//��������״̬
			nReset();
			//�����ʼ̬���д���
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * ��ǰΪCNM״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNMStatus(int inputStatus ,  Context context){
		if(NC_CHINESE == inputStatus){//��������
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else if(NC_NE == inputStatus){//�������ֽ�����
			//��¼��ǰ���ַ�״̬
			nStatus = inputStatus;
			//��¼���ܵĽ���λ��
			nEnd = context.getCursor();
			
		}else{//��������
			//������ܴ��ڵ�����
			outputNumLexeme(context);
			//��������״̬
			nReset();
			//�����ʼ̬���д���
			onNaNStatus(inputStatus , context);
			
		}		
	}
	
	/**
	 * ��ǰΪCNE״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNEStatus(int inputStatus ,  Context context){
		//������ܴ��ڵ�����
		outputNumLexeme(context);
		//��������״̬
		nReset();
		//�����ʼ̬���д���
		onNaNStatus(inputStatus , context);
				
	}
	
	/**
	 * ��ǰΪROME״̬ʱ��״̬���Ĵ���(״̬ת��)
	 * @param inputStatus
	 * @param context
	 */
//	private void onROMEStatus(int inputStatus ,  Context context){
//		if(NC_ROME == inputStatus){//��������
//			//��¼���ܵĽ���λ��
//			nEnd = context.getCursor();
//			
//		}else{//��������
//			//������ܴ��ڵ�����
//			outputNumLexeme(context);
//			//��������״̬
//			nReset();
//			//�����ʼ̬���д���
//			onNaNStatus(inputStatus , context);
//			
//		}
//	}
	
	/**
	 * ������ʴ�Ԫ�������
	 * @param context
	 */
	private void outputNumLexeme(Context context){
		if(nStart > -1 && nEnd > -1){
			//�������зֵĴ�Ԫ
			Lexeme newLexeme = new Lexeme(context.getBuffOffset() ,nStart , nEnd - nStart + 1 , Lexeme.TYPE_NUM );
			context.addLexeme(newLexeme);
			fCaN = true;
		}
	}
	
	/**
	 * ������ʴ�Ԫ�������
	 * @param context
	 */
	private void outputCountLexeme(Context context){
		if(countStart > -1 && countEnd > -1){
			//�������зֵĴ�Ԫ
			Lexeme countLexeme = new Lexeme(context.getBuffOffset() ,countStart , countEnd - countStart + 1 , Lexeme.TYPE_NUMCOUNT);
			context.addLexeme(countLexeme);
		}

	}	
	
	/**
	 * �������ʵ�״̬
	 */
	private void nReset(){
		this.nStart = -1;
		this.nEnd = -1;
		this.nStatus = NaN;
	}
	
	/**
	 * ʶ�������ַ�����
	 * @param input
	 * @return
	 */
	private int nIdentify(char[] segmentBuff , Context context){
		
		//��ȡ��ǰλ�õ�char	
		char input = segmentBuff[context.getCursor()];
		
		int type = NaN;
		if(!AllNumberChars.contains(input)){
			return type;
		}
		
		if(CharacterHelper.isArabicNumber(input)){
			type = NC_ARABIC;
			 
		}else if(ChnNumberChars.contains(input)){
			type = NC_CHINESE;
			
		}else if(Num_Pre.indexOf(input) >= 0){
			type = NC_NP;
			
		}else if(Chn_Num_Mid.indexOf(input) >= 0){
			type = NC_CNM;
			
		}else if(NumEndChars.contains(input)){
			type = NC_NE;
			
//		}else if(ArabicNumPreChars.contains(input)){
//			type = NC_ANP;
			
		}else if(ArabicNumMidChars.contains(input)){
			type = NC_ANM;
			
//		}else if(Arabic_Num_End.indexOf(input) >= 0){
//			type = NC_ANE;
//			
//		}else if(RomeNumChars.contains(input)){
//			type = NC_ROME;

		}
		return type;
	}

	/**
	 * ������������
	 * @param segmentBuff
	 * @param context
	 */
	private void processCount(char[] segmentBuff , Context context){
		Hit hit = null;

		if(countStart == -1){
			hit = Dictionary.matchInQuantifierDict(segmentBuff , context.getCursor() , 1);
		}else{
			hit = Dictionary.matchInQuantifierDict(segmentBuff , countStart , context.getCursor() - countStart + 1);
		}
		
		if(hit != null){
			if(hit.isPrefix()){
				if(countStart == -1){
					//�������ʵĿ�ʼ
					countStart = context.getCursor();
				}
			}
			
			if(hit.isMatch()){
				if(countStart == -1){
					countStart = context.getCursor();
				}
				//�������ʿ��ܵĽ���
				countEnd = context.getCursor();
				//������ܴ��ڵ�����
				outputCountLexeme(context);
			}
			
			if(hit.isUnmatch()){
				if(countStart != -1){
					//��������״̬
					countStart = -1;
					countEnd = -1;
				}
			}
		}
		
		//�������������һ���ַ���������δ���������
		if(context.getCursor() == context.getAvailable() - 1){
			//��������״̬
			countStart = -1;
			countEnd = -1;
		}
	}

	public void reset() {
		nStart = -1;
		nEnd = -1;
		nStatus = NaN;
		fCaN = false;
		
		countStart = -1;
		countEnd = -1;
	}

	
	public String getLanType() {
	
		return null;
	}

}
