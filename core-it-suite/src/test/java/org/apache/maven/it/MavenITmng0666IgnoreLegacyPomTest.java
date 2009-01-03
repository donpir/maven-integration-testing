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
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-666">MNG-666</a>.
 * 
 * @author John Casey
 * @version $Id$
 */
public class MavenITmng0666IgnoreLegacyPomTest
    extends AbstractMavenIntegrationTestCase
{

    /**
     * Verify that maven-1 POMs will be ignored but not stop the resolution
     * process.
     */
    public void testitMNG666()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-0666" );
        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.deleteArtifacts( "org.apache.maven.its.it0059" );
        Properties verifierProperties = new Properties();
        verifierProperties.put( "failOnErrorOutput", "false" );
        verifier.setVerifierProperties( verifierProperties );
        verifier.executeGoal( "package" );
        verifier.assertFilePresent( "target/maven-it-it0059-1.0.jar" );
        // don't verify error free log
        verifier.resetStreams();

    }
}

