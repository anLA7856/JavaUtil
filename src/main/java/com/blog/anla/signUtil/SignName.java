package com.blog.anla.signUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * @author anla7856
 * @date
 */
public class SignName {
	public static final String files = ".java";
	//public static final String rootDirectory = "/usr/temp/";

	public static void main(String[] args) {
		if(args.length == 0){
			System.out.println("usages: SignName rootDirectory");
			return;
		}
		SignName sn = new SignName();
		File file = new File(args[0]);
		sn.recursionSearchFile(file);
	}

	public String infos4Write2Java() {
		StringBuilder builder = new StringBuilder();
		builder.append("/**" + "\n");
		builder.append(" * @author anla7856" + "\n");
		builder.append(" * @date " + new Date()+"\n");
		builder.append(" */"+"\n");
		return builder.toString();
	}

	public void recursionSearchFile(File file) {
		if (file.isFile()) {
			if (file.getName().endsWith(files)) {
				// 如果是java文件，就开始写。
				write2File(file);
				return;
			} else {
				return;
			}

		}
		File[] files = file.listFiles();
		for (File f : files) {
			recursionSearchFile(f);
		}
	}

	public void write2File(File file) {
		File tmp = null;
		FileOutputStream tmpOut = null;
		FileInputStream tmpIn = null;
		RandomAccessFile raf = null;
		try {
			tmp = File.createTempFile("tmp", null);
			tmpOut = new FileOutputStream(tmp);
			tmpIn = new FileInputStream(tmp);

			raf = new RandomAccessFile(file, "rw");
			byte[] buf = new byte[64];
			int hasRead = 0;
			while ((hasRead = raf.read(buf)) > 0) {
				// 把原有内容读入临时文件
				tmpOut.write(buf, 0, hasRead);
			}
			raf.seek(0L);
			raf.write(infos4Write2Java().getBytes());
			// 追加临时文件内容
			while ((hasRead = tmpIn.read(buf)) > 0) {
				raf.write(buf, 0, hasRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				tmpOut.close();
				tmpIn.close();
				raf.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.out.println(file.getName() + "writes ok");
		}

	}

}
