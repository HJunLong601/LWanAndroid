

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class JarZipUtil {
    /**
     * 将该jar包解压到指定目录
     * @param jarPath jar包的绝对路径
     * @param destDirPath jar包解压后的保存路径
     */
    static void unzipJar(String jarPath, String destDirPath) {
        if (jarPath.endsWith('.jar')) {
            JarFile jarFile = new JarFile(jarPath)
            Enumeration<JarEntry> jarEntrys = jarFile.entries()
            while (jarEntrys.hasMoreElements()) {
                JarEntry jarEntry = jarEntrys.nextElement()
                if (jarEntry.directory) {
                    continue
                }
                String entryName = jarEntry.getName()
                String outFileName = destDirPath + "/" + entryName
                File outFile = new File(outFileName)
                outFile.getParentFile().mkdirs()
                InputStream inputStream = jarFile.getInputStream(jarEntry)
                FileOutputStream fileOutputStream = new FileOutputStream(outFile)
                fileOutputStream << inputStream
                fileOutputStream.close()
                inputStream.close()

            }
            jarFile.close()
        }
    }

    /**
     * 重新打包jar
     * @param packagePath 将这个目录下的所有文件打包成jar
     * @param destPath 打包好的jar包的绝对路径
     */
    static void zipJar(String packagePath, String destPath) {
        File file = new File(packagePath)
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(destPath))

        file.eachFileRecurse { File f ->
            String entryName = f.getAbsolutePath().substring(file.absolutePath.length() + 1)
            outputStream.putNextEntry(new ZipEntry(entryName))
            if(!f.directory) {
                InputStream inputStream = new FileInputStream(f)
                outputStream << inputStream
                inputStream.close()
            }
        }
        outputStream.close()
    }

    static boolean containsEntry(File f, String entry) {
        ZipFile zf = new ZipFile(f)
        boolean ret = (zf.getEntry(entry) != null)
        zf.close()
        return ret
    }
}