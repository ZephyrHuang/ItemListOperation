package itemCOOperation.fileCO;

import itemCOOperation.ItemCO;

/**
 * @author Zephyr Huang
 *
 */
public abstract class AbstractFileCO<T> implements ItemCO<T> {

	public abstract boolean copyToAccordingToName(T desItemList);

	public abstract boolean copyFromAccordingToName(T desItemList);

	public boolean copyToAccordingToContent(T desItemList) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean copyFromAccordingToContent(T desItemList) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean copyToAccordingToLastModifiedTime(T desItemList,
			String whichToRetain) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean copyFromAccordingToLastModifiedTime(T desItemList,
			String whichToRetain) {
		// TODO Auto-generated method stub
		return false;
	}

	public abstract boolean syncDirStructure(T desItemList);
}
