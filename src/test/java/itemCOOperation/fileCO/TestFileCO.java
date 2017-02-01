package itemCOOperation.fileCO;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import itemCOOperation.common.utils.Info;


public class TestFileCO {
	private File srcDir;
	private File desDir;
	private FileCO srcFileCO;
	private FileCO desFileCO;
	//本源入口目录路径
	private static String srcDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/src";
	//目标入口目录路径
	private static String desDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/des";
	
	@Before
	public void setUp() throws Exception {
		srcDir = new File(srcDirPath);
		desDir = new File(desDirPath);
		srcDir.mkdir();
		desDir.mkdir();
		//1. 校验。两者必须都是目录。
		if(!(srcDir.isDirectory() && desDir.isDirectory())) {
			Info.error("One or both of the entry dirs are not directories!");
			return;
		}

		//2. 构造 src 文件夹
		//2.1 src/*.txt
		createFiles(srcDir, "src");
		//2.2 src/common/*.txt
		createFiles(new File(srcDir, "common"	   ), "src");
		//2.3 src/common_blank/*.txt
		createFiles(new File(srcDir, "common_blank"), "src");
		//2.4 src/subsrc/*.txt
		createFiles(new File(srcDir, "subsrc"	   ), "src");
		//2.5 src/subsrc_blank/*.txt
		createFiles(new File(srcDir, "subsrc_blank"), "src");
		
		//3. 构造 des 文件夹
		//3.1 des/*.txt
		createFiles(desDir, "des");
		//3.2 des/common/*.txt
		createFiles(new File(desDir, "common"	   ), "des");
		//3.3 des/common_blank/*.txt
		createFiles(new File(desDir, "common_blank"), "des");
		//3.4 des/subdes/*.txt
		createFiles(new File(desDir, "subdes"	   ), "des");
		//3.5 des/subdes_blank/*.txt
		createFiles(new File(desDir, "subdes_blank"), "des");
		
		srcFileCO = new FileCO(srcDir);
		desFileCO = new FileCO(desDir);
	}
	
	@After
	public void tearDown() {
		Info.info("Tests end.");
	}
	
	/**
	 * 第一次运行
	 */
	@Test
	@SuppressWarnings("serial")
	public void testCopyToAccordingToName_FirstTime() {
		Info.info("==================== 第一遍 ====================");
		//The method to be tested.
		srcFileCO.copyToAccordingToName(desFileCO);
		
		//key:filename，value:content
		//src文件夹内的所有文件和文件夹
		Map<String, String> filesInSrc = new HashMap<String, String>() {
			{
				put("(Deleted)src_blank.txt", "");
				put("(ToBeDeleted)common.txt", "common");
				put("(ToBeDeleted)src.txt", "src");
				put("common_blank.txt", "");
				put("common_diff.txt", "commonsrc");
				put("des_blank.txt", "");
				put("des.txt", "");
			}
		};
		Map<String, String> dirsInSrc = new HashMap<String, String>() {
			{
				put("common", "");
				put("common_blank", "");
				put("subsrc", "");
				put("(Deleted)subsrc_blank", "");
				put("subdes", "");
				put("subdes_blank", "");
			}
		};
		Map<String, String> filesInSrc_common = new HashMap<String, String>() {
			{
				put("(ToBeDeleted)common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commonsrc");
				put("(ToBeDeleted)src.txt", "src");
				put("(Deleted)src_blank.txt", "");
				put("des.txt", "");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInSrc_common_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInSrc_subsrc = new HashMap<String, String>() {
			{
				put("(Deleted)common_blank.txt", "");
				put("(Deleted)src_blank.txt", "");
				put("(ToBeDeleted)common_diff.txt", "commonsrc");
				put("(ToBeDeleted)common.txt", "common");
				put("(ToBeDeleted)src.txt", "src");
			}
		};
		Map<String, String> filesInSrc_subsrc_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInSrc_subdes = new HashMap<String, String>() {
			{
				put("common.txt", "");
				put("common_blank.txt", "");
				put("common_diff.txt", "");
				put("des.txt", "");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInSrc_subdes_blank = new HashMap<String, String>() {
			//blank
		};
		
		//des文件夹内的所有文件和文件夹
		Map<String, String> filesInDes = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
				put("src.txt", "src");
			}
		};
		Map<String, String> dirsInDes = new HashMap<String, String>() {
			{
				put("common", "");
				put("common_blank", "");
				put("subsrc", "");
				put("subdes", "");
				put("subdes_blank", "");
			}
		};
		Map<String, String> filesInDes_common = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("src.txt", "src");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInDes_common_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInDes_subdes = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInDes_subdes_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInDes_subsrc = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_diff.txt", "commonsrc");
				put("src.txt", "src");
			}
		};

		
		//检查
		List<File> actualFileInSrc = new LinkedList<File>();
		List<File> actualDirInSrc  = new LinkedList<File>();
		for(File f : srcDir.listFiles()) {
			if(f.isFile()) {
				actualFileInSrc.add(f);
			} else {
				actualDirInSrc.add(f);
			}
		}
		List<File> actualFileInDes = new LinkedList<File>();
		List<File> actualDirInDes  = new LinkedList<File>();
		for(File f : desDir.listFiles()) {
			if(f.isFile()) {
				actualFileInDes.add(f);
			} else {
				actualDirInDes.add(f);
			}
		}
		File tmp = null;
		//src
		checkFilesAndContents(srcDir, filesInSrc, actualFileInSrc);
		checkFilesAndContents(srcDir, dirsInSrc , actualDirInSrc);
		checkFilesAndContents(tmp = new File(srcDir, "common"), 	  filesInSrc_common, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "common_blank"), filesInSrc_common_blank, new ArrayList<File>());
		checkFilesAndContents(tmp = new File(srcDir, "subsrc"), 	  filesInSrc_subsrc, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "(Deleted)subsrc_blank"), filesInSrc_subsrc_blank, new ArrayList<File>());
		checkFilesAndContents(tmp = new File(srcDir, "subdes"), 	  filesInSrc_subdes, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "subdes_blank"), filesInSrc_subdes_blank, new ArrayList<File>());
		//des
		checkFilesAndContents(desDir, filesInDes, actualFileInDes);
		checkFilesAndContents(desDir, dirsInDes , actualDirInDes);
		checkFilesAndContents(tmp = new File(desDir, "common"), 	  filesInDes_common, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "common_blank"), filesInDes_common_blank, new ArrayList<File>());
		checkFilesAndContents(tmp = new File(desDir, "subsrc"), 	  filesInDes_subsrc, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "subdes"), 	  filesInDes_subdes, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "subdes_blank"), filesInDes_subdes_blank, new ArrayList<File>());
		
		//测试第二次运行
		testCopyToAccordingToName_SecondTime();
	}
	
	/**
	 * 第二次运行
	 */
	//@Test
	@SuppressWarnings("serial")
	public void testCopyToAccordingToName_SecondTime() {
		Info.info();
		Info.info("==================== 第二遍 ====================");
		//The method to be tested.
		srcFileCO.copyToAccordingToName(desFileCO);
		
		//key:filename，value:content
		//src文件夹内的所有文件和文件夹
		Map<String, String> filesInSrc = new HashMap<String, String>() {
			{
				put("common_blank.txt", "");
				put("common_diff.txt", "commonsrc");
				put("common.txt", "");
				put("des_blank.txt", "");
				put("des.txt", "");
				put("src.txt", "");
			}
		};
		Map<String, String> dirsInSrc = new HashMap<String, String>() {
			{
				put("common", "");
				put("common_blank", "");
				put("subsrc", "");
				put("subdes", "");
				put("subdes_blank", "");
			}
		};
		Map<String, String> filesInSrc_common = new HashMap<String, String>() {
			{
				put("common.txt", "");
				put("common_blank.txt", "");
				put("common_diff.txt", "commonsrc");
				put("src.txt", "");
				put("des.txt", "");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInSrc_common_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInSrc_subsrc = new HashMap<String, String>() {
			{
				put("common.txt", "");
				put("common_diff.txt", "");
				put("src.txt", "");
			}
		};
		Map<String, String> filesInSrc_subdes = new HashMap<String, String>() {
			{
				put("common.txt", "");
				put("common_blank.txt", "");
				put("common_diff.txt", "");
				put("des.txt", "");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInSrc_subdes_blank = new HashMap<String, String>() {
			//blank
		};
		
		//des文件夹内的所有文件和文件夹
		Map<String, String> filesInDes = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
				put("src.txt", "src");
			}
		};
		Map<String, String> dirsInDes = new HashMap<String, String>() {
			{
				put("common", "");
				put("common_blank", "");
				put("subsrc", "");
				put("subdes", "");
				put("subdes_blank", "");
			}
		};
		Map<String, String> filesInDes_common = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("src.txt", "src");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInDes_common_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInDes_subdes = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_blank.txt", "");
				put("common_diff.txt", "commondesdes");
				put("des.txt", "desdes");
				put("des_blank.txt", "");
			}
		};
		Map<String, String> filesInDes_subdes_blank = new HashMap<String, String>() {
			//blank
		};
		Map<String, String> filesInDes_subsrc = new HashMap<String, String>() {
			{
				put("common.txt", "common");
				put("common_diff.txt", "commonsrc");
				put("src.txt", "src");
			}
		};

		
		//检查
		List<File> actualFileInSrc = new LinkedList<File>();
		List<File> actualDirInSrc  = new LinkedList<File>();
		for(File f : srcDir.listFiles()) {
			if(f.isFile()) {
				actualFileInSrc.add(f);
			} else {
				actualDirInSrc.add(f);
			}
		}
		List<File> actualFileInDes = new LinkedList<File>();
		List<File> actualDirInDes  = new LinkedList<File>();
		for(File f : desDir.listFiles()) {
			if(f.isFile()) {
				actualFileInDes.add(f);
			} else {
				actualDirInDes.add(f);
			}
		}
		File tmp = null;
		//src
		checkFilesAndContents(srcDir, filesInSrc, actualFileInSrc);
		checkFilesAndContents(srcDir, dirsInSrc , actualDirInSrc);
		checkFilesAndContents(tmp = new File(srcDir, "common"), 	  filesInSrc_common, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "common_blank"), filesInSrc_common_blank, new ArrayList<File>());
		checkFilesAndContents(tmp = new File(srcDir, "subsrc"), 	  filesInSrc_subsrc, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "subdes"), 	  filesInSrc_subdes, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(srcDir, "subdes_blank"), filesInSrc_subdes_blank, new ArrayList<File>());
		//des
		checkFilesAndContents(desDir, filesInDes, actualFileInDes);
		checkFilesAndContents(desDir, dirsInDes , actualDirInDes);
		checkFilesAndContents(tmp = new File(desDir, "common"), 	  filesInDes_common, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "common_blank"), filesInDes_common_blank, new ArrayList<File>());
		checkFilesAndContents(tmp = new File(desDir, "subsrc"), 	  filesInDes_subsrc, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "subdes"), 	  filesInDes_subdes, 	   Arrays.asList(tmp.listFiles()));
		checkFilesAndContents(tmp = new File(desDir, "subdes_blank"), filesInDes_subdes_blank, new ArrayList<File>());
	}
	
	/**
	 * 创建 dir 文件夹内的文件
	 * @param dir 被创建文件所属的文件夹
	 * @param srcOrDes 枚举："src"/"des"
	 * @return
	 * @throws IOException 
	 */
	private void createFiles(File dir, String srcOrDes) throws Exception {
		if(dir == null || StringUtils.isBlank(srcOrDes)) {
			throw new RuntimeException();
		}
		dir.mkdir();
		if(!dir.isDirectory()) {
			Info.error("When creating file:"+dir.getAbsolutePath());
			Info.error(dir.getAbsolutePath()+"is not a directory！");
			throw new IOException();
		}
		if(dir.getName().contains("blank")) {
			return;
		}
		
		String s = srcOrDes;
		String t = srcOrDes.equals("src") ? "src" : "desdes";//为保证*_diff.txt文件的大小不同
		Map<String, String> filesToBeCreated = new HashMap<String, String>();
		filesToBeCreated.put("common.txt"		, "common"	  );//共有文件，内容相同
		filesToBeCreated.put("common_blank.txt"	, ""		  );//共有文件，内容为空
		filesToBeCreated.put("common_diff.txt"	, "common"+t );//共有文件，内容不同
		filesToBeCreated.put(s+".txt"			, t		  	  );//src独有文件，内容非空
		filesToBeCreated.put(s+"_blank.txt"		, ""		  );//src独有文件，内容为空
		
		File tmpFile = null;
		BufferedWriter bw = null;
		for(Entry<String, String> entry: filesToBeCreated.entrySet()) {
			//创建文件并写入内容
			tmpFile = new File(dir, entry.getKey());
			try {
				tmpFile.createNewFile();
				bw = new BufferedWriter(new FileWriter(tmpFile));
				bw.write(entry.getValue());
				bw.flush();
				bw.close();
			} catch (IOException e) {
				Info.error("Error occurs when creating file: "+tmpFile.getAbsolutePath());
				throw e;
			}
		}
		return;
	}
	
	/**
	 * 检查文件及其内容或文件夹是否符合要求<br/>
	 * 1. 理应存在的文件和文件夹与实际存在的文件和文件夹应该一一对应，不多不少<br/>
	 * 2. 文件与文件内容应相符
	 * @param rootDir 被检查文件或文件夹所在的目录
	 * @param filesAndContents 理应存在的文件及其各自对应的内容
	 * @param actualFiles 实际存在的文件
	 * @return true if the check is passed;false otherwise
	 */
	private void checkFilesAndContents(File rootDir, Map<String, String> filesAndContents, List<File> actualFiles) {
		String fileOrDir = " 空文件夹 ";//标识所检查的是空文件夹/文件列表/文件夹列表
		if(CollectionUtils.isNotEmpty(actualFiles)) {
			if(actualFiles.get(0).isFile()) {
				fileOrDir = " 文件 ";
			} else {
				fileOrDir = " 文件夹 ";
			}
		}
		
		List<String> theoreticFileName = new LinkedList<String>();
		for(Entry<String, String> e : filesAndContents.entrySet()) {
			if(StringUtils.isNotBlank(e.getKey())) {
				theoreticFileName.add(e.getKey());
			}
		}
		List<String> actualFileName = new LinkedList<String>();
		for(File f : actualFiles) {
			if(StringUtils.isNotBlank(f.getName())) {
				actualFileName.add(f.getName());
			}
		}
		
		//检查是否一一对应，不多不少
		if(!" 空文件夹 ".equals(fileOrDir)) {
			assertTrue(fileOrDir+" actual包含theo "+rootDir.getAbsolutePath(), actualFileName.containsAll(theoreticFileName));
			assertTrue(fileOrDir+" theo包含actual "+rootDir.getAbsolutePath(), theoreticFileName.containsAll(actualFileName));
		}
		
		File file = null;
		if(CollectionUtils.isNotEmpty(actualFiles) && actualFiles.get(0).isFile()) {
			//检查文件
			for(Entry<String, String> e : filesAndContents.entrySet()) {
				file = new File(rootDir, e.getKey());
				//文件存在
				assertTrue("文件存在 "+rootDir.getAbsolutePath()+"\\"+file.getName(), file.exists() && file.isFile());
				//内容符合
				try {
					assertTrue("内容符合"+rootDir.getAbsolutePath()+"\\"+file.getName(), e.getValue().equals(FileUtils.readFileToString(file)));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			//检查文件夹
			for(Entry<String, String> e : filesAndContents.entrySet()) {
				file = new File(rootDir, e.getKey());
				//文件夹存在
				assertTrue("文件夹存在 "+rootDir.getAbsolutePath()+"\\"+file.getName(), file.exists() && file.isDirectory());
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		File srcDir = new File(srcDirPath);
		File desDir = new File(desDirPath);
		srcDir.mkdir();
		desDir.mkdir();
		
		File src = new File(srcDir, "test.txt");
		src.createNewFile();
		FileUtils.writeStringToFile(src, "test");
		
		FileCO srcFileCO = new FileCO(srcDir);
		FileCO desFileCO = new FileCO(desDir);
		
		srcFileCO.copyToAccordingToName(desFileCO);
		Info.info("end");
	}
}
