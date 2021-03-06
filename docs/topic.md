# Working with Firebase Topic Subscriptions

[[Overview](#overview)]  [[Operation details](#operation-details)]  [[Sample configuration](#sample-configuration)]

### Overview

The following operations allow you to work with topics. Click an operation name to see details on how to use it.

| Operation        | Description |
| ------------- |-------------|
| [subscribeToTopic](#subscribe-to-a-topic)    | It allows you to subscribe devices to a topic. |
| [unsubscribeFromTopic](#unsubscribe-from-a-topic)    | It allows you to unsubscribe devices from a topic. |


### Operation details

This section provides more details on each of the operations.


#### Subscribe to a topic

This operation allows you to subscribe devices to a topic by providing the registration tokens for the devices to subscribe.
Registration tokens are strings generated by the client FCM SDKs for each end-user client app instance.


**subscribeToTopic**

```xml
<googlefirebase.subscribeToTopic>
    <topicName>{$ctx:topicName}</topicName>
    <tokenList>{$ctx:tokenList}</tokenList>
</googlefirebase.subscribeToTopic>
```

**Properties**

* topicName - The topic name that need to be subscribe by devices.
* tokenList - List of registration tokens as comma separated values which are generated by the client FCM SDKs for each end-user client app instance.
              (Eg:- "YOUR_REGISTRATION_TOKEN_1,YOUR_REGISTRATION_TOKEN_2, ----,YOUR_REGISTRATION_TOKEN_n")


**Sample request**

```json
{
  "accountType": "service_account",
  "projectId": "Test-42xx9",
  "privateKeyId": "8d4ed1afsfsg56jiyu5ggfhgc02b3055a7197b0ac9afe",
  "privateKey": "-----BEGIN PRIVATE KEY-----\nMI3sfsgfgdhgdhgogxD9TEC3wkCC\nGKRvlPH1wqXPMhhsWEMHdhhghghgNBWyg/r+ff59KuqhQBs\nHy3ixWbSYW5XYd0ww6fT/UOH1dLYKSKLidEO6v2Rd2Xh+bxSwqi3IuyjuDy8WBJ6\asafrfrsfrsgtyuy\nS4hn+hfhgfhf/e6pGf0Z2KjL4375OUEEK6S\nCixuZQPFHlnqXi6OcmsLqa+A0kmjkLlVHPe9iH6n5Ku7Ikd+P+7ycRS2W8SF6+fg\nIt7oO9LpAgMBAAECggEACQy65kOIq+W2gFYcSfLHjhwqj/FKiaexd94l91slYPpV\n44v2ghxpOqmDTFRNA1rdgTM0NlFbFnCx8wc3TWOuZTN+0RoMHouPZo2GKX7Epepg\nSiVNW9NSaVQZHbTuAHV8ST41U7M4AyG+t6i1JEx2recStG+QCfqi2/xV2V0kN6ZL\neUdGIznl6CmfOdz2mU9JDOVLMpGBEfjEv8B9QukO0odTAJTTlP/XFHVbjHn14B+O\nn2YSIqzXD0aBAJxsecxkDwRsXP3wlg0viAu/wa8b7m6vKMJPYTf85/TThiZNloer\nl1PHuvLfCSlgV0XltyMoUzcNBw8mQgP7ggq4bR1etQKBgQDdGixkmQNIs8X78qAC\nonH6dFG8lUbDbOQCmM1UgijyZHiUNCC0pO6tKCHv9XVkhv845xdv+uIBtxixnETT\njiAQchiy+KaBEUrFY2XPkJt4XKTY8hOhg60IMk4fD9QIl8GUMqp/6ut4YcFe3ldL\nPXWQBsKXLWH7G4GV7Cb9OwEsRQKBgQDDG/Amuxc5vodv78DGDop1BIixW4FXqowD\nYs7LCbzSCTeZ3NF3gqXkF8GJRmBj6GOkrOBfleeffyjeVAuyX/K6PbXUFzrn0bHi\nYG1zDLMXUZEIguK3aJwl3sDnqNYr5GK92Yt6nMmZwXY/0E60b54PJpg1oKy8hfug\nZBC/N8mgVQKBgCn4BeUyhkUOms4wR984Jpp76ef6Deyahs1XY+JespcQKzM2kd64\nT/XeYFLELPxgA6Ixe2luHehlcPKFzyq5F60He1i9ih2FwsOlEnZL5Lb8Hu5vRPqr\nm/SqV9ndj0nyRHR1CZguZ3P6WlI/siI+EEq+fcFkg+y+U+K5aM04nghhAoGBAKSR\n+TPCFWoYgobxVMn6U+E2LNJkm6nFagolGsZ59TG4opR+hJRot+K4Av/2Q7GhwAKT\n60HU4KVRDbjSbXdMpSFgkfFOktocrw2CRm+Ho7wkidADDpajfyoWROJiMByfrIX0\nbEjE3Ot7GnHjE6/wggLHjBWX7HusC72TCekwdjptAoGAZO2GhJq72eaKs4WoaQys\nkzUTlYxzeP3/hOJbJiD/p2VJkNfwSV5AkOYcPFAyEV5kydA7DwzAKLGaTk9a04Wx\nWXm7wCSVm8QMCYirFU7HTpjnge96fSpO12w6tQ0PB2EIVZ4hFVRVW3sy1i67saqi\nzbNQ+/qgESIWS+7KXf2otwQ=\n-----END PRIVATE KEY-----\n",
  "clientEmail": "firebase-adminsdk-yr86b@Test-42xx9.iam.gserviceaccount.com",
  "clientId": "10805155677889363474",
  "authUri": "https://accounts.google.com/o/oauth2/auth",
  "tokenUri": "https://oauth2.googleapis.com/token",
  "authProviderCertUrl": "https://www.googleapis.com/oauth2/v1/certs",
  "clientCertUrl": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-yr86b%40Test-42xx9.iam.gserviceaccount.com",
  "topicName":"test2",
  "tokenList":"dlDIxxxxxxxxxxxxBHM:APA9cdfgdghhfjhjjkgkjkjklolooOI6ri8bwHafgNXDjX2n2kKwo4fK8hmuoamVddJfAwWr4xymkLZea_wVfLlENk,dlAIxxxxxxxxxxxxBHM:APAxvvfsojnwgfgwnwkww8bwHafgNXDjX2n2kKwo4fK8hmuRTBJJbksea_wVfLlENk"
}
```


**Sample Response**

```json
{
    "Result": {
        "SuccessCount":1,
        "FailureCount":0,
        "Errors":[]
    }
}
```

<br/>

#### Unsubscribe from a topic

This operation allows you to unsubscribe devices from a topic by passing list of registration tokens.
Registration tokens are strings generated by the client FCM SDKs for each end-user client app instance.

**unsubscribeFromTopic**

```xml
<googlefirebase.unsubscribeFromTopic>
    <topicName>{$ctx:topicName}</topicName>
    <tokenList>{$ctx:tokenList}</tokenList>
</googlefirebase.unsubscribeFromTopic>
```

**Properties**

* topicName - The topic name that need to be subscribe by devices.
* tokenList - List of registration tokens as comma separated values which are generated by the client FCM SDKs for each end-user client app instance.
              (Eg:- "YOUR_REGISTRATION_TOKEN_1,YOUR_REGISTRATION_TOKEN_2, ----,YOUR_REGISTRATION_TOKEN_n")


**Sample request**

```json
{
  "accountType": "service_account",
  "projectId": "Test-42xx9",
  "privateKeyId": "8d4ed1afsfsg56jiyu5ggfhgc02b3055a7197b0ac9afe",
  "privateKey": "-----BEGIN PRIVATE KEY-----\nMI3sfsgfgdhgdhgogxD9TEC3wkCC\nGKRvlPH1wqXPMhhsWEMHdhhghghgNBWyg/r+ff59KuqhQBs\nHy3ixWbSYW5XYd0ww6fT/UOH1dLYKSKLidEO6v2Rd2Xh+bxSwqi3IuyjuDy8WBJ6\asafrfrsfrsgtyuy\nS4hn+hfhgfhf/e6pGf0Z2KjL4375OUEEK6S\nCixuZQPFHlnqXi6OcmsLqa+A0kmjkLlVHPe9iH6n5Ku7Ikd+P+7ycRS2W8SF6+fg\nIt7oO9LpAgMBAAECggEACQy65kOIq+W2gFYcSfLHjhwqj/FKiaexd94l91slYPpV\n44v2ghxpOqmDTFRNA1rdgTM0NlFbFnCx8wc3TWOuZTN+0RoMHouPZo2GKX7Epepg\nSiVNW9NSaVQZHbTuAHV8ST41U7M4AyG+t6i1JEx2recStG+QCfqi2/xV2V0kN6ZL\neUdGIznl6CmfOdz2mU9JDOVLMpGBEfjEv8B9QukO0odTAJTTlP/XFHVbjHn14B+O\nn2YSIqzXD0aBAJxsecxkDwRsXP3wlg0viAu/wa8b7m6vKMJPYTf85/TThiZNloer\nl1PHuvLfCSlgV0XltyMoUzcNBw8mQgP7ggq4bR1etQKBgQDdGixkmQNIs8X78qAC\nonH6dFG8lUbDbOQCmM1UgijyZHiUNCC0pO6tKCHv9XVkhv845xdv+uIBtxixnETT\njiAQchiy+KaBEUrFY2XPkJt4XKTY8hOhg60IMk4fD9QIl8GUMqp/6ut4YcFe3ldL\nPXWQBsKXLWH7G4GV7Cb9OwEsRQKBgQDDG/Amuxc5vodv78DGDop1BIixW4FXqowD\nYs7LCbzSCTeZ3NF3gqXkF8GJRmBj6GOkrOBfleeffyjeVAuyX/K6PbXUFzrn0bHi\nYG1zDLMXUZEIguK3aJwl3sDnqNYr5GK92Yt6nMmZwXY/0E60b54PJpg1oKy8hfug\nZBC/N8mgVQKBgCn4BeUyhkUOms4wR984Jpp76ef6Deyahs1XY+JespcQKzM2kd64\nT/XeYFLELPxgA6Ixe2luHehlcPKFzyq5F60He1i9ih2FwsOlEnZL5Lb8Hu5vRPqr\nm/SqV9ndj0nyRHR1CZguZ3P6WlI/siI+EEq+fcFkg+y+U+K5aM04nghhAoGBAKSR\n+TPCFWoYgobxVMn6U+E2LNJkm6nFagolGsZ59TG4opR+hJRot+K4Av/2Q7GhwAKT\n60HU4KVRDbjSbXdMpSFgkfFOktocrw2CRm+Ho7wkidADDpajfyoWROJiMByfrIX0\nbEjE3Ot7GnHjE6/wggLHjBWX7HusC72TCekwdjptAoGAZO2GhJq72eaKs4WoaQys\nkzUTlYxzeP3/hOJbJiD/p2VJkNfwSV5AkOYcPFAyEV5kydA7DwzAKLGaTk9a04Wx\nWXm7wCSVm8QMCYirFU7HTpjnge96fSpO12w6tQ0PB2EIVZ4hFVRVW3sy1i67saqi\nzbNQ+/qgESIWS+7KXf2otwQ=\n-----END PRIVATE KEY-----\n",
  "clientEmail": "firebase-adminsdk-yr86b@Test-42xx9.iam.gserviceaccount.com",
  "clientId": "10805155677889363474",
  "authUri": "https://accounts.google.com/o/oauth2/auth",
  "tokenUri": "https://oauth2.googleapis.com/token",
  "authProviderCertUrl": "https://www.googleapis.com/oauth2/v1/certs",
  "clientCertUrl": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-yr86b%40Test-42xx9.iam.gserviceaccount.com",
  "topicName":"test2",
  "tokenList":"dlDIxxxxxxxxxxxxBHM:APA9cdfgdghhfjhjjkgkjkjklolooOI6ri8bwHafgNXDjX2n2kKwo4fK8hmuoamVddJfAwWr4xymkLZea_wVfLlENk,dlAIxxxxxxxxxxxxBHM:APAxvvfsojnwgfgwnwkww8bwHafgNXDjX2n2kKwo4fK8hmuRTBJJbksea_wVfLlENk"
}
```


**Sample Response**

```json
{
    "Result": {
        "SuccessCount":1,
        "FailureCount":0,
        "Errors":[]
    }
}
```



### Sample configuration

Following example illustrates how to connect to Google Firebase with the init operation and subscribeToTopic operation.

1. Create a sample proxy as below :
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <proxy xmlns="http://ws.apache.org/ns/synapse"
           name="SubscribeToTopic"
           transports="http https"
           startOnLoad="true">
       <description/>
       <target>
          <inSequence>
             <property name="accountType" expression="json-eval($.accountType)"/>
             <property name="projectId" expression="json-eval($.projectId)"/>
             <property name="privateKeyId" expression="json-eval($.privateKeyId)"/>
             <property name="privateKey" expression="json-eval($.privateKey)"/>
             <property name="clientEmail" expression="json-eval($.clientEmail)"/>
             <property name="clientId" expression="json-eval($.clientId)"/>
             <property name="authUri" expression="json-eval($.authUri)"/>
             <property name="tokenUri" expression="json-eval($.tokenUri)"/>
             <property name="authProviderCertUrl"
                       expression="json-eval($.authProviderCertUrl)"/>
             <property name="clientCertUrl" expression="json-eval($.clientCertUrl)"/>
             <property name="topicName" expression="json-eval($.topicName)"/>
             <property name="tokenList" expression="json-eval($.tokenList)"/>
             <googlefirebase.init>
                <accountType>{$ctx:accountType}</accountType>
                <projectId>{$ctx:projectId}</projectId>
                <privateKeyId>{$ctx:privateKeyId}</privateKeyId>
                <privateKey>{$ctx:privateKey}</privateKey>
                <clientEmail>{$ctx:clientEmail}</clientEmail>
                <clientId>{$ctx:clientId}</clientId>
                <authUri>{$ctx:authUri}</authUri>
                <tokenUri>{$ctx:tokenUri}</tokenUri>
                <authProviderCertUrl>{$ctx:authProviderCertUrl}</authProviderCertUrl>
                <clientCertUrl>{$ctx:clientCertUrl}</clientCertUrl>
             </googlefirebase.init>
             <googlefirebase.subscribeToTopic>
                <topicName>{$ctx:topicName}</topicName>
                <tokenList>{$ctx:tokenList}</tokenList>
             </googlefirebase.subscribeToTopic>
             <respond/>
          </inSequence>
          <outSequence>
             <log/>
             <send/>
          </outSequence>
       </target>
    </proxy>
    ```

2. Create a json file called `subscribeToTopic.json` containing the following json:

    ```json
    {
      "accountType": "service_account",
      "projectId": "Test-42xx9",
      "privateKeyId": "8d4ed1afsfsg56jiyu5ggfhgc02b3055a7197b0ac9afe",
      "privateKey": "-----BEGIN PRIVATE KEY-----\nMI3sfsgfgdhgdhgogxD9TEC3wkCC\nGKRvlPH1wqXPMhhsWEMHdhhghghgNBWyg/r+ff59KuqhQBs\nHy3ixWbSYW5XYd0ww6fT/UOH1dLYKSKLidEO6v2Rd2Xh+bxSwqi3IuyjuDy8WBJ6\asafrfrsfrsgtyuy\nS4hn+hfhgfhf/e6pGf0Z2KjL4375OUEEK6S\nCixuZQPFHlnqXi6OcmsLqa+A0kmjkLlVHPe9iH6n5Ku7Ikd+P+7ycRS2W8SF6+fg\nIt7oO9LpAgMBAAECggEACQy65kOIq+W2gFYcSfLHjhwqj/FKiaexd94l91slYPpV\n44v2ghxpOqmDTFRNA1rdgTM0NlFbFnCx8wc3TWOuZTN+0RoMHouPZo2GKX7Epepg\nSiVNW9NSaVQZHbTuAHV8ST41U7M4AyG+t6i1JEx2recStG+QCfqi2/xV2V0kN6ZL\neUdGIznl6CmfOdz2mU9JDOVLMpGBEfjEv8B9QukO0odTAJTTlP/XFHVbjHn14B+O\nn2YSIqzXD0aBAJxsecxkDwRsXP3wlg0viAu/wa8b7m6vKMJPYTf85/TThiZNloer\nl1PHuvLfCSlgV0XltyMoUzcNBw8mQgP7ggq4bR1etQKBgQDdGixkmQNIs8X78qAC\nonH6dFG8lUbDbOQCmM1UgijyZHiUNCC0pO6tKCHv9XVkhv845xdv+uIBtxixnETT\njiAQchiy+KaBEUrFY2XPkJt4XKTY8hOhg60IMk4fD9QIl8GUMqp/6ut4YcFe3ldL\nPXWQBsKXLWH7G4GV7Cb9OwEsRQKBgQDDG/Amuxc5vodv78DGDop1BIixW4FXqowD\nYs7LCbzSCTeZ3NF3gqXkF8GJRmBj6GOkrOBfleeffyjeVAuyX/K6PbXUFzrn0bHi\nYG1zDLMXUZEIguK3aJwl3sDnqNYr5GK92Yt6nMmZwXY/0E60b54PJpg1oKy8hfug\nZBC/N8mgVQKBgCn4BeUyhkUOms4wR984Jpp76ef6Deyahs1XY+JespcQKzM2kd64\nT/XeYFLELPxgA6Ixe2luHehlcPKFzyq5F60He1i9ih2FwsOlEnZL5Lb8Hu5vRPqr\nm/SqV9ndj0nyRHR1CZguZ3P6WlI/siI+EEq+fcFkg+y+U+K5aM04nghhAoGBAKSR\n+TPCFWoYgobxVMn6U+E2LNJkm6nFagolGsZ59TG4opR+hJRot+K4Av/2Q7GhwAKT\n60HU4KVRDbjSbXdMpSFgkfFOktocrw2CRm+Ho7wkidADDpajfyoWROJiMByfrIX0\nbEjE3Ot7GnHjE6/wggLHjBWX7HusC72TCekwdjptAoGAZO2GhJq72eaKs4WoaQys\nkzUTlYxzeP3/hOJbJiD/p2VJkNfwSV5AkOYcPFAyEV5kydA7DwzAKLGaTk9a04Wx\nWXm7wCSVm8QMCYirFU7HTpjnge96fSpO12w6tQ0PB2EIVZ4hFVRVW3sy1i67saqi\nzbNQ+/qgESIWS+7KXf2otwQ=\n-----END PRIVATE KEY-----\n",
      "clientEmail": "firebase-adminsdk-yr86b@Test-42xx9.iam.gserviceaccount.com",
      "clientId": "10805155677889363474",
      "authUri": "https://accounts.google.com/o/oauth2/auth",
      "tokenUri": "https://oauth2.googleapis.com/token",
      "authProviderCertUrl": "https://www.googleapis.com/oauth2/v1/certs",
      "clientCertUrl": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-yr86b%40Test-42xx9.iam.gserviceaccount.com",
      "topicName":"test2",
      "tokenList":"dlDIxxxxxxxxxxxxBHM:APA9cdfgdghhfjhjjkgkjkjklolooOI6ri8bwHafgNXDjX2n2kKwo4fK8hmuoamVddJfAwWr4xymkLZea_wVfLlENk,dlAIxxxxxxxxxxxxBHM:APAxvvfsojnwgfgwnwkww8bwHafgNXDjX2n2kKwo4fK8hmuRTBJJbksea_wVfLlENk"
    }
    ```

3. Replace accountType, projectId, privateKeyId, privateKey, clientEmail, clientId, authUri, tokenUri, tokenUri, authProviderCertUrl, clientCertUrl, topicName, tokenList with your values.

4. Execute the following curl command:

    ```curl
    curl http://localhost:8280/services/SubscribeToTopic -H "Content-Type: application/json" -d @subscribeToTopic.json
    ```

5. Firebase returns json response similar to the one shown below:

    ```json
    {
        "Result": {
            "SuccessCount": 1,
            "FailureCount": 0,
            "Errors": "[]"
        }
    }
    ```