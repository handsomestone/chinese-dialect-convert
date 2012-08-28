package org.handsomestone.example;
import java.util.*;
public class testList {
	private static Map<String,List<String>> hashmap;
	
	public static void init()
	{
		hashmap = new HashMap<String,List<String>>();
	}
	public static void insert(String key,String word)
	{
		List<String> list = hashmap.get(key);
		if(null == list)
		{
			list = new ArrayList<String>();
			list.add(word);
			hashmap.put(key,list);
		}
		else
		{
			list.add(word);
		}
	}
	public static List<String> getWord(String key)
	{
		return hashmap.get(key);
	}
	/*
	public static void main(String args[])
	{
		testList.init();
		testList.insert("ts1", "word1");
		testList.insert("ts1", "word2");
		testList.insert("ts1", "word3");
		testList.insert("ts1", "word4");
		testList.insert("ts1", "word5");
		testList.insert("ts1", "word6");
		testList.insert("ts2", "word1");
		testList.insert("ts2", "word2");
		testList.insert("ts2", "word3");
		testList.insert("ts2", "word4");
		testList.insert("ts2", "word5");
		testList.insert("ts2", "word6");
		
		List<String> list1 = testList.getWord("ts1");
		List<String> list2 = testList.getWord("ts1");
		
		list1.remove(0);
		list2.remove(0);
		
		List<String> listT = testList.hashmap.get("ts1");
		for(String iter:listT)
			System.out.println(iter);
		
	}*/

}
