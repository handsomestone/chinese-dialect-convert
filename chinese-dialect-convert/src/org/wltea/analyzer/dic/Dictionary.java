/**
 * 
 */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;

/**
 * IK Analyzer v3.2
 * �ʵ������,����ģʽ
 * @author ������
 *
 */
public class Dictionary {
	/*
	 * �ִ���Ĭ���ֵ�·�� 
	 */
	public static final String PATH_DIC_MAIN = "/org/wltea/analyzer/dic/main.dic";
	public static final String PATH_DIC_SURNAME = "/org/wltea/analyzer/dic/surname.dic";
	public static final String PATH_DIC_QUANTIFIER = "/org/wltea/analyzer/dic/quantifier.dic";
	public static final String PATH_DIC_SUFFIX = "/org/wltea/analyzer/dic/suffix.dic";
	public static final String PATH_DIC_PREP = "/org/wltea/analyzer/dic/preposition.dic";
	public static final String PATH_DIC_STOP = "/org/wltea/analyzer/dic/stopword.dic";
	
	
	/*
	 * �ʵ䵥��ʵ��
	 */
	private static  final Dictionary singleton;
	
	/*
	 * �ʵ��ʼ��
	 */
	static{
		singleton = new Dictionary();
	}
	
	/*
	 * ���ʵ����
	 */
	private DictSegment _MainDict;
	/*
	 * ���ϴʵ�
	 */
	private DictSegment _SurnameDict;
	/*
	 * ���ʴʵ�
	 */
	private DictSegment _QuantifierDict;
	/*
	 * ��׺�ʵ�
	 */
	private DictSegment _SuffixDict;
	/*
	 * ���ʣ���ʴʵ�
	 */
	private DictSegment _PrepDict;
	/*
	 * ֹͣ�ʼ���
	 */
	private DictSegment _StopWords;
	
	private Dictionary(){
		//��ʼ��ϵͳ�ʵ�
		loadMainDict();
		loadSurnameDict();
		loadQuantifierDict();
		loadSuffixDict();
		loadPrepDict();
		loadStopWordDict();
	}

	/**
	 * �������ʵ估��չ�ʵ�
	 */
	private void loadMainDict(){
		//����һ�����ʵ�ʵ��
		_MainDict = new DictSegment((char)0);
		//��ȡ���ʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
        if(is == null){
        	throw new RuntimeException("Main Dictionary not found!!!");
        }
        
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_MainDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Main Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//������չ�ʵ�����
		List<String> extDictFiles  = Configuration.getExtDictionarys();
		if(extDictFiles != null){
			for(String extDictName : extDictFiles){
				//��ȡ��չ�ʵ��ļ�
				is = Dictionary.class.getResourceAsStream(extDictName);
				//����Ҳ�����չ���ֵ䣬�����
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//������չ�ʵ����ݵ����ڴ�ʵ���
							//System.out.println(theWord);
							_MainDict.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension Dictionary loading exception.");
					ioe.printStackTrace();
					
				}finally{
					try {
						if(is != null){
		                    is.close();
		                    is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	
	
	/**
	 * �������ϴʵ�
	 */
	private void loadSurnameDict(){
		//����һ�����ϴʵ�ʵ��
		_SurnameDict = new DictSegment((char)0);
		//��ȡ���ϴʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SURNAME);
        if(is == null){
        	throw new RuntimeException("Surname Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_SurnameDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Surname Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * �������ʴʵ�
	 */
	private void loadQuantifierDict(){
		//����һ�����ʵ�ʵ��
		_QuantifierDict = new DictSegment((char)0);
		//��ȡ���ʴʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_QUANTIFIER);
        if(is == null){
        	throw new RuntimeException("Quantifier Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_QuantifierDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Quantifier Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ���غ�׺�ʵ�
	 */
	private void loadSuffixDict(){
		//����һ����׺�ʵ�ʵ��
		_SuffixDict = new DictSegment((char)0);
		//��ȡ���ʴʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SUFFIX);
        if(is == null){
        	throw new RuntimeException("Suffix Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_SuffixDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Suffix Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}			

	/**
	 * ���ؽ��\���ʴʵ�
	 */
	private void loadPrepDict(){
		//����һ�����\���ʴʵ�ʵ��
		_PrepDict = new DictSegment((char)0);
		//��ȡ���ʴʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_PREP);
        if(is == null){
        	throw new RuntimeException("Preposition Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					//System.out.println(theWord);
					_PrepDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Preposition Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ����ֹͣ�ʴʵ�
	 */
	private void loadStopWordDict(){
		//����һ��ֹͣ�ʵ�ʵ��
		_StopWords = new DictSegment((char)0);
		//��ȡ���ʴʵ��ļ�
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_STOP);
        if(is == null){
        	throw new RuntimeException("Stopword Dictionary not found!!!");
        }
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_StopWords.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Stopword Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//������չֹͣ�ʵ�
		List<String> extStopWordDictFiles  = Configuration.getExtStopWordDictionarys();
		if(extStopWordDictFiles != null){
			for(String extStopWordDictName : extStopWordDictFiles){
				//��ȡ��չ�ʵ��ļ�
				is = Dictionary.class.getResourceAsStream(extStopWordDictName);
				//����Ҳ�����չ���ֵ䣬�����
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//System.out.println(theWord);
							//������չֹͣ�ʵ����ݵ��ڴ���
							_StopWords.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension Stop word Dictionary loading exception.");
					ioe.printStackTrace();
					
				}finally{
					try {
						if(is != null){
		                    is.close();
		                    is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}		
		
	}			
	
	/**
	 * �ʵ��ʼ��
	 * ����IK Analyzer�Ĵʵ����Dictionary��ľ�̬�������дʵ��ʼ��
	 * ֻ�е�Dictionary�౻ʵ�ʵ���ʱ���ŻῪʼ����ʵ䣬
	 * �⽫�ӳ��״ηִʲ�����ʱ��
	 * �÷����ṩ��һ����Ӧ�ü��ؽ׶ξͳ�ʼ���ֵ���ֶ�
	 * ���������״ηִ�ʱ��ʱ��
	 * @return Dictionary
	 */
	public static Dictionary getInstance(){
		return Dictionary.singleton;
	}
	
	/**
	 * ������չ�Ĵ���
	 * @param extWords Collection<String>�����б�
	 */
	public static void loadExtendWords(Collection<String> extWords){
		if(extWords != null){
			for(String extWord : extWords){
				if (extWord != null) {
					//������չ���������ڴ�ʵ���
					singleton._MainDict.fillSegment(extWord.trim().toCharArray());
				}
			}
		}
	}
	
	/**
	 * ������չ��ֹͣ����
	 * @param extStopWords Collection<String>�����б�
	 */
	public static void loadExtendStopWords(Collection<String> extStopWords){
		if(extStopWords != null){
			for(String extStopWord : extStopWords){
				if (extStopWord != null) {
					//������չ��ֹͣ����
					singleton._StopWords.fillSegment(extStopWord.trim().toCharArray());
				}
			}
		}
	}
	
	/**
	 * ����ƥ�����ʵ�
	 * @param charArray
	 * @return Hit ƥ��������
	 */
	public static Hit matchInMainDict(char[] charArray){
		return singleton._MainDict.match(charArray);
	}
	
	/**
	 * ����ƥ�����ʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInMainDict(char[] charArray , int begin, int length){
		return singleton._MainDict.match(charArray, begin, length);
	}
	
	/**
	 * ����ƥ�����ʵ�,
	 * ����ƥ���Hit��ֱ��ȡ��DictSegment����������ƥ��
	 * @param charArray
	 * @param currentIndex
	 * @param matchedHit
	 * @return Hit
	 */
	public static Hit matchWithHit(char[] charArray , int currentIndex , Hit matchedHit){
		DictSegment ds = matchedHit.getMatchedDictSegment();
		return ds.match(charArray, currentIndex, 1 , matchedHit);
	}

	/**
	 * ����ƥ�����ϴʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInSurnameDict(char[] charArray , int begin, int length){
		return singleton._SurnameDict.match(charArray, begin, length);
	}		
	
//	/**
//	 * 
//	 * �����ϴʵ���ƥ��ָ��λ�õ�char����
//	 * ���Դ�����ִ����к�׺ƥ�䣩
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean endsWithSurnameDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SurnameDict.match(charArray, begin + (length - i) , i);
//			if(hit.isMatch()){
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * ����ƥ�����ʴʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInQuantifierDict(char[] charArray , int begin, int length){
		return singleton._QuantifierDict.match(charArray, begin, length);
	}
	
	/**
	 * ����ƥ���ں�׺�ʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInSuffixDict(char[] charArray , int begin, int length){
		return singleton._SuffixDict.match(charArray, begin, length);
	}
	
//	/**
//	 * �ں�׺�ʵ���ƥ��ָ��λ�õ�char����
//	 * ���Դ�����ִ�����ǰ׺ƥ�䣩
//	 * @param charArray
//	 * @param begin
//	 * @param end
//	 * @return
//	 */
//	public static boolean startsWithSuffixDict(char[] charArray , int begin, int length){
//		Hit hit = null;
//		for(int i = 1 ; i <= length ; i++){
//			hit = singleton._SuffixDict.match(charArray, begin , i);
//			if(hit.isMatch()){
//				return true;
//			}else if(hit.isUnmatch()){
//				return false;
//			}
//		}
//		return false;
//	}
	
	/**
	 * ����ƥ���ʡ����ʴʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return  Hit ƥ��������
	 */
	public static Hit matchInPrepDict(char[] charArray , int begin, int length){
		return singleton._PrepDict.match(charArray, begin, length);
	}
	
	/**
	 * �ж��Ƿ���ֹͣ��
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return boolean
	 */
	public static boolean isStopWord(char[] charArray , int begin, int length){			
		return singleton._StopWords.match(charArray, begin, length).isMatch();
	}	
}
