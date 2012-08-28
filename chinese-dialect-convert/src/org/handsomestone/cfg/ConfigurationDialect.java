/**
 * 
 */
package org.handsomestone.cfg;


import java.util.ArrayList;
import java.util.List;


import org.handsomestone.dic.LanDictionary;
import org.handsomestone.seg.ShanXiSegmenter;
import org.handsomestone.seg.SiChuanSegmenter;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.seg.CJKSegmenter;
import org.wltea.analyzer.seg.ISegmenter;
import org.wltea.analyzer.seg.LetterSegmenter;
import org.wltea.analyzer.seg.QuantifierSegmenter;


/**
 * Language Analyzer v1.0
 * �򵥵����ù�����,����ģʽ
 * @author ʯ����
 *
 */
public class ConfigurationDialect {
	/*
	 * �ִ��������ļ�·��
	 */	
	//private static final String FILE_NAME = "/IKAnalyzer.cfg.xml";

	
	//private static final Configuration CFG = new Configuration();
	
	//private Properties props;

	/*
	 * ��ʼ�������ļ�
	 */
	/*
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
	*/
	public static void initLanDictionary()
	{
		//��ʼ���ʵ䵥��
		LanDictionary.getInstance();
	}
	/**
	 * ��ʼ���Ĵ����Էִ���ʵ��
	 * 
	 * ISegmenter
	 */
	public static ISegmenter loadSegmenter_sichuan(){

		
		//�����Ĵ����Եķִ���
		ISegmenter segmenter = new SiChuanSegmenter();
		
		return segmenter;
	}
	/**
	 * ��ʼ���������Էִ���ʵ��
	 * 
	 * ISegmenter
	 */
	public static ISegmenter loadSegmenter_shanxi(){

		
		//�����������Եķִ���
		ISegmenter segmenter = new ShanXiSegmenter();
		
		return segmenter;
	}
	
	
		
	
	/**
	 * ��ʼ���ӷִ���ʵ��
	 * ��Ŀǰ��ʱ������������չ��
	 * @return List<ISegmenter>
	 */
	public static List<ISegmenter> loadSegmenter(){
		//��ʼ���ʵ䵥��
		LanDictionary.getInstance();
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

