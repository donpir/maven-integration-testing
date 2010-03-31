package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-2276">MNG-2276</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng2276ProfileActivationBySettingsPropertyTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng2276ProfileActivationBySettingsPropertyTest()
    {
        super( "[3.0-beta-1,)" );
    }

    /**
     * Test that profiles in the POM can be activated by properties declared in active profiles from the settings.
     */
    public void testitActivation()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2276" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.getCliOptions().add( "--settings" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.setLogFileName( "log-1.txt" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/profile.properties" );
        assertEquals( "settings", props.getProperty( "project.properties.settingsProperty" ) );
        assertEquals( "pom", props.getProperty( "project.properties.pomProperty" ) );
    }

    /**
     * Tests that system properties defined on the CLI are dominant over settings properties during profile activation.
     */
    public void testitCliWins()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-2276" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.getCliOptions().add( "--settings" );
        verifier.getCliOptions().add( "settings.xml" );
        verifier.setSystemProperty( "settingsProperty", "cli" );
        verifier.setLogFileName( "log-2.txt" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/profile.properties" );
        assertEquals( "settings", props.getProperty( "project.properties.settingsProperty" ) );
        assertEquals( "", props.getProperty( "project.properties.pomProperty", "" ) );
    }

}