package org.wltea.analyzer;

import java.util.HashSet;
import java.util.Set;

//import org.wltea.analyzer.dic.LanDictionary;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * �ִ���������״̬
 * @author ������
 *
 */
public class Context{
	
	//�Ƿ�ʹ�����ʳ��з֣������ȣ�
	private boolean isMaxWordLength = false;
    //�ַ��ܶ�ȡ����
   private char[] segmentBuff;
    //��¼Reader���ѷ������ִ��ܳ���
    //�ڷֶ�η�����Ԫʱ���ñ����ۼƵ�ǰ��segmentBuff�����reader��λ��
	private int buffOffset;	
	//���һ�ζ����,�ɴ�����ִ�����
	private int available;
    //���һ�η������ִ�����
    private int lastAnalyzed;	
    //��ǰ������λ��ָ��
    private int cursor; 

    
    /*
     * ��¼����ʹ��buffer�ķִ�������
     * ���set�д����зִ���������buffer���ܽ���λ�Ʋ���������locked״̬��
     */
    private Set<ISegmenter> buffLocker;
    /*
     * ��Ԫ�������Ϊÿ���α���ƶ����洢�зֳ����Ĵ�Ԫ
     */
	private IKSortedLinkSet lexemeSet;

    
    Context(char[] segmentBuff , boolean isMaxWordLength){
    	this.isMaxWordLength = isMaxWordLength;
    	this.segmentBuff = segmentBuff;
    	this.buffLocker = new HashSet<ISegmenter>(4);
    	this.lexemeSet = new IKSortedLinkSet();
	}
    
    /**
     * ����������
     */
    public void resetContext(){
    	buffLocker.clear();
    	lexemeSet = new IKSortedLinkSet();
    	buffOffset = 0;
    	available = 0;
    	lastAnalyzed = 0;
    	cursor = 0;
    }

	public boolean isMaxWordLength() {
		return isMaxWordLength;
	}

	public void setMaxWordLength(boolean isMaxWordLength) {
		this.isMaxWordLength = isMaxWordLength;
	}
    
	public int getBuffOffset() {
		return buffOffset;
	}


	public void setBuffOffset(int buffOffset) {
		this.buffOffset = buffOffset;
	}

	public int getLastAnalyzed() {
		return lastAnalyzed;
	}


	public void setLastAnalyzed(int lastAnalyzed) {
		this.lastAnalyzed = lastAnalyzed;
	}


	public int getCursor() {
		return cursor;
	}


	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	
	public void lockBuffer(ISegmenter segmenter){
		this.buffLocker.add(segmenter);
	}
	
	public void unlockBuffer(ISegmenter segmenter){
		this.buffLocker.remove(segmenter);
	}
	
	/**
	 * ֻҪbuffLocker�д���ISegmenter����
	 * ��buffer������
	 * @return boolean ����ȥ�Ƿ�����
	 */
	public boolean isBufferLocked(){
		return this.buffLocker.size() > 0;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}
	
	

	/**
	 * ȡ���ִʽ�����е��׸���Ԫ
	 * @return Lexeme ���ϵĵ�һ����Ԫ
	 */
	public Lexeme firstLexeme() {
		return this.lexemeSet.pollFirst();
	}
	
	/**
	 * ȡ���ִʽ�����е����һ����Ԫ
	 * @return Lexeme ���ϵ����һ����Ԫ
	 */
	public Lexeme lastLexeme() {
		return this.lexemeSet.pollLast();
	}
	
	/**
	 * ��ִʽ������Ӵ�Ԫ
	 * @param lexeme
	 */
	public void addLexeme(Lexeme lexeme){
	
			this.lexemeSet.addLexeme(lexeme);
	}
	
	/**
	 * ��ȡ�ִʽ������С
	 * @return int �ִʽ������С
	 */
	public int getResultSize(){
		return this.lexemeSet.size();
	}
	
	/**
	 * �ų����������ȫ�������˴˰������Ĵ�Ԫ
	 * ��������зֵ�ʱ�򣬹��˳��Ƚ�С�Ľ�����Ԫ
	 */
	public void excludeOverlap(){
		 this.lexemeSet.excludeOverlap();
	}
	
	/**
	 * 
	 * @author linly
	 *
	 */
	private class IKSortedLinkSet{
		//����ͷ
		private Lexeme head;
		//����β
		private Lexeme tail;
		//�����ʵ�ʴ�С
		private int size;
		
		private IKSortedLinkSet(){
			this.size = 0;
		}
		/**
		 * ����������Ӵ�Ԫ
		 * @param lexeme
		 */
		private void addLexeme(Lexeme lexeme){
			if(this.size == 0){
				this.head = lexeme;
				this.tail = lexeme;
				this.size++;
				return;
				
			}else{
				if(this.tail.compareTo(lexeme) == 0){//��Ԫ��β����Ԫ��ͬ�������뼯��
					return;
					
				}else if(this.tail.compareTo(lexeme) < 0){//��Ԫ��������β��
					this.tail.setNext(lexeme);
					lexeme.setPrev(this.tail);
					this.tail = lexeme;
					this.size++;
					return;
					
				}else if(this.head.compareTo(lexeme) > 0){//��Ԫ��������ͷ��
					this.head.setPrev(lexeme);
					lexeme.setNext(this.head);
					this.head = lexeme;
					this.size++;
					return;
					
				}else{					
					//��β������
					Lexeme l = this.tail;
					while(l != null && l.compareTo(lexeme) > 0){
						l = l.getPrev();
					}
					if(l.compareTo(lexeme) == 0){//��Ԫ�뼯���еĴ�Ԫ�ظ��������뼯��
						return;
						
					}else if(l.compareTo(lexeme) < 0){//��Ԫ���������е�ĳ��λ��
						lexeme.setPrev(l);
						lexeme.setNext(l.getNext());
						l.getNext().setPrev(lexeme);
						l.setNext(lexeme);
						this.size++;
						return;
						
					}
				}
			}
			
		}
		/**
		 * ȡ�������ϵĵ�һ��Ԫ��
		 * @return Lexeme
		 */
		private Lexeme pollFirst(){
			if(this.size == 1){
				Lexeme first = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return first;
			}else if(this.size > 1){
				Lexeme first = this.head;
				this.head = first.getNext();
				first.setNext(null);
				this.size --;
				return first;
			}else{
				return null;
			}
		}
		
		/**
		 * ȡ�������ϵ����һ��Ԫ��
		 * @return Lexeme
		 */
		private Lexeme pollLast(){
			if(this.size == 1){
				Lexeme last = this.head;
				this.head = null;
				this.tail = null;
				this.size--;
				return last;
				
			}else if(this.size > 1){
				Lexeme last = this.tail;
				this.tail = last.getPrev();
				last.setPrev(null);
				this.size--;
				return last;
				
			}else{
				return null;
			}
		}
		
		/**
		 * �޳����ϻ������ڵ�����ȫ������lexeme
		 * ��������зֵ�ʱ�򣬹��˳��Ƚ�С�Ľ�����Ԫ
		 */
		private void excludeOverlap(){
			if(this.size > 1){
				Lexeme one = this.head;
				Lexeme another = one.getNext();
				do{
					if( one.isOverlap(another)
//							&& Lexeme.TYPE_LETTER != one.getLexemeType()
//							&& Lexeme.TYPE_LETTER != another.getLexemeType()
							){
						
						//�ڽ���������Ԫ��ȫ����
						another = another.getNext();
						//�������жϿ������Ĵ�Ԫ
						one.setNext(another);
						if(another != null){
							another.setPrev(one);
						}
						this.size--;
						
					}else{//��Ԫ����ȫ����
						one = another;
						another = another.getNext();
					}
				}while(another != null);
			}
		}
		
		private int size(){
			return this.size;
		}
		
		
	}

}

