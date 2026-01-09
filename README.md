# Android Auto Xtream IPTV Player (AAPlayer)

## Description
The Android Auto Xtream IPTV Player (AAPlayer) is an application designed to bring IPTV streaming capabilities to your Android Auto-enabled vehicle. It allows users to access their Xtream IPTV services directly from their car's display, providing a seamless and integrated entertainment experience on the go.

If you wanna support me and my projects: https://paypal.me/akirayuki96

## Features
-   **Android Auto Integration**: Designed to work flawlessly within the Android Auto environment.
-   **Xtream IPTV Support**: Connects to Xtream IPTV services for live TV, movies, and series.
-   **Media Playback**: Provides capabilities for streaming various media content.
-   **User-Friendly Interface**: Optimized for car displays with a simple and intuitive interface.

## Getting Started

### Prerequisites
Before you can build and run this application, ensure you have the following installed:
*   **Java Development Kit (JDK)**: Version 17 or higher.
*   **Android SDK**: With platform-36 installed. You can set `ANDROID_HOME` environment variable to its path.
*   **Git**: For cloning the repository.
*   **GitHub CLI (gh)**: Recommended for authentication with GitHub.

### Installation and Build

To get a local copy up and running, follow these simple steps.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Ak1r4Yuk1/Android-Auto-Xtream-IPTV-Player
    ```

2.  **Navigate into the project directory:**
    ```bash
    cd Android-Auto-Xtream-IPTV-Player/
    ```

3.  **Build the application:**
    To build the release version of the application, run the following command. Make sure to replace `/path/to/your/Android/Sdk` with the actual path to your Android SDK installation.

    ```bash
    ANDROID_HOME=/path/to/your/Android/Sdk ./gradlew assembleRelease
    ```
4. **Install Application**
   Install application using **KingInstaller1.4**
   
    Alternatively, for a debug build (which does not require release signing), you can use:
    ```bash
    ANDROID_HOME=/path/to/your/Android/Sdk ./gradlew assembleDebug
    ```

### Important Notes for Release Build
The `assembleRelease` task requires the application to be signed. This project is configured to use `app/aaplayer.jks` and its credentials from `app/keystore.properties`.
You **must** create a file named `keystore.properties` inside the `app/` directory (e.g., `/home/akira/AAPlayer/app/keystore.properties`) with the following content (replace placeholders with your actual keystore details, ensuring no sensitive information is exposed if you plan to share this file):

```properties
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```
If you are using the `aaplayer.jks` and `keystore.properties` files that were just committed to the repository, ensure they match your expected credentials.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
