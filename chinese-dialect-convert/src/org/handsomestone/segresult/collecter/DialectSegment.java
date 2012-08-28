package org.handsomestone.segresult.collecter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.handsomestone.cfg.ConfigurationDialect;
import org.handsomestone.mmeg4j.seg.mmseg4jSeg;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.ISegmenter;
public class DialectSegment {
	
	private List<Lexeme> dialect;
	private String normalSentence;
	private ISegmenter segmenter;
	
	public DialectSegment(String normalSentence,ISegmenter segmenter)
	{
		this.normalSentence = normalSentence;
		this.segmenter = segmenter;
		this.dialect = new ArrayList<Lexeme>();
	}
	
	public List<Lexeme> getDialectSegment()
	{	//System.out.println("in");
		mmseg4jSeg seg = new mmseg4jSeg();
		IKSegmentation ikSeg = new IKSegmentation(new StringReader(normalSentence) , segmenter);
		
		try {
			Lexeme l = null;
			int lastType = -1; //默认不是方言词语
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
					seg.segNormalLan(normalLan,dialect);
					isFinished = true;
					dialect.add(l);
				}
				else if(l.getLexemeType() == 0)
				{
					dialect.add(l);
					
				}
				lastType = l.getLexemeType();
				
			}
			if(normalLan == null)
				isFinished = true;
			
			if(isFinished == false)
				seg.segNormalLan(normalLan,dialect);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return dialect;
		
	}
	
	public static void main(String args[])
	{
		ConfigurationDialect.initLanDictionary();
		ISegmenter segment = ConfigurationDialect.loadSegmenter_sichuan();
		DialectSegment diaSeg = new DialectSegment("呢",segment);
		List<Lexeme> testList = diaSeg.getDialectSegment();
		for(Lexeme iter:testList)
		{
			System.out.println(iter.getLexemeText()+"type"+iter.getLexemeType());
		}
	/*	DialectSegment diaSeg1 = new DialectSegment("你不要站在上面了",segment);
		List<Lexeme> testList1 = diaSeg1.getDialectSegment();
		for(Lexeme iter:testList1)
		{
			System.out.println(iter.getLexemeText()+"type"+iter.getLexemeType());
		}*/
	}
	
}
