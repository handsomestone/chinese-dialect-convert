package org.handsomestone.resultHandler;



import java.util.List;

import org.handsomestone.WordSimilarity.WordSimilarity;
import org.handsomestone.cfg.ConfigurationDialect;
import org.handsomestone.segresult.collecter.DialectSegment;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.ISegmenter;

public class ResultHandler {
	
	private List<Lexeme> DialectList;
	private List<DialectLexeme> simDialectList;
	private ISegmenter SichuanSegment;
	private ISegmenter ShanxiSegment;
	public ResultHandler(List<Lexeme> dialect)
	{
		this.DialectList = dialect;
		
	}
	public ResultHandler()
	{
	  this.SichuanSegment = ConfigurationDialect.loadSegmenter_sichuan();
	  this.ShanxiSegment = ConfigurationDialect.loadSegmenter_shanxi();
	 // WordSimilarity.loadGlossary();

	}
	public List<DialectLexeme> getSimDialect()
	{
		return this.simDialectList;
	}
	public DialectLexeme getSichuanDialect(String normalLen)
	{
		
		DialectSegment diaSeg = new DialectSegment(normalLen,SichuanSegment);
		List<Lexeme> dialectLexeme = diaSeg.getDialectSegment();
		DialectDao testDao = new DialectDao(dialectLexeme,"sichuan");
		DialectLexeme getDiaLex = testDao.getDialectSentence();
		simDialectList = testDao.getSimiWordsTst();
		//System.out.println("DialectLexme finish");
		return getDiaLex;
	}
	public DialectLexeme getShanxiDialect(String normalLen)
	{
		DialectSegment diaSeg = new DialectSegment(normalLen,ShanxiSegment);
		List<Lexeme> dialectLexeme = diaSeg.getDialectSegment();
		DialectDao testDao = new DialectDao(dialectLexeme,"shanxi");
		DialectLexeme getDiaLex = testDao.getDialectSentence();
		simDialectList = testDao.getSimiWords();
		return getDiaLex;
	}
	public static void main(String args[])
	{
		for(int i=0;i<1;i++)
			{		
				
				ResultHandler rst = new ResultHandler();
				DialectLexeme tst = rst.getSichuanDialect("街上有我我在街街边我不太清楚一大堆几个女人不管那么多了");
				System.out.println(tst.getNormalLanSentence());
				
				List<DialectLexeme> lst = rst.getSimDialect();
				//	System.out.println("DialectLexme....");
				if(lst != null)
				{
					for(DialectLexeme iter:lst)
					{
						System.out.println(iter.getNormalLan()+iter.getSimNormalLan()+iter.getDialectLan());
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}	
				/**/
	}

}
