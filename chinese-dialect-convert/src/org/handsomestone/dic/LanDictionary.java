/**
 * 
 */
package org.handsomestone.dic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.FilenameFilter;

import org.wltea.analyzer.dic.DictSegment;
import org.wltea.analyzer.dic.Hit;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.Seg;

/**
 * Language Analyzer v1.0
 * �ʵ������,����ģʽ
 * @author ʯ����
 *
 */
public class LanDictionary {
	/*
	 * �ִ���Ĭ���ֵ�·�� 
	 */
	
	public static final String PATH_DIC_SICHUAN = "/org/handsomestone/dic/sichuan.dic";
	public static final String PATH_DIC_SHANXI = "/org/handsomestone/dic/shanxi.dic";

	private File dicPath;//��ȡ��չ�ļ�·��
	private static File defalutPath = null;//Ĭ���ļ�

	
	/*
	 * �ʵ䵥��ʵ��
	 */
	private static final LanDictionary singleton;
	
	/*
	 * �ʵ��ʼ��
	 */
	static{
		singleton = new LanDictionary();
	}
	/*
	 * �Ĵ��ʵ����
	 */
	private DictSegment _SichuanDict;
	/*
	 * �����ʵ����
	 */	
	private DictSegment _ShanxiDict;
	/*
	 * mmseg4j�ʵ����ʵ��
	 */
	protected static Dictionary dic;
	private LanDictionary(){
		//��ʼ��ϵͳ�ʵ�
		loadSichuanDict(); //����¼���Ĵ����ʵ� 
		loadShanxiDict();//����¼���������ʵ�
	/*	dic = Dictionary.getInstance();*/
	
	}

	
	
	protected File[] listWordsFiles(final String lanName) {
		return dicPath.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				
				return name.startsWith(lanName+"_ext") && name.endsWith(".dic");
			}
			
		});
	}
	/**
	 * �����������ʵ估����չ�ʵ�
	 */	
	 private void loadShanxiDict(){
		//����һ�����ʵ�ʵ��
		_ShanxiDict = new DictSegment((char)0);
		//��ȡ���ʵ��ļ�
        InputStream is = LanDictionary.class.getResourceAsStream(LanDictionary.PATH_DIC_SHANXI);
        if(is == null){
        	throw new RuntimeException("Shanxi LanDictionary not found!!!");
        }
        
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_ShanxiDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Shanxi LanDictionary loading exception.");
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
		//������չ�ʵ���ļ�λ����Ҫ��dataĿ¼
		dicPath = getDefalutPath();
		File[] words = listWordsFiles("shanxi");
		
		//������չ�ʵ�����
		//List<String> extDictFiles  = Configuration.getExtLanDictionarys();
		if(words != null){
			for(File wordsFile : words){
				//��ȡ��չ�ʵ��ļ�
				try {
					is = new FileInputStream(wordsFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
							_ShanxiDict.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension LanDictionary loading exception.");
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
	 * �����Ĵ����ʵ估����չ�ʵ�
	 */
	private void loadSichuanDict(){
		//����һ�����ʵ�ʵ��
		_SichuanDict = new DictSegment((char)0);
		//��ȡ���ʵ��ļ�
        InputStream is = LanDictionary.class.getResourceAsStream(LanDictionary.PATH_DIC_SICHUAN);
        if(is == null){
        	throw new RuntimeException("Sichuan LanDictionary not found!!!");
        }
        
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
				//	_SichuanDict.fillSegment(theWord.trim().toCharArray());
					//System.out.println(theWord);
					//System.out.print(theWord.trim().toCharArray());
					_SichuanDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Sichuan LanDictionary loading exception.");
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
		//������չ�ʵ���ļ�λ����Ҫ��dataĿ¼
		dicPath = getDefalutPath();
		File[] words = listWordsFiles("sichuan");
		
		//������չ�ʵ�����
		//List<String> extDictFiles  = Configuration.getExtLanDictionarys();
		if(words != null){
			for(File wordsFile : words){
				//��ȡ��չ�ʵ��ļ�
				try {
					is = new FileInputStream(wordsFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
							_SichuanDict.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension LanDictionary loading exception.");
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
	 * ����IK Analyzer�Ĵʵ����LanDictionary��ľ�̬�������дʵ��ʼ��
	 * ֻ�е�LanDictionary�౻ʵ�ʵ���ʱ���ŻῪʼ����ʵ䣬
	 * �⽫�ӳ��״ηִʲ�����ʱ��
	 * �÷����ṩ��һ����Ӧ�ü��ؽ׶ξͳ�ʼ���ֵ���ֶ�
	 * ���������״ηִ�ʱ��ʱ��
	 * @return LanDictionary
	 */
	public static LanDictionary getInstance(){
		return LanDictionary.singleton;
	}
	/**
	 * ����ƥ���������Դʵ�
	 * @param charArray
	 * @return Hit ƥ��������
	 */
	public static Hit matchInShanxiDict(char[] charArray){
		return singleton._ShanxiDict.match(charArray);
	}	
	/**
	 * ����ƥ���������Դʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInShanxiDict(char[] charArray , int begin, int length){
		return singleton._ShanxiDict.match(charArray, begin, length);
	}
	
	
	/**
	 * ����ƥ���Ĵ����Դʵ�
	 * @param charArray
	 * @return Hit ƥ��������
	 */
	public static Hit matchInSichuanDict(char[] charArray){
		return singleton._SichuanDict.match(charArray);
	}
	/**
	 * ����ƥ���Ĵ����Դʵ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public static Hit matchInSichuanDict(char[] charArray , int begin, int length){
		return singleton._SichuanDict.match(charArray, begin, length);
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
	
	public static Seg getSeg()
	{
		return new ComplexSeg(dic);
	}
	
	public static File getDefalutPath() {
		if(defalutPath == null) {
			String defPath = System.getProperty("handsomestone.dic.path");
			//log.info("look up in mmseg.dic.path="+defPath);
			System.out.println("look up in handsomestone.dic.path="+defPath);
			if(defPath == null) {
				URL url = LanDictionary.class.getClassLoader().getResource("LanData");
				if(url != null) {
					defPath = url.getFile();
					//log.info("look up in classpath="+defPath);
					System.out.println("look up in classpath="+defPath);
				} else {
					defPath = System.getProperty("user.dir")+"/data";
					//log.info("look up in user.dir="+defPath);
					System.out.println("look up in user.dir="+defPath);
				}
				
			}
			
			defalutPath = new File(defPath);
			if(!defalutPath.exists()) {
				//log.warning("defalut dic path="+defalutPath+" not exist");
				System.out.println("defalut dic path="+defalutPath+" not exist");
			}
		}
		return defalutPath;
	}
	public static void main(String args[])
	{
		LanDictionary dic = new LanDictionary();
		dic.loadSichuanDict();
	}
}

