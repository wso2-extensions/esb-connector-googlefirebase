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

import com.google.firebase.messaging.ApsAlert;
import org.apache.commons.lang.StringUtils;

/**
 * Sets the Aps alert information to be included in the firebase message.
 */
public class ApsAlertBean {

    private ApsAlert.Builder apsAlert = ApsAlert.builder();

    public ApsAlertBean() {

    }

    /**
     * Set the text of the alert title.
     *
     * @param apnsAlertTitle Alert Title.
     */
    public void setTitle(String apnsAlertTitle) {

        if (StringUtils.isNotEmpty(apnsAlertTitle)) {
            apsAlert.setTitle(apnsAlertTitle);
        }
    }

    /**
     * Set the text of the alert message.
     *
     * @param apnsAlertBody Alert Body.
     */
    public void setBody(String apnsAlertBody) {

        if (StringUtils.isNotEmpty(apnsAlertBody)) {
            apsAlert.setBody(apnsAlertBody);
        }
    }

    /**
     * Get ApsAlert object.
     *
     * @return ApsAlert.Builder.
     */
    public ApsAlert.Builder getApsAlert() {

        return apsAlert;
    }
}
