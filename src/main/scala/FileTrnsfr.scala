import io.Source.fromFile
import java.io.File
import net.schmizz.sshj.SSHClient
import net.schmizz.sshj.sftp.SFTPClient
import net.schmizz.sshj.transport.verification.PromiscuousVerifier
import org.apache.log4j.Logger


object FileTrnsfr {
  def main(args: Array[String]): Unit = {
    // Read Properties file
    val propertiesFile = fromFile("config.properties")
    val configMap = propertiesFile
      .getLines()
      .filter(line => line.contains("="))
      .map { line =>
        val tkns = line.split("=")
        if (tkns.size == 1) (tkns(0) -> "")
        else (tkns(0) -> tkns(1))
      }
      .toMap
    propertiesFile.close()

    //Logging
    val logger = Logger.getLogger(this.getClass.getName)
    logger.info(configMap.toString())

    try{
      // Establish Connection
      logger.info("Establishing Connection")
      val ssh = SSHClient()
      ssh.addHostKeyVerifier(new PromiscuousVerifier)
      ssh.connect(configMap("remoteHost"))
      ssh.authPublickey(configMap("remoteUser"))
      logger.info("Connection Successful")
      
      val scp = ssh.newSCPFileTransfer()
      logger.info("SCP started")

      // Transfer files
      val localDir : File = File(configMap("localDir"))
      val remoteDir : String = configMap("remoteDir")
      var files = List[File]()
      if (localDir.exists() && localDir.isDirectory())
        files = localDir.listFiles().filter(_.isFile()).toList
      for (file <- files) {
        scp.upload(file.getAbsolutePath(), remoteDir)
      }
      logger.info("SCP Ended")
      ssh.disconnect()
    }
    catch{
      case e: Exception => logger.error(e.toString)
        System.exit(0)
    }
  }
}
