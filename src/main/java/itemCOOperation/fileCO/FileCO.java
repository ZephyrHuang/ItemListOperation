package itemCOOperation.fileCO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import itemCOOperation.common.constants.CommonCons;
import itemCOOperation.common.utils.CopyTo;
import itemCOOperation.common.utils.Info;

//import org.apache.commons.collections4.CollectionUtils;

/**
 * FileList对象包含一个目录和该目录下所有的子目录（不递归）和文件
 * @author Zephyr Huang
 *
 */
public class FileCO extends AbstractFileCO<FileCO>{
	//根目录文件
	private File rootDir = null;
	//根目录下的文件列表
	private List<File> subFileList = new LinkedList<File>();
	//根目录下的子文件夹列表
	private List<File> subDirList = new LinkedList<File>();
	
	/********************constructors********************/
	public FileCO(String path) {
		this(new File(path));
	}
	
	public FileCO(File rootDir) {
		//必须传入一个目录
		if(!rootDir.isDirectory()) {
			Info.error(CommonCons.A_DIRECTORY_NEEDED + rootDir.getAbsolutePath());
			throw new RuntimeException();
		}
		this.rootDir = rootDir;
		File tempFile = null;
		for(String fileName: rootDir.list()) {
			tempFile = new File(rootDir, fileName);
			if(tempFile.isDirectory() && tempFile.exists()) {
				subDirList.add(tempFile);
			}else if(tempFile.isFile() && tempFile.exists()){
				subFileList.add(tempFile);
			}
		}
	}
	
	public FileCO(FileCO fileList) {
		this.rootDir = new File(fileList.getRootDir().getAbsolutePath());
		this.subDirList.addAll(fileList.getSubDirList());
		this.subFileList.addAll(fileList.getSubFileList());
	}
	
	/********************copy 和 check 操作********************/
	
	/**
	 * 若“文件名不存在”则 copy 至 desFileCO，若“文件名存在”则不操作
	 */
	@Override
	public boolean copyToAccordingToName(FileCO desFileCO) {
		if(desFileCO == null) {
			return false;
		}
		/*若目标文件夹不存在则新建该文件夹*/
		if(!desFileCO.getRootDir().exists()) {
			desFileCO.getRootDir().mkdir();
		}
		
		
		Info.info("开始对目录内文件进行复制：" + rootDir.getAbsolutePath() + " --> " + desFileCO.getRootDir().getAbsolutePath());
		/*复制文件*/
		List<File> filesToCopy = getFilesToCopy(subFileList, desFileCO.getRootDir());
		File desFile = null;
		for(File srcFile : filesToCopy) {
			desFile = new File(desFileCO.getRootDir(), srcFile.getName());
			Info.info("   开始复制文件：" + srcFile.getAbsolutePath());
			CopyTo.copyTo(srcFile, desFile);
			Info.info("   文件复制结束：" + srcFile.getAbsolutePath());
		}

		/*复制结束后同步srcFileCO和desFileCO中的文件和文件夹*/
		syncDirStructure(desFileCO);
		
		/*对子文件夹进行递归操作*/
		File desSubDir = null;
		for(File srcSubDir : subDirList) {
			if(srcSubDir.getName().startsWith(CommonCons.FILECO_DELETED)) {
				continue;
			}
			try {
				desSubDir = new File(desFileCO.getRootDir(), srcSubDir.getName());
				desSubDir.mkdir();
				Info.info("   开始对 " + srcSubDir.getAbsolutePath() + " 进行递归复制。");
				new FileCO(srcSubDir).copyToAccordingToName(new FileCO(desSubDir));
			} catch (Exception e) {
				Info.error("Error occurs while doing recursive copy operation:" + srcSubDir.getAbsolutePath());
				e.printStackTrace();
				return false;
			}
		}
		
		/*本FileCO的复制工作完成*/
		return true;
	}

	/**
	 * 若“文件名不存在”则copy自desFileCO，若“文件名存在”则不操作
	 */
	@Override
	public boolean copyFromAccordingToName(FileCO desFileCO) {
		if(desFileCO == null) {
			return false;
		}
		return desFileCO.copyToAccordingToName(this);
	}
	
	/**
	 * 同步srcFileCO和desFileCO中的文件和文件夹
	 * 1. 将已被标记(ToBeDeleted)的文件和文件夹置空。<br/>
	 * 2. 若desFile不存在且srcFile大小为0，则标记(Deleted)。<br/>
	 * 3. 若desFile不存在且srcFile大小不为0，则警告：存在未复制的文件。<br/>
	 * 4. 若desFile存在且它和srcFile的大小相同，都不为0，则将srcFile标记(ToBeDeleted)。<br/>
	 * 5. 若desFile存在且它和srcFile的大小不同，则警告。<br/>
	 * 6. 若desFile对应的srcFile不存在，则创建该空文件srcFile。
	 * 
	 * @param desFileCO
	 * @return true if succeeded;false otherwise
	 */
	@Override
	public boolean syncDirStructure(FileCO desFileCO) {
		Info.info("开始复制目录结构  " + rootDir.getAbsolutePath() + " <-- " + desFileCO.getRootDir().getAbsolutePath());
		File tempFile = null;
		List<File> tmpSubFileList = new LinkedList<File>();
		List<File> tmpSubDirList  = new LinkedList<File>();
		tmpSubFileList.addAll(subFileList);
		tmpSubDirList.addAll(subDirList);
		
		//1. 删除被标记文件
		for(File srcFile : tmpSubFileList) {
			//删除所有被标记(ToBeDeleted)或(Deleted)的文件
			if(srcFile.getName().startsWith(CommonCons.FILECO_TOBEDELETED) || srcFile.getName().startsWith(CommonCons.FILECO_DELETED)) {
				if(srcFile.delete()) {
					subFileList.remove(srcFile);
				}
			}
		}

		//2. 删除被标记文件夹
		for(File srcDir : tmpSubDirList) {
			//删除被标记(Deleted)的文件夹
			if(srcDir.getName().startsWith(CommonCons.FILECO_DELETED)) {
				if(srcDir.delete()) {
					subDirList.remove(srcDir);
				}
			}
		}

		//3. 创建des中存在而src不存在的文件和文件夹
		for(File desFile : desFileCO.getSubFileList()) {
			tempFile = new File(this.rootDir, desFile.getName());
			if(!tempFile.exists() || !tempFile.isFile()) {
				try {
					if(tempFile.createNewFile()) {
						subFileList.add(tempFile);
					}
				} catch (IOException e) {
					Info.error("创建原本不存在的空文件时出错：" + desFile.getAbsolutePath());
					return false;
				}
			}
		}
		for(File desDir : desFileCO.getSubDirList()) {
			tempFile = new File(this.rootDir, desDir.getName());
			if(!tempFile.exists() || !tempFile.isDirectory()) {
				tempFile.mkdir();
				subDirList.add(tempFile);
			}
		}
		
		//4. 对文件进行标记
		for(File srcFile : subFileList) {
			tempFile = new File(desFileCO.getRootDir(), srcFile.getName());
			if(!tempFile.exists() || !tempFile.isFile()) {
				if(srcFile.length()==0) {
					//4.1 若desFile不存在且srcFile大小为0，则标记 (Deleted)
					//若已被标记，不重复标记
					if(!srcFile.getName().startsWith(CommonCons.FILECO_DELETED)) {
						srcFile.renameTo(new File(rootDir, CommonCons.FILECO_DELETED + srcFile.getName()));
					}
				} else {
					//4.2 若desFile不存在且srcFile大小不为0，则警告
					Info.warn("A file hasn't been copied:" + srcFile.getAbsolutePath());
				}
			} else {
				if(srcFile.length()!=0) {
					//4.3 若desFile存在且srcFile大小不为0且两者大小相同，则标记(ToBeDeleted)
					if(srcFile.length() == tempFile.length()) {	
						srcFile.renameTo(new File(rootDir, CommonCons.FILECO_TOBEDELETED + srcFile.getName()));
					}
					//4.4 若desFile存在且srcFile大小不为0且两者大小不同，则警告
					else {
						Info.warn("There are 2 files with same name but different sizes!");
						Info.warn(srcFile.getAbsolutePath());
						Info.warn(tempFile.getAbsolutePath());
					}
				}
				//4.5 若desFile存在且srcFile大小为0，则属正常情况，不需额外操作
			}		
		}
		
		//5. 对文件夹进行标记
		for(File srcDir : subDirList) {
			tempFile = new File(desFileCO.getRootDir(), srcDir.getName());
			if(!tempFile.exists() || !tempFile.isDirectory()) {
				//5.1 若desDir不存在且srcDir为空，则标记(Deleted)
				if(srcDir.length() == 0) {
					srcDir.renameTo(new File(rootDir, CommonCons.FILECO_DELETED + srcDir.getName()));
				}
				//5.2 若desDir不存在且srcDir不为空，则不做操作
			}
		}
		
		return true;
	}
	
	/********************private methods********************/

	/**
	 * 整理需要复制的文件<br/>
	 * 1.srcList 中存在，而 desList 中不存在，且不以 (Deleted) 和 (ToBeDeleted) 开头。<br/>
	 * 2.在控制台打印出 srcList 和 desList 中重名且大小不为零且大小不同的文件，此类文件需要手动进行确认并操作。
	 * @param srcList 源目录内的文件
	 * @param desRootDir 目标目录
	 * @return List<File> 待复制的文件
	 */
	private List<File> getFilesToCopy(final List<File> srcList, final File desRootDir) {
		List<File> resultList = new LinkedList<File>();
		File desFile = null;
		for(File srcFile : srcList) {
			/*当 srcFile 大小不为0且 srcFile 不以(Deleted) 和 (ToBeDeleted) 开头时*/
			if(srcFile.length()!=0
				&& !srcFile.getName().startsWith(CommonCons.FILECO_DELETED)
				&& !srcFile.getName().startsWith(CommonCons.FILECO_TOBEDELETED)) {
				desFile = new File(desRootDir, srcFile.getName());
				/*若 desFile 不存在*/
				if(!desFile.exists()) {
					resultList.add(srcFile);
				}
				/*若 desFile 存在且 srcFile 和 desFile 的大小不同*/
				else if(desFile.exists() && srcFile.length()!=desFile.length()) {
					Info.warn("   存在不同大小的同名文件，请手动确认！src:" + srcFile.getAbsolutePath() + "   des:" + desFile.getAbsolutePath());
				}
			}
			/*当srcFile大小为0时，不论desFile是否存在、大小是否为0，都不做操作*/
		}
		
		return resultList;
	}

	
	/********************getters********************/
	public File getRootDir() {
		return this.rootDir;
	}

	public List<File> getSubFileList() {
		return subFileList;
	}

	public List<File> getSubDirList() {
		return subDirList;
	}

	
	/********************main********************/
	public static void main(String[] args) {
		FileCO srcdir = new FileCO("E:\\directory");
		FileCO desdir = new FileCO("G:\\电影");
		Info.info("main:开始复制文件...");
		srcdir.copyToAccordingToName(desdir);
		Info.info("main:文件复制结束.");
		Info.info("");
		Info.info("main:开始复制目录结构...");
		srcdir.syncDirStructure(desdir);
		Info.info("main:目录结构复制结束.");

//		System.out.println(file1.length());

	}
}
