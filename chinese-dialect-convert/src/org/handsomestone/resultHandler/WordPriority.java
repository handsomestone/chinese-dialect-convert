package org.handsomestone.resultHandler;

class WordPriority {
	
	private String word;
	private String wordFrom;
	private double priority;
	
	public WordPriority(String word)
	{
		this.word = word;
	}
	public void setPriority(double priority)
	{
		this.priority = priority;
	}
	public double getPriority()
	{
		return this.priority;
	}
	public String getWord()
	{
		return this.word;
	}
	public void setWordFrom(String wordFrom)
	{
		this.wordFrom = wordFrom;
	}
	public String getWordFrom()
	{
		return this.wordFrom;
	}

}
