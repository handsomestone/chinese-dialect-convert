/**
 * 
 */
package org.wltea.analyzer.dic;

/**
 * IK Analyzer v3.2
 * ��ʾ�ʵ���������н��
 * @author ������
 *
 */
public class Hit {
	//Hit��ƥ��
	private static final int UNMATCH = 0x00000000;
	//Hit��ȫƥ��
	private static final int MATCH = 0x00000001;
	//Hitǰ׺ƥ��
	private static final int PREFIX = 0x00000010;
	
	
	//��HIT��ǰ״̬��Ĭ��δƥ��
	private int hitState = UNMATCH;
	
	//��¼�ʵ�ƥ������У���ǰƥ�䵽�Ĵʵ��֧�ڵ�
	private DictSegment matchedDictSegment; 
	/*
	 * �ʶο�ʼλ��
	 */
	private int begin;
	/*
	 * �ʶεĽ���λ��
	 */
	private int end;
	
	
	/**
	 * �ж��Ƿ���ȫƥ��
	 */
	public boolean isMatch() {
		return (this.hitState & MATCH) > 0;
	}
	/**
	 * 
	 */
	public void setMatch() {
		this.hitState = this.hitState | MATCH;
	}

	/**
	 * �ж��Ƿ��Ǵʵ�ǰ׺
	 */
	public boolean isPrefix() {
		return (this.hitState & PREFIX) > 0;
	}
	/**
	 * 
	 */
	public void setPrefix() {
		this.hitState = this.hitState | PREFIX;
	}
	/**
	 * �ж��Ƿ��ǲ�ƥ��
	 */
	public boolean isUnmatch() {
		return this.hitState == UNMATCH ;
	}
	/**
	 * 
	 */
	public void setUnmatch() {
		this.hitState = UNMATCH;
	}
	
	public DictSegment getMatchedDictSegment() {
		return matchedDictSegment;
	}
	
	public void setMatchedDictSegment(DictSegment matchedDictSegment) {
		this.matchedDictSegment = matchedDictSegment;
	}
	
	public int getBegin() {
		return begin;
	}
	
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}	
	
}

