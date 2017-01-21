 package itemCOOperation.common.utils;
 
 import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
 @Deprecated
 public class CopyFolder
 {
   private void copyFolder(File src, File des)
     throws IOException
   {
     if (src.exists())
     {
       if (src.isDirectory())
       {
         if (!des.exists())
         {
           des.mkdir();
         }
         String[] childfiles = src.list();
         if (childfiles != null) {
           String[] arrayOfString1;
           int j = (arrayOfString1 = childfiles).length; for (int i = 0; i < j; i++) { String childfile = arrayOfString1[i];
             
             File childsrc = new File(src, childfile);
             File childdes = new File(des, childfile);
             
             copyFolder(childsrc, childdes);
           }
           
         }
       }
       else
       {
         des.createNewFile();
 
       }
       
 
     }
     else
     {
       System.out.println("源目标不存在");
     }
   }
   
 
   private void reverseCheck(File des, File src)
   {
     String[] arrayOfString1;
     
     int j;
     
     int i;
     if (src.exists())
     {
 
       if (des.isDirectory())
       {
         String[] childfiles = des.list();
         if (childfiles != null)
         {
           j = (arrayOfString1 = childfiles).length; for (i = 0; i < j; i++) { String childfile = arrayOfString1[i];
             
             File childdes = new File(des, childfile);
             File childsrc = new File(src, childfile);
             reverseCheck(childdes, childsrc);
           }
           
         }
       }
       else if ((des.isFile()) && (des.length() != 0L) && (src.length() != 0L))
       {
         des.delete();
         try {
           des.createNewFile();
         } catch (IOException e) {
           System.out.println("将des置空失败：" + des.getAbsolutePath());
           e.printStackTrace();
 
         }
         
       }
       
 
     }
     else if (des.length() != 0L)
     {
       //copyTo(des, src);
    	 CopyTo.copyTo(des, src);
     }
     else if (des.isDirectory())
     {
       String[] childfiles = des.list();
       if (childfiles != null)
       {
         src.mkdir();
         j = (arrayOfString1 = childfiles).length; for (i = 0; i < j; i++) { String childfile = arrayOfString1[i];
           
           File childdes = new File(des, childfile);
           File childsrc = new File(src, childfile);
           reverseCheck(childdes, childsrc);
         }
       }
       else
       {
         System.out.println("源文件夹不存在：" + des.getAbsolutePath());
         des.renameTo(new File(des.getParent(), "(deleted)" + des.getName()));
       }
       
     }
     else
     {
       System.out.println("源文件不存在：" + des.getAbsolutePath());
       des.renameTo(new File(des.getParent(), "(deleted)" + des.getName()));
     }
   }
   
 
 
 
 
 
 
   private void copyTo(File des, File src)
   {
     try
     {
       InputStream in = new FileInputStream(des);
       OutputStream out = new FileOutputStream(src);
       
       byte[] buffer = new byte['?'];
       
 
       System.out.println("正在复制：" + des.toString());
       int length; while ((length = in.read(buffer)) > 0) {
         int length1 = 0;
         out.write(buffer, 0, length1);
       }
       System.out.println("复制完成:" + des.toString());
       
       in.close();
       out.close();
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
   
   public static void main(String[] args) throws IOException
   {
     File des = new File("G:\\电影");
     File src = new File("E:\\directory");
     CopyFolder cf = new CopyFolder();
     System.out.println("开始复制：src→des.");
     cf.copyFolder(src, des);
     System.out.println("开始检查：des→src.");
     cf.reverseCheck(des, src);
     
     System.out.println("结束.");
   }
 }
