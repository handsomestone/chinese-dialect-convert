package org.wltea.analyzer.test;

import java.util.List;

import org.handsomestone.cfg.ConfigurationDialect;
import org.handsomestone.segresult.collecter.segResultCollect;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.ISegmenter;

public class lanThread {

	private segResultCollect  segReCollect;
	public lanThread()
	{
		ConfigurationDialect.initLanDictionary();
		this.segReCollect = new segResultCollect();
		
	}
	public void start()
	{
		ISegmenter segmenter = ConfigurationDialect.loadSegmenter_sichuan();
		//
		
	
		/*
		Thread thread2 = new Thread(new lanSegTest("�Ҳ�̫���һ��Ѽ���Ů�˲�����ô����",Configuration.loadSegmenter_shanxi()));
		thread2.start();
		/*Thread thread3 = new Thread(new lanSegTest("�Ĵ���������Ŷ������ϵͳһ"));
		thread3.start();
		Thread thread4 = new Thread(new lanSegTest("�Ĵ�����İ���������ϵͳ��"));
		thread4.start();*/
	}
	public void show() throws InterruptedException
	{
		List<Lexeme> segResult = segReCollect.getSegResult("SiChuan");
		for(Lexeme iter:segResult)
		{
			System.out.println(iter.getLexemeText());
		}
	}
	
	public static void main(String args[])
	{
		segResultCollect  segReCollect = new segResultCollect();
		ISegmenter segmenter = ConfigurationDialect.loadSegmenter_sichuan();
		Thread thread1 = new Thread(new lanSegTest("�Ҳ�̫���һ��Ѽ���Ů�˲�����ô����",segmenter,segReCollect));
		thread1.start();
		ISegmenter segmenter1 = ConfigurationDialect.loadSegmenter_sichuan();
		Thread thread3 = new Thread(new lanSegTest("һ���С���Ӽ���Ů�˲�����ô����",segmenter1,segReCollect));
		thread3.start();
		List<Lexeme> segResult;
		try {
			segResult = segReCollect.getSegResult("SiChuan");
			for(Lexeme iter:segResult)
			{
				System.out.println(iter.getLexemeText());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		lanThread lan = new lanThread();
		lan.start();
		try {
			lan.show();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	segResultCollect testCollect = new segResultCollect();
		try {
			List<Lexeme> segResult = testCollect.getSegResult("SiChuan");
			for(Lexeme iter:segResult)
			{
				System.out.println(iter.getLexemeText());
			}
			System.out.println("*********************************************");
			List<Lexeme> segResult1 = testCollect.getSegResult("ShanXi");
			for(Lexeme iter:segResult1)
			{
				System.out.println(iter.getLexemeText());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
