package org.apache.maven.plugin.war;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Display help information on maven-war-plugin.<br/> Call <pre>  mvn war:help -Ddetail=true -Dgoal=&lt;goal-name&gt;</pre> to display parameter details.
 *
 * @version generated on Thu Jan 26 15:10:21 CET 2012
 * @author org.apache.maven.tools.plugin.generator.PluginHelpGenerator (version 2.8)
 * @goal help
 * @requiresProject false
 * @threadSafe
 */
@SuppressWarnings( "all" )
public class HelpMojo
    extends AbstractMojo
{
    /**
     * If <code>true</code>, display all settable properties for each goal.
     * 
     * @parameter expression="${detail}" default-value="false"
     */
    private boolean detail;

    /**
     * The name of the goal for which to show help. If unspecified, all goals will be displayed.
     * 
     * @parameter expression="${goal}"
     */
    private java.lang.String goal;

    /**
     * The maximum length of a display line, should be positive.
     * 
     * @parameter expression="${lineLength}" default-value="80"
     */
    private int lineLength;

    /**
     * The number of spaces per indentation level, should be positive.
     * 
     * @parameter expression="${indentSize}" default-value="2"
     */
    private int indentSize;


    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException
    {
        if ( lineLength <= 0 )
        {
            getLog().warn( "The parameter 'lineLength' should be positive, using '80' as default." );
            lineLength = 80;
        }
        if ( indentSize <= 0 )
        {
            getLog().warn( "The parameter 'indentSize' should be positive, using '2' as default." );
            indentSize = 2;
        }

        StringBuffer sb = new StringBuffer();

        append( sb, "org.apache.maven.plugins:maven-war-plugin:2.2", 0 );
        append( sb, "", 0 );

        append( sb, "Maven WAR Plugin", 0 );
        append( sb, "Builds a Web Application Archive (WAR) file from the project output and its dependencies.", 1 );
        append( sb, "", 0 );

        if ( goal == null || goal.length() <= 0 )
        {
            append( sb, "This plugin has 5 goals:", 0 );
            append( sb, "", 0 );
        }

        if ( goal == null || goal.length() <= 0 || "exploded".equals( goal ) )
        {
            append( sb, "war:exploded", 0 );
            append( sb, "Create an exploded webapp in a specified directory.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "archiveClasses (Default: false)", 2 );
                append( sb, "Whether a JAR file will be created for the classes in the webapp. Using this optional configuration parameter will make the compiled classes to be archived into a JAR file and the classes directory will then be excluded from the webapp.", 3 );
                append( sb, "Expression: ${archiveClasses}", 3 );
                append( sb, "", 0 );

                append( sb, "cacheFile (Default: ${project.build.directory}/war/work/webapp-cache.xml)", 2 );
                append( sb, "The file containing the webapp structure cache.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "containerConfigXML", 2 );
                append( sb, "The path to a configuration file for the servlet container. Note that the file name may be different for different servlet containers. Apache Tomcat uses a configuration file named context.xml. The file will be copied to the META-INF directory.", 3 );
                append( sb, "Expression: ${maven.war.containerConfigXML}", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarExcludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<excludes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to exclude when doing a WAR overlay.", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarIncludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<includes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to include when doing a WAR overlay. Default is \'**\'", 3 );
                append( sb, "", 0 );

                append( sb, "escapedBackslashesInFilePath (Default: false)", 2 );
                append( sb, "To escape interpolated values with Windows path c:\\foo\\bar will be replaced with c:\\\\foo\\\\bar.", 3 );
                append( sb, "Expression: ${maven.war.escapedBackslashesInFilePath}", 3 );
                append( sb, "", 0 );

                append( sb, "escapeString", 2 );
                append( sb, "Expression preceded with this String won\'t be interpolated. \\${foo} will be replaced with ${foo}.", 3 );
                append( sb, "Expression: ${maven.war.escapeString}", 3 );
                append( sb, "", 0 );

                append( sb, "filteringDeploymentDescriptors (Default: false)", 2 );
                append( sb, "To filter deployment descriptors. Disabled by default.", 3 );
                append( sb, "Expression: ${maven.war.filteringDeploymentDescriptors}", 3 );
                append( sb, "", 0 );

                append( sb, "filters", 2 );
                append( sb, "Filters (property files) to include during the interpolation of the pom.xml.", 3 );
                append( sb, "", 0 );

                append( sb, "nonFilteredFileExtensions", 2 );
                append( sb, "A list of file extensions that should not be filtered. Will be used when filtering webResources and overlays.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileNameMapping", 2 );
                append( sb, "The file name mapping to use when copying libraries and TLDs. If no file mapping is set (default) the files are copied with their standard names.", 3 );
                append( sb, "", 0 );

                append( sb, "overlays", 2 );
                append( sb, "The overlays to apply.", 3 );
                append( sb, "", 0 );

                append( sb, "useCache (Default: false)", 2 );
                append( sb, "Whether the cache should be used to save the status of the webapp across multiple runs. Experimental feature so disabled by default.", 3 );
                append( sb, "Expression: ${useCache}", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceDirectory (Default: ${basedir}/src/main/webapp)", 2 );
                append( sb, "Single directory for extra files to include in the WAR. This is where you place your JSP files.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceExcludes", 2 );
                append( sb, "The comma separated list of tokens to exclude when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceIncludes (Default: **)", 2 );
                append( sb, "The comma separated list of tokens to include when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "webappDirectory (Default: ${project.build.directory}/${project.build.finalName})", 2 );
                append( sb, "The directory where the webapp is built.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "webResources", 2 );
                append( sb, "The list of webResources we want to transfer.", 3 );
                append( sb, "", 0 );

                append( sb, "webXml", 2 );
                append( sb, "The path to the web.xml file to use.", 3 );
                append( sb, "Expression: ${maven.war.webxml}", 3 );
                append( sb, "", 0 );

                append( sb, "workDirectory (Default: ${project.build.directory}/war/work)", 2 );
                append( sb, "Directory to unpack dependent WARs into if needed.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "help".equals( goal ) )
        {
            append( sb, "war:help", 0 );
            append( sb, "Display help information on maven-war-plugin.\nCall\n\u00a0\u00a0mvn\u00a0war:help\u00a0-Ddetail=true\u00a0-Dgoal=<goal-name>\nto display parameter details.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "detail (Default: false)", 2 );
                append( sb, "If true, display all settable properties for each goal.", 3 );
                append( sb, "Expression: ${detail}", 3 );
                append( sb, "", 0 );

                append( sb, "goal", 2 );
                append( sb, "The name of the goal for which to show help. If unspecified, all goals will be displayed.", 3 );
                append( sb, "Expression: ${goal}", 3 );
                append( sb, "", 0 );

                append( sb, "indentSize (Default: 2)", 2 );
                append( sb, "The number of spaces per indentation level, should be positive.", 3 );
                append( sb, "Expression: ${indentSize}", 3 );
                append( sb, "", 0 );

                append( sb, "lineLength (Default: 80)", 2 );
                append( sb, "The maximum length of a display line, should be positive.", 3 );
                append( sb, "Expression: ${lineLength}", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "inplace".equals( goal ) )
        {
            append( sb, "war:inplace", 0 );
            append( sb, "Generate the webapp in the WAR source directory.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "archiveClasses (Default: false)", 2 );
                append( sb, "Whether a JAR file will be created for the classes in the webapp. Using this optional configuration parameter will make the compiled classes to be archived into a JAR file and the classes directory will then be excluded from the webapp.", 3 );
                append( sb, "Expression: ${archiveClasses}", 3 );
                append( sb, "", 0 );

                append( sb, "cacheFile (Default: ${project.build.directory}/war/work/webapp-cache.xml)", 2 );
                append( sb, "The file containing the webapp structure cache.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "containerConfigXML", 2 );
                append( sb, "The path to a configuration file for the servlet container. Note that the file name may be different for different servlet containers. Apache Tomcat uses a configuration file named context.xml. The file will be copied to the META-INF directory.", 3 );
                append( sb, "Expression: ${maven.war.containerConfigXML}", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarExcludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<excludes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to exclude when doing a WAR overlay.", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarIncludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<includes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to include when doing a WAR overlay. Default is \'**\'", 3 );
                append( sb, "", 0 );

                append( sb, "escapedBackslashesInFilePath (Default: false)", 2 );
                append( sb, "To escape interpolated values with Windows path c:\\foo\\bar will be replaced with c:\\\\foo\\\\bar.", 3 );
                append( sb, "Expression: ${maven.war.escapedBackslashesInFilePath}", 3 );
                append( sb, "", 0 );

                append( sb, "escapeString", 2 );
                append( sb, "Expression preceded with this String won\'t be interpolated. \\${foo} will be replaced with ${foo}.", 3 );
                append( sb, "Expression: ${maven.war.escapeString}", 3 );
                append( sb, "", 0 );

                append( sb, "filteringDeploymentDescriptors (Default: false)", 2 );
                append( sb, "To filter deployment descriptors. Disabled by default.", 3 );
                append( sb, "Expression: ${maven.war.filteringDeploymentDescriptors}", 3 );
                append( sb, "", 0 );

                append( sb, "filters", 2 );
                append( sb, "Filters (property files) to include during the interpolation of the pom.xml.", 3 );
                append( sb, "", 0 );

                append( sb, "nonFilteredFileExtensions", 2 );
                append( sb, "A list of file extensions that should not be filtered. Will be used when filtering webResources and overlays.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileNameMapping", 2 );
                append( sb, "The file name mapping to use when copying libraries and TLDs. If no file mapping is set (default) the files are copied with their standard names.", 3 );
                append( sb, "", 0 );

                append( sb, "overlays", 2 );
                append( sb, "The overlays to apply.", 3 );
                append( sb, "", 0 );

                append( sb, "useCache (Default: false)", 2 );
                append( sb, "Whether the cache should be used to save the status of the webapp across multiple runs. Experimental feature so disabled by default.", 3 );
                append( sb, "Expression: ${useCache}", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceDirectory (Default: ${basedir}/src/main/webapp)", 2 );
                append( sb, "Single directory for extra files to include in the WAR. This is where you place your JSP files.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceExcludes", 2 );
                append( sb, "The comma separated list of tokens to exclude when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceIncludes (Default: **)", 2 );
                append( sb, "The comma separated list of tokens to include when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "webappDirectory (Default: ${project.build.directory}/${project.build.finalName})", 2 );
                append( sb, "The directory where the webapp is built.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "webResources", 2 );
                append( sb, "The list of webResources we want to transfer.", 3 );
                append( sb, "", 0 );

                append( sb, "webXml", 2 );
                append( sb, "The path to the web.xml file to use.", 3 );
                append( sb, "Expression: ${maven.war.webxml}", 3 );
                append( sb, "", 0 );

                append( sb, "workDirectory (Default: ${project.build.directory}/war/work)", 2 );
                append( sb, "Directory to unpack dependent WARs into if needed.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "manifest".equals( goal ) )
        {
            append( sb, "war:manifest", 0 );
            append( sb, "Generate a manifest for this webapp. The manifest file is created in the warSourceDirectory.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "archiveClasses (Default: false)", 2 );
                append( sb, "Whether a JAR file will be created for the classes in the webapp. Using this optional configuration parameter will make the compiled classes to be archived into a JAR file and the classes directory will then be excluded from the webapp.", 3 );
                append( sb, "Expression: ${archiveClasses}", 3 );
                append( sb, "", 0 );

                append( sb, "cacheFile (Default: ${project.build.directory}/war/work/webapp-cache.xml)", 2 );
                append( sb, "The file containing the webapp structure cache.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "containerConfigXML", 2 );
                append( sb, "The path to a configuration file for the servlet container. Note that the file name may be different for different servlet containers. Apache Tomcat uses a configuration file named context.xml. The file will be copied to the META-INF directory.", 3 );
                append( sb, "Expression: ${maven.war.containerConfigXML}", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarExcludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<excludes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to exclude when doing a WAR overlay.", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarIncludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<includes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to include when doing a WAR overlay. Default is \'**\'", 3 );
                append( sb, "", 0 );

                append( sb, "escapedBackslashesInFilePath (Default: false)", 2 );
                append( sb, "To escape interpolated values with Windows path c:\\foo\\bar will be replaced with c:\\\\foo\\\\bar.", 3 );
                append( sb, "Expression: ${maven.war.escapedBackslashesInFilePath}", 3 );
                append( sb, "", 0 );

                append( sb, "escapeString", 2 );
                append( sb, "Expression preceded with this String won\'t be interpolated. \\${foo} will be replaced with ${foo}.", 3 );
                append( sb, "Expression: ${maven.war.escapeString}", 3 );
                append( sb, "", 0 );

                append( sb, "filteringDeploymentDescriptors (Default: false)", 2 );
                append( sb, "To filter deployment descriptors. Disabled by default.", 3 );
                append( sb, "Expression: ${maven.war.filteringDeploymentDescriptors}", 3 );
                append( sb, "", 0 );

                append( sb, "filters", 2 );
                append( sb, "Filters (property files) to include during the interpolation of the pom.xml.", 3 );
                append( sb, "", 0 );

                append( sb, "nonFilteredFileExtensions", 2 );
                append( sb, "A list of file extensions that should not be filtered. Will be used when filtering webResources and overlays.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileNameMapping", 2 );
                append( sb, "The file name mapping to use when copying libraries and TLDs. If no file mapping is set (default) the files are copied with their standard names.", 3 );
                append( sb, "", 0 );

                append( sb, "overlays", 2 );
                append( sb, "The overlays to apply.", 3 );
                append( sb, "", 0 );

                append( sb, "useCache (Default: false)", 2 );
                append( sb, "Whether the cache should be used to save the status of the webapp across multiple runs. Experimental feature so disabled by default.", 3 );
                append( sb, "Expression: ${useCache}", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceDirectory (Default: ${basedir}/src/main/webapp)", 2 );
                append( sb, "Single directory for extra files to include in the WAR. This is where you place your JSP files.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceExcludes", 2 );
                append( sb, "The comma separated list of tokens to exclude when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceIncludes (Default: **)", 2 );
                append( sb, "The comma separated list of tokens to include when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "webappDirectory (Default: ${project.build.directory}/${project.build.finalName})", 2 );
                append( sb, "The directory where the webapp is built.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "webResources", 2 );
                append( sb, "The list of webResources we want to transfer.", 3 );
                append( sb, "", 0 );

                append( sb, "webXml", 2 );
                append( sb, "The path to the web.xml file to use.", 3 );
                append( sb, "Expression: ${maven.war.webxml}", 3 );
                append( sb, "", 0 );

                append( sb, "workDirectory (Default: ${project.build.directory}/war/work)", 2 );
                append( sb, "Directory to unpack dependent WARs into if needed.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "war".equals( goal ) )
        {
            append( sb, "war:war", 0 );
            append( sb, "Build a WAR file.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "archiveClasses (Default: false)", 2 );
                append( sb, "Whether a JAR file will be created for the classes in the webapp. Using this optional configuration parameter will make the compiled classes to be archived into a JAR file and the classes directory will then be excluded from the webapp.", 3 );
                append( sb, "Expression: ${archiveClasses}", 3 );
                append( sb, "", 0 );

                append( sb, "attachClasses (Default: false)", 2 );
                append( sb, "Whether classes (that is the content of the WEB-INF/classes directory) should be attached to the project as an additional artifact.\nBy default the classifier for the additional artifact is \'classes\'. You can change it with the <classesClassifier>someclassifier</classesClassifier> parameter.\n\nIf this parameter true, another project can depend on the classes by writing something like:\n\n<dependency>\n\u00a0\u00a0<groupId>myGroup</groupId>\n\u00a0\u00a0<artifactId>myArtifact</artifactId>\n\u00a0\u00a0<version>myVersion</myVersion>\n\u00a0\u00a0<classifier>classes</classifier>\n</dependency>\n\n\n", 3 );
                append( sb, "", 0 );

                append( sb, "cacheFile (Default: ${project.build.directory}/war/work/webapp-cache.xml)", 2 );
                append( sb, "The file containing the webapp structure cache.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "classesClassifier (Default: classes)", 2 );
                append( sb, "The classifier to use for the attached classes artifact.", 3 );
                append( sb, "", 0 );

                append( sb, "classifier", 2 );
                append( sb, "Classifier to add to the generated WAR. If given, the artifact will be an attachment instead. The classifier will not be applied to the JAR file of the project - only to the WAR file.", 3 );
                append( sb, "", 0 );

                append( sb, "containerConfigXML", 2 );
                append( sb, "The path to a configuration file for the servlet container. Note that the file name may be different for different servlet containers. Apache Tomcat uses a configuration file named context.xml. The file will be copied to the META-INF directory.", 3 );
                append( sb, "Expression: ${maven.war.containerConfigXML}", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarExcludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<excludes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to exclude when doing a WAR overlay.", 3 );
                append( sb, "", 0 );

                append( sb, "dependentWarIncludes", 2 );
                append( sb, "Deprecated. Use <overlay>/<includes> instead", 3 );
                append( sb, "", 0 );
                append( sb, "The comma separated list of tokens to include when doing a WAR overlay. Default is \'**\'", 3 );
                append( sb, "", 0 );

                append( sb, "escapedBackslashesInFilePath (Default: false)", 2 );
                append( sb, "To escape interpolated values with Windows path c:\\foo\\bar will be replaced with c:\\\\foo\\\\bar.", 3 );
                append( sb, "Expression: ${maven.war.escapedBackslashesInFilePath}", 3 );
                append( sb, "", 0 );

                append( sb, "escapeString", 2 );
                append( sb, "Expression preceded with this String won\'t be interpolated. \\${foo} will be replaced with ${foo}.", 3 );
                append( sb, "Expression: ${maven.war.escapeString}", 3 );
                append( sb, "", 0 );

                append( sb, "failOnMissingWebXml (Default: true)", 2 );
                append( sb, "Whether or not to fail the build if the web.xml file is missing. Set to false if you want you WAR built without a web.xml file. This may be useful if you are building an overlay that has no web.xml file.", 3 );
                append( sb, "Expression: ${failOnMissingWebXml}", 3 );
                append( sb, "", 0 );

                append( sb, "filteringDeploymentDescriptors (Default: false)", 2 );
                append( sb, "To filter deployment descriptors. Disabled by default.", 3 );
                append( sb, "Expression: ${maven.war.filteringDeploymentDescriptors}", 3 );
                append( sb, "", 0 );

                append( sb, "filters", 2 );
                append( sb, "Filters (property files) to include during the interpolation of the pom.xml.", 3 );
                append( sb, "", 0 );

                append( sb, "nonFilteredFileExtensions", 2 );
                append( sb, "A list of file extensions that should not be filtered. Will be used when filtering webResources and overlays.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDirectory (Default: ${project.build.directory})", 2 );
                append( sb, "The directory for the generated WAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileNameMapping", 2 );
                append( sb, "The file name mapping to use when copying libraries and TLDs. If no file mapping is set (default) the files are copied with their standard names.", 3 );
                append( sb, "", 0 );

                append( sb, "overlays", 2 );
                append( sb, "The overlays to apply.", 3 );
                append( sb, "", 0 );

                append( sb, "packagingExcludes", 2 );
                append( sb, "The comma separated list of tokens to exclude from the WAR before packaging. This option may be used to implement the skinny WAR use case. Note that you can use the Java Regular Expressions engine to include and exclude specific pattern using the expression %regex[]. Hint: read the about (?!Pattern).", 3 );
                append( sb, "", 0 );

                append( sb, "packagingIncludes", 2 );
                append( sb, "The comma separated list of tokens to include in the WAR before packaging. By default everything is included. This option may be used to implement the skinny WAR use case. Note that you can use the Java Regular Expressions engine to include and exclude specific pattern using the expression %regex[].", 3 );
                append( sb, "", 0 );

                append( sb, "primaryArtifact (Default: true)", 2 );
                append( sb, "Whether this is the main artifact being built. Set to false if you don\'t want to install or deploy it to the local repository instead of the default one in an execution.", 3 );
                append( sb, "Expression: ${primaryArtifact}", 3 );
                append( sb, "", 0 );

                append( sb, "useCache (Default: false)", 2 );
                append( sb, "Whether the cache should be used to save the status of the webapp across multiple runs. Experimental feature so disabled by default.", 3 );
                append( sb, "Expression: ${useCache}", 3 );
                append( sb, "", 0 );

                append( sb, "warName (Default: ${project.build.finalName})", 2 );
                append( sb, "The name of the generated WAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceDirectory (Default: ${basedir}/src/main/webapp)", 2 );
                append( sb, "Single directory for extra files to include in the WAR. This is where you place your JSP files.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceExcludes", 2 );
                append( sb, "The comma separated list of tokens to exclude when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "warSourceIncludes (Default: **)", 2 );
                append( sb, "The comma separated list of tokens to include when copying the content of the warSourceDirectory.", 3 );
                append( sb, "", 0 );

                append( sb, "webappDirectory (Default: ${project.build.directory}/${project.build.finalName})", 2 );
                append( sb, "The directory where the webapp is built.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "webResources", 2 );
                append( sb, "The list of webResources we want to transfer.", 3 );
                append( sb, "", 0 );

                append( sb, "webXml", 2 );
                append( sb, "The path to the web.xml file to use.", 3 );
                append( sb, "Expression: ${maven.war.webxml}", 3 );
                append( sb, "", 0 );

                append( sb, "workDirectory (Default: ${project.build.directory}/war/work)", 2 );
                append( sb, "Directory to unpack dependent WARs into if needed.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );
            }
        }

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( sb.toString() );
        }
    }

    /**
     * <p>Repeat a String <code>n</code> times to form a new string.</p>
     *
     * @param str String to repeat
     * @param repeat number of times to repeat str
     * @return String with repeated String
     * @throws NegativeArraySizeException if <code>repeat < 0</code>
     * @throws NullPointerException if str is <code>null</code>
     */
    private static String repeat( String str, int repeat )
    {
        StringBuffer buffer = new StringBuffer( repeat * str.length() );

        for ( int i = 0; i < repeat; i++ )
        {
            buffer.append( str );
        }

        return buffer.toString();
    }

    /** 
     * Append a description to the buffer by respecting the indentSize and lineLength parameters.
     * <b>Note</b>: The last character is always a new line.
     * 
     * @param sb The buffer to append the description, not <code>null</code>.
     * @param description The description, not <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     */
    private void append( StringBuffer sb, String description, int indent )
    {
        for ( Iterator it = toLines( description, indent, indentSize, lineLength ).iterator(); it.hasNext(); )
        {
            sb.append( it.next().toString() ).append( '\n' );
        }
    }

    /** 
     * Splits the specified text into lines of convenient display length.
     * 
     * @param text The text to split into lines, must not be <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     * @return The sequence of display lines, never <code>null</code>.
     * @throws NegativeArraySizeException if <code>indent < 0</code>
     */
    private static List toLines( String text, int indent, int indentSize, int lineLength )
    {
        List<String> lines = new ArrayList<String>();

        String ind = repeat( "\t", indent );
        String[] plainLines = text.split( "(\r\n)|(\r)|(\n)" );
        for ( int i = 0; i < plainLines.length; i++ )
        {
            toLines( lines, ind + plainLines[i], indentSize, lineLength );
        }

        return lines;
    }

    /** 
     * Adds the specified line to the output sequence, performing line wrapping if necessary.
     * 
     * @param lines The sequence of display lines, must not be <code>null</code>.
     * @param line The line to add, must not be <code>null</code>.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     */
    private static void toLines( List<String> lines, String line, int indentSize, int lineLength )
    {
        int lineIndent = getIndentLevel( line );
        StringBuffer buf = new StringBuffer( 256 );
        String[] tokens = line.split( " +" );
        for ( int i = 0; i < tokens.length; i++ )
        {
            String token = tokens[i];
            if ( i > 0 )
            {
                if ( buf.length() + token.length() >= lineLength )
                {
                    lines.add( buf.toString() );
                    buf.setLength( 0 );
                    buf.append( repeat( " ", lineIndent * indentSize ) );
                }
                else
                {
                    buf.append( ' ' );
                }
            }
            for ( int j = 0; j < token.length(); j++ )
            {
                char c = token.charAt( j );
                if ( c == '\t' )
                {
                    buf.append( repeat( " ", indentSize - buf.length() % indentSize ) );
                }
                else if ( c == '\u00A0' )
                {
                    buf.append( ' ' );
                }
                else
                {
                    buf.append( c );
                }
            }
        }
        lines.add( buf.toString() );
    }

    /** 
     * Gets the indentation level of the specified line.
     * 
     * @param line The line whose indentation level should be retrieved, must not be <code>null</code>.
     * @return The indentation level of the line.
     */
    private static int getIndentLevel( String line )
    {
        int level = 0;
        for ( int i = 0; i < line.length() && line.charAt( i ) == '\t'; i++ )
        {
            level++;
        }
        for ( int i = level + 1; i <= level + 4 && i < line.length(); i++ )
        {
            if ( line.charAt( i ) == '\t' )
            {
                level++;
                break;
            }
        }
        return level;
    }
}
