# Escopo do Projeto

## Visão geral

`Scanora` é um aplicativo Android de escaneamento de documentos com foco em uso local, privacidade e fluxo rápido de captura, revisão e exportação. O projeto foi estruturado como um MVP sólido, modular e pronto para evolução.

Identidade técnica atual:

- Nome do app: `Scanora`
- Package base: `com.soturine.scanora`
- Stack principal: Kotlin, Jetpack Compose, Material 3, CameraX, ML Kit, Room, DataStore, Navigation Compose, WorkManager

## Objetivo do produto

O app deve permitir que a pessoa usuária:

- capture documentos pela câmera;
- importe imagens da galeria;
- detecte automaticamente o documento;
- ajuste manualmente os quatro cantos quando necessário;
- corrija perspectiva;
- aplique filtros voltados para legibilidade;
- gere PDF com múltiplas páginas;
- exporte imagens em JPG e PNG;
- compartilhe arquivos;
- organize scans localmente;
- pesquise documentos por título;
- execute OCR local com cópia rápida do texto.

## Diferenciais do MVP

O produto se diferencia por uma abordagem offline-first e por modos de captura orientados ao contexto:

- `Caderno / Faculdade`
- `Documento / Contrato`
- `Recibo / Nota`

Também fazem parte do escopo:

- tags locais;
- histórico de scans;
- favoritos;
- modo escuro;
- onboarding curto;
- interface em português do Brasil;
- documentação pública e técnica preparada para manutenção open-source.

## Diretrizes técnicas

Decisões obrigatórias para a base do projeto:

- arquitetura modular, mas sem modularização excessiva para o MVP;
- separação clara entre UI, domínio e dados;
- ViewModels com `StateFlow`;
- persistência local com Room;
- preferências com DataStore;
- exportação local de PDF e imagens;
- OCR local com ML Kit;
- tarefas leves de manutenção com WorkManager;
- testes unitários nas partes centrais do domínio e da exportação;
- CI Android com build, lint e testes.

## Pipeline de processamento de imagem

O pipeline deve priorizar aparência de scanner real e legibilidade para OCR:

1. captura ou importação da imagem;
2. estimativa automática do documento;
3. ajuste manual dos cantos, quando necessário;
4. correção de perspectiva;
5. redução básica de ruído e sombras;
6. melhoria de contraste;
7. filtros voltados a documento;
8. exportação e OCR sobre a imagem tratada.

Filtros mínimos previstos:

- Original corrigido
- Documento P&B
- Documento cinza
- Colorido aprimorado
- Recibo / Alto contraste

## Escopo funcional

Telas do MVP:

1. Splash
2. Onboarding
3. Home
4. Captura por câmera
5. Importação da galeria
6. Ajuste e crop
7. Filtros
8. Revisão de páginas
9. Exportação
10. Histórico
11. Detalhe do scan
12. Configurações
13. OCR
14. Sobre

Fluxos mínimos:

- abrir a câmera;
- capturar ou importar;
- revisar o recorte;
- aplicar filtro;
- reordenar páginas;
- excluir páginas;
- renomear documento;
- exportar PDF;
- exportar imagem;
- compartilhar;
- salvar no histórico;
- pesquisar scans;
- abrir OCR e copiar texto.

## Privacidade

Princípios do projeto:

- processamento local por padrão;
- sem upload obrigatório;
- sem dependência de backend no MVP;
- permissões reduzidas ao mínimo necessário;
- documentação pública clara sobre uso de câmera, armazenamento e compartilhamento.

## Publicação e documentação

O repositório deve permanecer pronto para publicação e colaboração:

- `README.md` enxuto e profissional;
- `CHANGELOG.md` seguindo versionamento semântico;
- documentação técnica em `docs/`;
- planejamento em `planning/`;
- site estático separado em `site/`;
- workflows de CI e GitHub Pages em `.github/workflows/`.

## Critérios de qualidade

O projeto é considerado saudável quando:

- compila sem erro;
- passa em `assembleDebug`, `testDebugUnitTest` e `lint`;
- mantém namespace e imports consistentes;
- evita arquivos gerados no versionamento;
- preserva legibilidade do código e da documentação;
- deixa claro o que já está pronto e o que ainda é roadmap.
