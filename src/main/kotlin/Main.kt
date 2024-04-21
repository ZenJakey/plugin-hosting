import com.google.gson.Gson
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList

fun main() {
    val rootPath = Paths.get("release").toAbsolutePath().toString()
    val directory = File(rootPath)

    val files = directory.listFiles()?.filter {it.isFile}
    val pluginsFile = File(Paths.get("").toAbsolutePath().toString() + "/plugins.json")
    if(!pluginsFile.exists()) {
        pluginsFile.createNewFile()
    }
    val releaseList = ArrayList<PluginModel>()

    files?.forEach {
        val checksum = checksum(rootPath + "/" + it.name)
        println(it.name + ": " + checksum)
        val release = ReleaseModel(date = "2024-04-21", sha512sum = checksum, url="https://github.com/ZenJakey/plugin-hosting/blob/master/release/" + it.name + "?raw=true")
        val plugin = PluginModel(name = it.name.split("-0")[0], id = it.name.split("-0")[0], releases = listOf(release))
        releaseList.add(plugin)
    }

    val gson = Gson()
    pluginsFile.writeText(gson.toJson(releaseList.toList()))
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

data class PluginModel(
    val projectUrl: String = "https://august-rsps.com/",
    val provider: String = "Jakey",
    val name: String,
    val description: String = "",
    val id: String,
    val releases: List<ReleaseModel>
)

data class ReleaseModel(
    val date: String,
    val sha512sum: String,
    val version: String = "0.0.1",
    val url: String,
    val requires: String = "0.0.1"
)