package org.handsomestone.resultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import org.handsomestone.ConnectionHelper.ConnectionHelper;
import org.handsomestone.segresult.collecter.DialectSegment;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.seg.ISegmenter;

import org.handsomestone.WordSimilarity.*;
import org.handsomestone.cfg.ConfigurationDialect;



public class DialectDao {
	
	private List<Lexeme> dialect;
	private List<DialectLexeme> dialectList;
	private String searchType;
	public DialectDao(List<Lexeme> dialect ,String searchType)
	{
		this.dialect = dialect;
		this.searchType = searchType;
		this.dialectList = new ArrayList<DialectLexeme>();
		//WordSimilarity.loadGlossary(); //add for test
	}
	
	
	public DialectDao(String searchType)
	{
		//WordSimilarity.loadGlossary();
		this.searchType = searchType;
	}
	public void setDialectList(List<DialectLexeme> dialectList)
	{
		this.dialectList = dialectList;
	}
	
	public DialectLexeme getDialectSentence()
	{
		Connection con = null;
		DialectLexeme sentence = new DialectLexeme();
		DialectLexeme simiWord ;
		String sql = "select "+searchType+".wordTo from "+searchType+" where wordFrom = ?";
		try {
			con = ConnectionHelper.getConnection();
			for(Lexeme iter:dialect)
			{
				if(iter.getLexemeType()==0)
				{
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, iter.getLexemeText());
					ResultSet rs = ps.executeQuery();
					if(rs.next())
					{
						sentence.setNormalLanSentence(rs.getString("wordTo"));
					}
				}
				else
				{
					sentence.setNormalLanSentence(iter.getLexemeText());
					simiWord = new DialectLexeme();
					simiWord.setNormalLan(iter.getLexemeText());
					this.dialectList.add(simiWord);
					//System.out.println("adding "+simiWord.getNormalLan());
				/*	if(dialectList.isEmpty())
					{
					System.out.println("dialectList null");
					
					}
					else
					{
						System.out.println("adding");
					}*/
				}
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			ConnectionHelper.closeConnection(con);
			
		}
		
		return sentence;
		
	}
	public List<DialectLexeme> getSimiWords()
	{
		
		Connection con = null;
		List<String> wordDescrip;
		String MD5Value = null;
		int times;
		//List<DialectLexeme> getDialectList = new ArrayList<DialectLexeme>();
		String sql = "select * from "+searchType+" where md5 like ?";
		Comparator<WordPriority> priorityCompate =  new Comparator<WordPriority>(){
			public int compare(WordPriority o1, WordPriority o2) {
				
				double prioritya = o1.getPriority();
				double priorityb = o2.getPriority();
				if(priorityb > prioritya)
				{
					return 1;
				}
				else if(priorityb<prioritya)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			
			}

			
			
		};
		if(dialectList.isEmpty())
		{
			//System.out.println("int ext");
			return null;
		}
		
		try {
			Queue<WordPriority> priorityQueue =  new PriorityQueue<WordPriority>(12,priorityCompate);
			MD5 md5 = new MD5(); 
			con = ConnectionHelper.getConnection();
			for(DialectLexeme iter:dialectList)
			{
				
					wordDescrip = WordSimilarity.getMapDetail(iter.getNormalLan());
					DialectLexeme temp =iter;
					if(wordDescrip == null)
					{
						//dialectList.remove(iter);
						
						continue;
					}
					
					boolean first = true;
					times = 0;
					
					for(String iterStr:wordDescrip)
					{
					//	System.out.println("rs.next() is not null"+iterStr);
						if(first)
						{
							MD5Value=md5.getMD5ofStr( iterStr );
							first = false;
							times++;
						}
						else if(times<3)
						{
							MD5Value+=md5.getMD5ofStr( iterStr );
							times++;
						}
					}
					
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, MD5Value);
				
					ResultSet rs = ps.executeQuery();
					
					while(rs.next())
					{	
						//System.out.println("rs.next() is not null");
						WordPriority wordPri = new WordPriority(rs.getString("wordTo"));
						
						String word1 = iter.getNormalLan();
						String word2 = rs.getString("keyword");
					//	System.out.println("normal"+word1+"keyword"+word2);
						double similarity = WordSimilarity.simWord(word1, word2);
						
						wordPri.setPriority(similarity);
						wordPri.setWordFrom(rs.getString("wordFrom"));
						//System.out.println("循环内："+wordPri.getWordFrom());
						priorityQueue.add(wordPri);
					}
					
					if(!priorityQueue.isEmpty())
					{
						WordPriority wordFirst = priorityQueue.poll();
						iter.setDialectLan(wordFirst.getWord());
					//	System.out.println(wordFirst.getWordFrom());
						iter.setSimNormalLan(wordFirst.getWordFrom());
						priorityQueue.clear();
					}
					else
					{ 	
						//dialectList.remove(temp);             //当没有相似词的时候，就需要删除该词语否则返回空，没用
					}
					
				} 
			
			}
		catch (SQLException e) {
			
			e.printStackTrace();
		}finally
		{
			ConnectionHelper.closeConnection(con);
		}
		return dialectList;
		
	}
	/*相似词计算同时删除数据库中没有找到的词*/
	public List<DialectLexeme> getSimiWordsTst()
	{
		
		Connection con = null;
		List<String> wordDescrip;
		String MD5Value = null;
		int times;
	//	boolean free[]={false,false,false,false,false};
		String sql = "select * from "+searchType+" where md5 like ?";
		Comparator<WordPriority> priorityCompate =  new Comparator<WordPriority>(){
			public int compare(WordPriority o1, WordPriority o2) {
				
				double prioritya = o1.getPriority();
				double priorityb = o2.getPriority();
				if(priorityb > prioritya)
				{
					return 1;
				}
				else if(priorityb<prioritya)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			
			}

			
			
		};
		if(dialectList.isEmpty())
		{
			//System.out.println("int ext");
			return null;
		}
		
		try {
			Queue<WordPriority> priorityQueue =  new PriorityQueue<WordPriority>(12,priorityCompate);
			MD5 md5 = new MD5(); 
			con = ConnectionHelper.getConnection();
			Iterator<DialectLexeme> iterDialect = dialectList.iterator();
			while(iterDialect.hasNext())
			{
					DialectLexeme iter = iterDialect.next();
					wordDescrip = WordSimilarity.getMapDetail(iter.getNormalLan());
					
					if(wordDescrip == null)
					{
						//dialectList.remove(iter);
					 //	System.out.println("wordDescrip == null");
						iterDialect.remove();  
						continue;
					}
					
					boolean first = true;
					times = 0;
					
					for(String iterStr:wordDescrip)
					{
					//	System.out.println("rs.next() is not null"+iterStr);
						if(first)
						{
							MD5Value=md5.getMD5ofStr( iterStr );
							first = false;
							times++;
						}
						/*else if(times<3)
						{
							MD5Value+=md5.getMD5ofStr( iterStr );
							times++;
						}*/
					}
					
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, MD5Value);
					//System.out.println("MD5Value"+MD5Value);
					ResultSet rs = ps.executeQuery();
					
					while(rs.next())
					{	
						//System.out.println("rs.next() is not null");
						WordPriority wordPri = new WordPriority(rs.getString("wordTo"));
						
						String word1 = iter.getNormalLan();
						String word2 = rs.getString("keyword");
					//	System.out.println("normal"+word1+"keyword"+word2);
						double similarity = WordSimilarity.simWord(word1, word2);
						
						wordPri.setPriority(similarity);
						wordPri.setWordFrom(rs.getString("wordFrom"));
						//System.out.println("循环内："+wordPri.getWordFrom());
						priorityQueue.add(wordPri);
					}
					
					if(!priorityQueue.isEmpty())
					{
						WordPriority wordFirst = priorityQueue.poll();
					
						iter.setDialectLan(wordFirst.getWord());
					//	System.out.println(wordFirst.getWordFrom());
						iter.setSimNormalLan(wordFirst.getWordFrom());
						priorityQueue.clear();
					}
					else
					{ 	//System.out.println("iterDialect.remove()");
						iterDialect.remove();             //当没有相似词的时候，就需要删除该词语否则返回空，没用
					}
					
				} 
			
			}
		catch (SQLException e) {
			
			e.printStackTrace();
		}finally
		{
			ConnectionHelper.closeConnection(con);
		}
		return dialectList;
		
	}
	public static void main(String args[])
	{
		ConfigurationDialect.initLanDictionary();
		ISegmenter  segment = ConfigurationDialect.loadSegmenter_sichuan();
		DialectSegment diaSeg = new DialectSegment("美丽",segment);
		List<Lexeme> testList_1 = diaSeg.getDialectSegment();
		DialectDao testDao = new DialectDao(testList_1,"sichuan");
		
		DialectLexeme testDialect = testDao.getDialectSentence();
		
		System.out.println(testDialect.getNormalLanSentence());
		
		List<DialectLexeme> testList = testDao.getSimiWordsTst();
		for(DialectLexeme iter : testList)
		{
			System.out.println("normal:"+iter.getNormalLan()+"simword:"+iter.getSimNormalLan()+"dialect:"+iter.getDialectLan());
		}
		//DialectDao testDao = new DialectDao("sichuan");
	/*	DialectLexeme testDia = new DialectLexeme();
	    testDia.setNormalLan("东拉西扯");
		DialectLexeme testDia1 = new DialectLexeme();
		testDia1.setNormalLan("回家");
		List<DialectLexeme> testList =  new ArrayList<DialectLexeme>();
		testList.add(testDia);
		testList.add(testDia1);
		testDao.setDialectList(testList);
		List<DialectLexeme> testSim = testDao.getSimiWords();
		for(DialectLexeme iter : testSim)
		{
			System.out.println("normal:"+iter.getNormalLan()+"simword:"+iter.getSimNormalLan()+"dialect:"+iter.getDialectLan());
		}*/
		/*WordSimilarity.loadGlossary();
		double similarity = WordSimilarity.simWord("回流", "回流");
		System.out.println(similarity);*/
		}

}
