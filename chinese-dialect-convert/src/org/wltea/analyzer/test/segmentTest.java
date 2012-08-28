package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.handsomestone.mmeg4j.seg.mmseg4jSeg;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class segmentTest {
	public void testNumberCount(){
		List<String> testStr = new ArrayList<String>();
		testStr.add("�����������ڽֱֽ��Ҳ�̫���һ��Ѽ���Ů�˲�����ô����");
		testStr.add("�����Ҳ�̫��Ը��");
	//	testStr.add("�´β�����������һ��ѵ������㲻Ҫһ��һէ��");
		List<Lexeme> segedStr = new ArrayList<Lexeme>();
		/*mmseg4jSeg seg = new mmseg4jSeg();*/
		
		
		for(String t : testStr){
			System.out.println(t);	
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(t) , true);
			try {
				Lexeme l = null;
				int lastType = -1; //Ĭ�ϲ��Ǵ����Դ���
				String normalLan = null ;//��ͨ���ﴮ
				boolean isFinished = false;
				while( (l = ikSeg.next()) != null){
					
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
					/*	seg.segNormalLan(normalLan,segedStr);*/
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
				{}	/*seg.segNormalLan(normalLan,segedStr);*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("***************");	
			for(Lexeme iter:segedStr)
			{
				System.out.println("Type"+iter.getLexemeType()+"value"+iter.getLexemeText());
			}
		}
		
	}
	public static void main(String args[])
	{
		segmentTest test = new segmentTest();
		test.testNumberCount();
	}
}
