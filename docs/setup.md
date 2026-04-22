# Setup

## Requisitos

- Android Studio recente com suporte a AGP 9.1
- JDK 17 ou superior compatível com AGP 9
- Android SDK Platform 36
- Android SDK Build Tools 36.0.0
- Emulador Android recente ou dispositivo físico
- `applicationId`: `com.soturine.scanora`

## Passos

1. Clone o repositório.
2. Abra a pasta raiz no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Confirme que o IDE está usando um JDK 17+ compatível.
5. Instale as plataformas Android exigidas se o Studio solicitar.
6. Rode a configuração `app`.

## Comandos úteis

```bash
./gradlew assembleDebug
./gradlew lint
./gradlew testDebugUnitTest
```

## Observações do ambiente desta entrega

- Android SDK instalado em `C:\Users\rafael\AppData\Local\Android\Sdk`
- Pacotes validados: `platform-tools`, `platforms;android-36`, `build-tools;36.0.0`
- Validação executada com:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-22"
$env:ANDROID_SDK_ROOT="C:\Users\rafael\AppData\Local\Android\Sdk"
.\gradlew.bat assembleDebug testDebugUnitTest lint --no-daemon
```

- O build de linha de comando passou nesta máquina em `2026-04-21`.
