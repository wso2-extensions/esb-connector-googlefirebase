# Configuring Google Firebase Operations

[[Prerequisites](#Prerequisites)] [[Initializing the connector](#initializing-the-connector)]


## Prerequisites

To use the Google Firebase connector,
1. Download the following JAR files [firebase-admin-6.5.0.jar](https://mvnrepository.com/artifact/com.google.firebase/firebase-admin/6.5.0), [google-auth-library-credentials-0.11.0.jar](https://mvnrepository.com/artifact/com.google.auth/google-auth-library-credentials/0.11.0), [google-auth-library-oauth2-http-0.11.0.jar](https://mvnrepository.com/artifact/com.google.auth/google-auth-library-oauth2-http/0.11.0), [api-common-1.7.0.jar](https://mvnrepository.com/artifact/com.google.api/api-common/1.7.0) and copy them into the <EI_HOME>/lib directory.

2. Restart the WSO2 EI server if it is already started. 
3. Add the `<googlefirebase.init>` element in your proxy configuration before any other Google Firebase connector
operations. This `<googlefirebase.init>` element authenticates the user against the Firebase servers. For more information on authorizing requests in Google Firebase, see [API Doc](https://firebase.google.com/docs/admin/setup).


#### Obtaining the credentials of the service account

**Follow the steps below to get your credentials of the service account via the Google Firebase console :**

1. If you do not have a Firebase project, add one in the [Firebase Console](https://console.firebase.google.com).
(The **Add project** dialog also gives you the option to add Firebase to an existing Google Cloud Platform project.)
3. Navigate to the [Service Accounts](https://console.firebase.google.com/project/_/settings/serviceaccounts/adminsdk) tab in thw settings page of your project.
4. Click the **Generate New Private Key** button at the bottom of the **Firebase Admin SDK** section in the **Service Accounts** tab.

This downloads a JSON file containing the credentials of your service account. You need these credentials to initialize the Firebase connector init operation in the next step.

## Initializing the connector
Add the following <googlefirebase.init> method to your proxy configuration:

**init**
```xml
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
```

<br/>


**Properties**

Obtain the parameter values for each of the below properties from the JSON file that you obtained in the previous step, which contains the credentials of the service account.

* accountType: Obtain the "type" parameter value.
* projectId: Obtain the "project_id" parameter value.
* privateKeyId: Obtain the "private_key_id" parameter value.
* privateKey: Obtain the "private_key" parameter value.
* clientEmail: Obtain the "client_email" parameter value.
* clientId: Obtain the "client_id" parameter value.
* authUri: Obtain the "auth_uri" parameter value.
* tokenUri: Obtain the "token_uri" parameter value.
* authProviderCertUrl: Obtain the "auth_provider_x509_cert_url" parameter value.
* clientCertUrl: Obtain the "client_x509_cert_url" parameter value.

<br/>

Now, that you have connected to Google Firebase, follow the below topics to perform various operations with the Google Firebase connector.

* To work with the operations that are related to Firebase messages, see [Working with Firebase Message](message.md).
* To work with the operations that are related to Firebase topic subscriptions, see [Working with Firebase Topic Subscriptions](topic.md).
