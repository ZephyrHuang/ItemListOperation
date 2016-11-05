package itemListOperation.fileList;

import itemListOperation.commonUtils.CopyTo;
import itemListOperation.commonUtils.Info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.apache.commons.collections4.CollectionUtils;

/**
 * FileList对象包含一个目录和该目录下所有的子目录和文件
 * @author Zephyr Huang
 *
 */
public class FileList extends AbstractFileList<FileList>{
	//根目录文件
	private File rootDir = null;
	//根目录下的文件列表
	private List<File> subFileList = new ArrayList<File>();
	//根目录下的子文件夹列表
	private List<File> subDirList = new ArrayList<File>();
	
	/*****************constructors******************/
	public FileList(String path) {
		this(new File(path));
	}
	
	public FileList(File rootDir) {
		//必须传入一个目录
		if(!rootDir.isDirectory()) {
			Info.error("A directory is requried. "+rootDir.getAbsolutePath());
			return;
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
	
	public FileList(FileList fileList) {
		this.rootDir = fileList.rootDir;
		this.subDirList = fileList.subDirList;
		this.subFileList = fileList.subFileList;
	}
	
	/********************copy和check操作********************/
	
	/* 若“文件名不存在”则copy至desItemList，若“文件名存在”则不操作
	 */
	@Override
	public boolean copyToAccordingToName(FileList desFileList) {
		Info.info("");
		Info.info("开始对目录内文件进行复制："+this.rootDir.getAbsolutePath()+"-->"+desFileList.getRootDir().getAbsolutePath());
		if(!desFileList.getRootDir().exists())
			desFileList.getRootDir().mkdir();
		
		//复制文件
		List<File> filesToCopy = new ArrayList<File>();
		filesToCopy = compareFilesAccordingToName(subFileList, desFileList.getRootDir());
		for(File srcFile:filesToCopy) {
			File desFile = new File(desFileList.getRootDir(), srcFile.getName());
			Info.info("   开始复制文件："+srcFile.getAbsolutePath());
			CopyTo.copyTo(srcFile, desFile);
			Info.info("   文件复制结束："+srcFile.getAbsolutePath());
		}

//		//复制结束后对当前目录内的文件进行复制目录结构的操作
//		getDirStructure(desFileList);
		
		//对子文件夹进行递归
		File desSubDir = null;
		for(File srcSubDir:subDirList) {
			try {
				desSubDir = new File(desFileList.getRootDir(), srcSubDir.getName());
				desSubDir.mkdir();
				Info.info("   开始对 "+srcSubDir.getAbsolutePath()+" 进行递归复制。");
				new FileList(srcSubDir).copyToAccordingToName(new FileList(desSubDir));
			} catch (Exception e) {
				Info.error("Error occurs while doing recursive copy operation:"+srcSubDir.getAbsolutePath());
				e.printStackTrace();
				return false;
			}
		}
		
		//本FileList的复制工作完成
		return true;
	}

	/* 若“文件名不存在”则copy自desItemList，若“文件名存在”则不操作
	 */
	@Override
	public boolean copyFromAccordingToName(FileList desFileList) {
		return new FileList(desFileList).copyToAccordingToName(this);
	}
	
	/* 生成desItemList及其子文件、子文件夹的目录结构，文件以同名空文件来表示
	 */
	@Override
	public boolean getDirStructure(FileList desFileList) {
		Info.info("开始复制目录结构  "+this.rootDir.getAbsolutePath()+"<--"+desFileList.getRootDir().getAbsolutePath());
		this.rootDir.mkdir();
		File tempFile = null;
		
		//以src为基点，进行标记（已删除、待删除）和删除工作
		for(File srcFile:this.subFileList) {
			//置空所有被标记的文件
			if(srcFile.getName().startsWith("(ToBeDeleted)")) {
				srcFile.delete();
				try {
					new File(rootDir, srcFile.getName().substring(13)).createNewFile();
					continue;
				} catch (IOException e) {
					Info.error("文件置空出错！"+rootDir.getAbsolutePath()+"\\"+srcFile.getName());
					e.printStackTrace();
					return false;
				}
			}
			//对src中的文件进行标记
			tempFile = new File(desFileList.getRootDir(), srcFile.getName());
			if(!tempFile.exists()) {
				if(srcFile.length()==0) {
					//若desFile不存在且srcFile大小为0，则标记
					//若已被标记，不重复标记
					if(!srcFile.getName().startsWith("(deleted)"))
						srcFile.renameTo(new File(this.rootDir,"(deleted)"+srcFile.getName()));
				}
				else
					//若desFile不存在且srcFile大小不为0，则警告
					Info.warn("A file hasn't been copied:"+srcFile.getAbsolutePath());
			}
			else {
				//若desFile存在且srcFile大小不为0且两者大小相同，则标记
				if(srcFile.length()!=0) {
					if(srcFile.length() == tempFile.length()) {	
							srcFile.renameTo(new File(this.rootDir,"(ToBeDeleted)"+srcFile.getName()));
					}
					//若两个文件大小不同则既不标记也不置空
				}
				//若desFile存在且srcFile大小为0，则属正常情况，不需额外操作
				//else 
			}		
		}
		
		//以des为基点，创建des中存在而src不存在的文件和目录
		for(File desFile: desFileList.getSubFileList()) {
			tempFile = new File(this.rootDir, desFile.getName());
			if(!tempFile.exists()) {
				try {
					if(tempFile.createNewFile()) {
						subFileList.add(tempFile);
					}
				} catch (IOException e) {
					Info.error("创建原本不存在的空文件时出错："+desFile.getAbsolutePath());
					e.printStackTrace();
				}
			}
		}
		for(File desDir: desFileList.getSubDirList()) {
			tempFile = new File(this.rootDir, desDir.getName());
			if(!tempFile.exists()) {
				tempFile.mkdir();
				subDirList.add(tempFile);
			}
		}
		
		//递归调用
		for(File srcSubDir: subDirList) {
			tempFile = new File(desFileList.getRootDir(), srcSubDir.getName());
			tempFile.mkdir();
			new FileList(srcSubDir).getDirStructure(new FileList(tempFile));
		}
		
		return true;
	}
	
//	/**
//	 * 只复制FileList中的文件和子文件夹，不复制子文件夹中的内容
//	 * @param desFileList
//	 * @return
//	 */
//	public boolean copyToAccordingToNameNoRecursive(ItemList desFileList) {
//		return false;
//	}
//	
//	/**
//	 * 只复制FileList中的文件和子文件夹，不复制子文件夹中的内容
//	 * @param desFileList
//	 * @return
//	 */
//	public boolean copyFromAccordingToNameNoRecursive(ItemList desFileList) {
//		return false;
//	}
	
	/********************private methods********************/

	/**
	 * 1.获得srcList中存在，而desList中不存在的文件列表
	 * 2.打印出所有重名且srcFile和desFile都大小都不为空的文件
	 * @param srcList
	 * @param desList
	 * @return
	 */
	private List<File> compareFilesAccordingToName(List<File> srcList, File desRootDir) {
		List<File> resultList = new ArrayList<File>();
		File desFile = null;
		//迭代找出所有名字不同的文件
		for(File srcFile:srcList) 
		{
			//当srcFile大小不为0时
			if(srcFile.length()!=0) {
				desFile = new File(desRootDir, srcFile.getName());
				if(!desFile.exists()) {
					if(!srcFile.getName().startsWith("(deleted)") && !srcFile.getName().startsWith("(ToBeDe"))
						resultList.add(srcFile);
				}
				else if(desFile.exists() && srcFile.length()!=desFile.length()) {
					Info.warn("   存在不同大小的同名文件，请手动确认！src:"+srcFile.getAbsolutePath()+"   des:"+desFile.getAbsolutePath());
				}
			}
			//当srcFile大小为0时，不论desFile是否存在、大小是否为0，都不做操作
		}
		
		return resultList;
	}

	
	/***********************getters**************************/
	public File getRootDir() {
		return rootDir;
	}

	public List<File> getSubFileList() {
		return subFileList;
	}

	public List<File> getSubDirList() {
		return subDirList;
	}

	
	/***************************main**************************/
	public static void main(String[] args) {
		FileList srcdir = new FileList("E:\\directory");
		FileList desdir = new FileList("G:\\电影");
		Info.info("main:开始复制文件...");
		srcdir.copyToAccordingToName(desdir);
		Info.info("main:文件复制结束.");
		Info.info("");
		Info.info("main:开始复制目录结构...");
		srcdir.getDirStructure(desdir);
		Info.info("main:目录结构复制结束.");

//		System.out.println(file1.length());

	}
}
