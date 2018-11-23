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
package org.wso2.carbon.connector.googlefirebase.utils;

import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sets the Webpush-specific information to be included in the firebase message.
 */
public class WebpushConfigBean {

    private WebpushConfig.Builder webpushConfig = WebpushConfig.builder();

    public WebpushConfigBean() {

    }

    /**
     * Adds the given key-value pair as a Webpush HTTP header.
     *
     * @param webPushHeaders Headers.
     */
    public void setWebPushHeaders(String webPushHeaders) {

        if (StringUtils.isNotEmpty(webPushHeaders)) {
            Map<String, String> kayValuePairs =
                    Stream.of(webPushHeaders.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(webpushConfig::putHeader);
        }
    }

    /**
     * A map of key-value pairs where each key and value are strings.
     *
     * @param webPushData web push data.
     */
    public void setWebPushData(String webPushData) {

        if (StringUtils.isNotEmpty(webPushData)) {
            Map<String, String> kayValuePairs =
                    Stream.of(webPushData.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(webpushConfig::putData);
        }
    }

    /**
     * Set web push notification.
     *
     * @param notification WebpushNotification.
     */
    public void setNotification(WebpushNotification notification) {

        webpushConfig.setNotification(notification);
    }

    /**
     * Get web push configuration.
     *
     * @return WebpushConfig.Builder.
     */
    public WebpushConfig.Builder getWebpushConfig() {

        return webpushConfig;
    }
}
