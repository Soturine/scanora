# Decisões Técnicas

## 1. MVP offline-first

Foi priorizado funcionamento local para reduzir complexidade operacional, risco de privacidade e custo de infraestrutura.

## 2. DI manual no `app`

Não foi introduzido framework de injeção para manter o bootstrap simples e fácil de entender. O `AppContainer` já cria uma base boa para migrar para Hilt/Koin no futuro, se fizer sentido.

## 3. Scanner híbrido

O projeto usa:

- CameraX para cumprir captura manual e controle da experiência própria.
- ML Kit Document Scanner para um caminho guiado, rápido e confiável no MVP.

Isso evita prometer um pipeline local de detecção perfeito em todos os cenários logo na primeira versão.

## 4. Quadrilátero normalizado

As coordenadas do crop são armazenadas em formato proporcional (`0..1`) em vez de pixels absolutos. Isso simplifica:

- preview responsivo;
- edição manual no overlay;
- reprocessamento da imagem em tamanhos diferentes.

## 5. Exportação local

PDF, JPG e PNG são gerados no armazenamento do app e compartilhados via `FileProvider`. Não há storage permission ampla nem upload automático.

## 6. Sem Firebase no MVP

Foi uma decisão consciente para manter o escopo honesto, offline e sem dependências de backend antes da hora.

