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

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPBody;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class FirebaseUtils defines the common methods to perform all firebase connector operations.
 */
public class FirebaseUtils {

    private static final Log log = LogFactory.getLog(FirebaseUtils.class);

    private FirebaseUtils() {

    }

    /**
     * Build the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build
     *                       the firebase message.
     * @return Firebase message.
     */
    public static Message buildFirebaseMessage(MessageContext messageContext) throws IllegalArgumentException {

        //Get the messaging type. It must be one of the "token" or "topic" or "condition"
        String messagingType = (String) messageContext.getProperty(FirebaseConstants.MESSAGING_TYPE);
        // Sets the registration token of the device to which the message should be sent.
        String registrationToken = (String) messageContext.getProperty(FirebaseConstants.REGISTRATION_TOKEN);
        //Sets the name of the FCM topic to which the message should be sent.
        String topicName = (String) messageContext.getProperty(FirebaseConstants.TOPIC_NAME);
        //Sets the FCM condition to which the message should be sent.
        String condition = (String) messageContext.getProperty(FirebaseConstants.CONDITION);

        if (StringUtils.isEmpty(messagingType)) {
            handleException("Messaging Type is not set. It must contain exactly one of the token, " +
                    "topic or condition field.");
        }

        Message.Builder message = Message.builder();
        //Adds custom key-value pair to the firebase message as a data field.
        setDataFields(messageContext, message);
        //Sets the notification information to be included in the firebase message.
        setNotificationInfo(messageContext, message);
        //Sets the Android-specific information to be included in the firebase message.
        setAndroidSpecificInfo(messageContext, message);
        //Sets the information specific to APNS (Apple Push Notification Service) to firebase message.
        setApnsSpecificInfo(messageContext, message);
        //Sets the Webpush-specific information to be included in the firebase message.
        setWebPushSpecificInfo(messageContext, message);

        switch (messagingType.toLowerCase()) {
            case FirebaseConstants.MESSAGING_TYPE_TOKEN:
                if (StringUtils.isEmpty(registrationToken)) {
                    handleException("A valid device registration token must be provided.");
                }
                message.setToken(registrationToken);
                break;
            case FirebaseConstants.MESSAGING_TYPE_TOPIC:
                if (StringUtils.isEmpty(topicName)) {
                    handleException("A valid topic name must be provided.");
                }
                message.setTopic(topicName);
                break;
            case FirebaseConstants.CONDITION:
                if (StringUtils.isEmpty(condition)) {
                    handleException("A valid condition string must be provided. Condition to send the " +
                            "message to, e.g. 'foo' in topics && 'bar' in topics");
                }
                message.setCondition(condition);
                break;
            default:
                handleException("Un supported messaging Type. It must contain exactly one of the token, " +
                        "topic or condition fields.");
        }

        return message.build();
    }

    /**
     * Sets the notification information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the
     *                       notification message.
     * @param message        Firebase message.
     */
    private static void setNotificationInfo(MessageContext messageContext, Message.Builder message) {

        String notificationTitle = (String) messageContext.getProperty(FirebaseConstants.NOTIFICATION_TITLE);
        String notificationBody = (String) messageContext.getProperty(FirebaseConstants.NOTIFICATION_BODY);
        // Notification message
        if (StringUtils.isNotEmpty(notificationTitle) || StringUtils.isNotEmpty(notificationBody)) {
            message.setNotification(new Notification(notificationTitle, notificationBody));
        }
    }

    /**
     * Adds custom key-value pair to the firebase message as a data field.
     *
     * @param messageContext The message context that holds the properties which is needed to build the data message.
     * @param message        Firebase message.
     */
    private static void setDataFields(MessageContext messageContext, Message.Builder message) {

        //A map of key-value pairs where each key and value are strings.
        String dataFieldsOfMessage = (String) messageContext.getProperty(FirebaseConstants.DATA_FIELDS_MESSAGE);
        // Data message
        if (StringUtils.isNotEmpty(dataFieldsOfMessage)) {
            Map<String, String> kayValuePairs =
                    Stream.of(dataFieldsOfMessage.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(message::putData);
        }
    }

    /**
     * Sets the Webpush-specific information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the Webpush config.
     * @param message        Firebase message.
     */
    private static void setWebPushSpecificInfo(MessageContext messageContext, Message.Builder message)
            throws IllegalArgumentException {

        // Webpush message
        WebpushConfig.Builder webpushConfig = WebpushConfig.builder();
        //Set headers
        String webPushHeaders = (String) messageContext.getProperty(FirebaseConstants.WEB_PUSH_HEADERS);
        if (StringUtils.isNotEmpty(webPushHeaders)) {
            Map<String, String> kayValuePairs =
                    Stream.of(webPushHeaders.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(webpushConfig::putHeader);
        }
        //Set data
        String webPushData = (String) messageContext.getProperty(FirebaseConstants.WEB_PUSH_DATA);
        if (StringUtils.isNotEmpty(webPushData)) {
            Map<String, String> kayValuePairs =
                    Stream.of(webPushData.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(webpushConfig::putData);
        }

        webpushConfig.setNotification(getWebPushNotificationInfo(messageContext).build());
        message.setWebpushConfig(webpushConfig.build());
    }

    /**
     * Sets the Webpush Notification information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the Webpush
     *                       Notification.
     * @return WebpushNotification.Builder object.
     */
    private static WebpushNotification.Builder getWebPushNotificationInfo(MessageContext messageContext)
            throws NumberFormatException {

        WebpushNotification.Builder webPushNotification = WebpushNotification.builder();
        String webPushNotificationTitle = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TITLE);
        if (StringUtils.isNotEmpty(webPushNotificationTitle)) {
            webPushNotification.setTitle(webPushNotificationTitle);
        }
        String webPushNotificationBody = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_BODY);
        if (StringUtils.isNotEmpty(webPushNotificationBody)) {
            webPushNotification.setBody(webPushNotificationBody);
        }
        String webPushNotificationIcon = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_ICON);
        if (StringUtils.isNotEmpty(webPushNotificationIcon)) {
            webPushNotification.setIcon(webPushNotificationIcon);
        }
        String webPushNotificationBadge = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_BADGE);
        if (StringUtils.isNotEmpty(webPushNotificationBadge)) {
            webPushNotification.setBadge(webPushNotificationBadge);
        }
        String webPushNotificationImage = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_IMAGE);
        if (StringUtils.isNotEmpty(webPushNotificationImage)) {
            webPushNotification.setImage(webPushNotificationImage);
        }
        String webPushNotificationLanguage = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_LANGUAGE);
        if (StringUtils.isNotEmpty(webPushNotificationLanguage)) {
            webPushNotification.setLanguage(webPushNotificationLanguage);
        }
        String webPushNotificationTag = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TAG);
        if (StringUtils.isNotEmpty(webPushNotificationTag)) {
            webPushNotification.setTag(webPushNotificationTag);
        }
        String webPushNotificationDirection = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_DIRECTION);
        if (StringUtils.isNotEmpty(webPushNotificationDirection)) {
            webPushNotification.setDirection(getWebpushNotificationDirection(webPushNotificationDirection));
        }
        String webPushNotificationRenotify = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_RENOTIFY);
        if (StringUtils.isNotEmpty(webPushNotificationRenotify)) {
            webPushNotification.setRenotify(Boolean.parseBoolean(webPushNotificationRenotify));
        }
        String webPushNotificationInteraction = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_INTERACTION);
        if (StringUtils.isNotEmpty(webPushNotificationInteraction)) {
            webPushNotification.setRequireInteraction(Boolean.parseBoolean(webPushNotificationInteraction));
        }
        String webPushNotificationSilent = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_SILENT);
        if (StringUtils.isNotEmpty(webPushNotificationSilent)) {
            webPushNotification.setSilent(Boolean.parseBoolean(webPushNotificationSilent));
        }
        String webPushNotificationTimestamp = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TIMESTAMP);
        if (StringUtils.isNotEmpty(webPushNotificationTimestamp)) {
            webPushNotification.setTimestampMillis(Long.parseLong(webPushNotificationTimestamp));
        }
        String webPushNotificationVibrate = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_VIBRATE);
        if (StringUtils.isNotEmpty(webPushNotificationVibrate)) {
            int[] numbers = Arrays.stream(webPushNotificationVibrate.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
            webPushNotification.setVibrate(numbers);
        }
        return webPushNotification;
    }

    /**
     * Get webpush notification direction value.
     *
     * @param webPushNotificationDirection User input direction value.
     * @return WebpushNotification.Direction object.
     */
    private static WebpushNotification.Direction getWebpushNotificationDirection(String webPushNotificationDirection) {

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
        return direction;
    }

    /**
     * Sets the Android-specific information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the Android config.
     * @param message        Firebase message.
     */
    private static void setAndroidSpecificInfo(MessageContext messageContext, Message.Builder message)
            throws IllegalArgumentException {
        // Android message
        String androidPriority = (String) messageContext.getProperty(FirebaseConstants.ANDROID_PRIORITY);
        AndroidConfig.Builder androidConfig = AndroidConfig.builder();
        if (StringUtils.isNotEmpty(androidPriority)) {
            if (androidPriority.equalsIgnoreCase(FirebaseConstants.HIGH)) {
                androidConfig.setPriority(AndroidConfig.Priority.HIGH);
            } else if (androidPriority.equalsIgnoreCase(FirebaseConstants.NORMAL)) {
                androidConfig.setPriority(AndroidConfig.Priority.NORMAL);
            } else {
                log.error(String.format("Provided Priority level value %s is wrong", androidPriority));
            }
        }
        String timeToLive = (String) messageContext.getProperty(FirebaseConstants.TIME_TO_LIVE_DURATION);
        if (StringUtils.isNotEmpty(timeToLive)) {
            long timeToLiveDuration = Long.parseLong(timeToLive);
            androidConfig.setTtl(TimeUnit.SECONDS.toMillis(timeToLiveDuration));
        }
        String restrictedPackageName = (String) messageContext.getProperty(FirebaseConstants.PACKAGE_NAME);
        if (StringUtils.isNotEmpty(restrictedPackageName)) {
            androidConfig.setRestrictedPackageName(restrictedPackageName);
        }
        String collapseKey = (String) messageContext.getProperty(FirebaseConstants.COLLAPSE_KAY);
        if (StringUtils.isNotEmpty(collapseKey)) {
            androidConfig.setCollapseKey(collapseKey);
        }
        //A map of key-value pairs where each key and value are strings.
        //Set data message in android config
        String dataFieldsOfAndroidConfig = (String) messageContext.getProperty(FirebaseConstants.DATA_FIELDS_ANDROID);
        if (StringUtils.isNotEmpty(dataFieldsOfAndroidConfig)) {
            Map<String, String> kayValuePairs =
                    Stream.of(dataFieldsOfAndroidConfig.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(androidConfig::putData);
        }
        //Set notification message in android config
        androidConfig.setNotification(getAndroidNotificationInfo(messageContext).build());
        message.setAndroidConfig(androidConfig.build());
    }

    /**
     * Sets the Android Notification information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the Android
     *                       Notification.
     * @return AndroidNotification.Builder object.
     */
    private static AndroidNotification.Builder getAndroidNotificationInfo(MessageContext messageContext) {

        AndroidNotification.Builder androidNotification = AndroidNotification.builder();
        String androidNotificationTitle = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_NOTIFICATION_TITLE);
        if (StringUtils.isNotEmpty(androidNotificationTitle)) {
            androidNotification.setTitle(androidNotificationTitle);
        }
        String androidNotificationBody = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_NOTIFICATION_BODY);
        if (StringUtils.isNotEmpty(androidNotificationBody)) {
            androidNotification.setBody(androidNotificationBody);
        }
        String androidClickAction = (String) messageContext.getProperty(FirebaseConstants.ANDROID_CLICK_ACTION);
        if (StringUtils.isNotEmpty(androidClickAction)) {
            androidNotification.setClickAction(androidClickAction);
        }
        String androidIcon = (String) messageContext.getProperty(FirebaseConstants.ANDROID_ICON);
        if (StringUtils.isNotEmpty(androidIcon)) {
            androidNotification.setIcon(androidIcon);
        }
        String androidColor = (String) messageContext.getProperty(FirebaseConstants.ANDROID_COLOR);
        if (StringUtils.isNotEmpty(androidColor)) {
            androidNotification.setColor(androidColor);
        }
        String androidTag = (String) messageContext.getProperty(FirebaseConstants.ANDROID_TAG);
        if (StringUtils.isNotEmpty(androidTag)) {
            androidNotification.setTag(androidTag);
        }
        String androidSound = (String) messageContext.getProperty(FirebaseConstants.ANDROID_SOUND);
        if (StringUtils.isNotEmpty(androidSound)) {
            androidNotification.setSound(androidSound);
        }
        String androidTitleLocalizationKey = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_TITLE_LOCALIZATION_KEY);
        if (StringUtils.isNotEmpty(androidTitleLocalizationKey)) {
            androidNotification.setTitleLocalizationKey(androidTitleLocalizationKey);
        }
        String androidBodyLocalizationKey = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_BODY_LOCALIZATION_KEY);
        if (StringUtils.isNotEmpty(androidBodyLocalizationKey)) {
            androidNotification.setBodyLocalizationKey(androidBodyLocalizationKey);
        }
        String androidTitleLocalizationArgs = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_TITLE_LOCALIZATION_ARGS);
        if (StringUtils.isNotEmpty(androidTitleLocalizationArgs)) {
            androidNotification.addAllTitleLocalizationArgs(Arrays.asList(androidTitleLocalizationArgs.split(",")));
        }
        String androidBodyLocalizationArgs = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_BODY_LOCALIZATION_ARGS);
        if (StringUtils.isNotEmpty(androidBodyLocalizationArgs)) {
            androidNotification.addAllBodyLocalizationArgs(Arrays.asList(androidBodyLocalizationArgs.split(",")));
        }
        return androidNotification;
    }

    /**
     * Sets the information specific to APNS (Apple Push Notification Service) to firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the apns config.
     * @param message        Firebase message.
     */
    private static void setApnsSpecificInfo(MessageContext messageContext, Message.Builder message)
            throws IllegalArgumentException {
        // APNS message
        ApnsConfig.Builder apnsConfig = ApnsConfig.builder();
        //Set headers
        String apnsHeaders = (String) messageContext.getProperty(FirebaseConstants.APNS_HEADERS);
        if (StringUtils.isNotEmpty(apnsHeaders)) {
            Map<String, String> kayValuePairs =
                    Stream.of(apnsHeaders.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(apnsConfig::putHeader);
        }
        //Set custom data
        String apnsCustomData = (String) messageContext.getProperty(FirebaseConstants.APNS_CUSTOM_DATA);
        if (StringUtils.isNotEmpty(apnsCustomData)) {
            Map<String, String> kayValuePairs =
                    Stream.of(apnsCustomData.split(","))
                            .map(element -> element.split(":"))
                            .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
            kayValuePairs.forEach(apnsConfig::putCustomData);
        }
        Aps.Builder aps = Aps.builder();
        String apnsBadge = (String) messageContext.getProperty(FirebaseConstants.APNS_BADGE);
        if (StringUtils.isNotEmpty(apnsBadge)) {
            aps.setBadge(Integer.parseInt(apnsBadge));
        }
        String apnsSound = (String) messageContext.getProperty(FirebaseConstants.APNS_SOUND);
        if (StringUtils.isNotEmpty(apnsSound)) {
            aps.setSound(apnsSound);
        }
        String apnsContentAvailable = (String) messageContext.getProperty(FirebaseConstants.APNS_CONTENT_AVAILABLE);
        if (StringUtils.isNotEmpty(apnsContentAvailable)) {
            aps.setContentAvailable(Boolean.parseBoolean(apnsContentAvailable));
        }
        String apnsCategory = (String) messageContext.getProperty(FirebaseConstants.APNS_CATEGORY);
        if (StringUtils.isNotEmpty(apnsCategory)) {
            aps.setCategory(apnsCategory);
        }
        String apnsThreadId = (String) messageContext.getProperty(FirebaseConstants.APNS_THREAD_ID);
        if (StringUtils.isNotEmpty(apnsThreadId)) {
            aps.setThreadId(apnsThreadId);
        }
        ApsAlert.Builder apsAlert = ApsAlert.builder();
        String apnsAlertTitle = (String) messageContext.getProperty(FirebaseConstants.APNS_ALERT_TITLE);
        if (StringUtils.isNotEmpty(apnsAlertTitle)) {
            apsAlert.setTitle(apnsAlertTitle);
        }
        String apnsAlertBody = (String) messageContext.getProperty(FirebaseConstants.APNS_ALERT_BODY);
        if (StringUtils.isNotEmpty(apnsAlertBody)) {
            apsAlert.setBody(apnsAlertBody);
        }
        aps.setAlert(apsAlert.build());
        apnsConfig.setAps(aps.build());
        message.setApnsConfig(apnsConfig.build());
    }

    /**
     * Prepare payload is used to delete the element in existing body and add the new element.
     *
     * @param messageContext The message context that is used to prepare payload message flow.
     * @param element        The OMElement that needs to be added in the body.
     */
    private static void preparePayload(MessageContext messageContext, OMElement element) {

        SOAPBody soapBody = messageContext.getEnvelope().getBody();
        for (Iterator itr = soapBody.getChildElements(); itr.hasNext(); ) {
            OMElement child = (OMElement) itr.next();
            child.detach();
        }
        soapBody.addChild(element);
    }

    /**
     * Generate the result to display after firebase operations complete.
     *
     * @param messageContext The message context that is used in generate result mediation flow.
     * @param resultElements List of property values to display in the response.
     */
    public static void generateResult(MessageContext messageContext, Map<Object, Object> resultElements) {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace ns = factory.createOMNamespace(FirebaseConstants.FIREBASE_CON, FirebaseConstants.NAMESPACE);
        OMElement result = factory.createOMElement(FirebaseConstants.RESULT, ns);
        for (Map.Entry<Object, Object> element : resultElements.entrySet()) {
            OMElement messageElement = factory.createOMElement(element.getKey().toString(), ns);
            messageElement.setText(element.getValue().toString());
            result.addChild(messageElement);
        }
        preparePayload(messageContext, result);
    }

    /**
     * Common method to throw exceptions.
     *
     * @param msg error message as a string.
     * @throws SynapseException If error occurred.
     */
    protected static void handleException(String msg) throws SynapseException {

        log.error(msg);
        throw new SynapseException(msg);
    }

    /**
     * Build error response.
     *
     * @param messageContext Message context.
     * @param errorMessage   Error message.
     */
    public static void buildErrorResponse(MessageContext messageContext, String errorMessage) {

        Map<Object, Object> result = new LinkedHashMap<>();
        result.put(FirebaseConstants.ERROR, errorMessage);
        FirebaseUtils.generateResult(messageContext, result);
    }

    /**
     * Build subscription response.
     *
     * @param messageContext Message context.
     * @param response       Subscription response.
     */
    public static void buildSubscriptionResponse(MessageContext messageContext, TopicManagementResponse response) {

        Map<Object, Object> result = new LinkedHashMap<>();
        result.put(FirebaseConstants.SUCCESS_COUNT, response.getSuccessCount());
        result.put(FirebaseConstants.FAIL_COUNT, response.getFailureCount());
        result.put(FirebaseConstants.ERRORS, response.getErrors());
        FirebaseUtils.generateResult(messageContext, result);
    }
}
