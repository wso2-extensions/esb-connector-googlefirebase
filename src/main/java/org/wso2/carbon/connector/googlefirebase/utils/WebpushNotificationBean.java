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

import com.google.firebase.messaging.WebpushNotification;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.connector.googlefirebase.FirebaseConstants;

import java.util.Arrays;

/**
 * Sets the Webpush Notification information to be included in the firebase message.
 */
public class WebpushNotificationBean {

    private static final Log log = LogFactory.getLog(WebpushNotificationBean.class);
    private WebpushNotification.Builder webPushNotification = WebpushNotification.builder();

    public WebpushNotificationBean() {

    }

    /**
     * Get web push notification information.
     *
     * @return WebpushNotification.Builder.
     */
    public WebpushNotification.Builder getWebPushNotificationInfo() {

        return webPushNotification;
    }

    /**
     * The title of the notification.
     *
     * @param webPushNotificationTitle Push Notification Title.
     */
    public void setTitle(String webPushNotificationTitle) {

        if (StringUtils.isNotEmpty(webPushNotificationTitle)) {
            webPushNotification.setTitle(webPushNotificationTitle);
        }
    }

    /**
     * The body of the notification.
     *
     * @param webPushNotificationBody Push Notification Body.
     */
    public void setBody(String webPushNotificationBody) {

        if (StringUtils.isNotEmpty(webPushNotificationBody)) {
            webPushNotification.setBody(webPushNotificationBody);
        }
    }

    /**
     * It contains the URL of an icon to be displayed as part of the web notification.
     *
     * @param webPushNotificationIcon Push notification icon.
     */
    public void setIcon(String webPushNotificationIcon) {

        if (StringUtils.isNotEmpty(webPushNotificationIcon)) {
            webPushNotification.setIcon(webPushNotificationIcon);
        }
    }

    /**
     * The URL of the image used to represent the notification when there is not enough space to display
     * the notification itself.
     *
     * @param webPushNotificationBadge Notification badge.
     */
    public void setBadge(String webPushNotificationBadge) {

        if (StringUtils.isNotEmpty(webPushNotificationBadge)) {
            webPushNotification.setBadge(webPushNotificationBadge);
        }
    }

    /**
     * The URL of an image to be displayed as part of the notification.
     *
     * @param webPushNotificationImage NotificationImage.
     */
    public void setImage(String webPushNotificationImage) {

        if (StringUtils.isNotEmpty(webPushNotificationImage)) {
            webPushNotification.setImage(webPushNotificationImage);
        }
    }

    /**
     * Sets the language of the notification.
     *
     * @param webPushNotificationLanguage NotificationLanguage.
     */
    public void setLanguage(String webPushNotificationLanguage) {

        if (StringUtils.isNotEmpty(webPushNotificationLanguage)) {
            webPushNotification.setLanguage(webPushNotificationLanguage);
        }
    }

    /**
     * Sets an identifying tag on the notification.
     *
     * @param webPushNotificationTag Notification tag.
     */
    public void setTag(String webPushNotificationTag) {

        if (StringUtils.isNotEmpty(webPushNotificationTag)) {
            webPushNotification.setTag(webPushNotificationTag);
        }
    }

    /**
     * The text direction of the notification.
     *
     * @param webPushNotificationDirection Notification Direction.
     */
    public void setDirection(String webPushNotificationDirection) {

        WebpushNotification.Direction direction = null;
        if (webPushNotificationDirection.equalsIgnoreCase(FirebaseConstants.AUTO)) {
            direction = WebpushNotification.Direction.AUTO;
        } else if (webPushNotificationDirection.equalsIgnoreCase(FirebaseConstants.LEFT_TO_RIGHT) ||
                webPushNotificationDirection.equalsIgnoreCase(FirebaseConstants.LTR)) {
            direction = WebpushNotification.Direction.LEFT_TO_RIGHT;
        } else if (webPushNotificationDirection.equalsIgnoreCase(FirebaseConstants.RIGHT_TO_LEFT) ||
                webPushNotificationDirection.equalsIgnoreCase(FirebaseConstants.RTL)) {
            direction = WebpushNotification.Direction.RIGHT_TO_LEFT;
        } else {
            log.error(String.format("Provided web push notification direction value %s is wrong",
                    webPushNotificationDirection));
        }
        if (StringUtils.isNotEmpty(webPushNotificationDirection)) {
            webPushNotification.setDirection(direction);
        }
    }

    /**
     * Specifies whether the user should be notified after a new notification replaces an old one.
     *
     * @param webPushNotificationRenotify whether the user should be notified after a new notification
     *                                    replaces an old one.
     */
    public void setRenotify(String webPushNotificationRenotify) {

        if (StringUtils.isNotEmpty(webPushNotificationRenotify)) {
            webPushNotification.setRenotify(Boolean.parseBoolean(webPushNotificationRenotify));
        }
    }

    /**
     * A Boolean indicating that a notification should remain active until the user clicks or dismisses it,
     * rather than closing automatically.
     *
     * @param webPushNotificationInteraction Indicating that a notification should remain active until the user
     *                                       clicks or dismisses it.
     */
    public void setRequireInteraction(String webPushNotificationInteraction) {

        if (StringUtils.isNotEmpty(webPushNotificationInteraction)) {
            webPushNotification.setRequireInteraction(Boolean.parseBoolean(webPushNotificationInteraction));
        }
    }

    /**
     * Specifies whether the notification should be silent.
     *
     * @param webPushNotificationSilent Notification Silent.
     */
    public void setSilent(String webPushNotificationSilent) {

        if (StringUtils.isNotEmpty(webPushNotificationSilent)) {
            webPushNotification.setSilent(Boolean.parseBoolean(webPushNotificationSilent));
        }
    }

    /**
     * Specifies the time at which a notification is created or applicable.
     *
     * @param webPushNotificationTimestamp Notification timestamp.
     */
    public void setTimestampMillis(String webPushNotificationTimestamp) {

        if (StringUtils.isNotEmpty(webPushNotificationTimestamp)) {
            webPushNotification.setTimestampMillis(Long.parseLong(webPushNotificationTimestamp));
        }
    }

    /**
     * Specifies a vibration pattern for devices with vibration hardware to emit.
     *
     * @param webPushNotificationVibrate Notification vibrate.
     */
    public void setVibrate(String webPushNotificationVibrate) {

        if (StringUtils.isNotEmpty(webPushNotificationVibrate)) {
            int[] numbers = Arrays.stream(webPushNotificationVibrate.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
            webPushNotification.setVibrate(numbers);
        }
    }
}
