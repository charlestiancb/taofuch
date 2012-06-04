package org.apache.maven.plugin.jar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Display help information on maven-jar-plugin.<br/> Call <pre>  mvn jar:help -Ddetail=true -Dgoal=&lt;goal-name&gt;</pre> to display parameter details.
 *
 * @version generated on Mon Jun 04 14:47:03 CST 2012
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

        append( sb, "com.tfc.maven.plugins:maven-jar-plugin:1.0.0", 0 );
        append( sb, "", 0 );

        append( sb, "Maven JAR Plugin", 0 );
        append( sb, "Builds a Java Archive (JAR) file from the compiled project classes and resources.", 1 );
        append( sb, "", 0 );

        if ( goal == null || goal.length() <= 0 )
        {
            append( sb, "This plugin has 5 goals:", 0 );
            append( sb, "", 0 );
        }

        if ( goal == null || goal.length() <= 0 || "help".equals( goal ) )
        {
            append( sb, "jar:help", 0 );
            append( sb, "Display help information on maven-jar-plugin.\nCall\n\u00a0\u00a0mvn\u00a0jar:help\u00a0-Ddetail=true\u00a0-Dgoal=<goal-name>\nto display parameter details.", 1 );
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

        if ( goal == null || goal.length() <= 0 || "jar".equals( goal ) )
        {
            append( sb, "jar:jar", 0 );
            append( sb, "Build a JAR from the current project.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "classesDirectory (Default: ${project.build.outputDirectory})", 2 );
                append( sb, "Directory containing the classes and resource files that should be packaged into the JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "classifier", 2 );
                append( sb, "Classifier to add to the artifact generated. If given, the artifact will be attached. If this is not given,it will merely be written to the output directory according to the finalName.", 3 );
                append( sb, "", 0 );

                append( sb, "excludes", 2 );
                append( sb, "List of files to exclude. Specified as fileset patterns which are relative to the input directory whose contents is being packaged into the JAR.", 3 );
                append( sb, "", 0 );

                append( sb, "finalName (Default: ${project.build.finalName})", 2 );
                append( sb, "Name of the generated JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${jar.finalName}", 3 );
                append( sb, "", 0 );

                append( sb, "forceCreation (Default: false)", 2 );
                append( sb, "Whether creating the archive should be forced.", 3 );
                append( sb, "Expression: ${jar.forceCreation}", 3 );
                append( sb, "", 0 );

                append( sb, "includes", 2 );
                append( sb, "List of files to include. Specified as fileset patterns which are relative to the input directory whose contents is being packaged into the JAR.", 3 );
                append( sb, "", 0 );

                append( sb, "newArtifactId", 2 );
                append( sb, "\u91cd\u65b0\u6307\u5b9a\u751f\u6210\u4e4b\u540e\u7684artifactId", 3 );
                append( sb, "", 0 );

                append( sb, "newGourpId", 2 );
                append( sb, "\u91cd\u65b0\u6307\u5b9a\u751f\u6210\u4e4b\u540e\u7684gouprId", 3 );
                append( sb, "", 0 );

                append( sb, "outputDirectory (Default: ${project.build.directory})", 2 );
                append( sb, "Directory containing the generated JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "skipIfEmpty (Default: false)", 2 );
                append( sb, "Skip creating empty archives", 3 );
                append( sb, "Expression: ${jar.skipIfEmpty}", 3 );
                append( sb, "", 0 );

                append( sb, "useDefaultManifestFile (Default: false)", 2 );
                append( sb, "Set this to true to enable the use of the defaultManifestFile.", 3 );
                append( sb, "Expression: ${jar.useDefaultManifestFile}", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "sign".equals( goal ) )
        {
            append( sb, "jar:sign", 0 );
            append( sb, "Deprecated. As of version 2.3, this goal is no longer supported in favor of the dedicated maven-jarsigner-plugin.", 1 );
            if ( detail )
            {
                append( sb, "", 0 );
                append( sb, "Signs a JAR using jarsigner.", 1 );
            }
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "alias", 2 );
                append( sb, "See options.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${alias}", 3 );
                append( sb, "", 0 );

                append( sb, "classifier", 2 );
                append( sb, "Classifier to use for the generated artifact. If not specified, the generated artifact becomes the primary artifact.", 3 );
                append( sb, "Expression: ${classifier}", 3 );
                append( sb, "", 0 );

                append( sb, "finalName", 2 );
                append( sb, "Name of the generated JAR (without classifier and extension).", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${project.build.finalName}", 3 );
                append( sb, "", 0 );

                append( sb, "jarPath (Default: ${project.build.directory}/${project.build.finalName}.${project.packaging})", 2 );
                append( sb, "Path of the jar to sign. When specified, the finalName is ignored.", 3 );
                append( sb, "", 0 );

                append( sb, "keypass", 2 );
                append( sb, "See options.", 3 );
                append( sb, "Expression: ${keypass}", 3 );
                append( sb, "", 0 );

                append( sb, "keystore", 2 );
                append( sb, "See options.", 3 );
                append( sb, "Expression: ${keystore}", 3 );
                append( sb, "", 0 );

                append( sb, "sigfile", 2 );
                append( sb, "See options.", 3 );
                append( sb, "Expression: ${sigfile}", 3 );
                append( sb, "", 0 );

                append( sb, "signedjar", 2 );
                append( sb, "See options. Not specifying this argument will sign the jar in-place (your original jar is going to be overwritten).", 3 );
                append( sb, "Expression: ${signedjar}", 3 );
                append( sb, "", 0 );

                append( sb, "skip (Default: false)", 2 );
                append( sb, "Set this to true to disable signing. Useful to speed up build process in development environment.", 3 );
                append( sb, "Expression: ${maven.jar.sign.skip}", 3 );
                append( sb, "", 0 );

                append( sb, "storepass", 2 );
                append( sb, "See options.", 3 );
                append( sb, "Expression: ${storepass}", 3 );
                append( sb, "", 0 );

                append( sb, "type", 2 );
                append( sb, "See options. The corresponding option in the command line is -storetype.", 3 );
                append( sb, "Expression: ${type}", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Enable verbose. See options.", 3 );
                append( sb, "Expression: ${verbose}", 3 );
                append( sb, "", 0 );

                append( sb, "verify (Default: false)", 2 );
                append( sb, "Automatically verify a jar after signing it. See options.", 3 );
                append( sb, "Expression: ${verify}", 3 );
                append( sb, "", 0 );

                append( sb, "workingDirectory (Default: ${basedir})", 2 );
                append( sb, "The working directory in which the jarsigner executable will be run.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${workingdir}", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "sign-verify".equals( goal ) )
        {
            append( sb, "jar:sign-verify", 0 );
            append( sb, "Deprecated. As of version 2.3, this goal is no longer supported in favor of the dedicated maven-jarsigner-plugin.", 1 );
            if ( detail )
            {
                append( sb, "", 0 );
                append( sb, "Checks the signature of a signed jar using jarsigner.", 1 );
            }
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "checkCerts (Default: false)", 2 );
                append( sb, "Check certificates. Requires setVerbose(). See options.", 3 );
                append( sb, "Expression: ${checkcerts}", 3 );
                append( sb, "", 0 );

                append( sb, "errorWhenNotSigned (Default: true)", 2 );
                append( sb, "When true this will make the execute() operation fail, throwing an exception, when verifying a non signed jar. Primarily to keep backwards compatibility with existing code, and allow reusing the bean in unattended operations when set to false.", 3 );
                append( sb, "Expression: ${errorWhenNotSigned}", 3 );
                append( sb, "", 0 );

                append( sb, "finalName", 2 );
                append( sb, "Name of the generated JAR (without classifier and extension).", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${project.build.finalName}", 3 );
                append( sb, "", 0 );

                append( sb, "jarPath", 2 );
                append( sb, "Path of the signed jar. When specified, the finalName is ignored.", 3 );
                append( sb, "Expression: ${jarpath}", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Enable verbose See options.", 3 );
                append( sb, "Expression: ${verbose}", 3 );
                append( sb, "", 0 );

                append( sb, "workingDirectory (Default: ${basedir})", 2 );
                append( sb, "The working directory in which the jarsigner executable will be run.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${workingdir}", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "test-jar".equals( goal ) )
        {
            append( sb, "jar:test-jar", 0 );
            append( sb, "Build a JAR of the test classes for the current project.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "archive", 2 );
                append( sb, "The archive configuration to use. See Maven Archiver Reference.", 3 );
                append( sb, "", 0 );

                append( sb, "excludes", 2 );
                append( sb, "List of files to exclude. Specified as fileset patterns which are relative to the input directory whose contents is being packaged into the JAR.", 3 );
                append( sb, "", 0 );

                append( sb, "finalName (Default: ${project.build.finalName})", 2 );
                append( sb, "Name of the generated JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "Expression: ${jar.finalName}", 3 );
                append( sb, "", 0 );

                append( sb, "forceCreation (Default: false)", 2 );
                append( sb, "Whether creating the archive should be forced.", 3 );
                append( sb, "Expression: ${jar.forceCreation}", 3 );
                append( sb, "", 0 );

                append( sb, "includes", 2 );
                append( sb, "List of files to include. Specified as fileset patterns which are relative to the input directory whose contents is being packaged into the JAR.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDirectory (Default: ${project.build.directory})", 2 );
                append( sb, "Directory containing the generated JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to true to bypass unit tests entirely. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "Expression: ${maven.test.skip}", 3 );
                append( sb, "", 0 );

                append( sb, "skipIfEmpty (Default: false)", 2 );
                append( sb, "Skip creating empty archives", 3 );
                append( sb, "Expression: ${jar.skipIfEmpty}", 3 );
                append( sb, "", 0 );

                append( sb, "testClassesDirectory (Default: ${project.build.testOutputDirectory})", 2 );
                append( sb, "Directory containing the test classes and resource files that should be packaged into the JAR.", 3 );
                append( sb, "Required: Yes", 3 );
                append( sb, "", 0 );

                append( sb, "useDefaultManifestFile (Default: false)", 2 );
                append( sb, "Set this to true to enable the use of the defaultManifestFile.", 3 );
                append( sb, "Expression: ${jar.useDefaultManifestFile}", 3 );
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
