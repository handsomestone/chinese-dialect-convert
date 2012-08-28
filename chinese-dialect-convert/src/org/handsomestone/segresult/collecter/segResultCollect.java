package org.handsomestone.segresult.collecter;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.handsomestone.cfg.ConfigurationDialect;
import org.handsomestone.mmeg4j.seg.mmseg4jSeg;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.ISegmenter;
public class segResultCollect {
	
	private   HashMap<String,List<Lexeme>> segResultCollector=new HashMap<String,List<Lexeme>>();
	private ISegmenter segmenter;
	private String txt;
	public segResultCollect(String txt,ISegmenter segmenter)
	{
		
		this.segmenter = segmenter;
		this.txt = txt;
	//	this.segResultCollector= 
	}
	public void start()
	{
		ISegmenter shanxisegmenter =  ConfigurationDialect.loadSegmenter_shanxi();

		new segResultProduce(shanxisegmenter).start();
		
		ISegmenter sichuansegmenter =  ConfigurationDialect.loadSegmenter_sichuan();

		new segResultProduce(sichuansegmenter).start();
	}
	//private static Object classLock = segResultCollect.class;
	public  void insertSegResult(String LanType,List<Lexeme> segResult)
	{
		//synchronized(classLock){
			
			if(segResultCollector.get(LanType)!=null)
			{
				segResultCollector.remove(LanType);
		
			}
			else
			{
				System.out.println("done");
				segResultCollector.put(LanType, segResult);
			}
		//}
	}
	public  List<Lexeme> getSegResult(String LanType) throws InterruptedException
	{
		//	synchronized(classLock){
			List<Lexeme> LanTypeResult = segResultCollector.get(LanType);
			if(LanTypeResult==null)
			{
				Thread.sleep(2000);
				LanTypeResult = segResultCollector.get(LanType);
			}
			System.out.println("done1");
			return LanTypeResult;
	
		//}
	}
	public  HashMap<String,List<Lexeme>> getHash()
	{
		return this.segResultCollector;
	}
	public static void main(String args[]) throws InterruptedException
	{
		ConfigurationDialect.initLanDictionary();
		ISegmenter segmenter = ConfigurationDialect.loadSegmenter_sichuan();
		segResultCollect segRec =  new segResultCollect("这是四川方言",segmenter);
		segRec.start();
		Thread.sleep(6000);
	
		if(segRec.getSegResult("SiChuan")==null)
		{
			System.out.println("error");
		}
		List<Lexeme> segResult = segRec.getSegResult("SiChuan");
		for(Lexeme iter:segResult)
		{
			System.out.println(iter.getLexemeText());
		}
	}
	class segResultProduce extends Thread{
		/*private String txt;
		private ISegmenter segmenter;
		private segResultCollect segCollect;*/
		/*public segResultProduce(String txt,ISegmenter segmenter)
		{
			this.txt = txt;
			this.segmenter = segmenter;
			
			//Configuration.initLanDictionary();
		}*/
		private ISegmenter threadSeg;
		public segResultProduce(ISegmenter threSegmenter)
		{
			threadSeg = threSegmenter;
		}
		public void run()
		{
	
			List<Lexeme> segedStr = new ArrayList<Lexeme>();
			mmseg4jSeg seg = new mmseg4jSeg();	
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(txt) , threadSeg);
				try {
					Lexeme l = null;
					int lastType = -1; //默认不是川方言词语
					String normalLan = null ;//普通词语串
					boolean isFinished = false;
					while( (l = ikSeg.next(true)) != null){
						
						if(l.getLexemeType() == 3 )
						{
							if(isFinished == true)
								normalLan = null;
							if(normalLan == null)
							{
								normalLan = l.getLexemeText();
								isFinished = false;
							}
							else
								normalLan += l.getLexemeText();
						}	
						else if(l.getLexemeType() == 0 &&(lastType == 3))
						{
							seg.segNormalLan(normalLan,segedStr);
							isFinished = true;
							segedStr.add(l);
						}
						else if(l.getLexemeType() == 0)
						{
							segedStr.add(l);
						}
						lastType = l.getLexemeType();
					}
					if(isFinished == false)
						seg.segNormalLan(normalLan,segedStr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				insertSegResult(threadSeg.getLanType(), segedStr);
				
				//segResultCollector.
				
			//}
		}


	}

}
