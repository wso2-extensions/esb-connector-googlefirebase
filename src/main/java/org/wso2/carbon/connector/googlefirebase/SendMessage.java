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
import com.google.firebase.messaging.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.connector.core.Connector;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * It allows you to send Firebase messages to end-user devices.
 */
public class SendMessage extends AbstractConnector implements Connector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        // Send a message in the dry run mode.
        boolean dryRun = false;
        String dryRunMode = (String) getParameter(messageContext, FirebaseConstants.DRY_RUN_MODE);
        if (StringUtils.isNotEmpty(dryRunMode)) {
            dryRun = Boolean.parseBoolean(dryRunMode);
        }
        Message message;
        Map<Object, Object> result = new LinkedHashMap<>();
        try {
            message = FirebaseUtils.buildFirebaseMessage(messageContext);

            // Send a message to the device corresponding to the provided registration token or topic or condition.
            String response;
            if (log.isDebugEnabled()) {
                log.debug(String.format("Started to send the firebase message with dryRun %s mode.", dryRun));
            }
            response = FirebaseMessaging.getInstance().send(message, dryRun);
            if (log.isDebugEnabled()) {
                log.debug(String.format("Successfully send the firebase message with MessageID: %s.", response));
            }
            // Response is a message ID string.
            result.put(FirebaseConstants.MESSAGE_ID, response);
            FirebaseUtils.generateResult(messageContext, result);
        } catch (NumberFormatException e) {
            FirebaseUtils.buildErrorResponse(messageContext, e.getMessage());
            log.error("Error occurred when attempted to convert a string to one of the numeric types, but that the" +
                    " string does not have the appropriate format.", e);
        } catch (IllegalArgumentException e) {
            FirebaseUtils.buildErrorResponse(messageContext, e.getMessage());
            log.error("Invalid argument specified.", e);
        } catch (FirebaseMessagingException e) {
            FirebaseUtils.buildErrorResponse(messageContext, e.getCause().getMessage());
            log.error("Error while sending firebase message.", e);
        }
    }
}
