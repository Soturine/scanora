# Testes

## O que já está coberto

- validação de nomes de documentos;
- formatação de datas em pt-BR;
- busca por título e tags;
- construção de nomes de arquivo para exportação;
- teste instrumentado mínimo de inicialização da UI;
- build `assembleDebug`, `testDebugUnitTest` e `lint` validados localmente na entrega `0.2.3`.

## O que ainda falta

- testes de integração para repositórios Room;
- cobertura mais forte do pipeline de imagem;
- testes de navegação e fluxos completos com Compose;
- cenários instrumentados para OCR e exportação;
- QA visual em aparelho real com publicação das capturas oficiais.

## Como rodar

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lint
./gradlew connectedDebugAndroidTest
```

## Estratégia sugerida para evoluir

- manter lógica pura em use cases e helpers testáveis em JVM;
- isolar regras de exportação, mapeamento e preparação de OCR;
- introduzir testes de UI por fluxo principal à medida que o produto estabilizar.
