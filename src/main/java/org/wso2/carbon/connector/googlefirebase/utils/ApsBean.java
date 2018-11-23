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

import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import org.apache.commons.lang.StringUtils;

/**
 * Sets the Aps information to be included in the firebase message.
 */
public class ApsBean {

    private Aps.Builder aps = Aps.builder();

    public ApsBean() {

    }

    /**
     * Include this key when you want the system to modify the badge of your app icon.
     *
     * @param apnsBadge Apns Badge.
     */
    public void setBadge(String apnsBadge) {

        if (StringUtils.isNotEmpty(apnsBadge)) {
            aps.setBadge(Integer.parseInt(apnsBadge));
        }
    }

    /**
     * Include this key when you want the system to play a sound.
     *
     * @param apnsSound The name of a sound file.
     */
    public void setSound(String apnsSound) {

        if (StringUtils.isNotEmpty(apnsSound)) {
            aps.setSound(apnsSound);
        }
    }

    /**
     * Set this key with a value of 1 to configure a background update notification.
     *
     * @param apnsContentAvailable Apns Content available.
     */
    public void setContentAvailable(String apnsContentAvailable) {

        if (StringUtils.isNotEmpty(apnsContentAvailable)) {
            aps.setContentAvailable(Boolean.parseBoolean(apnsContentAvailable));
        }
    }

    /**
     * Provide this key with a string value that represents the notification’s type.
     *
     * @param apnsCategory Apns Category.
     */
    public void setCategory(String apnsCategory) {

        if (StringUtils.isNotEmpty(apnsCategory)) {
            aps.setCategory(apnsCategory);
        }
    }

    /**
     * Provide this key with a string value that represents the app-specific identifier for grouping notifications.
     *
     * @param apnsThreadId Apns threadId.
     */
    public void setThreadId(String apnsThreadId) {

        if (StringUtils.isNotEmpty(apnsThreadId)) {
            aps.setThreadId(apnsThreadId);
        }
    }

    /**
     * Set alert object.
     *
     * @param alert ApsAlert object.
     */
    public void setAlert(ApsAlert alert) {

        aps.setAlert(alert);
    }

    /**
     * Get Aps object.
     *
     * @return Aps.Builder.
     */
    public Aps.Builder getAps() {

        return aps;
    }
}
