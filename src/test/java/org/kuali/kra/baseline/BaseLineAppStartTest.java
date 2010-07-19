/*
 * Copyright 2005-2010 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.baseline;

import java.net.URL;

import org.junit.Test;
import org.kuali.kra.test.infrastructure.KcUnitTestBase;
import org.kuali.kra.test.infrastructure.KcWebTestBase;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class BaseLineAppStartTest extends KcUnitTestBase {

    @Test public void testHomePage() throws Exception {
         final WebClient webClient = new WebClient();
         final URL url = new URL(KcWebTestBase.PROTOCOL_AND_HOST + ":" + getPort() + "/kc-dev/");
         final HtmlPage page = (HtmlPage)webClient.getPage(url);
         assertEquals("Kuali Portal Index", page.getTitleText() );
    }

}
