/**
 * 
 */
package org.wltea.analyzer.dic;

import java.util.HashMap;
import java.util.Map;


/**
 * IK Analyzer v3.2
 * �ֵ���Ƭ�� �ֵ�ƥ�������
 * ������� ���� ��� HashMap��ʵ�ִʵ�洢������ƥ��
 * 
 * ��������ҳ�ڵ�С����3ʱ����������洢
 * ��������ҳ�ڵ����3ʱ������HashMap�洢
 * @author ������
 *
 */
public class DictSegment {
	
	//�����ֵ���洢����
	private static final Map<Character , Character> charMap = new HashMap<Character , Character>(16 , 0.95f);
	
	//�����С����
	private static final int ARRAY_LENGTH_LIMIT = 3;
	
	//��ǰ�ڵ��ϴ洢���ַ�
	private Character nodeChar;
	
	//Map�洢�ṹ
	private Map<Character , DictSegment> childrenMap;
	
	//���鷽ʽ�洢�ṹ
	private DictSegment[] childrenArray;
	
	//��ǰ�ڵ�洢��Segment��Ŀ
	//storeSize <=ARRAY_LENGTH_LIMIT ��ʹ������洢�� storeSize >ARRAY_LENGTH_LIMIT ,��ʹ��Map�洢
	private int storeSize = 0;
	
	//��ǰDictSegment״̬ ,Ĭ�� 0 , 1��ʾ�Ӹ��ڵ㵽��ǰ�ڵ��·����ʾһ����
	private int nodeState = 0;	
	
	public DictSegment(Character nodeChar){
		if(nodeChar == null){
			throw new IllegalArgumentException("����Ϊ���쳣���ַ�����Ϊ��");
		}
		this.nodeChar = nodeChar;
	}

	public Character getNodeChar() {
		return nodeChar;
	}
	
	/*
	 * �ж��Ƿ�����һ���ڵ�
	 */
	public boolean hasNextNode(){
		return  this.storeSize > 0;
	}
	
	/**
	 * ƥ��ʶ�
	 * @param charArray
	 * @return Hit
	 */
	public Hit match(char[] charArray){
		return this.match(charArray , 0 , charArray.length , null);
	}
	
	/**
	 * ƥ��ʶ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit 
	 */
	public Hit match(char[] charArray , int begin , int length){
		return this.match(charArray , begin , length , null);
	}
	
	/**
	 * ƥ��ʶ�
	 * @param charArray
	 * @param begin
	 * @param length
	 * @param searchHit
	 * @return Hit 
	 */
	public Hit match(char[] charArray , int begin , int length , Hit searchHit){
		
		if(searchHit == null){
			//���hitΪ�գ��½�
			searchHit= new Hit();
			//����hit����ʵ�ı�λ��
			searchHit.setBegin(begin);
		}else{
			//����Ҫ��HIT״̬����
			searchHit.setUnmatch();
		}
		//����hit�ĵ�ǰ����λ��
		searchHit.setEnd(begin);
		
		Character keyChar = new Character(charArray[begin]);
		DictSegment ds = null;
		
		//����ʵ������Ϊ���ر����������ѯʱ�������µ�ͬ������
		DictSegment[] segmentArray = this.childrenArray;
		Map<Character , DictSegment> segmentMap = this.childrenMap;		
		
		//STEP1 �ڽڵ��в���keyChar��Ӧ��DictSegment
		if(segmentArray != null){
			//�������в���
			for(DictSegment seg : segmentArray){
				if(seg != null && seg.nodeChar.equals(keyChar)){
					//�ҵ�ƥ��Ķ�
					ds = seg;
				}
			}
		}else if(segmentMap != null){
			//��map�в���
			ds = (DictSegment)segmentMap.get(keyChar);
		}
		
		//STEP2 �ҵ�DictSegment���жϴʵ�ƥ��״̬���Ƿ�����ݹ飬���Ƿ��ؽ��
		if(ds != null){			
			if(length > 1){
				//��δƥ���꣬������������
				return ds.match(charArray, begin + 1 , length - 1 , searchHit);
			}else if (length == 1){
				
				//�������һ��char
				if(ds.nodeState == 1){
					//���HIT״̬Ϊ��ȫƥ��
					searchHit.setMatch();
				}
				if(ds.hasNextNode()){
					//���HIT״̬Ϊǰ׺ƥ��
					searchHit.setPrefix();
					//��¼��ǰλ�õ�DictSegment
					searchHit.setMatchedDictSegment(ds);
				}
				return searchHit;
			}
			
		}
		//STEP3 û���ҵ�DictSegment�� ��HIT����Ϊ��ƥ��
		return searchHit;		
	}

	/**
	 * �������ʵ�Ƭ��
	 * @param charArray
	 */
	public void fillSegment(char[] charArray){
		this.fillSegment(charArray, 0 , charArray.length); 
	}
	
	/**
	 * �������ʵ�Ƭ��
	 * @param charArray
	 * @param begin
	 * @param length
	 */
	public synchronized void fillSegment(char[] charArray , int begin , int length){
		//��ȡ�ֵ���еĺ��ֶ���
		Character beginChar = new Character(charArray[begin]);
		Character keyChar = charMap.get(beginChar);
		//�ֵ���û�и��֣�����������ֵ�
		if(keyChar == null){
			charMap.put(beginChar, beginChar);
			keyChar = beginChar;
		}
		
		//������ǰ�ڵ�Ĵ洢����ѯ��ӦkeyChar��keyChar�����û���򴴽�
		DictSegment ds = lookforSegment(keyChar);
		//����keyChar��Ӧ��segment
		if(length > 1){
			//��Ԫ��û����ȫ����ʵ���
			ds.fillSegment(charArray, begin + 1, length - 1);
		}else if (length == 1){
			//�Ѿ��Ǵ�Ԫ�����һ��char,���õ�ǰ�ڵ�״̬Ϊ1������һ�������Ĵ�
			ds.nodeState = 1;
		}

	}
	
	/**
	 * ���ұ��ڵ��¶�Ӧ��keyChar��segment
	 * ���û���ҵ����򴴽��µ�segment
	 * @param keyChar
	 * @return
	 */
	private DictSegment lookforSegment(Character keyChar){
		
		DictSegment ds = null;

		if(this.storeSize <= ARRAY_LENGTH_LIMIT){
			//��ȡ�����������������δ�����򴴽�����
			DictSegment[] segmentArray = getChildrenArray();			
			//��Ѱ����
			for(DictSegment segment : segmentArray){
				if(segment != null && segment.nodeChar.equals(keyChar)){
					//���������ҵ���keyChar��Ӧ��segment
					ds =  segment;
					break;
				}
			}			
			//���������û���ҵ���Ӧ��segment
			if(ds == null){
				//�����µ�segment
				ds = new DictSegment(keyChar);				
				if(this.storeSize < ARRAY_LENGTH_LIMIT){
					//��������δ����ʹ������洢
					segmentArray[this.storeSize] = ds;
					//segment��Ŀ+1
					this.storeSize++;
				}else{
					//���������������л�Map�洢
					//��ȡMap���������Mapδ����,�򴴽�Map
					Map<Character , DictSegment> segmentMap = getChildrenMap();
					//�������е�segmentǨ�Ƶ�Map��
					migrate(segmentArray ,  segmentMap);
					//�洢�µ�segment
					segmentMap.put(keyChar, ds);
					//segment��Ŀ+1 ��  �������ͷ�����ǰִ��storeSize++ �� ȷ����������£�����ȡ���յ�����
					this.storeSize++;
					//�ͷŵ�ǰ����������
					this.childrenArray = null;
				}

			}			
			
		}else{
			//��ȡMap���������Mapδ����,�򴴽�Map
			Map<Character , DictSegment> segmentMap = getChildrenMap();
			//����Map
			ds = (DictSegment)segmentMap.get(keyChar);
			if(ds == null){
				//�����µ�segment
				ds = new DictSegment(keyChar);
				segmentMap.put(keyChar , ds);
				//��ǰ�ڵ�洢segment��Ŀ+1
				this.storeSize ++;
			}
		}

		return ds;
	}
	
	
	/**
	 * ��ȡ��������
	 * �߳�ͬ������
	 */
	private DictSegment[] getChildrenArray(){
		if(this.childrenArray == null){
			synchronized(this){
				if(this.childrenArray == null){
					this.childrenArray = new DictSegment[ARRAY_LENGTH_LIMIT];
				}
			}
		}
		return this.childrenArray;
	}
	
	/**
	 * ��ȡMap����
	 * �߳�ͬ������
	 */	
	private Map<Character , DictSegment> getChildrenMap(){
		if(this.childrenMap == null){
			synchronized(this){
				if(this.childrenMap == null){
					this.childrenMap = new HashMap<Character , DictSegment>(ARRAY_LENGTH_LIMIT * 2,0.8f);
				}
			}
		}
		return this.childrenMap;
	}
	
	/**
	 * �������е�segmentǨ�Ƶ�Map��
	 * @param segmentArray
	 */
	private void migrate(DictSegment[] segmentArray , Map<Character , DictSegment> segmentMap){
		for(DictSegment segment : segmentArray){
			if(segment != null){
				segmentMap.put(segment.nodeChar, segment);
			}
		}
	}
	
}

