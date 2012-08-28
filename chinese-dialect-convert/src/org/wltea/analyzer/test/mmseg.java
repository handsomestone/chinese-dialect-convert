package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class mmseg {
	protected Dictionary dic;
	public mmseg()
	{
		dic = Dictionary.getInstance();
	}
	protected Seg getSeg() {
		return new ComplexSeg(dic);
	}
	public String segWord(Reader input,String wordSpilt) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		Seg seg = getSeg();//取得不同的分词具体算法
		MMSeg mmSeg = new MMSeg(input, seg);
		Word word = null;
		boolean first = true;
		//List<String> strings = new ArrayList<String>();
		while((word=mmSeg.next())!=null) {
		if(!first) {
			sb.append(wordSpilt);
			}
			//划分出来的词
			String w = word.getString();
			sb.append(w);
			first = false;
			
		}
	return sb.toString();
	}
	
	public static void main(String args[])
	{
		String txt = "天气不错啊";
		String spilt = "|";
		 mmseg mm = new mmseg();
		try {
			System.out.println(mm.segWord(new StringReader(txt),spilt));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
