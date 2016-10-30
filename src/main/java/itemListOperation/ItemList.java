package itemListOperation;

/**
 * item可以是某个目录下的所有文件、文本中特定内容组成的List等等
 * @author Zephyr Huang 
 */
public interface ItemList<T extends ItemList> {
	/********************copy操作********************/
	/**
	 * 若“文件名不存在”则copy至desItemList，若“文件名存在”则不操作
	 * @param itemList
	 * @return
	 */
	boolean copyToAccordingToName(T desItemList);
	
	/**
	 * 若“文件名不存在”则copy自desItemList，若“文件名存在”则不操作
	 * @param itemList
	 * @return
	 */
	boolean copyFromAccordingToName(T desItemList);
	
	/**
	 * 若“文件名不存在”或“文件名存在但内容不同”则copy至desItemList，若“文件名存在且内容相同”则不操作
	 * @param desItemList
	 * @return
	 */
	boolean copyToAccordingToContent(T desItemList);
	
	/**
	 * 若“文件名不存在”或“文件名存在但内容不同”则copy自desItemList，若“文件名存在且内容相同”则不操作
	 * @param desItemList
	 * @return
	 */
	boolean copyFromAccordingToContent(T desItemList);
	
	/**
	 * 若“文件名不存在”或“文件名存在但修改时间不同”则copy至desItemList，若“文件名存在且修改时间相同”则不操作
	 * @param desItemList
	 * @param whichToRetain
	 * @return
	 */
	boolean copyToAccordingToLastModifiedTime(T desItemList, String whichToRetain);
	
	/**
	 * 若“文件名不存在”或“文件名存在但修改时间不同”则copy至desItemList，若“文件名存在且修改时间相同”则不操作
	 * @param desItemList
	 * @param whichToRetain
	 * @return
	 */
	boolean copyFromAccordingToLastModifiedTime(T desItemList, String whichToRetain);
}
