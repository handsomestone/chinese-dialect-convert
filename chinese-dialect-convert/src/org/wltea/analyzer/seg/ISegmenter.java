/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;

/**
 * �ӷִ����ӿ�
 * @author ������
 *
 */
public interface ISegmenter {
	
	/**
	 * �ӷ�������ȡ��һ�����ֽܷ�Ĵ�Ԫ����
	 * @param segmentBuff �ı�����
	 * @param context �ִ��㷨������
	 */
	void nextLexeme(char[] segmentBuff , Context context);
	
	/**
	 * �����ӷ�����״̬
	 */
	void reset();
	/**
	 * ��÷ִ������������
	 */	
	String getLanType();
}

