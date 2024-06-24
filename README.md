# LabyMod 4 Server API

## Overview

The LabyMod 4 Server API is a tool for Minecraft server owners and developers to integrate LabyMod with their servers.
This API allows communication between the server and the LabyMod client, allowing for enhanced features and
customizations that improve the player experience.

## Project Structure

The Server API is organized into several modules:

- `api` - Contains the main interfaces and classes that define the protocol and does not contain any LabyMod related
  code.
- `core` - The LabyMod-specific implementation of the API, containing all packets and models to interact with the
  LabyMod client.
- `server` - This module is divided into submodules for different platform implementations:
    - `bukkit` - The platform-specific integration for Servers running on Bukkit (Spigot, Paper, etc.).
    - `bungeecord` - The platform-specific integration for Servers running on BungeeCord.
    - `common` - Contains shared classes and utilities used across different server implementations to ensure consistent
      behavior and reduce code duplication.
  - `velocity` - The platform-specific integration for Servers running on Velocity.

## Integrations

The API is designed to be extended by so-called "integrations". Integrations are modules that provide additional
functionality to the Server API. Official integrations can be
found [here](https://github.com/LabyMod/labymod4-server-api-integrations). <br/> An explanation of how to create your
own integration can be found on the [LabyMod Developer Portal](https://wiki.labymod.net).

## Usage

An extensive guide on how to use the LabyMod 4 Server API can be found on
the [LabyMod Developer Portal](https://wiki.labymod.net).

## Building

### Prerequisites

- Java Development Kit (JDK) 17 or higher

### Steps

1. Clone the repository:
   ```sh
   git clone https://github.com/LabyMod/labymod4-server-api.git
   cd labymod4-server-api
   ```

2. Build the project using Gradle:
   ```sh
   ./gradlew build
   ```

3. All compiled JAR files are in the `build/commonOutput` directory.

## License

This project is licensed under the [MIT License](https://github.com/LabyMod/labymod4-server-api/blob/master/LICENSE). 