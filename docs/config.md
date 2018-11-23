# Configuring Google Firebase Operations

[[Prerequisites](#Prerequisites)] [[Initializing the connector](#initializing-the-connector)]


## Prerequisites

To use the Google Firebase connector,
1. Download following jars [firebase-admin-6.5.0.jar](https://mvnrepository.com/artifact/com.google.firebase/firebase-admin/6.5.0), [google-auth-library-credentials-0.11.0.jar](https://mvnrepository.com/artifact/com.google.auth/google-auth-library-credentials/0.11.0), [google-auth-library-oauth2-http-0.11.0.jar](https://mvnrepository.com/artifact/com.google.auth/google-auth-library-oauth2-http/0.11.0), [api-common-1.7.0.jar](https://mvnrepository.com/artifact/com.google.api/api-common/1.7.0) and place those into <EI_HOME>/lib folder.

2. Restart the EI server if it is already started. Then add the `<googlefirebase.init>` element in your proxy configuration before use any other Google Firebase connector
operations. This `<googlefirebase.init>` element used to authenticates the user with firebase servers. For more information on authorizing requests in Google Firebase, see [API Doc](https://firebase.google.com/docs/admin/setup).


#### Obtaining service account's credentials

**Follow the steps below to get your service account's credentials from Google firebase console :**

1. If you don't already have a Firebase project, add one in the [Firebase console](https://console.firebase.google.com).
2. The **Add project** dialog also gives you the option to add Firebase to an existing Google Cloud Platform project.
3. Navigate to the [Service Accounts](https://console.firebase.google.com/project/_/settings/serviceaccounts/adminsdk) tab in your project's settings page.
4. Click the **Generate New Private Key** button at the bottom of the **Firebase Admin SDK** section of the **Service Accounts** tab.

After you click the button, a JSON file containing your service account's credentials will be downloaded. You'll need this to initialize the firebase connector init operation in the next step.

## Initializing the connector
Add the following <googlefirebase.init> method in your proxy configuration:

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

* accountType: Obtain the "type" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* projectId: Obtain the "project_id" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* privateKeyId: Obtain the "private_key_id" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* privateKey: Obtain the "private_key" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* clientEmail: Obtain the "client_email" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* clientId: Obtain the "client_id" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* authUri: Obtain the "auth_uri" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* tokenUri: Obtain the "token_uri" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* authProviderCertUrl: Obtain the "auth_provider_x509_cert_url" parameter value from the service account's credentials JSON file that you obtain in the previous step.
* clientCertUrl: Obtain the "client_x509_cert_url" parameter value from the service account's credentials JSON file that you obtain in the previous step.


<br/>

Now that you have connected to Google Firebase, follow the below topics to perform various operations with the google firebase connector.

* To work with operation which is related to firebase message, See [Working with Firebase Message](message.md).
* To work with operation which is related to firebase topic subscriptions, See [Working with Firebase Topic Subscriptions](topic.md).