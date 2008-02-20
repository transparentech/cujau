package org.cujau.maven;

import java.io.File;

import org.w3c.dom.Element;

/**
 * Class representing a dependency from a Maven POM file.
 */
public class POMDependency {
    private final String groupId;
    private final String artifactId;
    private final String version;

    /**
     * Construct a new instance.
     * 
     * @param elem
     *            A <tt>&lt;dependency&gt;</tt> Element from the POM file.
     */
    public POMDependency( Element elem ) {
        groupId = elem.getElementsByTagName( "groupId" ).item( 0 ).getFirstChild().getNodeValue();
        artifactId = elem.getElementsByTagName( "artifactId" ).item( 0 ).getFirstChild().getNodeValue();
        if ( elem.getElementsByTagName( "version" ).item( 0 ) != null ) {
            version = elem.getElementsByTagName( "version" ).item( 0 ).getFirstChild().getNodeValue();
        } else {
            version = "";
        }
    }

    /**
     * Get the absolute path name of the Maven artifact represented by this
     * dependency.
     * 
     * @param mavenRepo
     *            The absolute path name of the Maven repository in which this
     *            artifact can be found.
     * @return The concatenation of the mavenRepo base path plus the path to
     *         this artifact in the repository.
     */
    public String getArtifactPath( String mavenRepo ) {
        StringBuffer buf = buildBasePath( mavenRepo );
        buf.append( artifactId ).append( "-" ).append( version ).append( ".jar" );
        return buf.toString();
    }

    /**
     * Get the absolute path name of the POM file of the Maven artifact
     * represented by this dependency.
     * 
     * @param mavenRepo
     *            The absolute path name of the Maven repository in which this
     *            artifact can be found.
     * @return The concatentation of the mavenRepo base path plus the path to
     *         this artifact's POM file in the repository.
     */
    public String getPOMPath( String mavenRepo ) {
        StringBuffer buf = buildBasePath( mavenRepo );
        buf.append( artifactId ).append( "-" ).append( version ).append( ".pom" );
        return buf.toString();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( obj.getClass() != this.getClass() ) {
            return false;
        }
        POMDependency dep = (POMDependency) obj;
        return getGroupId().equals( dep.getGroupId() ) && getArtifactId().equals( dep.getArtifactId() )
               && getVersion().equals( dep.getVersion() );
    }

    @Override
    public int hashCode() {
        int hash = 89;
        hash = 31 * hash + ( null == getGroupId() ? 0 : getGroupId().hashCode() );
        hash = 31 * hash + ( null == getArtifactId() ? 0 : getArtifactId().hashCode() );
        hash = 31 * hash + ( null == getVersion() ? 0 : getVersion().hashCode() );
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append( groupId ).append( ":" ).append( artifactId ).append( "-" ).append( version );
        return buf.toString();
    }
    
    /**
     * @return This dependency's Maven groupId.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return This dependency's Maven artifactId.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * @return This dependency's version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Build the absolute directory path of the Maven dependency. This generates
     * only the directory path and does not include any specific file in that
     * directory.
     * 
     * @param mavenRepo
     *            The absolute path name of the Maven repository in which this
     *            artifact can be found.
     * @return A StringBuffer containing the path.
     */
    private StringBuffer buildBasePath( String mavenRepo ) {
        String[] groupPaths = groupId.split( "\\." );
        StringBuffer buf = new StringBuffer();
        buf.append( mavenRepo );
        for ( String gp : groupPaths ) {
            buf.append( File.separatorChar ).append( gp );
        }
        buf.append( File.separatorChar ).append( artifactId ).append( File.separatorChar ).append( version )
           .append( File.separatorChar );
        return buf;
    }
}