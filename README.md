# Google Firebase EI Connector

The Google Firebase [connector](https://docs.wso2.com/display/EI640/Working+with+Connectors) allows you to integrate your own backend services with Firebase Cloud Messaging (FCM).
It handles authenticating with Firebase servers while facilitating sending messages and managing topic subscriptions. Using FCM, you can notify a client app that a new email or other data are available to sync.
It allows you to access the [Firebase Cloud Messaging (FCM)](https://firebase.google.com/docs/cloud-messaging/) through WSO2 EI.


## Compatibility

| Connector version | Supported Firebase SDK version | Supported WSO2 EI version |
| ------------- | ------------- | ------------- |
| [1.0.0](https://github.com/wso2-extensions/esb-connector-googlefirebase/releases/tag/org.wso2.carbon.connector.googlefirebase-1.0.0) | firebase-admin : 6.5.0 | EI 6.4.0 |

## Getting started

###### Downloading and installing the connector


1. Download the connector from [WSO2 Store](https://store.wso2.com/store/assets/esbconnector/details/64a497ef-ca4c-4210-9215-da7946888a45) by clicking the Download Connector button.
2. Then you can follow this [Documentation](https://docs.wso2.com/display/EI640/Working+with+Connectors+via+the+Management+Console) to add and enable the connector via the Management Console in your EI instance.
3. For more information on using connectors and their operations in your EI configurations, see [Using a Connector](https://docs.wso2.com/display/EI640/Using+a+Connector).
4. If you want to work with connectors via EI tooling, see [Working with Connectors via Tooling](https://docs.wso2.com/display/EI640/Working+with+Connectors+via+Tooling).


###### Configuring the connector operations

To get started with Google Firebase connector and their operations, see [Configuring Google Firebase Operations](docs/config.md).

## Building from the Source

If you want to build the Google Firebase connector from the source code:

1. Get a clone or download the source from [GitHub](https://github.com/wso2-extensions/esb-connector-googlefirebase).
2. Navigate to the the `esb-connector-googlefirebase` directory and execute the following Maven command: `mvn clean install`.
This creates a ZIP file of the Google Firebase connector under the `esb-connector-googlefirebase/target` directory.

## Contributing to WSO2 Extensions

As an open source project, WSO2 extensions welcome contributions from the community.

Check the [issue tracker](https://github.com/wso2-extensions/esb-connector-googlefirebase/issues) for open issues that interest you. We look forward to receiving your contributions.
