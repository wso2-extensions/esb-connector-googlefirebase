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

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.connector.googlefirebase.FirebaseConstants;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sets the Android-Configuration information to be included in the firebase message.
 */
public class AndroidConfigBean {

    private static final Log log = LogFactory.getLog(AndroidConfigBean.class);
    private AndroidConfig.Builder androidConfig = AndroidConfig.builder();

    public AndroidConfigBean() {

    }

    /**
     * Set Message priority.
     * @param androidPriority Message priority. Must be one of "normal" and "high" values.
     */
    public void setPriority(String androidPriority) {

        if (StringUtils.isNotEmpty(androidPriority)) {
            if (androidPriority.equalsIgnoreCase(FirebaseConstants.HIGH)) {
                androidConfig.setPriority(AndroidConfig.Priority.HIGH);
            } else if (androidPriority.equalsIgnoreCase(FirebaseConstants.NORMAL)) {
                androidConfig.setPriority(AndroidConfig.Priority.NORMAL);
            } else {
                log.error(String.format("Provided Priority level value %s is wrong", androidPriority));
            }
        }
    }

    /**
     * This is how long the message will be kept in FCM storage if the target devices are offline.
     *
     * @param timeToLive The time-to-live duration of the message.
     */
    public void setTtl(String timeToLive) {

        if (StringUtils.isNotEmpty(timeToLive)) {
            long timeToLiveDuration = Long.parseLong(timeToLive);
            androidConfig.setTtl(TimeUnit.SECONDS.toMillis(timeToLiveDuration));
        }
    }

    /**
     * Package name of the application where the registration tokens must match in order to receive the message.
     *
     * @param restrictedPackageName Package name of the Android application.
     */
    public void setRestrictedPackageName(String restrictedPackageName) {

        if (StringUtils.isNotEmpty(restrictedPackageName)) {
            androidConfig.setRestrictedPackageName(restrictedPackageName);
        }
    }

    /**
     * An identifier of a group of messages that can be collapsed, so that only the last message gets sent
     * when delivery can be resumed.
     *
     * @param collapseKey An identifier of a group of messages.
     */
    public void setCollapseKey(String collapseKey) {

        if (StringUtils.isNotEmpty(collapseKey)) {
            androidConfig.setCollapseKey(collapseKey);
        }
    }

    /**
     * A map of key-value pairs where each key and value are strings.
     *
     * @param dataFieldsOfAndroidConfig Data fields.
     */
    public void setData(String dataFieldsOfAndroidConfig) {

        if (StringUtils.isNotEmpty(dataFieldsOfAndroidConfig)) {
            Map<String, String> kayValuePairs =
                    Stream.of(dataFieldsOfAndroidConfig.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(androidConfig::putData);
        }
    }

    /**
     * Set notification object.
     *
     * @param notification AndroidNotification.
     */
    public void setNotification(AndroidNotification notification) {

        androidConfig.setNotification(notification);
    }

    /**
     * Get android configuration.
     *
     * @return AndroidConfig.Builder.
     */
    public AndroidConfig.Builder getAndroidConfig() {

        return androidConfig;
    }
}
