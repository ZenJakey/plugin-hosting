import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val rootPath = Paths.get("release").toAbsolutePath().toString()
    val directory = File(rootPath)

    val files = directory.listFiles()?.filter {it.isFile}

    files?.forEach {
        println(it.name + ": " + checksum(rootPath + "/" + it.name))
    }

    println(rootPath)
}

fun checksum(path: String): String {
    try {
        val array = Files.readAllBytes(Paths.get(path))
        val genChecksum = DigestUtils.sha512Hex(array)

        return genChecksum.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "failed"
}