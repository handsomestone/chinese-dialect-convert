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
 * 陕西（Shanxi）词元处理子分词器，涵盖一下范围
 * 1.短语
 * 2.短句
 * 3.专有名词
 * @author 石俊杰
 * @version 1.0
 */
public class ShanXiSegmenter implements ISegmenter {
	/*
	 * 已完成处理的位置
	 */
	private int doneIndex;

	
	/*
	 * Hit对列，记录匹配中的Hit对象
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

		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		if(CharacterHelper.isCJKCharacter(input)){//是（CJK）字符，则进行处理
			if(hitList.size() > 0){
				//处理词段队列
				Hit[] tmpArray = hitList.toArray(new Hit[hitList.size()]);
				for(Hit hit : tmpArray){
					hit = LanDictionary.matchWithHit(segmentBuff, context.getCursor() , hit);
					
					if(hit.isMatch()){//匹配成词
						//判断是否有不可识别的词段
						if(hit.getBegin() > doneIndex + 1){
							//输出并处理从doneIndex+1 到 seg.start - 1之间的未知词段
							processUnknown(segmentBuff , context , doneIndex + 1 , hit.getBegin()- 1);
						}
						//输出当前的词
						Lexeme newLexeme = new Lexeme(context.getBuffOffset() , hit.getBegin() , context.getCursor() - hit.getBegin() + 1 , Lexeme.TYPE_CJK_NORMAL);
						context.addLexeme(newLexeme);
						//更新goneIndex，标识已处理
						if(doneIndex < context.getCursor()){
							doneIndex = context.getCursor();
						}
						
						if(hit.isPrefix()){//同时也是前缀
							
						}else{ //后面不再可能有匹配了
							//移出当前的hit
							hitList.remove(hit);
						}
						
					}else if(hit.isPrefix()){//前缀，未匹配成词
						
					}else if(hit.isUnmatch()){//不匹配
						//移出当前的hit
						hitList.remove(hit);
					}
				}
			}
			
			//处理以input为开始的一个新hit
			Hit hit = LanDictionary.matchInShanxiDict(segmentBuff, context.getCursor() , 1);
			if(hit.isMatch()){//匹配成词
				//判断是否有不可识别的词段
				if(context.getCursor() > doneIndex + 1){
					//输出并处理从doneIndex+1 到 context.getCursor()- 1之间的未知
					processUnknown(segmentBuff , context , doneIndex + 1 , context.getCursor()- 1);
				}
				//输出当前的词
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , context.getCursor() , 1 , Lexeme.TYPE_CJK_NORMAL);
				context.addLexeme(newLexeme);
				//更新doneIndex，标识已处理
				if(doneIndex < context.getCursor()){
					doneIndex = context.getCursor();
				}

				if(hit.isPrefix()){//同时也是前缀
					//向词段队列增加新的Hit
					hitList.add(hit);
				}
				
			}else if(hit.isPrefix()){//前缀，未匹配成词
				//向词段队列增加新的Hit
				hitList.add(hit);
				
			}else if(hit.isUnmatch()){//不匹配，当前的input不是词，也不是词前缀，将其视为分割性的字符
				if(doneIndex >= context.getCursor()){
					//当前不匹配的字符已经被处理过了，不需要再processUnknown
					return;
				}
				
				//输出从doneIndex到当前字符（含当前字符）之间的未知词
				processUnknown(segmentBuff , context , doneIndex + 1 , context.getCursor());
				//processMain(segmentBuff , context , doneIndex + 1 , context.getCursor());
			
				//更新doneIndex，标识已处理
				doneIndex = context.getCursor();
			}
			
		}else {//输入的不是中文(CJK)字符
			if(hitList.size() > 0
					&&  doneIndex < context.getCursor() - 1){
				for(Hit hit : hitList){
					//判断是否有不可识别的词段
					if(doneIndex < hit.getEnd()){
						//输出并处理从doneIndex+1 到 seg.end之间的未知词段
						processUnknown(segmentBuff , context , doneIndex + 1 , hit.getEnd());
					}
				}
			}
			//清空词段队列
			hitList.clear();
			//更新doneIndex，标识已处理
			if(doneIndex < context.getCursor()){
				doneIndex = context.getCursor();
			}
		}
		
		//缓冲区结束临界处理
		if(context.getCursor() == context.getAvailable() - 1){ //读取缓冲区结束的最后一个字符			
			if( hitList.size() > 0 //队列中还有未处理词段
				&& doneIndex < context.getCursor()){//最后一个字符还未被输出过
				for(Hit hit : hitList){
					//判断是否有不可识别的词段
					if(doneIndex < hit.getEnd() ){
						//输出并处理从doneIndex+1 到 seg.end之间的未知词段
						processUnknown(segmentBuff , context , doneIndex + 1 , hit.getEnd());
					}
				}
			}
			//清空词段队列
			hitList.clear();;
		}
		
		//判断是否锁定缓冲区
		if(hitList.size() == 0){
			context.unlockBuffer(this);
			
		}else{
			context.lockBuffer(this);
	
		}
	}
	
	/**
	 * 处理未知词段
	 * @param segmentBuff 
	 * @param uBegin 起始位置
	 * @param uEnd 终止位置
	 */
	private void processUnknown(char[] segmentBuff , Context context , int uBegin , int uEnd){
		Lexeme newLexeme = null;
		
		//以单字输出未知词段
		for(int i = uBegin ; i <= uEnd ; i++){
			newLexeme = new Lexeme(context.getBuffOffset() , i , 1  , Lexeme.TYPE_CJK_UNKNOWN);
			context.addLexeme(newLexeme);	
			
		}		
		
	}
	
	public void reset() {
		//重置已处理标识
		doneIndex = -1;
		hitList.clear();
	}
}



