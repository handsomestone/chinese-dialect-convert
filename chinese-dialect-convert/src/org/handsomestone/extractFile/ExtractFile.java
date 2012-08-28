package org.handsomestone.extractFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.handsomestone.ConnectionHelper.ConnectionHelper;
import org.handsomestone.mmeg4j.seg.mmseg4jSeg;

import org.handsomestone.WordSimilarity.WordSimilarity;

import org.handsomestone.resultHandler.MD5;



/*
 * 解析文件用来完善方言词库，包含wordFrom普通话，wordTo方言，Keyword关键词，md5词语义原含义 
 * 扩展方言词库需为文本文件，只需提供普通话和方言的对照关系即可
 * 文本文件的内容结构可以如下：
 * 格式一： 方言:普通话
 *          方言:普通话1，普通话2
 *          方言 普通话1、普通话2
 * 格式二： 方言-普通话
 *          方言-普通话1，普通话2
 *          方言-普通话1、普通话2      
 */
public class ExtractFile {
	public static final String DefaultPath = "/org/handsomestone/extractFile/";

	public ExtractFile()
	{
		 //InputStream is = ExtractFile.class.getResourceAsStream(ExtractFile.DefaultPath);
		
		 //  if(is == null){
	      // 	throw new RuntimeException("Sichuan LanDictionary not found!!!");
	      //  }
	}
	
	protected String getMd5Value(MD5 m,String keyWord)
	{
		List<String> Strings = WordSimilarity.getMapDetail(keyWord);
		boolean first = true;
		String MD5Value = null;
		int times = 0;
		for(String iter:Strings)
		{
			if(first)
			{
				MD5Value=m.getMD5ofStr( iter );
				first = false;
				times++;
			}
			else if(times<3)
			{
				MD5Value+=m.getMD5ofStr( iter );
				times++;
			}
		}
		return MD5Value;
	}
	protected boolean isWordInDB(String wordFrom,String lanType,Connection con)
	{
		String sql_s = "select "+lanType+".wordFrom"+" from "+lanType+" where wordFrom ="+"'"+wordFrom+"'";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql_s);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	protected void analyzeRawFile( String lanType) 
	{
		String rawLine ;
		String md5Value;
		String keyWord;
		Connection con = null;
		Statement state = null;
		MD5 m = new MD5(); 
		int spilt;
		mmseg4jSeg mseg = new mmseg4jSeg();
	
		
		String sql_p = "insert into "+lanType+"(wordFrom,wordTo)"+" values ";
		String sql_f = "insert into "+lanType+"(wordFrom,wordTo,md5,keyword)"+" values ";
		//add for test
		
	//	lanType
		String path = ExtractFile.DefaultPath+"sichuan"+"_raw.txt";
		InputStream is = ExtractFile.class.getResourceAsStream(path);
		if(is == null)
		{
			System.out.println("no data to load");
			return;
		}
		try {
			Scanner rawScan = new Scanner(is);
			con = ConnectionHelper.getConnection();
			state = con.createStatement();
			while(rawScan.hasNext())
			{
				rawLine = rawScan.next();
				System.out.println(rawLine);
				if(rawLine.contains("：") || rawLine.contains("，"))
				{
					rawLine.replace("：", ":");
					rawLine.replace("，", ",");
				}
			
				if(rawLine.contains(":")&&rawLine.contains("-"))
				{
					System.out.println("The format of the rawFile is wrong");
					continue;
				}
				String wordTo = null;
				String wordsFrom = null;
				String[] wordFrom = null;
					//String[] rawStrings = rawLine.split("[:]");
				spilt = rawLine.indexOf(":");
				if(spilt != -1)
				{
					 wordTo = rawLine.substring(0,spilt);
					 wordsFrom = rawLine.substring(spilt+":".length(),rawLine.length());
				 }
				else
				{
					 spilt = rawLine.indexOf("-");
					 wordTo = rawLine.substring(0,spilt);
					 wordsFrom = rawLine.substring(spilt+"-".length(),rawLine.length());
				 }
				System.out.println("hello");
				if(wordsFrom.contains(","))
				{
					 wordFrom = wordsFrom.split("[,]");
					 System.out.println("hello1");
				} 
				 else if(wordsFrom.contains("、"))
				 {
					 wordFrom = wordsFrom.split("[、]");
					 System.out.println("hello2");
				 }	
				 else
				 {
					
					 try {
						 System.out.println("hello3");
						keyWord = mseg.getKeyWord(wordsFrom);
						System.out.println(keyWord);
						//keyWord = mseg.getKeyWord(iter);
						if(keyWord == null)
						{
							String sql = null;
							sql = sql_p+"('"+wordsFrom+"','"+wordTo+"')";
							 System.out.println(sql);
							if(state != null)
							{
								
								if(!isWordInDB(wordsFrom,"sichuan",con))
								{
										 try
							        {
							            if (state != null)
							                state.executeUpdate(sql);
							        }catch(Exception e){
							        	System.out.println("error in");
							            e.printStackTrace();
							        }
								}
								
							}
							continue;
						}
						else
						{
							md5Value = getMd5Value(m,keyWord);
							System.out.println(md5Value);
							if(md5Value == null)
							{
								String sql = null;
								sql = sql_p+"('"+wordsFrom+"','"+wordTo+"')";
								if(state != null)
								{
									if(!isWordInDB(wordsFrom,"sichuan",con))
									state.executeQuery(sql);
								}
								continue;
							}
							else
							{
								String sql = null;
								sql = sql_f+"('"+wordsFrom+"','"+wordTo+"','"+md5Value+"','"+keyWord+"')";
								System.out.println(sql);
								if(state != null)
								{
									if(!isWordInDB(wordsFrom,"sichuan",con))
									state.execute(sql);
								}
							}
						}
						
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					 continue;
				 }
				 System.out.println("else");
				 for(String iter:wordFrom)
				 {
						try {
								keyWord = mseg.getKeyWord(iter);
								if(keyWord == null)
								{
									String sql = null;
									sql = sql_p+"('"+iter+"','"+wordTo+"')";
									if(state != null)
									{
										if(!isWordInDB(iter,"sichuan",con))
										state.executeQuery(sql);
									}
									continue;
								}
								else
								{
									md5Value = getMd5Value(m,keyWord);
									if(md5Value == null)
									{
										String sql = null;
										sql = sql_p+"('"+iter+"','"+wordTo+"')";
										if(state != null)
										{
											if(!isWordInDB(iter,"sichuan",con))
											state.executeQuery(sql);
										}
										continue;
									}
									else
									{
										String sql = null;
										sql = sql_f+"('"+iter+"','"+wordTo+"','"+md5Value+"','"+keyWord+"')";
										if(state != null)
										{
											if(!isWordInDB(iter,"sichuan",con))
											state.execute(sql);
										}
									}
								}
									
							} catch (IOException e) {
									e.printStackTrace();
							}
					}
						
				
				}
					
		
				
			
		}  catch (SQLException e) {
			
			e.printStackTrace();
		}finally
		{
			try {
				is.close();
				con.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	public void loadRawFileToDataBase()
	{

		analyzeRawFile("sichuan");
	
	}
	public static void main(String args[])
	{
		ExtractFile ts = new ExtractFile();
		ts.loadRawFileToDataBase();
		System.out.println("hello");
	}
	
}
