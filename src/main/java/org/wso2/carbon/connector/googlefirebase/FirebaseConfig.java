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

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.apache.synapse.MessageContext;
import org.json.JSONObject;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.Connector;
import org.wso2.carbon.connector.googlefirebase.utils.FirebaseUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Initializes the FirebaseApp (if not already initialized).
 */
public class FirebaseConfig extends AbstractConnector implements Connector {

    private FirebaseApp firebaseApp;

    @Override
    public void connect(MessageContext messageContext) {

        if (firebaseApp == null) {
            if (log.isDebugEnabled()) {
                log.debug("Getting firebase service account's credentials from messageContext to initiate the " +
                        "firebase connector init operation.");
            }

            JSONObject credentials = new JSONObject();
            String accountType = (String) getParameter(messageContext, FirebaseConstants.ACCOUNT_TYPE);
            String projectId = (String) getParameter(messageContext, FirebaseConstants.PROJECT_ID);
            String privateKeyId = (String) getParameter(messageContext, FirebaseConstants.PRIVATE_KEY_ID);
            String privateKey = (String) getParameter(messageContext, FirebaseConstants.PRIVATE_KEY);
            String clientEmail = (String) getParameter(messageContext, FirebaseConstants.CLIENT_EMAIL);
            String clientId = (String) getParameter(messageContext, FirebaseConstants.CLIENT_ID);
            String authUri = (String) getParameter(messageContext, FirebaseConstants.AUTH_URI);
            String tokenUri = (String) getParameter(messageContext, FirebaseConstants.TOKEN_URI);
            String authProviderCertUrl = (String) getParameter(messageContext,
                    FirebaseConstants.AUTH_PROVIDER_CERT_URL);
            String clientCertUrl = (String) getParameter(messageContext, FirebaseConstants.CLIENT_CERT_URL);

            credentials.put(FirebaseConstants.API_ACCOUNT_TYPE, accountType);
            credentials.put(FirebaseConstants.API_PROJECT_ID, projectId);
            credentials.put(FirebaseConstants.API_PRIVATE_KEY_ID, privateKeyId);
            credentials.put(FirebaseConstants.API_PRIVATE_KEY, privateKey);
            credentials.put(FirebaseConstants.API_CLIENT_EMAIL, clientEmail);
            credentials.put(FirebaseConstants.API_CLIENT_ID, clientId);
            credentials.put(FirebaseConstants.API_AUTH_URI, authUri);
            credentials.put(FirebaseConstants.API_TOKEN_URI, tokenUri);
            credentials.put(FirebaseConstants.API_AUTH_PROVIDER_CERT_URL, authProviderCertUrl);
            credentials.put(FirebaseConstants.API_CLIENT_CERT_URL, clientCertUrl);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Successfully loaded firebase service account's credentials with accountType " +
                                ": %s, projectId : %s, clientEmail : %s, clientId : %s, authUri : %s, tokenUri : %s, " +
                                "authProviderCertUrl : %s, clientCertUrl : %s.", accountType, projectId, clientEmail,
                        clientId, authUri, tokenUri, authProviderCertUrl, clientCertUrl));
            }
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Started to initialize the firebase admin sdk with the provided firebase service " +
                            "account's credentials");
                }
                FirebaseOptions options = FirebaseOptions.builder()
                        .setDatabaseUrl(FirebaseConstants.HTTPS + credentials.get(FirebaseConstants.API_PROJECT_ID)
                                + FirebaseConstants.FIREBASE_IO)
                        .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(
                                credentials.toString().getBytes())))
                        .build();
                firebaseApp = FirebaseApp.initializeApp(options);
                if (log.isDebugEnabled()) {
                    log.debug("Successfully initialized the firebase admin sdk with the provided firebase service " +
                            "account's credentials");
                }
            } catch (IOException e) {
                FirebaseUtils.handleException("Failed to read service account credentials from stream. ", e);
            }
        }
    }
}
