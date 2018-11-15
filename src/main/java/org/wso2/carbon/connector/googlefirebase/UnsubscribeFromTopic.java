/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
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
package org.wso2.carbon.connector.googlefirebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.Connector;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * It allows you to unsubscribe devices from a topic by passing list of registration tokens.
 */
public class UnsubscribeFromTopic extends AbstractConnector implements Connector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        String topicName = (String) getParameter(messageContext, FirebaseConstants.TOPIC_NAME);
        String tokenList = (String) getParameter(messageContext, FirebaseConstants.TOKEN_LIST);

        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(tokenList.split(","));

        // Unsubscribe the devices corresponding to the registration tokens from the topic.
        TopicManagementResponse response;
        try {
            if (log.isDebugEnabled()) {
                log.debug(String.format("Started to unsubscribe the list of tokens %s from the topic %s.", tokenList,
                        topicName));
            }
            response = FirebaseMessaging.getInstance().unsubscribeFromTopic(registrationTokens, topicName);
            FirebaseUtils.buildSubscriptionResponse(messageContext, response);
        } catch (IllegalArgumentException e) {
            FirebaseUtils.buildErrorResponse(messageContext, e.getMessage());
            log.error("Invalid argument specified.", e);
        } catch (FirebaseMessagingException e) {
            FirebaseUtils.buildErrorResponse(messageContext, e.getCause().getMessage());
            log.error("Error occurred while un subscribing the topic.", e);
        }
    }
}
