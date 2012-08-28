/**
 * 
 */
package org.wltea.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.handsomestone.cfg.ConfigurationDialect;
import org.wltea.analyzer.help.CharacterHelper;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * IK Analyzer v3.2
 * IK���ִ���
 * ע��IKSegmentation��һ��lucene�޹ص�ͨ�÷ִ���
 * @author ������
 *
 */
public final class IKSegmentation{

	
	private Reader input;	
	//Ĭ�ϻ�������С
	private static final int BUFF_SIZE = 3072;
	//�������ľ����ٽ�ֵ
	private static final int BUFF_EXHAUST_CRITICAL = 48;	
    //�ַ��ܶ�ȡ����
    private char[] segmentBuff;
	//�ִ���������
	private Context context;
	//�ִʴ������б�
	private List<ISegmenter> segmenters;
    //����ģʽ�ִ���
	private ISegmenter segmenter;
	/**
	 * IK���ִ������캯��
	 * Ĭ����ϸ�����з�
	 * @param input
	 */
	public IKSegmentation(Reader input){
		this(input , false);
	}
    
	/**
	 * IK���ִ������캯��
	 * @param input
	 * @param isMaxWordLength ��Ϊtrueʱ���ִ����������ʳ��з�
	 */
	public IKSegmentation(Reader input , boolean isMaxWordLength){
		this.input = input ;
		segmentBuff = new char[BUFF_SIZE];
		context = new Context(segmentBuff , isMaxWordLength);
		segmenters = ConfigurationDialect.loadSegmenter();

	}
	public IKSegmentation(boolean isMaxWordLength){
		segmentBuff = new char[BUFF_SIZE];
		context = new Context(segmentBuff , isMaxWordLength);
		segmenters = ConfigurationDialect.loadSegmenter();
	}
	public void setInput(Reader input)
	{
		this.input = input;
	}
	/**
	 * IK���ִ������캯��
	 * @param input
	 * @param isMaxWordLength Ĭ��Ϊtrue���ִ����������ʳ��з�
	 * @param segmenter ���Եķִ������
	 */
	 public IKSegmentation(Reader input,ISegmenter segmenter)
	{
		this.input =  input;
		segmentBuff = new char[BUFF_SIZE];
		context = new Context(segmentBuff,true);
		this.segmenter = segmenter;
	}
	/**
	 * ���Էִ�����ȡ��һ�����嵥Ԫ
	 * @param isLan Ϊtrue,�������Էִ�ģʽ
	 * @return û�и���Ĵ�Ԫ���򷵻�null
	 * @throws IOException
	 */
	public synchronized Lexeme next(boolean isLan) throws IOException {
		if(context.getResultSize() == 0){
			/*
			 * ��reader�ж�ȡ���ݣ����buffer
			 * ���reader�Ƿִζ���buffer�ģ���ôbufferҪ������λ����
			 * ��λ�����ϴζ���ĵ�δ���������
			 */
			int available = fillBuffer(input);
			
            if(available <= 0){
            	context.resetContext();
                return null;
            }else{
            	//�ִʴ���
            	int analyzedLength = 0;
        		for(int buffIndex = 0 ; buffIndex < available ;  buffIndex++){
        			//�жϷ��Էִ����Ƿ�����
        			if(isLan == false)
        			{
        				break;
        			}	
        			//�ƶ�������ָ��
        			context.setCursor(buffIndex);
        			//�����ַ���񻯣�ȫ��ת��ǣ���дתСд����
        			segmentBuff[buffIndex] = CharacterHelper.regularize(segmentBuff[buffIndex]);
        			//�����ӷִ���
        			
        				this.segmenter.nextLexeme(segmentBuff , context);
        			
        			analyzedLength++;
        			/*
        			 * ����һ������ʱ��
        			 * 1.available == BUFF_SIZE ��ʾbuffer����
        			 * 2.buffIndex < available - 1 && buffIndex > available - BUFF_EXHAUST_CRITICAL��ʾ��ǰָ�봦���ٽ�����
        			 * 3.!context.isBufferLocked()��ʾû��segmenter��ռ��buffer
        			 * Ҫ�жϵ�ǰѭ����bufferҪ������λ�����ٶ�ȡ���ݵĲ�����
        			 */        			
        			if(available == BUFF_SIZE
        					&& buffIndex < available - 1   
        					&& buffIndex > available - BUFF_EXHAUST_CRITICAL
        					&& !context.isBufferLocked()){

        				break;
        			}
        		}
				
				
					this.segmenter.reset();
				
        		//System.out.println(available + " : " +  buffIndex);
            	//��¼���һ�η������ַ�����
        		context.setLastAnalyzed(analyzedLength);
            	//ͬʱ�ۼ��ѷ������ַ�����
        		context.setBuffOffset(context.getBuffOffset() + analyzedLength);
        		//���ʹ������з֣�����˽����Ķ̴�Ԫ
        		if(context.isMaxWordLength()){
        			context.excludeOverlap();
        		}
            	//��ȡ��Ԫ���еĴ�Ԫ
            	return buildLexeme(context.firstLexeme());
            }
		}else{
			//��ȡ��Ԫ���е����д�Ԫ
			return buildLexeme(context.firstLexeme());
		}	
	
	}
	
	/**
	 * ��ȡ��һ�����嵥Ԫ
	 * @return û�и���Ĵ�Ԫ���򷵻�null
	 * @throws IOException
	 */
	public synchronized Lexeme next() throws IOException {
		if(context.getResultSize() == 0){
			/*
			 * ��reader�ж�ȡ���ݣ����buffer
			 * ���reader�Ƿִζ���buffer�ģ���ôbufferҪ������λ����
			 * ��λ�����ϴζ���ĵ�δ���������
			 */
			int available = fillBuffer(input);
			
            if(available <= 0){
            	context.resetContext();
                return null;
            }else{
            	//�ִʴ���
            	int analyzedLength = 0;
        		for(int buffIndex = 0 ; buffIndex < available ;  buffIndex++){
        			//�ƶ�������ָ��
        			context.setCursor(buffIndex);
        			//�����ַ���񻯣�ȫ��ת��ǣ���дתСд����
        			segmentBuff[buffIndex] = CharacterHelper.regularize(segmentBuff[buffIndex]);
        			//�����ӷִ���
        			for(ISegmenter segmenter : segmenters){
        				segmenter.nextLexeme(segmentBuff , context);
        			}
        			analyzedLength++;
        			/*
        			 * ����һ������ʱ��
        			 * 1.available == BUFF_SIZE ��ʾbuffer����
        			 * 2.buffIndex < available - 1 && buffIndex > available - BUFF_EXHAUST_CRITICAL��ʾ��ǰָ�봦���ٽ�����
        			 * 3.!context.isBufferLocked()��ʾû��segmenter��ռ��buffer
        			 * Ҫ�жϵ�ǰѭ����bufferҪ������λ�����ٶ�ȡ���ݵĲ�����
        			 */        			
        			if(available == BUFF_SIZE
        					&& buffIndex < available - 1   
        					&& buffIndex > available - BUFF_EXHAUST_CRITICAL
        					&& !context.isBufferLocked()){

        				break;
        			}
        		}
				
				for(ISegmenter segmenter : segmenters){
					segmenter.reset();
				}
        		//System.out.println(available + " : " +  buffIndex);
            	//��¼���һ�η������ַ�����
        		context.setLastAnalyzed(analyzedLength);
            	//ͬʱ�ۼ��ѷ������ַ�����
        		context.setBuffOffset(context.getBuffOffset() + analyzedLength);
        		//���ʹ������з֣�����˽����Ķ̴�Ԫ
        		if(context.isMaxWordLength()){
        			context.excludeOverlap();
        		}
            	//��ȡ��Ԫ���еĴ�Ԫ
            	return buildLexeme(context.firstLexeme());
            }
		}else{
			//��ȡ��Ԫ���е����д�Ԫ
			return buildLexeme(context.firstLexeme());
		}	
	}
	
    /**
     * ����context����������������segmentBuff 
     * @param reader
     * @return ���ش������ģ���Ч�ģ��ִ�����
     * @throws IOException 
     */
    private int fillBuffer(Reader reader) throws IOException{
    	int readCount = 0;
    	if(context.getBuffOffset() == 0){
    		//�״ζ�ȡreader
    		readCount = reader.read(segmentBuff);
    	}else{
    		int offset = context.getAvailable() - context.getLastAnalyzed();
    		if(offset > 0){
    			//���һ�ζ�ȡ��>���һ�δ���ģ���δ������ִ�������segmentBuffͷ��
    			System.arraycopy(segmentBuff , context.getLastAnalyzed() , this.segmentBuff , 0 , offset);
    			readCount = offset;
    		}
    		//������ȡreader ����onceReadIn - onceAnalyzedΪ��ʼλ�ã��������segmentBuffʣ��Ĳ���
    		readCount += reader.read(segmentBuff , offset , BUFF_SIZE - offset);
    	}            	
    	//��¼���һ�δ�Reader�ж���Ŀ����ַ�����
    	context.setAvailable(readCount);
    	return readCount;
    }	
	
    /**
     * ȡ����Ԫ�����е���һ����Ԫ
     * @return Lexeme
     */
    private Lexeme buildLexeme(Lexeme lexeme){
    	if(lexeme != null){
			//����lexeme�Ĵ�Ԫ�ı�
			lexeme.setLexemeText(String.valueOf(segmentBuff , lexeme.getBegin() , lexeme.getLength()));
			return lexeme;
			
		}else{
			return null;
		}
    }

    /**
     * ���÷ִ�������ʼ״̬
     * @param input
     */
	public synchronized void reset(Reader input) {
		this.input = input;
		context.resetContext();
		for(ISegmenter segmenter : segmenters){
			segmenter.reset();
		}
	}

}

