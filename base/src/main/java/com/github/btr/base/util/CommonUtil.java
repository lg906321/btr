package com.github.btr.base.util;

/**
 * 当前线程下常用工具
 * 一个线程只需要一个就好
 * 减少new开销和噪声
 * Created by ryze on 2017/4/29.
 */
public class CommonUtil
{
	/**
	 * 线程工具 String
	 */
	private static final ThreadLocal<StringHolder>  STRING_HOLDER_THREAD_LOCAL = ThreadLocal.withInitial(StringHolder::new);

	public static StringBuilder getStringBuilder()
	{
		return STRING_HOLDER_THREAD_LOCAL.get().getStringBuilder();
	}

	public static StringBuffer getStringBuffer()
	{
		return STRING_HOLDER_THREAD_LOCAL.get().getStringBuffer();
	}

	private static class StringHolder
	{
		private final StringBuilder sBuilder;
		private final StringBuffer  sBuffer;
		//创建时候指定默认长度为16
		private final int size = 16;
		private final int zero = 0;

		StringHolder()
		{
			sBuilder = new StringBuilder(size);
			sBuffer = new StringBuffer(size);
		}

		StringBuilder getStringBuilder()
		{
			//将上次使用数据清空 线程重复使用单个StringBuilder
			sBuilder.setLength(zero);
			return sBuilder;
		}

		StringBuffer getStringBuffer()
		{
			sBuffer.setLength(zero);
			return sBuffer;
		}
	}
}
