package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.handsomestone.mmeg4j.seg.mmseg4jSeg;
import org.handsomestone.segresult.collecter.segResultCollect;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;


import org.wltea.analyzer.seg.ISegmenter;

public class lanSegTest implements Runnable{
	private String txt;
	private ISegmenter segmenter;
	private segResultCollect segCollect;
	public lanSegTest(String txt,ISegmenter segmenter,segResultCollect segCollect)
	{
		this.txt = txt;
		this.segmenter = segmenter;
		this.segCollect = segCollect;
		//Configuration.initLanDictionary();
	}
	public void run()
	{
		List<String> testStr = new ArrayList<String>();
		testStr.add("这是四川方言启动模块我不太清楚一大堆几个女人不管那么多了");
		testStr.add("但是我不太情愿啊");
	//	testStr.add("下次不可能我这里一大堆的事情你不要一惊一乍的");
		List<Lexeme> segedStr = new ArrayList<Lexeme>();
		mmseg4jSeg seg = new mmseg4jSeg();
		
		//ISegmenter segmenter = Configuration.loadSegmenter_sichuan();
		
		//for(String t : testStr){
		//	System.out.println(t);	
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(this.txt) , this.segmenter);
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
			
			this.segCollect.insertSegResult(segmenter.getLanType(), segedStr);
			
			
			
		//}
	}


}
