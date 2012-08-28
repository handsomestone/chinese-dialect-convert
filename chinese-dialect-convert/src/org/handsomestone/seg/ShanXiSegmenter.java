/**
 * 
 */
package org.handsomestone.seg;

import java.util.LinkedList;
import java.util.List;

import org.handsomestone.dic.LanDictionary;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.dic.Hit;
import org.wltea.analyzer.help.CharacterHelper;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * ������Shanxi����Ԫ�����ӷִ���������һ�·�Χ
 * 1.����
 * 2.�̾�
 * 3.ר������
 * @author ʯ����
 * @version 1.0
 */
public class ShanXiSegmenter implements ISegmenter {
	/*
	 * ����ɴ����λ��
	 */
	private int doneIndex;

	
	/*
	 * Hit���У���¼ƥ���е�Hit����
	 */
	private List<Hit> hitList;

	
	
	public ShanXiSegmenter(){
		doneIndex = -1;	
		hitList = new LinkedList<Hit>();
		}
	public String getLanType()
	{
		return "ShanXi";
	}
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.seg.ISegmenter#nextLexeme(org.wltea.analyzer.Context)
	 */
	public void nextLexeme(char[] segmentBuff , Context context) {

		//��ȡ��ǰλ�õ�char	
		char input = segmentBuff[context.getCursor()];
		
		if(CharacterHelper.isCJKCharacter(input)){//�ǣ�CJK���ַ�������д���
			if(hitList.size() > 0){
				//����ʶζ���
				Hit[] tmpArray = hitList.toArray(new Hit[hitList.size()]);
				for(Hit hit : tmpArray){
					hit = LanDictionary.matchWithHit(segmentBuff, context.getCursor() , hit);
					
					if(hit.isMatch()){//ƥ��ɴ�
						//�ж��Ƿ��в���ʶ��Ĵʶ�
						if(hit.getBegin() > doneIndex + 1){
							//����������doneIndex+1 �� seg.start - 1֮���δ֪�ʶ�
							processUnknown(segmentBuff , context , doneIndex + 1 , hit.getBegin()- 1);
						}
						//�����ǰ�Ĵ�
						Lexeme newLexeme = new Lexeme(context.getBuffOffset() , hit.getBegin() , context.getCursor() - hit.getBegin() + 1 , Lexeme.TYPE_CJK_NORMAL);
						context.addLexeme(newLexeme);
						//����goneIndex����ʶ�Ѵ���
						if(doneIndex < context.getCursor()){
							doneIndex = context.getCursor();
						}
						
						if(hit.isPrefix()){//ͬʱҲ��ǰ׺
							
						}else{ //���治�ٿ�����ƥ����
							//�Ƴ���ǰ��hit
							hitList.remove(hit);
						}
						
					}else if(hit.isPrefix()){//ǰ׺��δƥ��ɴ�
						
					}else if(hit.isUnmatch()){//��ƥ��
						//�Ƴ���ǰ��hit
						hitList.remove(hit);
					}
				}
			}
			
			//������inputΪ��ʼ��һ����hit
			Hit hit = LanDictionary.matchInShanxiDict(segmentBuff, context.getCursor() , 1);
			if(hit.isMatch()){//ƥ��ɴ�
				//�ж��Ƿ��в���ʶ��Ĵʶ�
				if(context.getCursor() > doneIndex + 1){
					//����������doneIndex+1 �� context.getCursor()- 1֮���δ֪
					processUnknown(segmentBuff , context , doneIndex + 1 , context.getCursor()- 1);
				}
				//�����ǰ�Ĵ�
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , context.getCursor() , 1 , Lexeme.TYPE_CJK_NORMAL);
				context.addLexeme(newLexeme);
				//����doneIndex����ʶ�Ѵ���
				if(doneIndex < context.getCursor()){
					doneIndex = context.getCursor();
				}

				if(hit.isPrefix()){//ͬʱҲ��ǰ׺
					//��ʶζ��������µ�Hit
					hitList.add(hit);
				}
				
			}else if(hit.isPrefix()){//ǰ׺��δƥ��ɴ�
				//��ʶζ��������µ�Hit
				hitList.add(hit);
				
			}else if(hit.isUnmatch()){//��ƥ�䣬��ǰ��input���Ǵʣ�Ҳ���Ǵ�ǰ׺��������Ϊ�ָ��Ե��ַ�
				if(doneIndex >= context.getCursor()){
					//��ǰ��ƥ����ַ��Ѿ���������ˣ�����Ҫ��processUnknown
					return;
				}
				
				//�����doneIndex����ǰ�ַ�������ǰ�ַ���֮���δ֪��
				processUnknown(segmentBuff , context , doneIndex + 1 , context.getCursor());
				//processMain(segmentBuff , context , doneIndex + 1 , context.getCursor());
			
				//����doneIndex����ʶ�Ѵ���
				doneIndex = context.getCursor();
			}
			
		}else {//����Ĳ�������(CJK)�ַ�
			if(hitList.size() > 0
					&&  doneIndex < context.getCursor() - 1){
				for(Hit hit : hitList){
					//�ж��Ƿ��в���ʶ��Ĵʶ�
					if(doneIndex < hit.getEnd()){
						//����������doneIndex+1 �� seg.end֮���δ֪�ʶ�
						processUnknown(segmentBuff , context , doneIndex + 1 , hit.getEnd());
					}
				}
			}
			//��մʶζ���
			hitList.clear();
			//����doneIndex����ʶ�Ѵ���
			if(doneIndex < context.getCursor()){
				doneIndex = context.getCursor();
			}
		}
		
		//�����������ٽ紦��
		if(context.getCursor() == context.getAvailable() - 1){ //��ȡ���������������һ���ַ�			
			if( hitList.size() > 0 //�����л���δ����ʶ�
				&& doneIndex < context.getCursor()){//���һ���ַ���δ�������
				for(Hit hit : hitList){
					//�ж��Ƿ��в���ʶ��Ĵʶ�
					if(doneIndex < hit.getEnd() ){
						//����������doneIndex+1 �� seg.end֮���δ֪�ʶ�
						processUnknown(segmentBuff , context , doneIndex + 1 , hit.getEnd());
					}
				}
			}
			//��մʶζ���
			hitList.clear();;
		}
		
		//�ж��Ƿ�����������
		if(hitList.size() == 0){
			context.unlockBuffer(this);
			
		}else{
			context.lockBuffer(this);
	
		}
	}
	
	/**
	 * ����δ֪�ʶ�
	 * @param segmentBuff 
	 * @param uBegin ��ʼλ��
	 * @param uEnd ��ֹλ��
	 */
	private void processUnknown(char[] segmentBuff , Context context , int uBegin , int uEnd){
		Lexeme newLexeme = null;
		
		//�Ե������δ֪�ʶ�
		for(int i = uBegin ; i <= uEnd ; i++){
			newLexeme = new Lexeme(context.getBuffOffset() , i , 1  , Lexeme.TYPE_CJK_UNKNOWN);
			context.addLexeme(newLexeme);	
			
		}		
		
	}
	
	public void reset() {
		//�����Ѵ����ʶ
		doneIndex = -1;
		hitList.clear();
	}
}



