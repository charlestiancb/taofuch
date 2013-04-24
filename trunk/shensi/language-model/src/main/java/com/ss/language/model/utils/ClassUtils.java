package com.ss.language.model.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

/**
 * 类相关的工具类
 * 
 * @author taofucheng
 * 
 */
public class ClassUtils {

	public static Set<Class<?>> getClasses(Package pack) {

		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageName = pack.getName();
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol) || "bundle".equals(protocol)) {
					JarFile jar;
					try {
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/') {
								name = name.substring(1);
							}
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1) {
									packageName = name.substring(0, idx).replace('/', '.');
								}
								if ((idx != -1) || recursive) {
									if (name.endsWith(".class") && !entry.isDirectory()) {
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	protected static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
			final boolean recursive, Set<Class<?>> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 寰幆鎵�湁鏂囦欢
		for (File file : dirfiles) {
			// 濡傛灉鏄洰褰�鍒欑户缁壂鎻�
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 濡傛灉鏄痡ava绫绘枃浠�鍘绘帀鍚庨潰鐨�class 鍙暀涓嬬被鍚�
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 娣诲姞鍒伴泦鍚堜腑鍘�
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 灏嗕竴涓洰褰曟坊鍔犲埌鐜鍙橀噺涓紒
	 * 
	 * @param dirs
	 * @throws IOException
	 */
	public static void addDir2LibPath(String... dirs) {
		if (dirs == null || dirs.length == 0) {
			return;
		}
		try {
			Field[] fields = ClassLoader.class.getDeclaredFields();
			for (Field f : fields) {
				try {
					String[] paths = (String[]) f.get(null);
					System.err.println(Arrays.toString(paths));
				} catch (Exception e) {
				}
			}
			addDir2Property("usr_paths", dirs);
			addDir2Property("sys_paths", dirs);
			// 鏇存柊Sytem.getProperty涓殑鍐呭
			for (String d : dirs) {
				System.setProperty("java.library.path", System.getProperty("java.library.path") + ";" + d);
			}
		} catch (Exception e) {
			System.err.println("Failed to get field handle to set library path");
		}
	}

	private static void addDir2Property(String key, String... dirs) throws Exception {
		if (StringUtils.isBlank(key) || dirs == null || dirs.length == 0) {
			return;
		}
		Field field = ClassLoader.class.getDeclaredField(key);
		field.setAccessible(true);
		String[] paths = (String[]) field.get(ClassLoader.class);
		for (int i = 0; i < paths.length; i++) {
			if (dirs.equals(paths[i])) {
				return;
			}
		}
		String[] tmp = new String[paths.length + dirs.length];
		System.arraycopy(paths, 0, tmp, 0, paths.length);
		for (int i = 0; i < dirs.length; i++) {
			tmp[paths.length + i] = dirs[i];
		}
		field.set(ClassLoader.class, tmp);
	}

	/**
	 * 获取类路径下的文件的完整路径。
	 * 
	 * @param file
	 * @return
	 */
	public static String getClassPathFile(String file) {
		try {
			String path = ClassUtils.class.getResource(file).getFile();
			if (StringUtils.isNotBlank(path)) {
				if (path.startsWith("/")) {
					path = path.substring(1);
				}
				path = URLDecoder.decode(path, "UTF-8");
			}
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
