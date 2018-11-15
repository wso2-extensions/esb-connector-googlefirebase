/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.googlefirebase;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.util.HashMap;
import java.util.Map;

public class GoogleFirebaseConnectorIntegrationTest extends ConnectorIntegrationTestBase {

    private Map<String, String> esbRequestHeadersMap = new HashMap<>();

    /**
     * Set up the environment.
     */
    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        String connectorName = System.getProperty("connector_name") + "-connector-" +
                System.getProperty("connector_version") + ".zip";
        init(connectorName);
        getApiConfigProperties();

        esbRequestHeadersMap.put("Content-Type", "application/json");
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector send Message integration test " +
            "with mandatory parameter")
    public void testSendMessageMandatory() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:sendMessage");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "sendMessageMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result").get("MessageID"));
        Assert.assertTrue(esbRestResponse.getBody().getJSONObject("Result").get("MessageID").toString().
                contains(connectorProperties.getProperty("projectId")));
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector send Message integration test " +
            "with optional parameters")
    public void testSendMessageOptional() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:sendMessage");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "sendMessageOptional.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result").get("MessageID"));
        Assert.assertTrue(esbRestResponse.getBody().getJSONObject("Result").get("MessageID").toString().
                contains(connectorProperties.getProperty("projectId")));
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector send Message negative integration test")
    public void testSendMessageNegative() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:sendMessage");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "sendMessageNegative.json");
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result").get("Error"));
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector subscribe to topic integration test " +
            "with mandatory parameter")
    public void testSubscribeToTopicMandatory() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:subscribeToTopic");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "subscribeToTopicMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result"));
        Assert.assertTrue(Integer.parseInt(esbRestResponse.getBody().getJSONObject("Result")
                .get("SuccessCount").toString()) > 0);
        Assert.assertTrue(Integer.parseInt(esbRestResponse.getBody().getJSONObject("Result")
                .get("FailureCount").toString()) == 0);
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector subscribe to topic negative integration test")
    public void testSubscribeToTopicNegative() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:subscribeToTopic");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "subscribeToTopicNegative.json");
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result").get("Error"));
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector unsubscribe from topic integration test " +
            "with mandatory parameter")
    public void testUnsubscribeFromTopicMandatory() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:unsubscribeFromTopic");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "unsubscribeFromTopicMandatory.json");
        Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result"));
        Assert.assertTrue(Integer.parseInt(esbRestResponse.getBody().getJSONObject("Result")
                .get("SuccessCount").toString()) > 0);
        Assert.assertTrue(Integer.parseInt(esbRestResponse.getBody().getJSONObject("Result")
                .get("FailureCount").toString()) == 0);
    }

    @Test(groups = {"wso2.esb"}, description = "Firebase connector unsubscribe from topic negative integration test")
    public void testUnsubscribeFromTopicNegative() throws Exception {

        esbRequestHeadersMap.put("Action", "urn:unsubscribeFromTopic");
        RestResponse<JSONObject> esbRestResponse =
                sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap, "unsubscribeFromTopicNegative.json");
        Assert.assertNotNull(esbRestResponse.getBody().getJSONObject("Result").get("Error"));
    }
}
