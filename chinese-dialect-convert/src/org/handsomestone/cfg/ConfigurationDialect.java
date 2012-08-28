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
 * 简单的配置管理类,单子模式
 * @author 石俊杰
 *
 */
public class ConfigurationDialect {
	/*
	 * 分词器配置文件路径
	 */	
	//private static final String FILE_NAME = "/IKAnalyzer.cfg.xml";

	
	//private static final Configuration CFG = new Configuration();
	
	//private Properties props;

	/*
	 * 初始化配置文件
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
		//初始化词典单例
		LanDictionary.getInstance();
	}
	/**
	 * 初始化四川方言分词器实现
	 * 
	 * ISegmenter
	 */
	public static ISegmenter loadSegmenter_sichuan(){

		
		//处理四川方言的分词器
		ISegmenter segmenter = new SiChuanSegmenter();
		
		return segmenter;
	}
	/**
	 * 初始化陕西方言分词器实现
	 * 
	 * ISegmenter
	 */
	public static ISegmenter loadSegmenter_shanxi(){

		
		//处理陕西方言的分词器
		ISegmenter segmenter = new ShanXiSegmenter();
		
		return segmenter;
	}
	
	
		
	
	/**
	 * 初始化子分词器实现
	 * （目前暂时不考虑配置扩展）
	 * @return List<ISegmenter>
	 */
	public static List<ISegmenter> loadSegmenter(){
		//初始化词典单例
		LanDictionary.getInstance();
		Dictionary.getInstance();
		List<ISegmenter> segmenters = new ArrayList<ISegmenter>(4);
		//处理数量词的子分词器
		segmenters.add(new QuantifierSegmenter());
		//处理中文词的子分词器
		segmenters.add(new CJKSegmenter());
		//处理字母的子分词器
		segmenters.add(new LetterSegmenter()); 
		return segmenters;
	}
}

