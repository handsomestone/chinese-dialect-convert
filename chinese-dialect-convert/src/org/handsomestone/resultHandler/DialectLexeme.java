package org.handsomestone.resultHandler;

public class DialectLexeme {
	private String normalLan;
	private String simNormalLan;
	private String dialect;
	private String normalLanSentence;
	public DialectLexeme()
	{
		
	}
	public void setNormalLan(String normalLan)
	{
		this.normalLan = normalLan;
	}
	public String getNormalLan()
	{
		return normalLan;
	}
	public void setSimNormalLan(String simNormalLan)
	{
		this.simNormalLan = simNormalLan;
	}
	public String getSimNormalLan()
	{
		return this.simNormalLan;
	}
	public void setDialectLan(String dialect)
	{
		this.dialect = dialect;
	}
	public String getDialectLan()
	{
		return dialect;
	}
	
	public void setNormalLanSentence(String sentence)
	{
		if(this.normalLanSentence == null)
		{
			this.normalLanSentence = sentence;
		}
		else
		{
			this.normalLanSentence+=sentence;
		}
		
	}
	public String getNormalLanSentence()
	{
		return this.normalLanSentence;
	}

}
