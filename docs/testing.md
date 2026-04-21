# Testes

## O que já está coberto

- validação de nomes de documentos;
- formatação de datas em pt-BR;
- busca por título e tags;
- construção de nomes de arquivo para exportação;
- teste instrumentado mínimo de inicialização da UI;
- build `assembleDebug`, `lint` e `testDebugUnitTest` validados localmente nesta entrega.

## O que ainda falta

- testes de integração para repositórios Room;
- cobertura mais forte do pipeline de imagem;
- testes de navegação e fluxos completos com Compose;
- cenários instrumentados para OCR e exportação;
- revisão visual em emulador/dispositivos reais com captura de screenshots.

## Como rodar

```bash
./gradlew testDebugUnitTest
./gradlew lint
./gradlew connectedDebugAndroidTest
```

## Estratégia sugerida para evoluir

- manter lógica pura em use cases e helpers testáveis em JVM;
- isolar regras de exportação e mapeamento;
- introduzir testes de UI por fluxo principal à medida que o produto estabilizar.
