# Setup

## Requisitos

- Android Studio recente com suporte a AGP 9.1
- JDK 17 configurado no Gradle
- Android SDK Platform 36
- Build Tools compatíveis
- Emulador Android recente ou dispositivo físico

## Passos

1. Clone o repositório.
2. Abra a pasta raiz no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Confirme que o IDE está usando JDK 17.
5. Instale as plataformas Android exigidas se o Studio solicitar.
6. Rode a configuração `app`.

## Comandos úteis

```bash
./gradlew assembleDebug
./gradlew lint
./gradlew testDebugUnitTest
```

## Observações do ambiente desta entrega

Durante esta geração, o ambiente de terminal não possuía Android SDK instalado. O repositório foi estruturado para uso normal no Android Studio, mas a compilação completa não pôde ser validada localmente aqui.

