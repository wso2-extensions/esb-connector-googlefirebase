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

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
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
import org.wso2.carbon.connector.googlefirebase.FirebaseConstants;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
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
    public static Message buildFirebaseMessage(MessageContext messageContext) {

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
    private static void setWebPushSpecificInfo(MessageContext messageContext, Message.Builder message) {

        // Build Webpush message
        WebpushConfigBean webpushConfigBean = new WebpushConfigBean();
        WebpushNotificationBean webpushNotificationBean = new WebpushNotificationBean();
        //Set headers
        String webPushHeaders = (String) messageContext.getProperty(FirebaseConstants.WEB_PUSH_HEADERS);
        webpushConfigBean.setWebPushHeaders(webPushHeaders);
        //Set data
        String webPushData = (String) messageContext.getProperty(FirebaseConstants.WEB_PUSH_DATA);
        webpushConfigBean.setWebPushData(webPushData);

        //Sets the Webpush Notification information to be included in the firebase message.
        String webPushNotificationTitle = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TITLE);
        webpushNotificationBean.setTitle(webPushNotificationTitle);

        String webPushNotificationBody = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_BODY);
        webpushNotificationBean.setBody(webPushNotificationBody);

        String webPushNotificationIcon = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_ICON);
        webpushNotificationBean.setIcon(webPushNotificationIcon);

        String webPushNotificationBadge = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_BADGE);
        webpushNotificationBean.setBadge(webPushNotificationBadge);

        String webPushNotificationImage = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_IMAGE);
        webpushNotificationBean.setImage(webPushNotificationImage);

        String webPushNotificationLanguage = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_LANGUAGE);
        webpushNotificationBean.setLanguage(webPushNotificationLanguage);

        String webPushNotificationTag = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TAG);
        webpushNotificationBean.setTag(webPushNotificationTag);

        String webPushNotificationDirection = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_DIRECTION);
        webpushNotificationBean.setDirection(webPushNotificationDirection);

        String webPushNotificationRenotify = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_RENOTIFY);
        webpushNotificationBean.setRenotify(webPushNotificationRenotify);

        String webPushNotificationInteraction = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_INTERACTION);
        webpushNotificationBean.setRequireInteraction(webPushNotificationInteraction);

        String webPushNotificationSilent = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_SILENT);
        webpushNotificationBean.setSilent(webPushNotificationSilent);

        String webPushNotificationTimestamp = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_TIMESTAMP);
        webpushNotificationBean.setTimestampMillis(webPushNotificationTimestamp);

        String webPushNotificationVibrate = (String) messageContext.getProperty(
                FirebaseConstants.WEB_PUSH_NOTIFICATION_VIBRATE);
        webpushNotificationBean.setVibrate(webPushNotificationVibrate);

        boolean isWebpushConfigExist = false;
        if (StringUtils.isNotEmpty(webPushNotificationTitle) || StringUtils.isNotEmpty(webPushNotificationBody) ||
                StringUtils.isNotEmpty(webPushNotificationIcon) || StringUtils.isNotEmpty(webPushNotificationBadge) ||
                StringUtils.isNotEmpty(webPushNotificationImage) || StringUtils.isNotEmpty(webPushNotificationLanguage)
                || StringUtils.isNotEmpty(webPushNotificationTag) || StringUtils.isNotEmpty(
                webPushNotificationDirection) || StringUtils.isNotEmpty(webPushNotificationRenotify) ||
                StringUtils.isNotEmpty(webPushNotificationInteraction) || StringUtils.isNotEmpty(
                webPushNotificationSilent) || StringUtils.isNotEmpty(webPushNotificationTimestamp) ||
                StringUtils.isNotEmpty(webPushNotificationVibrate)) {
            isWebpushConfigExist = true;
            webpushConfigBean.setNotification(webpushNotificationBean.getWebPushNotificationInfo().build());
        }

        if (isWebpushConfigExist || StringUtils.isNotEmpty(webPushHeaders) || StringUtils.isNotEmpty(webPushData)) {
            message.setWebpushConfig(webpushConfigBean.getWebpushConfig().build());
        }
    }

    /**
     * Sets the Android-specific information to be included in the firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the Android config.
     * @param message        Firebase message.
     */
    private static void setAndroidSpecificInfo(MessageContext messageContext, Message.Builder message) {

        // Build Android message
        AndroidConfigBean androidConfigBean = new AndroidConfigBean();
        AndroidNotificationBean androidNotificationBean = new AndroidNotificationBean();
        String androidPriority = (String) messageContext.getProperty(FirebaseConstants.ANDROID_PRIORITY);
        androidConfigBean.setPriority(androidPriority);

        String timeToLive = (String) messageContext.getProperty(FirebaseConstants.TIME_TO_LIVE_DURATION);
        androidConfigBean.setTtl(timeToLive);

        String restrictedPackageName = (String) messageContext.getProperty(FirebaseConstants.PACKAGE_NAME);
        androidConfigBean.setRestrictedPackageName(restrictedPackageName);

        String collapseKey = (String) messageContext.getProperty(FirebaseConstants.COLLAPSE_KAY);
        androidConfigBean.setCollapseKey(collapseKey);

        String dataFieldsOfAndroidConfig = (String) messageContext.getProperty(FirebaseConstants.DATA_FIELDS_ANDROID);
        androidConfigBean.setData(dataFieldsOfAndroidConfig);

        String androidNotificationTitle = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_NOTIFICATION_TITLE);
        androidNotificationBean.setTitle(androidNotificationTitle);

        String androidNotificationBody = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_NOTIFICATION_BODY);
        androidNotificationBean.setBody(androidNotificationBody);

        String androidClickAction = (String) messageContext.getProperty(FirebaseConstants.ANDROID_CLICK_ACTION);
        androidNotificationBean.setClickAction(androidClickAction);

        String androidIcon = (String) messageContext.getProperty(FirebaseConstants.ANDROID_ICON);
        androidNotificationBean.setIcon(androidIcon);

        String androidColor = (String) messageContext.getProperty(FirebaseConstants.ANDROID_COLOR);
        androidNotificationBean.setColor(androidColor);

        String androidTag = (String) messageContext.getProperty(FirebaseConstants.ANDROID_TAG);
        androidNotificationBean.setTag(androidTag);

        String androidSound = (String) messageContext.getProperty(FirebaseConstants.ANDROID_SOUND);
        androidNotificationBean.setSound(androidSound);

        String androidTitleLocalizationKey = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_TITLE_LOCALIZATION_KEY);
        androidNotificationBean.setTitleLocalizationKey(androidTitleLocalizationKey);

        String androidBodyLocalizationKey = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_BODY_LOCALIZATION_KEY);
        androidNotificationBean.setBodyLocalizationKey(androidBodyLocalizationKey);

        String androidTitleLocalizationArgs = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_TITLE_LOCALIZATION_ARGS);
        androidNotificationBean.addAllTitleLocalizationArgs(androidTitleLocalizationArgs);

        String androidBodyLocalizationArgs = (String) messageContext.getProperty(
                FirebaseConstants.ANDROID_BODY_LOCALIZATION_ARGS);
        androidNotificationBean.addAllBodyLocalizationArgs(androidBodyLocalizationArgs);

        boolean isNotificationExist = false;
        if (StringUtils.isNotEmpty(androidNotificationTitle) || StringUtils.isNotEmpty(androidNotificationBody) ||
                StringUtils.isNotEmpty(androidClickAction) || StringUtils.isNotEmpty(androidIcon) ||
                StringUtils.isNotEmpty(androidColor) || StringUtils.isNotEmpty(androidTag) || StringUtils.isNotEmpty(
                androidSound) || StringUtils.isNotEmpty(androidTitleLocalizationKey) || StringUtils.isNotEmpty(
                androidBodyLocalizationKey) || StringUtils.isNotEmpty(androidTitleLocalizationArgs)
                || StringUtils.isNotEmpty(androidBodyLocalizationArgs)) {
            //Set notification message in android config
            isNotificationExist = true;
            androidConfigBean.setNotification(androidNotificationBean.getAndroidNotificationInfo().build());
        }
        if (isNotificationExist || StringUtils.isNotEmpty(androidPriority) || StringUtils.isNotEmpty(timeToLive) ||
                StringUtils.isNotEmpty(restrictedPackageName) || StringUtils.isNotEmpty(collapseKey) ||
                StringUtils.isNotEmpty(dataFieldsOfAndroidConfig)) {
            message.setAndroidConfig(androidConfigBean.getAndroidConfig().build());
        }
    }

    /**
     * Sets the information specific to APNS (Apple Push Notification Service) to firebase message.
     *
     * @param messageContext The message context that holds the properties which is needed to build the apns config.
     * @param message        Firebase message.
     */
    private static void setApnsSpecificInfo(MessageContext messageContext, Message.Builder message) {

        // Build APNS message
        ApnsConfigBean apnsConfigBean = new ApnsConfigBean();

        String apnsHeaders = (String) messageContext.getProperty(FirebaseConstants.APNS_HEADERS);
        apnsConfigBean.setHeader(apnsHeaders);

        String apnsCustomData = (String) messageContext.getProperty(FirebaseConstants.APNS_CUSTOM_DATA);
        apnsConfigBean.setCustomData(apnsCustomData);

        ApsBean apsBean = new ApsBean();
        String apnsBadge = (String) messageContext.getProperty(FirebaseConstants.APNS_BADGE);
        apsBean.setBadge(apnsBadge);

        String apnsSound = (String) messageContext.getProperty(FirebaseConstants.APNS_SOUND);
        apsBean.setSound(apnsSound);

        String apnsContentAvailable = (String) messageContext.getProperty(FirebaseConstants.APNS_CONTENT_AVAILABLE);
        apsBean.setContentAvailable(apnsContentAvailable);

        String apnsCategory = (String) messageContext.getProperty(FirebaseConstants.APNS_CATEGORY);
        apsBean.setCategory(apnsCategory);

        String apnsThreadId = (String) messageContext.getProperty(FirebaseConstants.APNS_THREAD_ID);
        apsBean.setThreadId(apnsThreadId);

        ApsAlertBean apsAlertBean = new ApsAlertBean();
        String apnsAlertTitle = (String) messageContext.getProperty(FirebaseConstants.APNS_ALERT_TITLE);
        apsAlertBean.setTitle(apnsAlertTitle);

        String apnsAlertBody = (String) messageContext.getProperty(FirebaseConstants.APNS_ALERT_BODY);
        apsAlertBean.setBody(apnsAlertBody);

        boolean isAlertExist = false;
        boolean isApsExist = false;
        if (StringUtils.isNotEmpty(apnsAlertTitle) || StringUtils.isNotEmpty(apnsAlertBody)) {
            isAlertExist = true;
            apsBean.setAlert(apsAlertBean.getApsAlert().build());
        }
        if ((StringUtils.isNotEmpty(apnsBadge) || StringUtils.isNotEmpty(apnsSound) || StringUtils.isNotEmpty(
                apnsContentAvailable) || StringUtils.isNotEmpty(apnsCategory) || StringUtils.isNotEmpty(apnsThreadId))
                || isAlertExist) {
            isApsExist = true;
            apnsConfigBean.setAps(apsBean.getAps().build());
        }
        if (isApsExist || StringUtils.isNotEmpty(apnsHeaders) || StringUtils.isNotEmpty(apnsCustomData)) {
            message.setApnsConfig(apnsConfigBean.getApnsConfig().build());
        }
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
    public static void handleException(String msg) throws SynapseException {

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

    /**
     * Common method to throw exceptions.
     *
     * @param msg this parameter contain error message that we need to throw.
     * @param e   Exception object.
     * @throws SynapseException If error occurred.
     */
    public static void handleException(String msg, Exception e) throws SynapseException {

        log.error(msg + e);
        throw new SynapseException(msg, e);
    }
}
