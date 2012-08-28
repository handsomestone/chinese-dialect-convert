/**
 * 
 */
package org.wltea.analyzer.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.seg.CJKSegmenter;
import org.wltea.analyzer.seg.ISegmenter;
import org.wltea.analyzer.seg.LetterSegmenter;
import org.wltea.analyzer.seg.QuantifierSegmenter;

/**
 * IK Analyzer v3.2
 * �򵥵����ù�����,����ģʽ
 * @author ������
 *
 */
public class Configuration {
	/*
	 * �ִ��������ļ�·��
	 */	
	private static final String FILE_NAME = "/IKAnalyzer.cfg.xml";
	//�������ԡ�����չ�ֵ�
	private static final String EXT_DICT = "ext_dict";
	//�������ԡ�����չֹͣ�ʵ�
	private static final String EXT_STOP = "ext_stopwords";
	
	private static final Configuration CFG = new Configuration();
	
	private Properties props;
	
	/*
	 * ��ʼ�������ļ�
	 */
	private Configuration(){
		
		props = new Properties();
		
		InputStream input = Configuration.class.getResourceAsStream(FILE_NAME);
		if(input != null){
			try {
				props.loadFromXML(input);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ȡ��չ�ֵ�����·��
	 * @return List<String> ������������·��
	 */
	public static List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<String>(2);
		String extDictCfg = CFG.props.getProperty(EXT_DICT);
		if(extDictCfg != null){
			//ʹ��;�ָ�����չ�ֵ�����
			String[] filePaths = extDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extDictFiles.add(filePath.trim());
						//System.out.println(filePath.trim());
					}
				}
			}
		}		
		return extDictFiles;		
	}
	
	/**
	 * ��ȡ��չֹͣ�ʵ�����·��
	 * @return List<String> ������������·��
	 */
	public static List<String> getExtStopWordDictionarys(){
		List<String> extStopWordDictFiles = new ArrayList<String>(2);
		String extStopWordDictCfg = CFG.props.getProperty(EXT_STOP);
		if(extStopWordDictCfg != null){
			//ʹ��;�ָ�����չ�ֵ�����
			String[] filePaths = extStopWordDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extStopWordDictFiles.add(filePath.trim());
						//System.out.println(filePath.trim());
					}
				}
			}
		}		
		return extStopWordDictFiles;		
	}
		
	
	/**
	 * ��ʼ���ӷִ���ʵ��
	 * ��Ŀǰ��ʱ������������չ��
	 * @return List<ISegmenter>
	 */
	public static List<ISegmenter> loadSegmenter(){
		//��ʼ���ʵ䵥��
		Dictionary.getInstance();
		List<ISegmenter> segmenters = new ArrayList<ISegmenter>(4);
		//���������ʵ��ӷִ���
		segmenters.add(new QuantifierSegmenter());
		//�������Ĵʵ��ӷִ���
		segmenters.add(new CJKSegmenter());
		//������ĸ���ӷִ���
		segmenters.add(new LetterSegmenter()); 
		return segmenters;
	}
}

