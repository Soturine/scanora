# Decisões Técnicas

## 1. MVP offline-first

Foi priorizado funcionamento local para reduzir complexidade operacional, risco de privacidade e custo de infraestrutura.

## 2. DI manual no `app`

Não foi introduzido framework de injeção para manter o bootstrap simples e fácil de entender. O `AppContainer` já cria uma base boa para migrar para Hilt/Koin no futuro, se fizer sentido.

## 3. Scanner híbrido

O projeto usa:

- CameraX para cumprir captura manual e controle da experiência própria;
- ML Kit Document Scanner para o caminho principal, rápido e confiável;
- editor manual local como fallback obrigatório quando o palpite inicial não basta.

Isso evita prometer um pipeline local perfeito em todos os cenários, mas mantém autonomia de edição quando o scanner guiado não é suficiente.

## 4. Quadrilátero normalizado

As coordenadas do crop são armazenadas em formato proporcional (`0..1`) em vez de pixels absolutos. Isso simplifica:

- preview responsivo;
- edição manual no overlay;
- reprocessamento da imagem em tamanhos diferentes.

## 5. Preview em duas etapas

A prévia de filtros não usa full-res na UI. O editor primeiro renderiza uma imagem intermediária rápida e depois substitui por uma refinada em segundo plano, com debounce, cancelamento de job e cache.

## 6. OCR com imagem preparada

O OCR local não depende mais só do filtro final salvo da página. A decisão da `0.2.0` foi criar uma saída dedicada para leitura, reduzindo ruído visual e casos em que o próprio filtro prejudicava a engine.

## 7. Exportação local mais encontrável

PDF, JPG e PNG seguem locais, mas a `0.2.0` passa a usar `Downloads/Scanora` em Android 10+ para resolver o problema prático de “exportei e não achei o arquivo”.

## 8. Sem Firebase no MVP

Foi uma decisão consciente para manter o escopo honesto, offline e sem dependências de backend antes da hora.
