package org.handsomestone.mmeg4j.seg;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import org.handsomestone.WordSimilarity.WordSimilarity;
import org.handsomestone.dic.LanDictionary;

public class mmseg4jSeg {
	private IKSegmentation seg;
	public mmseg4jSeg()
	{
		 this.seg = new IKSegmentation(true);
	}
	public  void segNormalLan(String normalLan,List<Lexeme> strings) throws IOException
	{
	/*	Seg seg = LanDictionary.getSeg();//取得不同的分词具体算法
		MMSeg mmSeg = new MMSeg(new StringReader(normalLan), seg);*/
		seg.setInput(new StringReader(normalLan));
		Lexeme t;
		
		while((t=seg.next())!=null) 
		{
			//划分出来的词
			Lexeme l = new Lexeme(t.getLexemeText(),3);
			strings.add(l);
		}
	
	}
	
	public String getKeyWord(String wordFrom) throws IOException
	{
	/*	Seg seg = LanDictionary.getSeg();
		MMSeg mmSeg = new MMSeg(new StringReader(wordFrom),seg);*/
		seg.setInput(new StringReader(wordFrom));
	/*	Word word = null;*/
		Lexeme t;
		int maxLength = 0;
		List<String> strings = new ArrayList<String>();
		List<String> maxLengthStrings = null;
		while((t=seg.next())!=null) {
	
			String w = t.getLexemeText();
			
			strings.add(w);
			if(w.length()>=maxLength)
			{
				maxLength = w.length();
			}
		}
		//取出最长词语串
		for(String iter:strings)
		{
			int length = iter.length();
			if(length==maxLength)
			{
				if(maxLengthStrings == null)
				{
					maxLengthStrings= new ArrayList<String>();
					maxLengthStrings.add(iter);
				}
				else
				{
					maxLengthStrings.add(iter);
				}
			}
		}
		//最长串列表对比，取出意思最多的
		boolean ready = false;
		int maxSize =0;
		String rem_word = null;
		List<String> list = null;
		for(String iterMax:maxLengthStrings)
		{
			list = WordSimilarity.getMapDetail(iterMax);
			if(list == null)
				continue;
			if(!ready)
			{	
				maxSize = list.size();
				rem_word = iterMax;
				ready = true;
			}
			else if (list.size()>maxSize)
			{
				maxSize = list.size();
				rem_word = iterMax;
				
			}
		}
		return rem_word;
	}
	
	
}
