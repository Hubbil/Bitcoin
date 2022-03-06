public enum Configuration {
    instance;

    public final String nameOfJavaArchive = "AES256.jar";
    public final String nameOfClass = "Applikation";

    public final String fileSeparator = System.getProperty("file.separator");
    public final String userDirectory = System.getProperty("user.dir");

    public final String nameOfSubFolder = "AES256/jar";
    public final String subFolderPathOfJarArchive = nameOfSubFolder+fileSeparator+nameOfJavaArchive;
    public final String fullPathToJar = userDirectory+subFolderPathOfJarArchive;
}