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
 * 词典管理类,单子模式
 * @author 石俊杰
 *
 */
public class LanDictionary {
	/*
	 * 分词器默认字典路径 
	 */
	
	public static final String PATH_DIC_SICHUAN = "/org/handsomestone/dic/sichuan.dic";
	public static final String PATH_DIC_SHANXI = "/org/handsomestone/dic/shanxi.dic";

	private File dicPath;//获取扩展文件路径
	private static File defalutPath = null;//默认文件

	
	/*
	 * 词典单子实例
	 */
	private static final LanDictionary singleton;
	
	/*
	 * 词典初始化
	 */
	static{
		singleton = new LanDictionary();
	}
	/*
	 * 四川词典对象
	 */
	private DictSegment _SichuanDict;
	/*
	 * 陕西词典对象
	 */	
	private DictSegment _ShanxiDict;
	/*
	 * mmseg4j词典对象实例
	 */
	protected static Dictionary dic;
	private LanDictionary(){
		//初始化系统词典
		loadSichuanDict(); //用于录入四川话词典 
		loadShanxiDict();//用于录入陕西话词典
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
	 * 加载陕西话词典及其扩展词典
	 */	
	 private void loadShanxiDict(){
		//建立一个主词典实例
		_ShanxiDict = new DictSegment((char)0);
		//读取主词典文件
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
		//加载扩展词典读文件位置主要是data目录
		dicPath = getDefalutPath();
		File[] words = listWordsFiles("shanxi");
		
		//加载扩展词典配置
		//List<String> extDictFiles  = Configuration.getExtLanDictionarys();
		if(words != null){
			for(File wordsFile : words){
				//读取扩展词典文件
				try {
					is = new FileInputStream(wordsFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//如果找不到扩展的字典，则忽略
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//加载扩展词典数据到主内存词典中
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
	 * 加载四川话词典及其扩展词典
	 */
	private void loadSichuanDict(){
		//建立一个主词典实例
		_SichuanDict = new DictSegment((char)0);
		//读取主词典文件
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
		//加载扩展词典读文件位置主要是data目录
		dicPath = getDefalutPath();
		File[] words = listWordsFiles("sichuan");
		
		//加载扩展词典配置
		//List<String> extDictFiles  = Configuration.getExtLanDictionarys();
		if(words != null){
			for(File wordsFile : words){
				//读取扩展词典文件
				try {
					is = new FileInputStream(wordsFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//如果找不到扩展的字典，则忽略
				if(is == null){
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							//加载扩展词典数据到主内存词典中
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
	 * 词典初始化
	 * 由于IK Analyzer的词典采用LanDictionary类的静态方法进行词典初始化
	 * 只有当LanDictionary类被实际调用时，才会开始载入词典，
	 * 这将延长首次分词操作的时间
	 * 该方法提供了一个在应用加载阶段就初始化字典的手段
	 * 用来缩短首次分词时的时延
	 * @return LanDictionary
	 */
	public static LanDictionary getInstance(){
		return LanDictionary.singleton;
	}
	/**
	 * 检索匹配陕西方言词典
	 * @param charArray
	 * @return Hit 匹配结果描述
	 */
	public static Hit matchInShanxiDict(char[] charArray){
		return singleton._ShanxiDict.match(charArray);
	}	
	/**
	 * 检索匹配陕西方言词典
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit 匹配结果描述
	 */
	public static Hit matchInShanxiDict(char[] charArray , int begin, int length){
		return singleton._ShanxiDict.match(charArray, begin, length);
	}
	
	
	/**
	 * 检索匹配四川方言词典
	 * @param charArray
	 * @return Hit 匹配结果描述
	 */
	public static Hit matchInSichuanDict(char[] charArray){
		return singleton._SichuanDict.match(charArray);
	}
	/**
	 * 检索匹配四川方言词典
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit 匹配结果描述
	 */
	public static Hit matchInSichuanDict(char[] charArray , int begin, int length){
		return singleton._SichuanDict.match(charArray, begin, length);
	}
	
	
	/**
	 * 检索匹配主词典,
	 * 从已匹配的Hit中直接取出DictSegment，继续向下匹配
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

