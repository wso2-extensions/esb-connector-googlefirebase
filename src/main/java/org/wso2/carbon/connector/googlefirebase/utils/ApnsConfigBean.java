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

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sets the Apns-Configuration information to be included in the firebase message.
 */
public class ApnsConfigBean {

    private ApnsConfig.Builder apnsConfig = ApnsConfig.builder();

    public ApnsConfigBean() {

    }

    /**
     * Set HTTP request headers defined in Apple Push Notification Service.
     *
     * @param apnsHeaders Headers.
     */
    public void setHeader(String apnsHeaders) {

        if (StringUtils.isNotEmpty(apnsHeaders)) {
            Map<String, String> kayValuePairs =
                    Stream.of(apnsHeaders.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(apnsConfig::putHeader);
        }
    }

    /**
     * A map of key-value pairs where each key and value are strings.
     *
     * @param apnsCustomData Custom data.
     */
    public void setCustomData(String apnsCustomData) {

        if (StringUtils.isNotEmpty(apnsCustomData)) {
            Map<String, String> kayValuePairs =
                    Stream.of(apnsCustomData.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(apnsConfig::putCustomData);
        }
    }

    /**
     * Set Aps object.
     *
     * @param aps Aps object.
     */
    public void setAps(Aps aps) {

        apnsConfig.setAps(aps);
    }

    /**
     * Get Apns configuration.
     *
     * @return ApnsConfig.Builder.
     */
    public ApnsConfig.Builder getApnsConfig() {

        return apnsConfig;
    }
}
