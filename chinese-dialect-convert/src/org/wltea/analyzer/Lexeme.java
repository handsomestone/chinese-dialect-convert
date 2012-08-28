/**
 * 
 */
package org.wltea.analyzer;

/**
 * IK Analyzer v3.2
 * ���嵥Ԫ����Ԫ�� * 
 * @author ������
 *
 */
public final class Lexeme implements Comparable<Lexeme>{
	//lexemeType����
	//��ͨ��Ԫ
	public static final int TYPE_CJK_NORMAL = 0;
	//����
	public static final int TYPE_CJK_SN = 1;
	//β׺
	public static final int TYPE_CJK_SF = 2;
	//δ֪��
	public static final int TYPE_CJK_UNKNOWN = 3;
	//����
	public static final int TYPE_NUM = 10;
	//����
	public static final int TYPE_NUMCOUNT = 11;
	//Ӣ��
	public static final int TYPE_LETTER = 20;
	
	//��Ԫ����ʼλ��
	private int offset;
    //��Ԫ�������ʼλ��
    private int begin;
    //��Ԫ�ĳ���
    private int length;
    //��Ԫ�ı�
    private String lexemeText;
    //��Ԫ����
    private int lexemeType;
    
    //��ǰ��Ԫ��ǰһ����Ԫ
    private Lexeme prev;
    //��ǰ��Ԫ�ĺ�һ����Ԫ
    private Lexeme next;
    
    public Lexeme(String lexemeText,int lexemeType)
    {
    	if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
    	this.lexemeType = lexemeType;
    }
    
	public Lexeme(int offset , int begin , int length , int lexemeType){
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
		this.lexemeType = lexemeType;
	}
	
    /*
     * �жϴ�Ԫ����㷨
     * ��ʼλ��ƫ�ơ���ʼλ�á���ֹλ����ͬ
     * @see java.lang.Object#equals(Object o)
     */
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof Lexeme){
			Lexeme other = (Lexeme)o;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
    /*
     * ��Ԫ��ϣ�����㷨
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
    	int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }
    
    /*
     * ��Ԫ�����򼯺��еıȽ��㷨
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	public int compareTo(Lexeme other) {
		//��ʼλ������
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//��Ԫ��������
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {//this.length < other.getLength()
        		return 1;
        	}
        	
        }else{//this.begin > other.getBegin()
        	return 1;
        }
	}
	
	/**
	 * �жϴ�Ԫ�Ƿ�˴˰���
	 * @param other
	 * @return boolean true ��ȫ���� �� false ���ܲ��ཻ ���� �ཻ��������
	 */
	public boolean isOverlap(Lexeme other){
		if(other != null){
			if(this.getBeginPosition() <= other.getBeginPosition() 
					&& this.getEndPosition() >= other.getEndPosition()){
				return true;
				
			}else if(this.getBeginPosition() >= other.getBeginPosition() 
					&& this.getEndPosition() <= other.getEndPosition()){
				return true;
				
			}else {
				return false;
			}
		}
		return false;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getBegin() {
		return begin;
	}
	/**
	 * ��ȡ��Ԫ���ı��е���ʼλ��
	 * @return int
	 */
	public int getBeginPosition(){
		return offset + begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * ��ȡ��Ԫ���ı��еĽ���λ��
	 * @return int
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	/**
	 * ��ȡ��Ԫ���ַ�����
	 * @return int
	 */
	public int getLength(){
		return this.length;
	}	
	
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	/**
	 * ��ȡ��Ԫ���ı�����
	 * @return String
	 */
	public String getLexemeText() {
		if(lexemeText == null){
			return "";
		}
		return lexemeText;
	}

	public void setLexemeText(String lexemeText) {
		if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
	}

	/**
	 * ��ȡ��Ԫ����
	 * @return int
	 */
	public int getLexemeType() {
		return lexemeType;
	}

	public void setLexemeType(int lexemeType) {
		this.lexemeType = lexemeType;
	}	
	
	public String toString(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
		strbuf.append(" : ").append(this.lexemeText).append(" : \t");
		switch(lexemeType) {
			case TYPE_CJK_NORMAL : 
				strbuf.append("CJK_NORMAL");
				break;
			case TYPE_CJK_SF :
				strbuf.append("CJK_SUFFIX");
				break;
			case TYPE_CJK_SN :
				strbuf.append("CJK_NAME");
				break;
			case TYPE_CJK_UNKNOWN :
				strbuf.append("UNKNOWN");
				break;
			case TYPE_NUM : 
				strbuf.append("NUMEBER");
				break;
			case TYPE_NUMCOUNT :
				strbuf.append("COUNT");
				break;
			case TYPE_LETTER :
				strbuf.append("LETTER");
				break;

		}

		return strbuf.toString();
	}

	Lexeme getPrev() {
		return prev;
	}

	void setPrev(Lexeme prev) {
		this.prev = prev;
	}

	Lexeme getNext() {
		return next;
	}

	void setNext(Lexeme next) {
		this.next = next;
	}

	
}

