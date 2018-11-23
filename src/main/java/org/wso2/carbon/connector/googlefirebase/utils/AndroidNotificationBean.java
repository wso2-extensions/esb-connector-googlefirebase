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

import com.google.firebase.messaging.AndroidNotification;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Sets the Android Notification information to be included in the firebase message.
 */
public class AndroidNotificationBean {

    private AndroidNotification.Builder androidNotification = AndroidNotification.builder();

    public AndroidNotificationBean() {

    }

    /**
     * Sets notification title that is specific to Android notifications.
     *
     * @param androidNotificationTitle Notification Title.
     */
    public void setTitle(String androidNotificationTitle) {

        if (StringUtils.isNotEmpty(androidNotificationTitle)) {
            androidNotification.setTitle(androidNotificationTitle);
        }
    }

    /**
     * Sets notification body that is specific to Android notifications.
     *
     * @param androidNotificationBody Notification Body.
     */
    public void setBody(String androidNotificationBody) {

        if (StringUtils.isNotEmpty(androidNotificationBody)) {
            androidNotification.setBody(androidNotificationBody);
        }
    }

    /**
     * Sets the action associated with a user click on the notification.
     *
     * @param androidClickAction Click Action.
     */
    public void setClickAction(String androidClickAction) {

        if (StringUtils.isNotEmpty(androidClickAction)) {
            androidNotification.setClickAction(androidClickAction);
        }
    }

    /**
     * The notification icon.
     *
     * @param androidIcon Notification Icon.
     */
    public void setIcon(String androidIcon) {

        if (StringUtils.isNotEmpty(androidIcon)) {
            androidNotification.setIcon(androidIcon);
        }
    }

    /**
     * The notification's icon color
     *
     * @param androidColor Color in #rrggbb format.
     */
    public void setColor(String androidColor) {

        if (StringUtils.isNotEmpty(androidColor)) {
            androidNotification.setColor(androidColor);
        }
    }

    /**
     * Identifier used to replace existing notifications in the notification drawer.
     *
     * @param androidTag Android Tag.
     */
    public void setTag(String androidTag) {

        if (StringUtils.isNotEmpty(androidTag)) {
            androidNotification.setTag(androidTag);
        }
    }

    /**
     * The sound to play when the device receives the notification.
     *
     * @param androidSound Filename of a sound resource bundled in the app.
     */
    public void setSound(String androidSound) {

        if (StringUtils.isNotEmpty(androidSound)) {
            androidNotification.setSound(androidSound);
        }
    }

    /**
     * The key of the title string in the app's string resources to use to localize the title text to the user's
     * current localization.
     *
     * @param androidTitleLocalizationKey TitleLocalizationKey.
     */
    public void setTitleLocalizationKey(String androidTitleLocalizationKey) {

        if (StringUtils.isNotEmpty(androidTitleLocalizationKey)) {
            androidNotification.setTitleLocalizationKey(androidTitleLocalizationKey);
        }
    }

    /**
     * The key of the body string in the app's string resources to use to localize the body text to the user's
     * current localization.
     *
     * @param androidBodyLocalizationKey BodyLocalizationKey.
     */
    public void setBodyLocalizationKey(String androidBodyLocalizationKey) {

        if (StringUtils.isNotEmpty(androidBodyLocalizationKey)) {
            androidNotification.setBodyLocalizationKey(androidBodyLocalizationKey);
        }
    }

    /**
     * A list of string values to be used in place of the format specifiers in androidTitleLocalizationKey to use to
     * localize the title text to the user's current localization.
     *
     * @param androidTitleLocalizationArgs TitleLocalizationArgs.
     */
    public void addAllTitleLocalizationArgs(String androidTitleLocalizationArgs) {

        if (StringUtils.isNotEmpty(androidTitleLocalizationArgs)) {
            androidNotification.addAllTitleLocalizationArgs(Arrays.asList(androidTitleLocalizationArgs.split(",")));
        }
    }

    /**
     * A list of string values to be used in place of the format specifiers in androidBodyLocalizationKey to use to
     * localize the body text to the user's current localization.
     *
     * @param androidBodyLocalizationArgs BodyLocalizationArgs.
     */
    public void addAllBodyLocalizationArgs(String androidBodyLocalizationArgs) {

        if (StringUtils.isNotEmpty(androidBodyLocalizationArgs)) {
            androidNotification.addAllBodyLocalizationArgs(Arrays.asList(androidBodyLocalizationArgs.split(",")));
        }
    }

    /**
     * Get android notification object.
     *
     * @return AndroidNotification.Builder.
     */
    public AndroidNotification.Builder getAndroidNotificationInfo() {

        return androidNotification;
    }
}
